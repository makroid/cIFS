package armin.cifs.complex;

import java.util.HashMap;

public class ComplexPointSet {
	private ComplexPoint _startPoint;
	private HashMap<String,ComplexPoint> _points;
	private ComplexPoint _selectedPoint;
	private int _labelCount;	
	
	public ComplexPointSet() {
		_startPoint    = new ComplexPoint(Complex.ZERO, "S");
		_points        = new HashMap<String,ComplexPoint>();
		_selectedPoint = null;
		_labelCount    = 0;
	}
	
	public void updateStartPoint(Complex c) {
		_startPoint.z.update(c);
	}
	
	public void updatePoint(String label, Complex ac) {
		if (_points.containsKey(label)) {
			_points.get(label).z.update(ac);
		}
	}
	
	public void addPointAndSelect(Complex c) {
		String label = nextLabel();
		_points.put(label, new ComplexPoint(c, label));
		_selectedPoint = _points.get(label);
	}
	
	public void addPoint(Complex c) {
		String label = nextLabel();
		_points.put(label, new ComplexPoint(c, label));		
	}
	
	private String nextLabel() {
		if (_labelCount < 26) {
			char c = (char) (65 + _labelCount);
			_labelCount++;
			return String.valueOf(c);
		} else {
			_labelCount++;
			return String.valueOf(_labelCount-25);
		}
	}
	
	public ComplexPoint getStartPoint() {
		return _startPoint;	
	}
	
	public HashMap<String,ComplexPoint> getPoints() {
		return _points;
	}
	
	public ComplexPoint getSelectedPoint() {
		return _selectedPoint;
	}
	
	public void setSelectedPoint(ComplexPoint p) {
		_selectedPoint = p;
	}
	
	public boolean hasSelectedPoint() {
		return _selectedPoint != null;
	}
	
	public void unselectPoint() {
		if (_selectedPoint != null) {
			_selectedPoint = null;
		}
	}
	
	public ComplexPoint getPoint(String label) {
		return _points.get(label);		
	}
}
