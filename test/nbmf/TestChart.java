package nbmf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.junit.Test;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import pdf.nbmf.CircleSeparateManySectorsChart;
import pdf.nbmf.TableChartedPositionAreaCurveChart;
import pdf.nbmf.TableChartedPositionAreaCurveScoresChart;
import pdf.nbmf.TableGradeDistributionOnlyTextChart;

public class TestChart {
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
	public void testTableGradeDistributionOnlyTextChart() throws Exception {
		doc.open();

		TableGradeDistributionOnlyTextChart tableGradeDistributionChart = new TableGradeDistributionOnlyTextChart(writer,
				writer.getDirectContent(), doc, bfChinese);
		tableGradeDistributionChart.setX(80).setY(550)
		.setGradeDescs(new String[]{"前前前前15% ","前15%-前45% ","后55%-后15% ","后15% "})
		.setMaxNumber(100).setCurNumber(15f)
		.setMarkCurPositon(false).setWidth(200)
		.setShowSepLine(true)
		;

		tableGradeDistributionChart.chart();

		writer.getDirectContent().setColorStroke(BaseColor.BLACK);

		float y = tableGradeDistributionChart.getPositionY();
		tableGradeDistributionChart.moveLine(writer.getDirectContent(), 0, y, 550, y);

		doc.close();
	}
	
	@Test
	public void testCircleSeparateManySectorsChart() throws Exception {
		doc.open();

		CircleSeparateManySectorsChart tableGradeDistributionChart = new CircleSeparateManySectorsChart(writer,
				writer.getDirectContent(), doc, bfChinese);
		tableGradeDistributionChart.setX(doc.getPageSize().getWidth()/2).setY(550)
		.setScores(new float[]{10,
				20,50,
				60})
		.setR(120)
		.setItemNames(new String[]{"我认识我认识去认识我认识",
				"爱我发给爱我去给爱我发给","爱我发给爱我去给爱我发给",
				"还让他杀人还让他"}).setTotalScore(50.6f).setTotalScoreNameColor(0x16CA4)
		.setFillColors(new int[] { 0x1897BD, 0x2DBFD5, 0xBFBFBF, 0x16CA4})
		;
 
		tableGradeDistributionChart.chart();

		writer.getDirectContent().setColorStroke(BaseColor.BLACK);

		float y = tableGradeDistributionChart.getPositionY();
		tableGradeDistributionChart.moveLine(writer.getDirectContent(), 0, y, 550, y);

		doc.close();
	}
	
	@Test
	public void testTableChartedPositionAreaCurveChart() throws Exception {
		doc.open();

		for (int i = 0; i < 30; i++) {
			doc.add(new Paragraph("极乐空间阿尔法", new Font(bfChinese)));
		}

		TableChartedPositionAreaCurveChart chart = new TableChartedPositionAreaCurveChart(writer,
				writer.getDirectContent(), doc, bfChinese);

		chart.setWidth(450)
		.setFontSize(8)
		.setLineHeight(20)
		.setWidths(new float[] { 10, 35, 5, 50 })
		.setScoreLevels(new int[] { 0, 1, 2, 3, 4,5 })
		.setRowColors(new int[] { 0xFFFFFF, 0xF2F2F2 })
		.setLevelFontSize(7)
		.setColNumber(2)
		.setScores(new float[] {   1.5f ,   2.25f ,   3 ,  4 ,
 			  4.2f ,  1.5f ,   0f , 5 , 0.5f ,  
			 1 ,  3.5f ,  3 ,  3.5f })
		.setGradeUpperLimitScore(new float[]{3f,2.25f,1.5f})
		.setTableHeads(new String[] { "指  标", "维度", "得得分", "  " })
		.setChildrenTypes(new ArrayList<String[]>() {
			{
				add(new String[] { "沟通能力沟通能力沟通能力沟通能力沟通能力", "合作能力"});
				add(new String[] { "语言能力沟通能力沟通能力沟通能力沟通能力", "数字能力"});
				add(new String[] { "组织归", "物质回报", "理想抱负", "自我实现", "获得尊重"
						, "创新能力属组织归属组织归属组织归属组织归属组织归属组织归属", "学习能力", "逻辑能力", "机械推理"   });
			}
		})
		.setParentTypes(new String[] { "基础机械推械推", "基本", "领导" });

		chart.chart();
		
		writer.getDirectContent().setColorStroke(BaseColor.BLACK);

		float y = chart.getPositionY();
		chart.moveLine(writer.getDirectContent(), 0, y, 550, y);

		doc.close();
	}
	
	@Test
	public void testTableChartedPositionAreaCurveScoresChart() throws Exception {
		doc.open();

		for (int i = 0; i < 30; i++) {
			doc.add(new Paragraph("极乐空间阿尔法", new Font(bfChinese)));
		}

		TableChartedPositionAreaCurveScoresChart chart = new TableChartedPositionAreaCurveScoresChart(writer,
				writer.getDirectContent(), doc, bfChinese);

		chart.setWidth(450)
		.setFontSize(8)
		.setLineHeight(15)
		.setWidths(new float[] { 10, 30,5,5,5, 5, 50 })
		.setScoreLevels(new int[] { 0, 1, 2, 3, 4,5 })
		.setRowColors(new int[] { 0xFFFFFF, 0xF2F2F2 })
		.setLevelFontSize(7)
		.setMaxScoreColNum(3)
		.setMinScoreColNum(2)
		.setCurScoreColNum(0)
		.setScores(new float[][] { { 3f, 2f,1.5f,4f }, { 2, 2.5f, 2.0f,3.5f }, { 3f, 2f,1.5f,4f }, { 2, 1, 0.8f ,2.2f},
 			{ 3.2f, 2, 1.85f,3.2f }, { 2.3f, 3.1f, 1.05f,3.2f }, { 5, 4, 3.02f,5f }, { 2, 2.5f, 2.0f,3.5f },  { 2, 1, 0.8f ,2.2f},
 			{ 3.2f, 2, 1.85f,3.2f },{ 2.3f, 3.1f, 1.05f,3.2f }, { 3f, 2f,1.5f,4f }, { 5, 4, 3.02f,5f }})
		.setTableHeads(new String[] { "指  标", "维度", "得分", "平均", "最低", "最高", "  " })
		.setChildrenTypes(new ArrayList<String[]>() {
			{
				add(new String[] { "沟通能力沟通能力沟通能力沟通能力沟通能力", "合作能力"});
				add(new String[] { "语言能力沟通能力沟通能力沟通能力沟通能力", "数字能力"});
				add(new String[] { "组织归", "物质回报", "理想抱负", "自我实现", "获得尊重"
						, "创新能力属组织归属组织归属组织归属组织归属组织归属组织归属", "学习能力", "逻辑能力", "机械推理"   });
			}
		})
		.setParentTypes(new String[] { "基础机械推械推", "基本", "领导" });

		chart.chart();
		
		writer.getDirectContent().setColorStroke(BaseColor.BLACK);

		float y = chart.getPositionY();
		chart.moveLine(writer.getDirectContent(), 0, y, 550, y);

		doc.close();
	}
}
