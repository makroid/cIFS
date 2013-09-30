package armin.cifs;

import java.util.ArrayList;

import armin.cifs.complex.*;

public class IFSsampler {
	private TransformationSet _tfsList;
	private int _iterations;
	private int _burnin;
	private ComplexPoint _startPoint;
	private ArrayList<Complex> _sample;
	
	public IFSsampler(TransformationSet atfsList) {
		_tfsList = atfsList;
		_iterations = 0;
		_burnin = 100;
		_startPoint = null;
		_sample = new ArrayList<Complex>();
	}
	
	
	public void setIterations(int aiterations) {
		_iterations = aiterations;
	}
	
	public void setBurnin(int aburnin) {
		if (aburnin >= 0) {
			_burnin = aburnin;
		}
	}
	
	public void setStartPoint(ComplexPoint c) {
		_startPoint = c;
	}
	
	public void sample() {
		if (_tfsList.size()==0 || _startPoint==null) return;
		Complex curZ;
		Transformation curTfs;		
		_sample.clear();
		
		if (_iterations > 0) {
			curTfs = _tfsList.sampleTransformation();			
			curZ = curTfs.apply(_startPoint.z);
			_sample.add(curZ);
			for (int i=0; i<_iterations + _burnin; i++) {
				curTfs = _tfsList.sampleTransformation();
				//System.out.println("tfs=" + curTfs);
				curZ = _sample.get(_sample.size()-1);
				//System.out.println(curZ);
				if (i >= _burnin) {
					_sample.add(curTfs.apply(curZ));
				}
			}
		}
	}
	
	public ArrayList<Complex> getSample() {
		return _sample;
	}
}
