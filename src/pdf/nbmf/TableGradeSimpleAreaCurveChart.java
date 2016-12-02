package pdf.nbmf;

import org.apache.commons.lang3.ObjectUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import pdf.base.unit.FourTopQuadrilateralGraph;
import pdf.base.unit.RectangleLineThroughGraph;
import pdf.chart.AbstractChart;

/**
 * 点连接线+简单的表格+阴影区域图，positionY为画好图形后当前的Y坐标
 * @author cheny
 */
public class TableGradeSimpleAreaCurveChart extends AbstractChart {

	private float width = 450;// 表格的宽度
	private float height = 120;
	private float x;
	private float y;
	private float[] levels;// 刻度
	
	private float fontSize = 9;// 除了刻线之外的文字大小
	private int fontColor = 0x000000;// 字体的颜色
	private float lineHeight = 20;// 每一行的高度
	private String[] itemNames;//名称
	/**
	 * 所有的分数
	 */
	private float[] scores;// 每一行的每列的分数
	private int tableBorderColor=0xBFBFBF;//表格边框的颜色
	/**
	 * 行的颜色，仅仅取前面两种颜色
	 */
	private int tableBackgroundColor=0xF2F2F2;//表格背景颜色
	private float positionY;// 画完表格之后，当前所在的横坐标
	/**
	 * 曲线的颜色
	 */
	private int curveColor = 0x4F81BD;// 折现的颜色
	private float levelFontSize =8;// 刻度文本的字体大小
	private int levelFontColor =0x559CC2;// 刻度文本的字体大小
	private int borderColor = 0x7F7F7F;// 表格边框的颜色

	private String[] gradeNames;// 级别的名称
	private int[] gradeColors;// 级别的颜色
	private float[] gradeUpperLimitScore;// 级别的上限分数

	private int verticalLineColor = 0xD0D0D0;// 垂直线的颜色
	private int verticalAreaColor = 0xFAFAFA;// 垂直线所在区域的颜色
	private float gradeRectangeGap = 15;// 级别颜色标注框与表格间的空隙

	private FourTopQuadrilateralGraph fillAreaGraph;// 画填充区域的工具

	public TableGradeSimpleAreaCurveChart() {
		super();
	}

	public TableGradeSimpleAreaCurveChart(PdfWriter writer, PdfContentByte contentByte, Document document,
			BaseFont baseFont) {
		super(writer, contentByte, document, baseFont);
	}

	@Override
	public void chart() {
		this.positionY = 0;
		if (ObjectUtils.equals(null, this.scores) || this.scores.length < 1)
			throw new RuntimeException("请检测scores数据是否存在！");

		if (ObjectUtils.equals(null, this.levels)) {
			this.levels = new float[] { 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f };
		}

		if (ObjectUtils.equals(null, this.gradeNames)) {
			this.gradeNames = new String[] { "优秀", "良好", "合格"};
		}

		if (ObjectUtils.equals(null, this.gradeColors)) {
			this.gradeColors = new int[] { 0x5DD3B0, 0x92D050, 0xFFC000};
		}

		this.fillAreaGraph = new FourTopQuadrilateralGraph(this.writer, this.contentByte, this.document);
		this.fillAreaGraph.setBaseChart(this);
		this.fillAreaGraph.setColor(this.verticalAreaColor);

		try {

			BaseColor borderColor_ = new BaseColor(this.borderColor);
			BaseColor fontColor_ = new BaseColor(this.fontColor);

			this.setLine(1, this.gradeRectangeGap, this.document);
			drawGradeDescs();
			
			drawArea();
			
			// 绘画柱状图框架
			this.drawFrame(borderColor_);
			
			drawScoreGraph();
			
			addTableBody(fontColor_);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 画出填充的区域
	 */
	private void drawArea() {
		float kHeight = this.height / (this.levels[this.levels.length - 1] - this.levels[0])
				,sepWidth=this.width/this.itemNames.length,
				x0=this.x+sepWidth*3/2,y0=this.y;
		
		for (int i =1, l = this.scores.length; i < l; i++) {
			this.fillAreaGraph.setFirst_x(x0)
					.setFirst_y(y0 +kHeight*(this.scores[i] - this.levels[0]))
					.setSecond_x(x0)
					.setSecond_y(y0)
					.setThird_x(x0 -sepWidth)
					.setThird_y(y0)
					.setFourth_x(x0 -sepWidth)
					.setFourth_y(y0 +kHeight*(this.scores[i-1] - this.levels[0]));
			this.fillAreaGraph.chart();
			x0+=sepWidth;
		}
	}
	
	/**
	 * 画出分数对应的折线
	 */
	private void drawScoreGraph(){
		float kHeight = this.height / (this.levels[this.levels.length - 1] - this.levels[0])
				,sepWidth=this.width/this.itemNames.length,
				x0=this.x+sepWidth/2,y0=this.y;
		
		BaseColor curveColor_=new BaseColor(this.curveColor);
		BaseColor verticalLineColor_=new BaseColor(this.verticalLineColor);
		
		for (int i =0, l = this.scores.length; i < l; i++) {
			this.contentByte.setColorFill(curveColor_);
			this.contentByte.setColorStroke(curveColor_);
			this.moveCircle(this.contentByte, x0, y0 +kHeight*(this.scores[i] - this.levels[0]), 2, true);
			
			this.contentByte.setColorStroke(verticalLineColor_);
			this.moveLine(this.contentByte, x0,y0, x0,
					y0 +kHeight*(this.scores[i] - this.levels[0]));
			
			if(i>0){
				this.contentByte.setColorFill(curveColor_);
				this.contentByte.setColorStroke(curveColor_);
				this.moveLine(this.contentByte, x0 -sepWidth,
						y0 +kHeight*(this.scores[i-1] - this.levels[0]), x0,
						y0 +kHeight*(this.scores[i] - this.levels[0]));
			}
			x0+=sepWidth;
		}
	}
	
	/**
	 * 绘画柱状图框架
	 */
	private void drawFrame(BaseColor borderColor) {
		BaseColor fontColor_ = new BaseColor(this.levelFontColor);
		this.contentByte.setColorStroke(borderColor);
		this.contentByte.setLineWidth(1f);
		this.contentByte.setFontAndSize(this.baseFont, this.levelFontSize);
		// 绘画x,y
		this.moveLine(this.contentByte, this.x, this.y, this.x, this.y + this.height);
		// 画Y轴的刻度
		float x1 = this.x, kHeight = this.height / (this.levels[this.levels.length - 1] - this.levels[0]), y1 = 0;

		for (int i = 0; i < this.levels.length; i++) {
			y1 = this.y + kHeight * (this.levels[i] - this.levels[0]);
			this.contentByte.setColorStroke(borderColor);
			this.contentByte.setLineDash(1f);
			this.moveLine(this.contentByte, x1, y1, this.x - 2, y1);

			this.contentByte.setColorFill(fontColor_);
			this.moveText(this.contentByte, this.levels[i] + "", x1 - 6, y1 - 3, Element.ALIGN_RIGHT, 0);
		}

		this.contentByte.setColorFill(fontColor_);
		this.moveText(this.contentByte, this.levels[this.levels.length - 1] + ""
				, x1 - 6, y1 - 3, Element.ALIGN_RIGHT,0);
		
		//画出各个级别的分界线
		BaseColor gradeColor=null;
		for(int i=0,len=this.gradeUpperLimitScore.length;i<len;i++){
			gradeColor=new BaseColor(this.gradeColors[i]);
			this.contentByte.setColorStroke(gradeColor);
			y1 = this.y + kHeight * (this.gradeUpperLimitScore[i] - this.levels[0]);
			this.moveLine(this.contentByte, this.x, y1, this.x +this.width, y1);
		}
	}
	
	/**
	 * 画出各个级别所对应的颜色
	 */
	private void drawGradeDescs() {
		float y =this.y+this.height+ this.gradeRectangeGap;
		float x =this.x+ this.width-15;
		float rectangleWidth = 0;

		RectangleLineThroughGraph gradeGraph = new RectangleLineThroughGraph(this.writer, this.contentByte,
				this.document);
		for (int len = this.gradeColors.length, i = len - 1; i >= 0; i--) {
			rectangleWidth = 2 * this.calTextWidth(this.fontSize, this.gradeNames[i]);
			x -= rectangleWidth;

			gradeGraph.setBaseChart(this);
			gradeGraph.setBaseFont(this.baseFont);
			gradeGraph.setHasLineThrough(true).setHeight(15).setFontSize(this.fontSize).setLineThroughLength(30)
					.setY(y).setX(x).setText(this.gradeNames[i]).setWidth(rectangleWidth).setColor(this.gradeColors[i])
					.setFontColor(0xFFFFFF);

			gradeGraph.chart();
			x =x - 40;
		}
	}

	/**
	 * 表格体，文本显示部分
	 * @param borderColor
	 * @param fontColor
	 * @throws Exception
	 */
	private void addTableBody(BaseColor fontColor) throws Exception {
		float sepWidth=this.width/this.itemNames.length,
				x0=this.x,y0=this.y-2;
		
		this.contentByte.setFontAndSize(this.baseFont, this.fontSize);
		this.contentByte.setLineWidth(1.5f);
		
		BaseColor tableBorderColor_=new BaseColor(this.tableBorderColor);
		this.contentByte.setColorStroke(tableBorderColor_);
		this.moveLine(this.contentByte, x0, y0, x0+this.width, y0);
		float temp=calTableHeadHeight(sepWidth);
		this.positionY=temp;
		//表头中文信息
		for(int i=0,len=this.itemNames.length;i<len;i++){
			this.moveRect(this.contentByte, x0, y0, x0+sepWidth-1, y0-temp, this.tableBackgroundColor);
			this.contentByte.setColorStroke(tableBorderColor_);
			this.moveLine(this.contentByte, x0+sepWidth, y0, x0+sepWidth, y0-temp);
			this.moveLine(this.contentByte, x0, y0, x0, y0-temp);
			
			this.contentByte.setColorFill(fontColor);
			this.moveMultiLineWText(this.contentByte, this.itemNames[i], this.fontSize,
							sepWidth, temp, x0, y0, 0);
			x0+=sepWidth;
		}
		
		//表格底部的边框
		y0-=temp;
		x0=this.x;
		this.contentByte.setColorStroke(tableBorderColor_);
		this.moveLine(this.contentByte, x0, y0, x0+this.width, y0);
		
		//画出分数
		for(int i=0,len=this.scores.length;i<len;i++){
			this.moveRect(this.contentByte, x0, y0, x0+sepWidth-1, y0-this.lineHeight, this.tableBackgroundColor);
			this.contentByte.setColorStroke(tableBorderColor_);
			this.moveLine(this.contentByte, x0+sepWidth, y0, x0+sepWidth, y0-this.lineHeight);
			this.moveLine(this.contentByte, x0, y0, x0, y0-this.lineHeight);
			
			this.contentByte.setColorFill(fontColor);
			this.moveMultiLineText(this.contentByte, this.scores[i]+"", this.fontSize,
							sepWidth, lineHeight, x0, y0, 0);
			x0+=sepWidth;
		}
		
		//表格底部的边框
		y0-=this.lineHeight;
		this.positionY+=this.lineHeight;
		x0=this.x;
		this.contentByte.setColorStroke(tableBorderColor_);
		this.moveLine(this.contentByte, x0, y0, x0+this.width, y0);
	}
	
	private float calTableHeadHeight(float width) {
		String maxLenText="";
		for (int i = 0; i < this.itemNames.length; i++) {
			if(maxLenText.length()<this.itemNames[i].length()){
				maxLenText=this.itemNames[i];
			}
		}
		return this.calRealHeight(maxLenText, this.fontSize, width, this.lineHeight);
	}
	
	public TableGradeSimpleAreaCurveChart setWidth(float width) {
		this.width = width;
		return this;
	}

	public TableGradeSimpleAreaCurveChart setScores(float[] scores) {
		this.scores = scores;
		return this;
	}

	public TableGradeSimpleAreaCurveChart setCurveColor(int curveColor) {
		this.curveColor = curveColor;
		return this;
	}

	public TableGradeSimpleAreaCurveChart setLineHeight(float lineHeight) {
		this.lineHeight = lineHeight;
		return this;
	}

	public TableGradeSimpleAreaCurveChart setFontSize(float fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	public TableGradeSimpleAreaCurveChart setLevelFontSize(float levelFontSize) {
		this.levelFontSize = levelFontSize;
		return this;
	}

	public float getPositionY() {
		return this.y-this.positionY-10;
	}

	public TableGradeSimpleAreaCurveChart setFontColor(int fontColor) {
		this.fontColor = fontColor;
		return this;
	}

	public TableGradeSimpleAreaCurveChart setBorderColor(int borderColor) {
		this.borderColor = borderColor;
		return this;
	}

	public TableGradeSimpleAreaCurveChart setGradeNames(String[] gradeNames) {
		this.gradeNames = gradeNames;
		return this;
	}

	public TableGradeSimpleAreaCurveChart setGradeColors(int[] gradeColors) {
		this.gradeColors = gradeColors;
		return this;
	}

	public TableGradeSimpleAreaCurveChart setGradeUpperLimitScore(float[] gradeUpperLimitScore) {
		this.gradeUpperLimitScore = gradeUpperLimitScore;
		return this;
	}

	public TableGradeSimpleAreaCurveChart setVerticalLineColor(int verticalLineColor) {
		this.verticalLineColor = verticalLineColor;
		return this;
	}

	public TableGradeSimpleAreaCurveChart setVerticalAreaColor(int verticalAreaColor) {
		this.verticalAreaColor = verticalAreaColor;
		return this;
	}

	public TableGradeSimpleAreaCurveChart setGradeRectangeGap(float gradeRectangeGap) {
		this.gradeRectangeGap = gradeRectangeGap;
		return this;
	}

	public TableGradeSimpleAreaCurveChart setHeight(float height) {
		this.height = height;
		return this;
	}

	public TableGradeSimpleAreaCurveChart setX(float x) {
		this.x = x;
		return this;
	}

	public TableGradeSimpleAreaCurveChart setY(float y) {
		this.y = y;
		return this;
	}

	public TableGradeSimpleAreaCurveChart setTableBackgroundColor(int tableBackgroundColor) {
		this.tableBackgroundColor = tableBackgroundColor;
		return this;
	}

	public TableGradeSimpleAreaCurveChart setItemNames(String[] itemNames) {
		this.itemNames = itemNames;
		return this;
	}

	public TableGradeSimpleAreaCurveChart setLevels(float[] levels) {
		this.levels = levels;
		return this;
	}
}
