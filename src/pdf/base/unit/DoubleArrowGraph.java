package pdf.base.unit;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import pdf.base.AbstractBaseChart;
import pdf.base.AbstractBaseUnitChart;
import pdf.base.TPoint;
/**
 * 双箭头图
 * @author cheny
 *
 */
public class DoubleArrowGraph extends AbstractBaseUnitChart {
	
	//是横向还是纵向
	public static final int LEVEL_X=1;
	public static final int LEVEL_Y=2;
	
	private float x;
	private float y;
	private float x0;
	private float y0;
	private float height;
	private float arrowWidth=5;
	private int level=LEVEL_X;
	private int color;
	private float arrowLineWidth=1.5f;
	

	public DoubleArrowGraph(AbstractBaseChart baseChart, PdfWriter writer, PdfContentByte contentByte,
			Document document) {
		super(baseChart, writer, contentByte, document);
	}

	public DoubleArrowGraph(PdfWriter writer, PdfContentByte contentByte, Document document) {
		super(writer, contentByte, document);
	}

	@Override
	public void chart() {
		TPoint onePoint=new TPoint();
		TPoint onePoint_1=new TPoint();
		
		TPoint anotherPoint=new TPoint();
		TPoint anotherPoint_1=new TPoint();
		switch (level) {
		case LEVEL_X:{
			onePoint.setX(this.x+this.arrowWidth);
			onePoint.setY(this.y+this.height/2);
			
			onePoint_1.setX(this.x+this.arrowWidth);
			onePoint_1.setY(this.y-this.height/2);
			
			anotherPoint.setX(this.x0-this.arrowWidth);
			anotherPoint.setY(this.y0-this.height/2);
			
			anotherPoint_1.setX(this.x0-this.arrowWidth);
			anotherPoint_1.setY(this.y0+this.height/2);
			break;
		}
		case LEVEL_Y:{
			onePoint.setX(this.x+this.height/2);
			onePoint.setY(this.y-this.arrowWidth);
			
			onePoint_1.setX(this.x-this.height/2);
			onePoint_1.setY(this.y-this.arrowWidth);
			
			anotherPoint.setX(this.x0-this.height/2);
			anotherPoint.setY(this.y0+this.arrowWidth);
			
			anotherPoint_1.setX(this.x0+this.height/2);
			anotherPoint_1.setY(this.y0+this.arrowWidth);
			break;
		}
		default:
			break;
		}
		
		this.getBaseChart().moveRect(this.contentByte, onePoint.getX(), onePoint.getY()
													, anotherPoint.getX(), anotherPoint.getY(), this.color);
		
		this.contentByte.setLineWidth(this.arrowLineWidth);
		this.contentByte.setColorStroke(new BaseColor(this.color));
		this.getBaseChart().moveLine(this.contentByte, onePoint.getX(), onePoint.getY(), this.x, this.y);
		this.getBaseChart().moveLine(this.contentByte, onePoint_1.getX(), onePoint_1.getY(), this.x, this.y);
		 
		this.getBaseChart().moveLine(this.contentByte, anotherPoint.getX(), anotherPoint.getY(), this.x0, this.y0);
		this.getBaseChart().moveLine(this.contentByte, anotherPoint_1.getX(), anotherPoint_1.getY(), this.x0, this.y0);
	}

	public DoubleArrowGraph setX(float x) {
		this.x = x;
		return this;
	}

	public DoubleArrowGraph setY(float y) {
		this.y = y;
		return this;
	}

	public DoubleArrowGraph setX0(float x0) {
		this.x0 = x0;
		return this;
	}

	public DoubleArrowGraph setY0(float y0) {
		this.y0 = y0;
		return this;
	}

	public DoubleArrowGraph setHeight(float height) {
		this.height = height;
		return this;
	}

	public DoubleArrowGraph setArrowWidth(float arrowWidth) {
		this.arrowWidth = arrowWidth;
		return this;
	}

	public DoubleArrowGraph setLevel(int level) {
		this.level = level;
		return this;
	}

	public DoubleArrowGraph setColor(int color) {
		this.color = color;
		return this;
	}

	public DoubleArrowGraph setArrowLineWidth(float arrowLineWidth) {
		this.arrowLineWidth = arrowLineWidth;
		return this;
	}
}
