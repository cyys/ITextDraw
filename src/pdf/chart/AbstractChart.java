package pdf.chart;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import pdf.base.AbstractBaseChart;

public abstract class AbstractChart extends AbstractBaseChart {
	
	protected PdfWriter writer;
	protected PdfContentByte contentByte;
	protected Document document;
	protected BaseFont baseFont;
	
	public abstract void chart();
	
	public AbstractChart(PdfWriter writer, PdfContentByte contentByte,
										Document document, BaseFont baseFont) {
		super();
		this.writer = writer;
		this.contentByte = contentByte;
		this.document = document;
		this.baseFont = baseFont;
	}

	public AbstractChart() {
		super();
	}

	public void setWriter(PdfWriter writer) {
		this.writer = writer;
	}

	public void setContentByte(PdfContentByte contentByte) {
		this.contentByte = contentByte;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public void setBaseFont(BaseFont baseFont) {
		this.baseFont = baseFont;
	}
}
