package pdf.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.AsianFontMapper;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.FontMapper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 分区域+多条点线
 * @author ruany
 * @see pdf.chart.LineMulCurveLineAreaTableChart
 */
public class PolyLineChart extends AbstractChart {

	/**
	 * 数据
	 */
	private List<Float[]> dataList;
	/**
	 * 坐标最大值
	 */
	private float maxValue = 9f;
	/**
	 * 坐标行数
	 */
	private int rownum = 9;
	/**
	 * 背景矩形色
	 */
	private Integer[] normBGColor;
	/**
	 * 线色
	 */
	private Integer[] batchColor;
	/**
	 * 模板宽
	 */
	private int templateWidth = 470;
	/**
	 * 模板高
	 */
	private int templateHeight = 200;
	/**
	 * 垂直偏移量
	 */
	private int yOffset = 50;
	/**
	 * 水平偏移量
	 */
	private int xOffset = 50;
	/**
	 * 坐标系左侧文字占位
	 */
	private int tableTitleWidth = 20;
	/**
	 * 上留白
	 */
	private int before = 5;
	/**
	 * 左边距
	 */
	private int leftBlank = 20;
	
	private float positionY;// 画完表格之后，当前所在的横坐标

	public PolyLineChart() {
		super();
	}

	public PolyLineChart(PdfWriter writer, PdfContentByte contentByte, Document document, BaseFont baseFont) {
		super(writer, contentByte, document, baseFont);
	}

	@Override
	public void chart() {
		this.positionY = 0;
		
		if (ObjectUtils.equals(null, this.dataList) || this.dataList.isEmpty()) {
			throw new RuntimeException("请检测dataList数据是否存在！");
		}

		if (ObjectUtils.equals(null, this.normBGColor) || this.normBGColor.length < 1) {
			this.normBGColor = new Integer[] { 0xDEF5FF, 0xEEF7DF, 0xFFF5DD };
		}

		if (ObjectUtils.equals(null, this.batchColor) || this.batchColor.length < 1) {
			this.batchColor = new Integer[] { 0x9792DD, 0xFFCE54, 0x72CCAD, 0xE57D54, 0x89CFFF };
		}

		// 图形格宽
		int chartColWidth = (this.templateWidth - this.tableTitleWidth) / this.normBGColor.length;
		// 模板宽
		this.templateWidth = this.tableTitleWidth + chartColWidth * this.normBGColor.length;
		// 图形行高
		int chartRowHeight = (this.templateHeight - this.before) / (this.rownum);
		// 图形高
		int chartHeight = chartRowHeight * this.rownum;
		// 模板高
		this.templateHeight = chartHeight + this.before;

		FontMapper fm = new AsianFontMapper(AsianFontMapper.ChineseSimplifiedFont,
				AsianFontMapper.ChineseSimplifiedEncoding_H);
		PdfTemplate template = contentByte.createTemplate(this.templateWidth, this.templateHeight); // 绘制图形模板.
		// 模板位置 ,这个地方的 y 是从下面计算的（比较特殊）
		this.contentByte.addTemplate(template, this.xOffset, this.document.getPageSize().getHeight() - this.templateHeight -this.yOffset);
		Graphics2D g2d = template.createGraphics(this.templateWidth, this.templateHeight, fm); // 绘图接口，继承自Graphics2D

		// 画矩形底色
		for (int j = 0; j < this.normBGColor.length; j++) {
			g2d.setColor(new Color(this.normBGColor[j]));
			g2d.fillRect(this.tableTitleWidth + j * chartColWidth, this.before, chartColWidth, chartHeight);
		}

		Stroke dash = new BasicStroke(1f, BasicStroke.CAP_BUTT, 
				BasicStroke.JOIN_ROUND, 1f,
				 // 线长
				new float[] { 4, 3, 1, 3 },0f);
		// 画横线 画10根线 和 左边的参数值ֵ
		for (int i = 0; i < this.rownum + 1; i++) {
			// 虚线
			g2d.setColor(Color.GRAY);
			// 设置虚线样式
			
			g2d.setStroke(dash);
			if (i < this.rownum)
				g2d.drawLine(this.tableTitleWidth, before + i * chartRowHeight, this.templateWidth, this.before + i * chartRowHeight);
			// 左边的参数值
			g2d.setColor(Color.GRAY);
			if (i < this.rownum) {
				g2d.drawString((this.maxValue - i * (this.maxValue / this.rownum)) + "", this.tableTitleWidth - this.leftBlank,
						this.before + i * chartRowHeight + 4);
			} else {
				g2d.drawString((this.maxValue - i * (this.maxValue / this.rownum)) + "", this.tableTitleWidth - this.leftBlank,
						this.before + i * chartRowHeight);
			}
		}

		// 画左边的竖线
		dash = new BasicStroke(1);
		g2d.setStroke(dash);
		g2d.setColor(Color.GRAY);
		g2d.drawLine(this.tableTitleWidth, this.before, this.tableTitleWidth, this.before + chartHeight);

		// 数据操作
		Float[] datas;
		float score;
		int x;
		int y;
		List<List<int[]>> dots = new ArrayList<List<int[]>>();
		List<int[]> dot;
		for (int i = 0; i < this.dataList.size(); i++) {
			dot = new ArrayList<int[]>();
			dots.add(dot);
			datas = this.dataList.get(i);
			for (int j = 0; j < datas.length; j++) {
				score = datas[j];
				// 画数据圆点
				g2d.setColor(new Color(this.batchColor[i]));
				x = this.tableTitleWidth + j * chartColWidth + chartColWidth / 2;
				y = this.before + (int) ((this.maxValue - score) / this.maxValue * chartHeight);
				g2d.fillOval(x - 2, y - 2, 4, 4);
				g2d.setColor(Color.WHITE);
				g2d.fillOval(x - 1, y - 1, 2, 2);
				dot.add(new int[] { x, y });
			}
		}
		// 画折线
		for (int i = 0; i < dots.size(); i++) {
			g2d.setColor(new Color(this.batchColor[i]));
			dot = dots.get(i);
			for (int j = 0; j < dot.size() - 1; j++) {
				g2d.drawLine(dot.get(j)[0] + 1, dot.get(j)[1], dot.get(j + 1)[0] - 1, dot.get(j + 1)[1]);
			}
		}
		g2d.dispose();
	}

	public PolyLineChart setDataList(List<Float[]> dataList) {
		this.dataList = dataList;
		return this;
	}

	public PolyLineChart setMaxValue(float maxValue) {
		this.maxValue = maxValue;
		return this;
	}

	public PolyLineChart setRownum(int rownum) {
		this.rownum = rownum;
		return this;
	}

	public PolyLineChart setNormBGColor(Integer[] normBGColor) {
		this.normBGColor = normBGColor;
		return this;
	}

	public PolyLineChart setBatchColor(Integer[] batchColor) {
		this.batchColor = batchColor;
		return this;
	}

	public PolyLineChart setTemplateWidth(int templateWidth) {
		this.templateWidth = templateWidth;
		return this;
	}

	public PolyLineChart setTemplateHeight(int templateHeight) {
		this.templateHeight = templateHeight;
		return this;
	}

	public PolyLineChart setyOffset(int yOffset) {
		this.yOffset = yOffset;
		return this;
	}

	public PolyLineChart setxOffset(int xOffset) {
		this.xOffset = xOffset;
		return this;
	}

	public PolyLineChart setTableTitleWidth(int tableTitleWidth) {
		this.tableTitleWidth = tableTitleWidth;
		return this;
	}

	public PolyLineChart setBefore(int before) {
		this.before = before;
		return this;
	}

	public PolyLineChart setLeftBlank(int leftBlank) {
		this.leftBlank = leftBlank;
		return this;
	}
	
	public float getPositionY() {
		this.positionY=this.writer.getVerticalPosition(true)+30;
		this.positionY-=this.templateHeight;
		this.positionY-=this.yOffset;
		return this.positionY;
	}
}
