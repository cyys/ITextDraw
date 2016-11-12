package pdf.chart;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 2D饼状图
 * 
 * @author cheny
 *
 */
public class Round2DCakeChart extends AbstractChart {

	private float x;// 横坐标
	private float y;// 纵坐标
	private float r=80;// 半径
	private float rotation = 90;// 旋转角度
	private BaseColor[] fillColors;// 每个图形块的填充颜色
	private String[] descs;// 每一个图形块的描述
	private float fontSize = 10;
	private float[] scores;// 分数百分比
	private float positionY;// 画完图形之后，当前所在的横坐标

	public Round2DCakeChart() {
		super();
	}

	public Round2DCakeChart(PdfWriter writer, PdfContentByte contentByte, Document document, BaseFont baseFont) {
		super(writer, contentByte, document, baseFont);
	}

	@Override
	public void chart() {
		this.positionY=0;
		if (ObjectUtils.equals(null, this.scores)|| this.scores.length < 1 
				 ||ObjectUtils.equals(null, this.descs)|| this.descs.length < 1)
			throw new RuntimeException("请检测scores、descs数据是否存在！");

		if(ObjectUtils.equals(null, this.fillColors)){
			this.fillColors=new BaseColor[]{new BaseColor(0x59CFFF),new BaseColor(0xF8DB92),
									new BaseColor(0xA9D961),  new BaseColor(0xB3A2C7),  new BaseColor(0xFF0000)};
		}
		
		int sideNum = 720;
		float space = 360f / sideNum, spaceSize = space;

		float x0 = 0, y0 = 0;
		final float max = 180f;

		float tempValue = 0f;// 计算分数的总和
		for (int i = 0; i < this.scores.length; i++) {
			tempValue += this.scores[i];
		}

		float[] everyEreas = new float[this.scores.length];
		for (int i = 0; i < this.scores.length; i++) {// 计算每一个百分百对应的图形的边数
			everyEreas[i] = this.scores[i] / tempValue * sideNum;
		}

		// 开始画出图形
		int areaNum = 0;
		float areaSum = everyEreas[areaNum];
		for (int i = 0; i < sideNum; i++) {
			if (i <= areaSum) {
				this.contentByte.setColorStroke(this.fillColors[areaNum]);
			} else {
				areaSum += everyEreas[++areaNum];
			}
			x0 = this.x + (float) (this.r * Math.sin(Math.PI * (space + this.rotation) / max));
			y0 = this.y + (float) (this.r * Math.cos(Math.PI * (space + this.rotation) / max));
			this.moveLine(this.contentByte, this.x, this.y, x0, y0);
			space += spaceSize;
		}
		drawDataText(everyEreas, spaceSize, max);
		drawDesc();
		 
		// 画出中间白色的分界线
		if(this.scores.length>1)
			drawSepLine(everyEreas, sideNum, max);
	}

	/**
	 * 画出底部的描述
	 */
	private void drawDesc() {
		float allWidth = StringUtils.join(this.descs, "").length() * this.fontSize + (this.descs.length - 1) * 10
				+ this.descs.length * 10;
		float benginX = this.x - allWidth / 2, benginY = this.y - this.r - 30;
		for (int i = 0; i < this.descs.length; i++) {
			this.contentByte.setColorFill(this.fillColors[i]);
			this.contentByte.setColorStroke(this.fillColors[i]);
			this.moveRect(this.contentByte, benginX, benginY, benginX + 10, benginY + 10, this.fillColors[i].getRGB(),
					true);
			benginX += 13;
			this.contentByte.setColorFill(BaseColor.BLACK);
			this.moveText(this.contentByte, this.descs[i], benginX, benginY, Element.ALIGN_LEFT, 0);
			benginX += (this.descs[i].length() + 1) * this.fontSize;
		}
	}

	/**
	 * 显示百分比文字
	 * 
	 * @param everyEreas
	 * @param spaceSize
	 * @param max
	 */
	private void drawDataText(float[] everyEreas, float spaceSize, final float max) {
		this.contentByte.setFontAndSize(this.baseFont, this.fontSize);
		float curEreaHalf = 0f;
		float space = 0f;
		float offsetX = 0, offsetY = 0, x0 = 0, y0 = 0, tempValue = 0;
		int oneNum = 0;
		for (int i = 0; i < everyEreas.length; i++) {
			space += curEreaHalf * spaceSize;
			curEreaHalf = everyEreas[i] / 2;
			space += spaceSize * curEreaHalf;

			this.contentByte.setColorStroke(fillColors[i]);
			x0 = this.x + (float) (this.r / 2 * Math.sin(Math.PI * (space + this.rotation) / max));
			y0 = this.y + (float) (this.r / 2 * Math.cos(Math.PI * (space + this.rotation) / max));

			if (this.scores[i] < 10) {// 当小于10的时候，画在图形的外面
				tempValue = this.fontSize * ("" + this.scores[i]).length();
				// 当前坐标在圆心的延长线
				if (this.scores[i] <= 2) {// 如果数值太小，就处理：一次短,一次长
					oneNum++;
					tempValue = oneNum % 3 == 2 ? // 根据数据的长度，设置偏移量
							(tempValue + this.fontSize * ("" + this.scores[i]).length() - 5)
							: (oneNum % 3 == 1 ? 5f : tempValue);
				}

				offsetX = this.x + (float) ((this.r + tempValue) * Math.sin(Math.PI * (space + this.rotation) / max));
				offsetY = this.y + (float) ((this.r + tempValue) * Math.cos(Math.PI * (space + this.rotation) / max));

				this.moveLine(this.contentByte, x0, y0, offsetX, offsetY);
				if (offsetX > this.x) {
					this.moveText(this.contentByte, scores[i] + "%", offsetX, offsetY, Element.ALIGN_LEFT, 0);
				} else if (offsetX < this.x) {
					this.moveText(this.contentByte, scores[i] + "%", offsetX, offsetY, Element.ALIGN_RIGHT, 0);
				} else {
					this.moveText(this.contentByte, scores[i] + "%", offsetX, offsetY, Element.ALIGN_CENTER, 0);
				}
				offsetX = offsetY = 0;
				continue;
			}

			if (this.scores[i] < 20) {// 小于20的时候，将坐标偏移
				offsetX = (float) (this.r / 2 * Math.sin(Math.PI * (space + this.rotation) / max)) * 6 / this.scores[i];
				offsetY = (float) (this.r / 2 * Math.cos(Math.PI * (space + this.rotation) / max)) * 4 / this.scores[i];
			}

			if(this.scores.length<2){
				offsetX=0;
				offsetY=0;
				x0=this.x;
				y0=this.y;
			}
				
			this.moveText(this.contentByte, this.scores[i] + "%", x0 + offsetX, y0 + offsetY, Element.ALIGN_CENTER, 0);
			offsetX = offsetY = 0;

		}
	}

	/**
	 * 画出中间白色的分界线
	 * 
	 * @param space
	 */
	private void drawSepLine(float[] everyEreas, int sideNum, final float max) {
		float space = 360f / sideNum, spaceSize = space;
		int areaNum = 0;

		float areaSum = everyEreas[areaNum];
		float x0 = 0, y0 = 0;
		this.contentByte.setColorStroke(BaseColor.WHITE);
		this.contentByte.setLineWidth(2f);

		for (int i = 0; i < sideNum; i++) {
			if (i > areaSum) {
				areaSum += everyEreas[++areaNum];
				x0 = this.x + (float) (this.r * Math.sin(Math.PI * (space + this.rotation) / max));
				y0 = this.y + (float) (this.r * Math.cos(Math.PI * (space + this.rotation) / max));
				this.moveLine(this.contentByte, this.x, this.y, x0, y0);
			}
			space += spaceSize;
		}
		x0 = this.x + (float) (this.r * Math.sin(Math.PI * (spaceSize + this.rotation) / max));
		y0 = this.y + (float) (this.r * Math.cos(Math.PI * (spaceSize + this.rotation) / max));
		this.moveLine(this.contentByte, this.x, this.y, x0, y0);
	}

	public Round2DCakeChart setX(float x) {
		this.x = x;
		return this;
	}

	public Round2DCakeChart setY(float y) {
		this.y = y;
		return this;
	}

	public Round2DCakeChart setR(float r) {
		this.r = r;
		return this;
	}

	public Round2DCakeChart setFillColors(BaseColor[] fillColors) {
		this.fillColors = fillColors;
		return this;
	}

	public Round2DCakeChart setDescs(String[] descs) {
		this.descs = descs;
		return this;
	}

	public Round2DCakeChart setScores(float[] scores) {
		this.scores = scores;
		return this;
	}

	public Round2DCakeChart setRotation(float rotation) {
		this.rotation = rotation;
		return this;
	}

	public Round2DCakeChart setFontSize(float fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	public float getPositionY() {
		this.positionY = this.y - this.r - 40;
		return this.positionY;
	}
}
