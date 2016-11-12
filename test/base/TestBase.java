package base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.junit.Test;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import pdf.base.AbstractBaseChart;
import pdf.base.text.DiamondBeforeParagraph;
import pdf.base.unit.ArrowGraph;
import pdf.base.unit.BalloonGraph;
import pdf.base.unit.CurveHasRadianGraph;
import pdf.base.unit.DoubleArrowGraph;
import pdf.base.unit.FiveTopStarGraph;
import pdf.base.unit.NoteTipGraph;
import pdf.base.unit.PolygonGraph;
import pdf.base.unit.TwoTriangleGraph;

public class TestBase {
	
	 //写入器
	private static PdfWriter writer;
	 //定义的文档对象
	private static Document doc ;
	//打开文档对象
	private static BaseFont bfChinese;
   
	static{
		try {
			//定义输出位置并将新创建的文件放入输出对象中
			OutputStream outputStream = new FileOutputStream(
					new File("C:\\Users\\cheny\\Desktop\\temp\\MyJSBPDF.pdf"));
			
			doc = new Document(PageSize.A4);
			
			writer=PdfWriter.getInstance(doc, outputStream);
			
			bfChinese = BaseFont.createFont("D:\\PDF\\font\\msyh.ttf",
					BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testTwoTriangleGraph() throws Exception{
		doc.open();
		
		TwoTriangleGraph twoTriangleGraph=new TwoTriangleGraph(writer, writer.getDirectContent(), doc);
		twoTriangleGraph.setBaseChart(new TestBaseChart());
		twoTriangleGraph.setHeight(10).setWidth(10).setX(100).setY(writer.getVerticalPosition(true));
		twoTriangleGraph.chart();
		
        doc.close();
	}
	
	@Test
	public void testPolygon() throws Exception{
		doc.open();
		
		PolygonGraph polygon=new PolygonGraph(writer, writer.getDirectContent(), doc);
		polygon.setBaseChart(new TestBaseChart());
		polygon.setR(7).setRotation(0).setSideNum(6).setX(200).setY(400).setFillColor(BaseColor.BLUE);
		polygon.chart();
		
        doc.close();
	}
	
	@Test
	public void testBaseChar() throws Exception{
		doc.open();
		
		AbstractBaseChart chart=new TestBaseChart();
		PdfContentByte cb=writer.getDirectContent();
		cb.setColorStroke(BaseColor.BLUE);
		cb.setColorFill(BaseColor.BLUE);
		chart.moveCircle(cb, 200, 400, 20, true);
		
		chart.moveRoundRect(cb, 200, 300, 120,50, true);
		
		chart.moveRect(cb, 200, 200, 300,250,0xFF0000, true);
		
        doc.close();
	}
	
	@Test
	public void testArrowGraph() throws Exception{
		doc.open();
		
		ArrowGraph chart=new ArrowGraph(writer, writer.getDirectContent(), doc);
		chart.setBaseChart(new TestBaseChart());
		chart.setArrowWidth(5).setX(200).setY(400).setWidth(5).setHeight(15).setColor(0xFF0000)
		.setArrowDirection(ArrowGraph.ARROW_DOWN);
		
		chart.chart();
		
		chart.setArrowWidth(5).setX(200).setY(300).setWidth(5).setHeight(15).setColor(0xFF0000)
		.setArrowDirection(ArrowGraph.ARROW_UP);
		
		chart.chart();
		
		chart.setArrowWidth(5).setX(300).setY(250).setWidth(5).setHeight(15).setColor(0xFF0000)
		.setArrowDirection(ArrowGraph.ARROW_RIGHT);
		
		chart.chart();
		
		chart.setArrowWidth(5).setX(300).setY(350).setWidth(5).setHeight(15).setColor(0xFF0000)
		.setArrowDirection(ArrowGraph.ARROW_LEFT);
		
		chart.chart();
		
        doc.close();
	}
	
	@Test
	public void testFiveTopStarGraph() throws Exception{
		doc.open();
		
		FiveTopStarGraph chart=new FiveTopStarGraph(writer, writer.getDirectContent(), doc);
		chart.setBaseChart(new TestBaseChart());
		chart.setX(200).setY(600).setRotation(0).setR(6).setColor(BaseColor.RED).setFillColor(true);
		
		chart.chart();
		
		chart.setX(300).setY(600).setRotation(0).setR(6).setColor(BaseColor.RED).setFillColor(false);
		
		chart.chart();
		
        doc.close();
	}
	
	@Test
	public void testNoteTipGraph() throws Exception{
		doc.open();
		
		NoteTipGraph chart=new NoteTipGraph(writer, writer.getDirectContent(), doc);
		chart.setBaseChart(new TestBaseChart());
		chart.setX(300).setY(600).setWidth(20).setHeight(15).setColor(0xFF0000);
		
		chart.chart();
		
        doc.close();
	}
	
	@Test
	public void testDoubleArrowGraph() throws Exception{
		doc.open();
		
		DoubleArrowGraph chart=new DoubleArrowGraph(writer, writer.getDirectContent(), doc);
		chart.setBaseChart(new TestBaseChart());
		chart.setX(300).setY(600).setX0(300).setY0(600).setHeight(15).setColor(0xFF0000)
		.setArrowWidth(5).setArrowLineWidth(1.2f);
		
		chart.chart();
		
//		chart.setX(400).setY(600).setX0(400).setY0(500).setHeight(15).setColor(0xFF0000).
//		setArrowWidth(10).setArrowLineWidth(1.5f).setLevel(DoubleArrowGraph.LEVEL_Y);
//		
//		chart.chart();
		
        doc.close();
	}
	
	@Test
	public void testCurveHasRadianGraph() throws Exception{
		doc.open();
		 
		CurveHasRadianGraph chart=new CurveHasRadianGraph(writer, writer.getDirectContent(), doc);
		
		chart.setBaseChart(new TestBaseChart());
		
		chart.setX(10).setY(550).setX0(20).setY0(520).setHasCircle(true).setColor(0xff0);
		
		chart.chart();
		
		writer.getDirectContent().setLineDash(1,1);
		
		chart.setX(20).setY(520).setX0(200).setY0(490);
		
		chart.chart();
		 
        doc.close();
	}
	
	@Test
	public void testBalloonGraph() throws Exception{
		doc.open();
		 
		BalloonGraph chart=new BalloonGraph(writer, writer.getDirectContent(), doc);
		
		chart.setBaseChart(new TestBaseChart());
		
		chart.setX(200).setY(550);
		
		chart.chart();
		
		 
		 
        doc.close();
	}
	
	@Test
	public void testDiamondBeforeParagraph() throws Exception{
		doc.open();
		 
		DiamondBeforeParagraph chart=new DiamondBeforeParagraph(writer, writer.getDirectContent(), doc);
		
		chart.setBaseChart(new TestBaseChart());
		chart.setBaseFont(bfChinese);
		
		chart.setItemNames(new String[]{"能力能力能力","我就垃圾费","邮箱箱邮邮箱","散步散步",
				"散步散步","散步散步","我就垃圾费","邮箱箱邮邮箱","散步散步","我就垃圾费","邮箱箱邮邮箱","散步散步",
				"散步散步","散步散步","我就垃圾费","邮箱箱邮邮箱","散步散步"})
		       .setDescs(new String[]{"发违法","案件范围","家居服挖坟","家居服挖坟","家居服挖坟","家居服挖坟"
				,"家居服挖坟","家居服挖坟","家居服挖坟","案件范围","家居服挖坟","家居服挖坟","家居服挖坟","家居服挖坟"
				,"家居服挖坟","家居服挖坟","家居服挖坟"});
		
		chart.chart();
		 
        doc.close();
	}
}