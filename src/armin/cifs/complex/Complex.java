package armin.cifs.complex;

public class Complex {
	private float _real;
	private float _imag;
	private boolean _isNaN;
	private boolean _isInf;
	
	public static final Complex I = new Complex(0.0f, 1.0f);
	public static final Complex NaN = new Complex(Float.NaN, Float.NaN);
	public static final Complex ONE = new Complex(1.0f, 0.0f);    
    public static final Complex ZERO = new Complex(0.0f, 0.0f);
    public static final Complex INF = new Complex(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
	
	public Complex(float real, float imag) {
		_real  = real;
		_imag  = imag;
		_isNaN = Float.isNaN(_real) || Float.isNaN(_imag);
		_isInf = !_isNaN && (Float.isInfinite(_real) || Float.isInfinite(_imag));
	}
	
	public Complex(float real) {
		this(real, 0.0f);
	}
	
	public Complex add(Complex c) {
		if (_isNaN || c.isNaN()) {
            return NaN;
        }
		if (_isInf || c.isInf()) {
			return INF;
		}
		return new Complex(_real + c.getReal(), _imag + c.getImag());
	}
	
	public Complex sub(Complex c) {
		if (_isNaN || c.isNaN()) {
            return NaN;
        }
		if (_isInf || c.isInf()) {
			return INF;
		}
		return new Complex(_real - c.getReal(), _imag - c.getImag());
	}
	
	public Complex mult(Complex c) {
		if (_isNaN || c.isNaN()) {
            return NaN;
        }
		if (_isInf || c.isInf()) {
			return INF;
		}
		float mult_real = _real*c.getReal() - _imag*c.getImag();
		float mult_imag = _real*c.getImag() + _imag*c.getReal();
		return new Complex(mult_real, mult_imag);
	}
	
	public Complex div(Complex c) {
		if (_isNaN || c.isNaN() || (_isInf && c.isInf())) {
            return NaN;
        }
		if (_isInf || (c.getReal()==0 && c.getImag()==0)) {
			return INF;
		}
		if ( ! (c.getReal()==0 && c.getImag()==0) && c.isInf()) {
			return ZERO;
		}
		float denominator = c.getReal()*c.getReal() + c.getImag()*c.getImag();
		float real_nominator = _real*c.getReal() + _imag*c.getImag();
		float imag_nominator = _imag*c.getReal() - _real*c.getImag();
		return new Complex(real_nominator/denominator, imag_nominator/denominator);
	}
	
	public Complex conjugate() {
		if (_isNaN) {
			return NaN;
		}
		if (_isInf) {
			return INF;
		}
		return new Complex(_real, -_imag);
	}
	
	public float length() {
		return (float) Math.sqrt(_real*_real + _imag*_imag);
	}
	
	public float getReal() {
		return _real;
	}
	
	public float getImag() {
		return _imag;
	}
	
	public void setReal(float real) {
		_real = real;
	}
	
	public void setImag(float imag) {
		_imag= imag;
	}
	
	public boolean isNaN() {
		return _isNaN;
	}
	
	public boolean isInf() {
		return _isInf;
	}
	
	public void update(Complex c) {
		_real = c.getReal();
		_imag = c.getImag();
		_isNaN = c.isNaN();
		_isInf = c.isInf();
	}
	
	public String toString() {
		return "Complex(" + _real + "," + _imag +")";
	}
}
