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
 * 表格+虚线的边框+柱状图
 * 
 * @author cheny
 *
 */
public class TableDashTableRectChart extends AbstractChart {

	// 初始化x,y的位置，及图形的大小
	private float x;
	private float width = 450;
	private float y;

	private int[] levels;// 刻度
	private String[] itemNames;// 名称
	private String[] headNames;// 表头名称
	private float[] scores;// 分数
	private int levelShowInColmun = 2;// 刻度所在的列
	private float[] colWidths;// 每一列的宽度

	private float fontSize = 9;// 字体大小
	private int fontColor = 0x000000;// 字体的颜色
	private float positionY;// 画完表格之后，当前所在的横坐标
	private int fillRectColor = 0xCD614A;// 矩形填充颜色
	private int fristColmunBackgroundColor = 0xDFEEFA;// 第一列背景颜色
	private int borderColor = 0xAAAAAA;// 边框的颜色
	private int headBackgroundColor = 0x074688;// 表头的颜色

	private float cellHeight = 25;// 每一个单元格的高度
	private float headFontSize = 12;// 表头的字体大小
	private int scale = 2;// 保留的小数点位数

	public TableDashTableRectChart() {
		super();
	}

	public TableDashTableRectChart(PdfWriter writer, PdfContentByte contentByte, Document document, BaseFont baseFont) {
		super(writer, contentByte, document, baseFont);
	}

	public TableDashTableRectChart setScale(int scale) {
		this.scale = scale;
		return this;
	}

	@Override
	public void chart() {
		this.positionY=0;
		
		if (ObjectUtils.equals(null, this.itemNames) || this.itemNames.length < 1
				|| ObjectUtils.equals(null, this.headNames) || this.headNames.length < 1
				|| ObjectUtils.equals(null, this.scores) || this.scores.length < 1)
			throw new RuntimeException("请检测itemNames、headNames、scores数据是否存在！");

		if (ObjectUtils.equals(null, this.levels)) {
			this.levels = new int[] { 10, 20, 30, 40, 50, 60, 70, 80, 90 };
		}

		if (ObjectUtils.equals(null, this.colWidths)) {
			this.colWidths = new float[] { 15, 70, 15 };
		}

		float sum = 0;
		for (float s : this.colWidths) {
			sum += s;
		}

		BaseColor fontColor_ = new BaseColor(this.fontColor);
		BaseColor headBackgroundColor_ = new BaseColor(this.headBackgroundColor);
		BaseColor borderColor_ = new BaseColor(this.borderColor);

		try {
			addTableHead(headBackgroundColor_, sum);

			addBody(borderColor_, fontColor_, sum);

		} catch (Exception e) {
			e.printStackTrace();
		}

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
	private void drawMulRowText(float lineHeight, float witdh, float fontSize_, String text, float x, float y) {
		// 如果是数值，宽度折半
		float fontSize = text.matches("^(?:\\d{1,})|(?:\\d{1,}\\.\\d{1,})$") ? fontSize_ / 2 : fontSize_;
		float lineNumber = text.length() * fontSize / witdh;

		lineNumber = lineNumber > 1 ? (float) Math.ceil(lineNumber) : 1;

		int everyLen = (int) (Math.round(text.length() / lineNumber));

		String tempText = null;
		float x0 = 0, y0 = y - (lineHeight - lineNumber * fontSize_) / 2 - fontSize_ + 1f;

		for (int i = 0; i < lineNumber; i++) {
			tempText = text.substring(i * everyLen,
					(lineNumber > 1 && i == lineNumber - 1 ? text.length() : everyLen * (i + 1)));
			x0 = x + (witdh - tempText.length() * fontSize) / 2;
			this.moveText(this.contentByte, tempText, x0, y0, Element.ALIGN_LEFT, 0);
			y0 -= fontSize_ + 1f;
		}
	}

	private float calTableHeadHeight(float sum, float fontSize) {
		float width = 0;
		int lineNumber = 0;
		for (int i = 0; i < this.colWidths.length; i++) {
			width = this.width * this.colWidths[i] / sum;
			lineNumber = Math.max(lineNumber, (int) Math.ceil(this.itemNames[i].length() * fontSize / width));
		}
		return (lineNumber - 1) * fontSize + this.cellHeight;
	}

	private void addTableHead(BaseColor headBackgroundColor, float sum) throws Exception {
		float x1 = 0, levelWidth = 0, x0 = this.x, y0 = this.y;
		this.contentByte.setFontAndSize(this.baseFont, this.headFontSize);

		float realHeight = calTableHeadHeight(sum, this.headFontSize);
		this.positionY+=realHeight;

		for (int i = 0; i < this.colWidths.length; i++) {
			if (i + 1 == this.levelShowInColmun) {
				x1 = x0;
				levelWidth = this.width * this.colWidths[i] / sum;
			}
			this.moveRect(this.contentByte, x0, y0, x0 + this.width * this.colWidths[i] / sum, y0 - realHeight,
					this.headBackgroundColor);
			if (i + 1 != this.levelShowInColmun) {
				this.contentByte.setColorFill(BaseColor.WHITE);
				drawMulRowText(realHeight, this.width * this.colWidths[i] / sum, this.headFontSize, this.headNames[i],
						x0, y0);
			}

			x0 += this.width * this.colWidths[i] / sum;

			if (i != this.colWidths.length - 1) {
				this.contentByte.setColorStroke(BaseColor.BLACK);
				this.contentByte.setLineWidth(1.5f);
				this.moveLine(this.contentByte, x0, y0, x0, y0 - realHeight + 0.3f);
			}
		}

		// 开始画刻度
		this.contentByte.setFontAndSize(this.baseFont, this.fontSize);
		this.contentByte.setColorFill(BaseColor.WHITE);
		this.contentByte.setColorStroke(BaseColor.WHITE);

		float sepWidth = levelWidth / this.levels[this.levels.length - 1];
		this.contentByte.setLineWidth(1);
		for (int j = 0; j < this.levels.length; j++) {
			if (j != this.levels.length - 1) {
				this.moveText(this.contentByte, levels[j] + "", x1 + sepWidth * this.levels[j], y0 - realHeight + 3,
						Element.ALIGN_CENTER, 0);
				this.moveLine(this.contentByte, x1 + sepWidth * this.levels[j], y0 - realHeight,
						x1 + sepWidth * this.levels[j], y0 - realHeight + 3);
			}
		}
	}

	/**
	 * 表体部分
	 * 
	 * @param realHeight
	 * @throws Exception
	 */
	private void addBody(BaseColor borderColor, BaseColor fontColor, float sum) throws Exception {
		DecimalFormat df = new DecimalFormat("0" + (this.scale == 0 ? "" : "." + StringUtils.repeat("#", this.scale)));
		float x1 = 0, levelWidth = 0, x0 = this.x, y0 = this.y;
		float realHeight = calTableHeadHeight(sum, this.headFontSize);
		
		this.contentByte.setColorStroke(BaseColor.BLACK);
		this.contentByte.setFontAndSize(this.baseFont, this.fontSize);

		for (int k = 0; k < this.itemNames.length; k++) {
			x0 = this.x;
			y0 -= realHeight;
			for (int i = 0; i < this.colWidths.length; i++) {

				if (i + 1 != this.levelShowInColmun) {
					this.contentByte.setColorFill(fontColor);
					switch (i) {
					case 0: {
						realHeight=calTableHeadHeight(sum, this.fontSize);
						this.positionY+=realHeight;
						this.moveRect(this.contentByte, x0,y0,
								x0+this.width * this.colWidths[i] / sum, y0-realHeight, this.fristColmunBackgroundColor);
						drawMulRowText(realHeight, this.width * this.colWidths[i] / sum, this.fontSize,
								this.itemNames[k], x0, y0);
						break;
						}
					default: {
						drawMulRowText(realHeight, this.width * this.colWidths[i] / sum, this.fontSize,
								df.format(this.scores[k]), x0, y0);
						break;
						}
					}
				}else{
					levelWidth = this.width * this.colWidths[i] / sum;
					float sepWidth = levelWidth / this.levels[this.levels.length - 1];
					x1 = x0+this.scores[k]*sepWidth;
					this.moveRect(this.contentByte, x0,y0-realHeight/4, x1, y0-realHeight*3/4, this.fillRectColor);
				}

				x0 += this.width * this.colWidths[i] / sum;

				this.contentByte.setColorStroke(borderColor);
				this.contentByte.setLineWidth(2);
				this.contentByte.setLineDash(2, 2);
				this.moveLine(this.contentByte, x0, y0, x0, y0 - realHeight + 0.3f);

				this.moveLine(this.contentByte, x0 - this.width * this.colWidths[i] / sum, y0,
						x0 - this.width * this.colWidths[i] / sum, y0 - realHeight + 0.3f);

				this.moveLine(this.contentByte, x0, y0 - realHeight + 0.3f, x0 - this.width * this.colWidths[i] / sum,
						y0 - realHeight + 0.3f);
			}
		}

	}

	public TableDashTableRectChart setWidth(float width) {
		this.width = width;
		return this;
	}

	public TableDashTableRectChart setScores(float[] scores) {
		this.scores = scores;
		return this;
	}

	public TableDashTableRectChart setX(float x) {
		this.x = x;
		return this;
	}

	public TableDashTableRectChart setY(float y) {
		this.y = y;
		return this;
	}

	public TableDashTableRectChart setLevels(int[] levels) {
		this.levels = levels;
		return this;
	}

	public TableDashTableRectChart setItemNames(String[] itemNames) {
		this.itemNames = itemNames;
		return this;
	}

	public TableDashTableRectChart setHeadNames(String[] headNames) {
		this.headNames = headNames;
		return this;
	}

	public TableDashTableRectChart setFontSize(float fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	public TableDashTableRectChart setFillRectColor(int fillRectColor) {
		this.fillRectColor = fillRectColor;
		return this;
	}

	public TableDashTableRectChart setFristColmunBackgroundColor(int fristColmunBackgroundColor) {
		this.fristColmunBackgroundColor = fristColmunBackgroundColor;
		return this;
	}

	public TableDashTableRectChart setBorderColor(int borderColor) {
		this.borderColor = borderColor;
		return this;
	}

	public TableDashTableRectChart setHeadBackgroundColor(int headBackgroundColor) {
		this.headBackgroundColor = headBackgroundColor;
		return this;
	}

	public TableDashTableRectChart setLevelShowInColmun(int levelShowInColmun) {
		this.levelShowInColmun = levelShowInColmun;
		return this;
	}

	public TableDashTableRectChart setColWidths(float[] colWidths) {
		this.colWidths = colWidths;
		return this;
	}

	public TableDashTableRectChart setFontColor(int fontColor) {
		this.fontColor = fontColor;
		return this;
	}

	public TableDashTableRectChart setCellHeight(float cellHeight) {
		this.cellHeight = cellHeight;
		return this;
	}

	public TableDashTableRectChart setHeadFontSize(float headFontSize) {
		this.headFontSize = headFontSize;
		return this;
	}

	public float getPositionY() {
		this.positionY=this.y-this.positionY-10;
		return this.positionY;
	}
}
