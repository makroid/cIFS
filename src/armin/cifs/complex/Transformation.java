package armin.cifs.complex;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Transformation {
	private int _color;
	
	abstract public Complex apply(Complex z);	
	abstract public void paint(Canvas c, Paint p);
	public int getColor() { return _color; }
	public void setColor(int col) { _color = col; }
}
