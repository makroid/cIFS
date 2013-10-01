package armin.cifs;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
import armin.cifs.complex.*;


/*
 * This Panel draws within the UI thread.
 * => In order to do all drawing in a separate thread, comment 
 *    out all ranges flanked by // THREAD tags 
 */

public class IFSDrawingPanel extends SurfaceView implements SurfaceHolder.Callback {
	
//	private PanelThread _thread; // THREAD	
	private Paint _paint;
	private ComplexPointSet _pointSet;
	private TransformationSet _tfsSet;
	private IFSsampler _sampler;
	
	private float _cpointRadius;
	private float _selectPointRadius;
	private float _sampleRadius;
	private int _sampleColor;
	private boolean _drawTrace;

	public IFSDrawingPanel(Context acontext, ComplexPointSet apointSet, TransformationSet atfsSet, IFSsampler asampler) {
		super(acontext);
		getHolder().addCallback(this);		
		_pointSet = apointSet;
		_tfsSet   = atfsSet;
		_sampler  = asampler;
		_paint    = new Paint();
		
		setDefaultPaintValues(acontext);
	}
	
	private void setDefaultPaintValues(Context acontext) {	
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(acontext);
		_cpointRadius          = pref.getInt("pref_nodeRadius", 18);		
		_selectPointRadius     = _cpointRadius + 1; 
		_sampleRadius          = pref.getInt("pref_sampleRadius", 4);
		_sampler.setIterations(pref.getInt("pref_numberOfSamples", 150));
		_sampler.setBurnin(pref.getInt("pref_numberOfBurnins", 100));
		_sampleColor           = pref.getInt("pref_sampleColor", -256);
		_drawTrace             = pref.getBoolean("pref_drawTrace", false);
	}
	

	protected void onSizeChanged (int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		System.out.println("w=" + w + "h=" + w);
		_pointSet.updateStartPoint(new Complex(w/2, 3*h/4));
	}
	
//	class PanelThread extends Thread { // THREAD
//		private SurfaceHolder _surfaceHolder;
//		private IFSDrawingPanel _panel;
//		private boolean _run = false;
//
//		public PanelThread(SurfaceHolder surfaceHolder, IFSDrawingPanel panel) {
//			_surfaceHolder = surfaceHolder;
//			_panel = panel;
//		}
//
//		public void setRunning(boolean run) {
//			_run = run;
//		}
//
//		@Override
//		public void run() {
//			Canvas c;
//			while (_run) { 
//				c = null;
//				try {
//					c = _surfaceHolder.lockCanvas(null);
//					synchronized (_surfaceHolder) {
//						//Insert methods to modify positions of items in onDraw()					
//						postInvalidate();					
//					}
//				} finally {
//					if (c != null) {
//						_surfaceHolder.unlockCanvasAndPost(c);
//					}
//				}
//			}
//		}
//	} // THREAD

	@Override
	public void onDraw(Canvas canvas) {
		_paint.reset();
		// draw background
		_paint.setStyle(Paint.Style.FILL);
		_paint.setColor(Color.BLACK);
		canvas.drawPaint(_paint);

		// draw startPoint		
		_paint.setColor(Color.GREEN);
		_paint.setAntiAlias(true);
		canvas.drawCircle(_pointSet.getStartPoint().z.getReal(), _pointSet.getStartPoint().z.getImag(), _cpointRadius, _paint);
			
		drawTransformationSet(canvas);
		drawComplexPointSet(canvas);
				
		if (_pointSet.hasSelectedPoint()) {			
			_paint.setStyle(Paint.Style.STROKE);
			_paint.setStrokeWidth(3);
			_paint.setColor(Color.RED);
			canvas.drawCircle(_pointSet.getSelectedPoint().z.getReal(), _pointSet.getSelectedPoint().z.getImag(), _selectPointRadius, _paint);
		}
		
		drawSample(canvas);
		if (_drawTrace) {
			drawTrace(canvas);
		}
	}
	
	private void drawComplexPointSet(Canvas canvas) {
		_paint.setColor(Color.WHITE);
		_paint.setStyle(Paint.Style.FILL);
		_paint.setStrokeWidth(1);
		for (ComplexPoint p : _pointSet.getPoints().values()) {
			canvas.drawText(p.name, p.z.getReal()-_cpointRadius-1, p.z.getImag()-_cpointRadius-1, _paint);
			canvas.drawCircle(p.z.getReal(), p.z.getImag(), _cpointRadius, _paint);
		}
	}
	
	private void drawTransformationSet(Canvas canvas) {		
		_paint.setStrokeWidth(3);
		for (TransformationSet.TfsWithWeight tfs : _tfsSet.get_tfsSet().values()) {			
			tfs.tfs.paint(canvas, _paint);		
		}
	}
	
	private void drawSample(Canvas canvas) {
		_paint.setStyle(Paint.Style.FILL);
		_paint.setColor(_sampleColor);
		for (Complex c : _sampler.getSample()) {
			canvas.drawCircle(c.getReal(), c.getImag(), _sampleRadius, _paint);
		}
	}
	
	private void drawTrace(Canvas canvas) {
		_paint.setStyle(Paint.Style.STROKE);
		_paint.setStrokeWidth(1);
		_paint.setColor(_sampleColor);
		ArrayList<Complex> sample = _sampler.getSample();
		for (int i=1; i<sample.size(); i++) {
			canvas.drawLine(sample.get(i-1).getReal(), sample.get(i-1).getImag(), sample.get(i).getReal(), sample.get(i).getImag(), _paint);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int eventaction = event.getAction();
		float touchX = event.getX();
		float touchY = event.getY();
		
	    switch (eventaction) {
	        case MotionEvent.ACTION_DOWN:
	        	ComplexPoint touchedPoint = findPointTouched(touchX, touchY);
	            if (_pointSet.hasSelectedPoint()) {	
	            	_pointSet.getSelectedPoint().z.update(new Complex(touchX, touchY));
	            	_pointSet.unselectPoint();
	            	calculateSample();
	            } else {
	            	if (touchedPoint != null) {
	            		_pointSet.setSelectedPoint(touchedPoint);
	            	}
	            }
	            break;

	        case MotionEvent.ACTION_MOVE:
	        	if (_pointSet.hasSelectedPoint()) {
	        		_pointSet.getSelectedPoint().z.update(new Complex(touchX, touchY));
	        		calculateSample();
	        	}
	            break;

	        case MotionEvent.ACTION_UP: 
	        	if (_pointSet.hasSelectedPoint()) {
	        		_pointSet.unselectPoint();
	        	}
	            break;
	    }
		invalidate();
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,int arg3) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		setWillNotDraw(false); // use invalidate() to call onDraw()

//		_thread = new PanelThread(getHolder(), this); // THREAD 
//		_thread.setRunning(true); 
//		_thread.start(); // finally calling onDraw() // THREAD
	}


	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
//		try { // THREAD
//			_thread.setRunning(false); 
//			_thread.join(); // removes thread from mem
//		} catch (InterruptedException e) {} // THREAD
	}
	
	public void calculateSample() {
		_sampler.setStartPoint(_pointSet.getStartPoint());
		_sampler.sample();
	}
	
	public ComplexPoint findPointTouched(float touchX, float touchY) {
		// TODO: handle startpoint in for-loop
		ComplexPoint s = _pointSet.getStartPoint();
		if (s.z.getReal() - _cpointRadius < touchX && s.z.getReal() + _cpointRadius > touchX) {
			if (s.z.getImag() - _cpointRadius < touchY && s.z.getImag() + _cpointRadius > touchY) {
				return s;
			}
		}
		for (ComplexPoint  p : _pointSet.getPoints().values()) {
			if (p.z.getReal() - _cpointRadius < touchX && p.z.getReal() + _cpointRadius > touchX) {
				if (p.z.getImag() - _cpointRadius < touchY && p.z.getImag() + _cpointRadius > touchY) {
					return p;
				}
			}
		}
		return null;
	}
	
	public ComplexPointSet get_pointSet() {
		return _pointSet;
	}

	public void set_pointSet(ComplexPointSet apointSet) {
		this._pointSet = apointSet;
	}

	public TransformationSet get_tfsSet() {
		return _tfsSet;
	}

	public void set_tfsSet(TransformationSet _tfsSet) {
		this._tfsSet = _tfsSet;
	}
	
	public void set_pointRadius(float aradius) {
		if (aradius > 0) {
			_cpointRadius = aradius;
			_selectPointRadius = aradius+1;
		}
	}
	
	public void set_sampleRadius(float aradius) {
		if (aradius > 0) {
			_sampleRadius = aradius;
		}
	}
	
	public void set_sampleColor(int col) {
		_sampleColor = col;
	}
	
	public void set_drawTrace(boolean _drawTrace) {
		this._drawTrace = _drawTrace;
	}
} 



