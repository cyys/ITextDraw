package pdf.base;

import com.itextpdf.text.BaseColor;

public class TPoint {
		private float x;
		
		private float y;
		
		private float r;
		
		private BaseColor bg;
		
		public TPoint(){}

		/**
		 * @param x
		 * @param y
		 */
		public TPoint(float x, float y) {
			super();
			this.x = x;
			this.y = y;
		}
		
		/**
		 * @param x
		 * @param y
		 * @param r
		 */
		public TPoint(float x, float y, float r) {
			this.x = x;
			this.y = y;
			this.r = r;
		}
		
		

		/**
		 * @param x
		 * @param y
		 * @param r
		 * @param bg
		 */
		public TPoint(float x, float y, float r, BaseColor bg) {
			super();
			this.x = x;
			this.y = y;
			this.r = r;
			this.bg = bg;
		}

		/**
		 * @return the r
		 */
		public float getR() {
			return r;
		}

		/**
		 * @param r the r to set
		 */
		public void setR(float r) {
			this.r = r;
		}

		/**
		 * @return the x
		 */
		public float getX() {
			return x;
		}

		/**
		 * @param x the x to set
		 */
		public void setX(float x) {
			this.x = x;
		}

		/**
		 * @return the y
		 */
		public float getY() {
			return y;
		}

		/**
		 * @param y the y to set
		 */
		public void setY(float y) {
			this.y = y;
		}

		/**
		 * @return the bg
		 */
		public BaseColor getBg() {
			return bg;
		}

		/**
		 * @param bg the bg to set
		 */
		public void setBg(BaseColor bg) {
			this.bg = bg;
		}
}
