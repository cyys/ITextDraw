package chart;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.junit.Test;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import pdf.chart.HistogramCurveExtremumChart;
import pdf.chart.HistogramGradeDistributionChart;
import pdf.chart.TableDashTableRectChart;

public class TestChart2 {
	// 写入器
	private static PdfWriter writer;
	// 定义的文档对象
	private static Document doc;
	// 打开文档对象
	private static BaseFont bfChinese;

	static {
		try {
			// 定义输出位置并将新创建的文件放入输出对象中
			OutputStream outputStream = new FileOutputStream(new File("C:\\Users\\cheny\\Desktop\\temp\\MyJSBPDF.pdf"));

			doc = new Document(PageSize.A4);

			writer = PdfWriter.getInstance(doc, outputStream);

			bfChinese = BaseFont.createFont("D:\\PDF\\font\\msyh.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testHistogramCurveExtremumChart() throws Exception {
		doc.open();

		HistogramCurveExtremumChart histogramCurveExtremumChart = new HistogramCurveExtremumChart(writer, writer.getDirectContent(), doc, bfChinese);

		histogramCurveExtremumChart.setX(100).setY(500).setWidth(200)
		.setItemNames(new String[]{"我我我我我我我","你","他","他"})
		.setMaxScores(new float[]{2.5f,8,6,9}).setMinScores(new float[]{1.5f,2.5f,5f,5.4f})
		.setAvgScores(new float[]{1.5f,8f,5f,8.5f});
		
		 histogramCurveExtremumChart.chart();
		 
		 writer.getDirectContent().setColorStroke(BaseColor.BLACK);
		
		doc.close();
	}
	
	@Test
	public void testHistogramGradeDistributionChart() throws Exception {
		doc.open();

		HistogramGradeDistributionChart histogramGradeDistributionChart = new HistogramGradeDistributionChart(writer, writer.getDirectContent(), doc, bfChinese);

		histogramGradeDistributionChart.setX(50).setY(450).setWidth(150).setCellHeight(15)
		.setHeight(150).setFrameLeftOffsetX(100)
		.setItemNames(new String[]{"我我我我我我我","你","他","他","就拉尔夫"})
		.setScores(new float[]{25.229f,30,60,90,50});
		
		histogramGradeDistributionChart.chart();
		 
//		 writer.getDirectContent().setColorStroke(BaseColor.BLACK);
//		
//		 float y1 =histogramGradeDistributionChart.getPositionY();
//		 histogramGradeDistributionChart.moveLine(writer.getDirectContent(), 0, y1, 550, y1);	
		doc.close();
	}

	@Test
	public void testTableDashTableRectChart() throws Exception {
		doc.open();

		TableDashTableRectChart tableDashTableRectChart = new TableDashTableRectChart(writer, writer.getDirectContent(), doc, bfChinese);

		tableDashTableRectChart.setX(40).setY(650).setWidth(250)
		.setHeadNames(new String[]{"我我我我我我我","你","他"})
		.setItemNames(new String[]{"我我我我我我我","你","他","他","就拉尔夫"})
		.setScores(new float[]{25.229f,0,60,90,50});
		
		tableDashTableRectChart.chart();
		 
		 writer.getDirectContent().setColorStroke(BaseColor.BLACK);
		
		 float y1 =tableDashTableRectChart.getPositionY();
		 tableDashTableRectChart.moveLine(writer.getDirectContent(), 0, y1, 550, y1);	
		doc.close();
	}
	
}
