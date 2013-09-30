package armin.cifs.complex;

import java.util.HashMap;

import armin.cifs.complex.*;

public class TransformationSet {
		
	public class TfsWithWeight {
		public Transformation tfs;
		public double weight;
		
		public TfsWithWeight(Transformation atfs) {
			tfs = atfs;
			weight = 1;
		}
	}
	
	private HashMap<String,TfsWithWeight> _tfsSet;
	
	public TransformationSet() {
		_tfsSet = new HashMap<String,TfsWithWeight>();		
	}
	
	public boolean addTransformation(Transformation tfs) {
		if ( ! _tfsSet.containsKey(tfs.toString())) {
			_tfsSet.put(tfs.toString(), new TfsWithWeight(tfs));
			return true;
		}
		return false;
	}
	
	public boolean removeTransformation(String name) {
		if ( _tfsSet.containsKey(name)) {
			_tfsSet.remove(name);
			return true;
		}
		return false;
	}
	
	public HashMap<String, TfsWithWeight> get_tfsSet() {
		return _tfsSet;
	}

	// assert; weights > 0
	public Transformation sampleTransformation() {
		double sumWeights = 0;
		for (TfsWithWeight tfs : _tfsSet.values()) {
			sumWeights += tfs.weight;
		}
		double random = Math.random() * sumWeights;
		double cumSum = 0;
		for (TfsWithWeight tfs : _tfsSet.values()) {
			cumSum += tfs.weight;
			if (cumSum >= random) {
				return tfs.tfs;
			}
		}
		return null;
	}
	
	public int size() {
		return _tfsSet.size();
	}
}
