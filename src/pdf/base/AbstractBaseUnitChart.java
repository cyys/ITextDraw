package pdf.base;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public abstract class AbstractBaseUnitChart {
	private AbstractBaseChart baseChart;
	
	protected PdfWriter writer;
	protected PdfContentByte contentByte;
	protected Document document;
	protected BaseFont baseFont;
	
	public abstract void chart();

	public AbstractBaseUnitChart(AbstractBaseChart baseChart,
			PdfWriter writer,PdfContentByte contentByte,Document document) {
		this.baseChart = baseChart;
		this.writer=writer;
		this.contentByte=contentByte;
		this.document=document;
	}

	public AbstractBaseUnitChart(PdfWriter writer,PdfContentByte contentByte,Document document) {
		this.writer=writer;
		this.contentByte=contentByte;
		this.document=document;
	}
	
	public void setBaseChart(AbstractBaseChart baseChart) {
		this.baseChart = baseChart;
	}

	public void setBaseFont(BaseFont baseFont) {
		this.baseFont = baseFont;
	}

	public AbstractBaseChart getBaseChart() {
		return baseChart;
	}
}
