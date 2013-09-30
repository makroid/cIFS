package armin.cifs.complex;

import android.graphics.Canvas;
import android.graphics.Paint;

public class InvTransformation extends Transformation {

	private ComplexPoint _center;
	private ComplexPoint _through;
	
	public InvTransformation(ComplexPoint center, ComplexPoint through) {
		_center = center;
		_through = through;
	}
	
	@Override
	public Complex apply(Complex z) {
		// r^2/(conj(z) - conj(center)) + center
		float radius = _center.z.sub(_through.z).length();
		Complex r = new Complex(radius, 0.0f);
		return r.mult(r).div(z.conjugate().sub(_center.z.conjugate())).add(_center.z);
	}

	@Override
	public void paint(Canvas canvas, Paint paint) {
		float radius = _center.z.sub(_through.z).length();
		paint.setColor(getColor());
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(3);
		canvas.drawCircle(_center.z.getReal(), _center.z.getImag(), radius, paint);
	}

}
