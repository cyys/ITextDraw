package pdf.chart;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 柱状图+多柱状+ 类别
 * 
 * @author cheny
 */
public class HistogramManyHistogramChart extends AbstractChart {

	private float x;
	private float y;
	private float height = 160;
	private float width = 450;
	private float fontSize = 10;

	private int[] levels;// 刻度
	private String[] itemNames;// 分数对应的名称
	private int itemBackgroundColor = 0x397BC6;// 背景颜色
	private int frameBackgroundColor = 0xF2F2F2;// 坐标轴的背景颜色
	private String[] typeNames;// 类型名称
	private int[] typeFillColors;// 类型的背景颜色

	private int[] rowColors;// 多个类型时候，行的交替颜色
	private List<float[]> scores;// 分数
	private int borderColor = 0x868686;// 坐标轴框的颜色
	private int fontColor = 0x000000;// 字体的颜色

	private float positionY;// 画完表格之后，当前所在的横坐标
	private float cellHeight = 30;// 表格高度
	private float typeWidth = 40;// 类型的宽度
	private int tabelBorderColor = 0xD2D2D2;// 表格边框的颜色

	public HistogramManyHistogramChart() {
		super();
	}

	public HistogramManyHistogramChart(PdfWriter writer, PdfContentByte contentByte, Document document,
			BaseFont baseFont) {
		super(writer, contentByte, document, baseFont);
	}

	@Override
	public void chart() {

		this.positionY = 0;

		if (ObjectUtils.equals(null, this.itemNames) || this.itemNames.length < 1
				|| ObjectUtils.equals(null, this.scores) || this.scores.size() < 1
				|| ObjectUtils.equals(null, this.typeNames) || this.typeNames.length < 1)
			throw new RuntimeException("请检测itemNames、typeNames、scores数据是否存在！");

		if (ObjectUtils.equals(null, this.levels)) {
			this.levels = new int[] { 0, 20, 40, 60, 80, 100 };
		}

		if (ObjectUtils.equals(null, this.typeFillColors)) {
			this.typeFillColors = new int[] { 0x72CBAD, 0xE47D54 };
		}

		if (ObjectUtils.equals(null, this.rowColors)) {
			this.rowColors = new int[] { 0xFFFFFF, 0xF2F2F2 };
		}

		try {
			BaseColor borderColor_ = new BaseColor(this.borderColor);
			BaseColor fontColor_ = new BaseColor(this.fontColor);
			BaseColor tabelBorderColor_ = new BaseColor(this.tabelBorderColor);

			this.contentByte.setFontAndSize(this.baseFont, this.fontSize);

			this.contentByte.setColorFill(fontColor_);

			// 绘画柱状图框架
			this.drawFrame(borderColor_, fontColor_);

			this.drawTable(fontColor_, tabelBorderColor_);
			
			this.drawScore(fontColor_, tabelBorderColor_);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void drawScore(BaseColor fontColor, BaseColor tabelBorderColor) {
		float kHeight = this.height / (this.levels[this.levels.length - 1] - this.levels[0]);
		
		float sepWidth = this.width / this.itemNames.length,y0=0;
		
		float offset=sepWidth/(this.typeNames.length+2),x0 =0;
		
		for(int i=0;i<this.scores.size();i++){
			 x0=this.x+offset*(i+1);
			 for(int j=0;j<this.scores.get(i).length;j++){
				 y0=kHeight*(this.scores.get(i)[j]- this.levels[0]);
				 this.moveRect(this.contentByte, x0, this.y+1f, x0+offset,  
						 				this.y+y0, this.typeFillColors[i%this.typeNames.length]);
				 x0+=sepWidth;
			 }
		}
	}
	
	private void drawTable(BaseColor fontColor, BaseColor tabelBorderColor) {
		float sepWidth = this.width / this.itemNames.length;
		float x0 = this.x, y0 = this.y - this.cellHeight, tempX = 0;

		for (int i = 0; i < this.itemNames.length; i++) {
			this.moveRect(this.contentByte, x0, this.y, x0 + sepWidth - 1, y0, this.itemBackgroundColor);

			this.contentByte.setColorFill(BaseColor.WHITE);
			drawMulRowText(this.cellHeight, sepWidth, this.itemNames[i], x0, this.y);

			this.contentByte.setColorStroke(tabelBorderColor);
			this.contentByte.setLineDash(1);
			this.moveLine(this.contentByte, x0, this.y, x0, y0);
			x0 += sepWidth;
		}

		this.contentByte.setColorStroke(tabelBorderColor);
		this.contentByte.setLineDash(1);
		this.moveLine(this.contentByte, x0, this.y, x0, y0);

		x0 = this.x;
		// 开始画出分数
		for (int i = 0; i < scores.size(); i++) {
			for (int j = 0; j < scores.get(i).length; j++) {

				this.moveRect(this.contentByte, x0, y0, x0 + sepWidth - 1, y0 - this.cellHeight, this.rowColors[i % 2]);

				this.contentByte.setColorFill(fontColor);
				drawMulRowText(this.cellHeight, sepWidth, this.scores.get(i)[j] + "", x0, y0);

				this.contentByte.setColorStroke(tabelBorderColor);
				this.contentByte.setLineDash(1);
				this.moveLine(this.contentByte, x0, y0, x0, y0 - this.cellHeight);

				x0 += sepWidth;
			}
			this.contentByte.setColorStroke(tabelBorderColor);
			this.contentByte.setLineDash(1);
			this.moveLine(this.contentByte, x0, y0, x0, y0 - this.cellHeight);

			x0 = this.x;
			this.moveRect(this.contentByte, x0 - 1, y0, x0 - 1 - this.typeWidth, y0 - this.cellHeight,
					this.rowColors[i % 2]);

			tempX = drawMulRowText(this.cellHeight, this.typeWidth - 5, this.typeNames[i] + "", x0 + 5 - this.typeWidth,
					y0);

			this.moveRect(this.contentByte, tempX - 2, y0 - this.cellHeight / 2 + 2.5f, tempX - 7,
					y0 - this.cellHeight / 2 - 2.5f, this.typeFillColors[i % this.typeNames.length]);

			this.contentByte.setColorStroke(tabelBorderColor);
			this.contentByte.setLineDash(1);
			this.contentByte.rectangle(x0 - 1 - this.typeWidth, y0 - this.cellHeight, this.typeWidth, this.cellHeight);
			this.contentByte.stroke();

			y0 -= this.cellHeight;
			this.positionY+=this.cellHeight;
		}
		
		this.positionY+=this.cellHeight;
		this.contentByte.setColorStroke(tabelBorderColor);
		this.contentByte.setLineDash(1);
		this.moveLine(this.contentByte, this.x, y0, this.x + this.width, y0);
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
	private float drawMulRowText(float lineHeight, float witdh, String text, float x, float y) {
		// 如果是数值，宽度折半
		float fontSize = text.matches("^(?:\\d{1,})|(?:\\d{1,}\\.\\d{1,})$") ? this.fontSize / 2 : this.fontSize;
		float lineNumber = text.length() * fontSize / witdh;

		lineNumber = lineNumber > 1 ? (float) Math.ceil(lineNumber) : 1;

		int everyLen = (int) (Math.round(text.length() / lineNumber));

		String tempText = null;
		float x0 = 0, y0 = y - (lineHeight - lineNumber * this.fontSize) / 2 - this.fontSize + 1f;
		lineHeight = Float.MAX_VALUE;
		for (int i = 0; i < lineNumber; i++) {
			tempText = text.substring(i * everyLen,
					(lineNumber > 1 && i == lineNumber - 1 ? text.length() : everyLen * (i + 1)));
			x0 = x + (witdh - tempText.length() * fontSize) / 2;
			lineHeight = Math.min(lineHeight, x0);
			this.moveText(this.contentByte, tempText, x0, y0, Element.ALIGN_LEFT, 0);
			y0 -= this.fontSize + 1f;
		}
		return lineHeight;
	}

	/**
	 * 绘画柱状图框架
	 */
	private void drawFrame(BaseColor borderColor, BaseColor fontColor) {
		this.contentByte.setColorStroke(borderColor);
		// 绘画x,y
		this.moveLine(this.contentByte, this.x, this.y, this.x, this.y + this.height);
		this.moveRect(this.contentByte, this.x, this.y + 1.5f, this.x + this.width, this.y + this.height + 1.5f,
				this.frameBackgroundColor);
		// 画Y轴的刻度
		float x1 = this.x, kHeight = this.height / (this.levels[this.levels.length - 1] - this.levels[0]), y1 = 0;

		for (int i = 0; i < this.levels.length; i++) {
			y1 = this.y + kHeight * (this.levels[i] - this.levels[0]);
			this.contentByte.setColorStroke(borderColor);
			this.contentByte.setLineDash(1f);
			this.moveLine(this.contentByte, x1, y1, this.x - 2, y1);

			this.contentByte.setColorFill(fontColor);
			this.moveText(this.contentByte, this.levels[i] + "", x1 - 6, y1 - 3, Element.ALIGN_RIGHT, 0);

			if (i > 0) {
				this.contentByte.setColorStroke(borderColor);
				this.contentByte.setLineDash(3f, 2f);
				this.moveLine(this.contentByte, x1, y1, x1 + this.width, y1);
			}
		}

		this.moveText(this.contentByte, this.levels[this.levels.length - 1] + "", x1 - 6, y1 - 3, Element.ALIGN_RIGHT,
				0);
	}

	public float getPositionY() {
		this.positionY = this.y - this.positionY - 10;
		return this.positionY;
	}

	public HistogramManyHistogramChart setX(float x) {
		this.x = x;
		return this;
	}

	public HistogramManyHistogramChart setY(float y) {
		this.y = y;
		return this;
	}

	public HistogramManyHistogramChart setHeight(float height) {
		this.height = height;
		return this;
	}

	public HistogramManyHistogramChart setWidth(float width) {
		this.width = width;
		return this;
	}

	public HistogramManyHistogramChart setFontSize(float fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	public HistogramManyHistogramChart setLevels(int[] levels) {
		this.levels = levels;
		return this;
	}

	public HistogramManyHistogramChart setItemNames(String[] itemNames) {
		this.itemNames = itemNames;
		return this;
	}

	public HistogramManyHistogramChart setScores(List<float[]> scores) {
		this.scores = scores;
		return this;
	}

	public HistogramManyHistogramChart setBorderColor(int borderColor) {
		this.borderColor = borderColor;
		return this;
	}

	public HistogramManyHistogramChart setFontColor(int fontColor) {
		this.fontColor = fontColor;
		return this;
	}

	public HistogramManyHistogramChart setItemBackgroundColor(int itemBackgroundColor) {
		this.itemBackgroundColor = itemBackgroundColor;
		return this;
	}

	public HistogramManyHistogramChart setFrameBackgroundColor(int frameBackgroundColor) {
		this.frameBackgroundColor = frameBackgroundColor;
		return this;
	}

	public HistogramManyHistogramChart setTypeNames(String[] typeNames) {
		this.typeNames = typeNames;
		return this;
	}

	public HistogramManyHistogramChart setTypeFillColors(int[] typeFillColors) {
		this.typeFillColors = typeFillColors;
		return this;
	}

	public HistogramManyHistogramChart setRowColors(int[] rowColors) {
		this.rowColors = rowColors;
		return this;
	}

	public HistogramManyHistogramChart setCellHeight(float cellHeight) {
		this.cellHeight = cellHeight;
		return this;
	}

	public HistogramManyHistogramChart setTypeWidth(float typeWidth) {
		this.typeWidth = typeWidth;
		return this;
	}
}
