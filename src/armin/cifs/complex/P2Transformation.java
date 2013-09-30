package armin.cifs.complex;

import android.graphics.Canvas;
import android.graphics.Paint;

public class P2Transformation extends Transformation {
	private ComplexPoint _A;
	private ComplexPoint _B;
	private ComplexPoint _C;
	private ComplexPoint _D;
	
	public P2Transformation(ComplexPoint A, 
							ComplexPoint B, 
							ComplexPoint C, 
							ComplexPoint D) {
		_A = A;
		_B = B;
		_C = C;
		_D = D;
	}
	
	// (A,B) -> (C,D)
	// f(z) = (z-A)(D-C)/(B-A) + C 
	public Complex apply(Complex z) {
		Complex one = z.sub(_A.z);
		Complex two = _D.z.sub(_C.z);
		Complex three = one.mult(two);
		Complex four = _B.z.sub(_A.z);
		Complex five = three.div(four);
		return five.add(_C.z);
	}
	
	public void paint(Canvas canvas, Paint paint) {				
		paint.setColor(getColor());
		canvas.drawLine(_A.z.getReal(), _A.z.getImag(), _C.z.getReal(), _C.z.getImag(), paint);
		canvas.drawLine(_B.z.getReal(), _B.z.getImag(), _D.z.getReal(), _D.z.getImag(), paint);
	}
	
	public String toString() {
		String name = "(" + _A.name + "=" + _A.z + "," + _B.name + "=" + _B.z +")->";
		name += "(" + _C.name + "=" + _C.z + "," + _D.name + "=" +_D.z + ")";
		return name;
	}
	
}
