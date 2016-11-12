package pdf.base;

import org.apache.commons.lang3.ObjectUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public abstract class AbstractBaseChart {

	/**
	 * 换行
	 * 
	 * @param num
	 * @param doc
	 * @throws DocumentException
	 */
	public void setLine(int num, Document doc) throws DocumentException {
		for (int i = 0; i < num; i++) {
			doc.add(new Paragraph(" "));
		}
	}

	/**
	 * 指定的字体大小换行
	 * 
	 * @param num
	 * @param fontSize
	 * @param doc
	 * @throws DocumentException
	 */
	public void setLine(int num, float fontSize, Document doc) throws DocumentException {
		Font font = new Font(Font.FontFamily.COURIER, fontSize);
		for (int i = 0; i < num; i++) {
			doc.add(new Paragraph(" ", font));
		}
	}

	public void addCell(PdfPTable table, BaseFont pFont, float fontSize, int fontStyle, int fontColor, String str,
			BaseColor backgroundColor, int num, float height, boolean... hasBorder) {
		PdfPCell pdfPCell = new PdfPCell(
				new Paragraph(str, new Font(pFont, fontSize, fontStyle, new BaseColor(fontColor))));
		pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);// 水平居中Element.ALIGN_CENTER
		pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);// 垂直居中
		pdfPCell.setBackgroundColor(backgroundColor);
		pdfPCell.setFixedHeight(height);
		if (hasBorder.length > 0 && !hasBorder[0])
			pdfPCell.setBorderWidth(PdfPCell.NO_BORDER);// 表格边框为0
		pdfPCell.setRowspan(num);
		table.addCell(pdfPCell);
	}

	public void addCell(PdfPTable table, Font pFont, String str, BaseColor backgroundColor, int num, float height,
			boolean... hasBorder) {
		PdfPCell pdfPCell = new PdfPCell(new Paragraph(str, pFont));
		pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);// 水平居中Element.ALIGN_CENTER
		pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);// 垂直居中
		pdfPCell.setBackgroundColor(backgroundColor);
		pdfPCell.setFixedHeight(height);
		if (hasBorder.length > 0 && !hasBorder[0])
			pdfPCell.setBorderWidth(PdfPCell.NO_BORDER);// 表格边框为0
		pdfPCell.setRowspan(num);
		table.addCell(pdfPCell);
	}

	/**
	 * 边框与背景色相同的
	 * 
	 * @param table
	 * @param pFont
	 * @param str
	 * @param backgroundColor
	 * @param num
	 * @param height
	 * @param hasBorder
	 */
	public void addBorderColorCell(PdfPTable table, Font pFont, String str, BaseColor backgroundColor, int num,
			float height,Integer align, boolean... hasBorder) {
		PdfPCell pdfPCell = new PdfPCell(new Paragraph(str, pFont));
		pdfPCell.setHorizontalAlignment(ObjectUtils.equals(null, align)?Element.ALIGN_CENTER:align);// 水平居中Element.ALIGN_CENTER
		pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);// 垂直居中
		pdfPCell.setBackgroundColor(backgroundColor);
		pdfPCell.setFixedHeight(height);
		if (hasBorder.length > 0 && !hasBorder[0])
			pdfPCell.setBorderColor(backgroundColor);
		pdfPCell.setRowspan(num);
		table.addCell(pdfPCell);
	}

	/**
	 * @param pFont
	 * @param str
	 * @param height
	 * @param fontColor
	 * @param backColor
	 * @return
	 */
	public PdfPCell addCell(Font pFont,String str, float height, BaseColor backColor) {
		PdfPCell cell = new PdfPCell(new Paragraph(str, pFont));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);// 定义水平方向
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);// 定义垂直方向
		cell.setBackgroundColor(backColor);// 背景颜色
		cell.setPaddingTop(6);// 设置上边距
		cell.setPaddingBottom(6);// 设置下边距
		cell.setFixedHeight(height);
		cell.setBorderColor(BaseColor.BLACK);// 设置线的颜色
		cell.setBorderWidth(0.2f);
		return cell;
	}

	public void moveText(PdfContentByte cb, String text, float x1, float y1, int align, float rotation) {
		cb.beginText();
		cb.showTextAligned(align, text, x1, y1, rotation);
		cb.endText();
	}

	public void moveLine(PdfContentByte cb, float x1, float y1, float x2, float y2) {
		cb.moveTo(x1, y1);
		cb.lineTo(x2, y2);
		cb.stroke();
	}

	public void moveRect(PdfContentByte cb, float x1, float y1, float x2, float y2, int color, boolean... hasBorder) {
		Rectangle rect = new Rectangle(x1, y1, x2, y2);
		if (hasBorder.length > 1 && hasBorder[0])
			rect.setBorder(1);// 有边框
		rect.setBackgroundColor(new BaseColor(color));
		cb.rectangle(rect);
	}

	/**
	 * 圆角矩形
	 * 
	 * @param cb
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public void moveRoundRect(PdfContentByte cb, float x, float y, float w, float h, boolean... isFill) {
		cb.roundRectangle(x, y, w, h, h / 2);
		if (isFill.length < 1 || isFill[0])
			cb.fillStroke();
	}

	/**
	 * 填充圆形
	 * 
	 * @param cb
	 * @param x1
	 * @param y1
	 * @param r
	 */
	public void moveCircle(PdfContentByte cb, float x1, float y1, float r, boolean... isFill) {
		cb.circle(x1, y1, r);
		if (isFill.length < 1 || isFill[0])
			cb.fillStroke();
	}

}
