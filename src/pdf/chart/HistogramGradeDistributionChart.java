package pdf.chart;

import java.text.DecimalFormat;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 柱状图+区域分级+ 柱状图
 * 
 * @author cheny
 *
 */
public class HistogramGradeDistributionChart extends AbstractChart {

	private float x;
	private float y;
	private float height =120;
	private float width = 330;
	private float fontSize = 10;

	private int[] levels;// 刻度
	private String[] itemNames;// 名称
	private String[] gradeNames;// 级别名称
	private int[] gradeBackgroundColors;// 级别的背景颜色
	private int[] scoreBackgroundColors;// 每一个分数对应的颜色
	private float[] scores;// 分数

	private int histogramBackgroundColor = 0x8892BE;// 柱状体背景颜色
	private int fontColor = 0x000000;// 字体颜色
	private int levelSepLineXColor = 0xBFBFBF;// 边框的颜色
	private int levelYBorderColor = 0xBFBFBF;// 刻度线的颜色
	private int levelBackgroundColor = 0xBFBFBF;// 单元格背景颜色

	private float positionY;// 画完表格之后，当前所在的横坐标
	private float cellHeight = 15;// 单元格的高度
	private float frameLeftOffsetX =120;// 坐标左边的偏移量
	private float levelCellWidth = 30;// 显示刻度的矩形框宽度
	private int scale=2;//保留的小数点位数
	private float itemRightOffsetX=10;

	public HistogramGradeDistributionChart setScale(int scale) {
		this.scale = scale;
		return this;
	}

	public HistogramGradeDistributionChart setFrameLeftOffsetX(float frameLeftOffsetX) {
		this.frameLeftOffsetX = frameLeftOffsetX;
		return this;
	}

	public HistogramGradeDistributionChart setItemRightOffsetX(float itemRightOffsetX) {
		this.itemRightOffsetX = itemRightOffsetX;
		return this;
	}

	public HistogramGradeDistributionChart() {
		super();
	}

	public HistogramGradeDistributionChart(PdfWriter writer, PdfContentByte contentByte, Document document,
			BaseFont baseFont) {
		super(writer, contentByte, document, baseFont);
	}

	@Override
	public void chart() {
		this.positionY = 0;
		if (ObjectUtils.equals(null, this.itemNames) || this.itemNames.length < 1
				|| ObjectUtils.equals(null, this.scores) || this.scores.length < 1)
			throw new RuntimeException("请检测itemNames、scores数据是否存在！");

		if (ObjectUtils.equals(null, this.levels)) {
			this.levels = new int[] { 0, 30, 60, 90 };
		}

		if (ObjectUtils.equals(null, this.gradeNames)) {
			this.gradeNames = new String[] { "弱需求", "中等需求", "强需求" };
		}

		if (ObjectUtils.equals(null, this.gradeBackgroundColors)) {
			this.gradeBackgroundColors = new int[] { 0x9BB7D2, 0x8892BE, 0x8366A3 };
		}

		if (ObjectUtils.equals(null, this.scoreBackgroundColors)) {
			this.scoreBackgroundColors = new int[] { 0x6E5E96, 0xF8C760, 0xE6652B, 0x74842E, 0xB77A7F };
		}

		try {
			BaseColor fontColor_ = new BaseColor(this.fontColor);
			BaseColor levelSepLineXColor_ = new BaseColor(this.levelSepLineXColor);
			BaseColor levelYBorderColor_ = new BaseColor(this.levelYBorderColor);

			this.contentByte.setFontAndSize(this.baseFont, this.fontSize);
			drawFrame(levelYBorderColor_, fontColor_);

			drawScore(levelSepLineXColor_, fontColor_);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void drawFrame(BaseColor levelYBorderColor, BaseColor fontColor) throws Exception {
		float sepWidth = this.width / this.gradeNames.length;
		float x0 = this.x + this.frameLeftOffsetX, y0 = this.y + this.height;
		float headHeight=calTableHeadHeight(sepWidth);

		for (int i = 0; i < this.gradeNames.length; i++) {
			//画出级别的名称
			this.moveRect(this.contentByte, x0, y0, x0 + sepWidth - 1, y0 +headHeight,
					this.gradeBackgroundColors[i]);
			this.contentByte.setColorFill(BaseColor.WHITE);
			this.drawMulRowText(headHeight, sepWidth, this.gradeNames[i], x0, y0 +headHeight);
			
			//画出底部的刻度
			this.moveRect(this.contentByte, x0 - this.levelCellWidth / 2, this.y - this.fontSize,
					x0 + this.levelCellWidth / 2, this.y - this.fontSize * 2 - 3, this.levelBackgroundColor);
			this.contentByte.setColorFill(fontColor);
			this.drawMulRowText(this.fontSize, this.levelCellWidth, this.levels[i] + "", x0 - this.levelCellWidth / 2,
					this.y - this.fontSize - 1.5f);

			//画出X的刻度线
			this.contentByte.setLineDash(2f, 3f);
			this.contentByte.setColorStroke(levelYBorderColor);
			this.moveLine(this.contentByte, x0, y0 + this.cellHeight, x0, this.y - this.fontSize);

			x0 += sepWidth;
		}

		//画出最后一个底部的刻度
		this.moveRect(this.contentByte, x0 - this.levelCellWidth / 2, this.y - this.fontSize,
				x0 + this.levelCellWidth / 2, this.y - this.fontSize * 2 - 3, this.levelBackgroundColor);
		this.contentByte.setColorFill(fontColor);
		this.drawMulRowText(this.fontSize, this.levelCellWidth, this.levels[this.levels.length - 1] + "",
				x0 - this.levelCellWidth / 2, this.y - this.fontSize - 1.5f);

		//画出最后一个X的刻度线
		this.contentByte.setLineDash(2f, 3f);
		this.contentByte.setColorStroke(levelYBorderColor);
		this.moveLine(this.contentByte, x0, y0 + this.cellHeight, x0, this.y - this.fontSize);
	}

	/**
	 * 将文字居中画出来
	 * 
	 * @param lineHeight
	 * @param witdh
	 * @param text
	 * @param x
	 * @param y
	 */
	private void drawMulRowText(float lineHeight, float witdh, String text, float x, float y) {
		// 如果是数值，宽度折半
		float fontSize = text.matches("^(?:\\d{1,})|(?:\\d{1,}\\.\\d{1,})$") ? this.fontSize / 2 : this.fontSize;
		float lineNumber = text.length() * fontSize / witdh;

		lineNumber = lineNumber > 1 ? (float) Math.ceil(lineNumber) : 1;

		int everyLen = (int) (Math.round(text.length() / lineNumber));

		String tempText = null;
		float x0 = 0, y0 = y - (lineHeight - lineNumber * this.fontSize) / 2 - this.fontSize + 1f;

		for (int i = 0; i < lineNumber; i++) {
			tempText = text.substring(i * everyLen,
					(lineNumber > 1 && i == lineNumber - 1 ? text.length() : everyLen * (i + 1)));
			x0 = x + (witdh - tempText.length() * fontSize) / 2;
			this.moveText(this.contentByte, tempText, x0, y0, Element.ALIGN_LEFT, 0);
			y0 -= this.fontSize + 1f;
		}
	}

	/**
	 * 计算出表格头的最长文字占据的高度
	 * @param width
	 * @return
	 */
	private float calTableHeadHeight(float width) {
		float sum = 0;
		for (int i = 0; i < this.gradeNames.length; i++) {
			sum = Math.max(sum, this.gradeNames[i].length());
		}
		sum = sum * this.fontSize % width == 0 ? sum * this.fontSize / width
				: (float) (Math.floor(sum * this.fontSize / width) + 1);
		return (sum - 1) * this.fontSize + this.cellHeight;
	}
	
	private void drawScore(BaseColor levelSepLineXColor,BaseColor fontColor) throws Exception {
		DecimalFormat df = new DecimalFormat("0"+(this.scale==0?"":"."+StringUtils.repeat("#", this.scale)));
		//画分数区域的间隔
		float sepHeight=(this.height-this.cellHeight-this.fontSize/2)/(this.itemNames.length-1);
		float y0 = this.y+this.height-this.cellHeight;
		float x0=this.x+this.frameLeftOffsetX;
		
		float kWidth=this.width/(this.levels[this.levels.length-1]-this.levels[0]);
		BaseColor[] scoreColors=new BaseColor[this.scoreBackgroundColors.length];
		for(int j=0;j<scoreColors.length;j++){
			scoreColors[j]=new BaseColor(this.scoreBackgroundColors[j]);
		}
		
		this.contentByte.setColorStroke(levelSepLineXColor);
		this.contentByte.setLineDash(1);
		for(int i=0;i<this.itemNames.length;i++){
			this.moveLineCircle(x0-this.itemRightOffsetX, y0, 3,
					this.frameLeftOffsetX-3-this.itemRightOffsetX, scoreColors[i], levelSepLineXColor);
			this.contentByte.setColorFill(fontColor);
			//画出名称
			this.moveText(this.contentByte, this.itemNames[i],
					x0-this.itemRightOffsetX-3-this.fontSize
					, y0+3, Element.ALIGN_RIGHT, 0);
			
			this.contentByte.setColorStroke(levelSepLineXColor);
			//画出水平的x轴线
			this.moveLine(this.contentByte, x0, y0, x0+this.width, y0);
			//画出分数的矩形图
			this.moveRect(this.contentByte, x0+1f, y0+this.cellHeight/2,
					x0+kWidth*(this.scores[i]-this.levels[0]), y0-this.cellHeight/2, this.histogramBackgroundColor);
			
			this.contentByte.setColorFill(fontColor);
			//画出当前的分数
			this.moveText(this.contentByte,df.format(this.scores[i]),
						x0+kWidth*(this.scores[i]-this.levels[0])+this.fontSize
						, y0-this.fontSize/3, Element.ALIGN_LEFT, 0);
			y0-=sepHeight;
		}
	}
	
	/**
	 * 画出左边的圆圈+线条
	 * @param x
	 * @param y
	 * @param r
	 * @param width
	 * @param color
	 * @param lineColor
	 */
	 private void moveLineCircle(float x, float y,float r,float width,BaseColor color,BaseColor lineColor){
		 this.contentByte.setColorStroke(color);
		 this.contentByte.setColorFill(color);
		 this.moveCircle(this.contentByte, x, y, r);
		 
		 this.contentByte.setColorStroke(lineColor);
		 this.moveLine(this.contentByte,x-width,y,x-r,y);
	 }

	public HistogramGradeDistributionChart setX(float x) {
		this.x = x;
		return this;
	}

	public HistogramGradeDistributionChart setY(float y) {
		this.y = y;
		return this;
	}

	public HistogramGradeDistributionChart setHeight(float height) {
		this.height = height;
		return this;
	}

	public HistogramGradeDistributionChart setWidth(float width) {
		this.width = width;
		return this;
	}

	public HistogramGradeDistributionChart setFontSize(float fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	public HistogramGradeDistributionChart setLevels(int[] levels) {
		this.levels = levels;
		return this;
	}

	public HistogramGradeDistributionChart setGradeNames(String[] gradeNames) {
		this.gradeNames = gradeNames;
		return this;
	}

	public HistogramGradeDistributionChart setScores(float[] scores) {
		this.scores = scores;
		return this;
	}

	public HistogramGradeDistributionChart setFontColor(int fontColor) {
		this.fontColor = fontColor;
		return this;
	}

	public HistogramGradeDistributionChart setItemNames(String[] itemNames) {
		this.itemNames = itemNames;
		return this;
	}

	public HistogramGradeDistributionChart setGradeBackgroundColors(int[] gradeBackgroundColors) {
		this.gradeBackgroundColors = gradeBackgroundColors;
		return this;
	}

	public HistogramGradeDistributionChart setScoreBackgroundColors(int[] scoreBackgroundColors) {
		this.scoreBackgroundColors = scoreBackgroundColors;
		return this;
	}

	public HistogramGradeDistributionChart setHistogramBackgroundColor(int histogramBackgroundColor) {
		this.histogramBackgroundColor = histogramBackgroundColor;
		return this;
	}

	public HistogramGradeDistributionChart setLevelSepLineXColor(int levelSepLineXColor) {
		this.levelSepLineXColor = levelSepLineXColor;
		return this;
	}

	public HistogramGradeDistributionChart setLevelYBorderColor(int levelYBorderColor) {
		this.levelYBorderColor = levelYBorderColor;
		return this;
	}

	public HistogramGradeDistributionChart setLevelBackgroundColor(int levelBackgroundColor) {
		this.levelBackgroundColor = levelBackgroundColor;
		return this;
	}

	public HistogramGradeDistributionChart setCellHeight(float cellHeight) {
		this.cellHeight = cellHeight;
		return this;
	}

	public HistogramGradeDistributionChart setLevelCellWidth(float levelCellWidth) {
		this.levelCellWidth = levelCellWidth;
		return this;
	}

	public float getPositionY() {
		this.positionY = this.y - this.fontSize * 2 - 10;
		return this.positionY;
	}
}
