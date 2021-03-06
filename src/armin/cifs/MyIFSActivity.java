package armin.cifs;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import armin.cifs.complex.Complex;
import armin.cifs.complex.ComplexPointSet;
import armin.cifs.complex.Transformation;
import armin.cifs.complex.TransformationSet;
import armin.cifs.dialogs.InvTfsDialog;
import armin.cifs.dialogs.TransformationDialog;

public class MyIFSActivity extends Activity implements OnSharedPreferenceChangeListener {

	private IFSDrawingPanel _panel;
	private PointMovePanel _pmPanel;
	private ComplexPointSet _pointSet;
	private TransformationSet _tfsSet;
	private IFSsampler _sampler;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_pointSet = new ComplexPointSet();
		_tfsSet   = new TransformationSet();
		_sampler  = new IFSsampler(_tfsSet);		
		_panel    = new IFSDrawingPanel(MyIFSActivity.this, _pointSet, _tfsSet, _sampler);
		_pmPanel  = new PointMovePanel(MyIFSActivity.this);
		
		_pmPanel.setVisibility(View.GONE);
				
		LinearLayout layout = new LinearLayout(MyIFSActivity.this);
		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);
		layout.addView(_panel, p);
		layout.addView(_pmPanel);

		setContentView(layout);
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		pref.registerOnSharedPreferenceChangeListener(this);				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_if, menu);
		getActionBar().setDisplayShowTitleEnabled(false);
		return true;
	}

	public boolean onClickAddPoint(MenuItem item) {		
		_pointSet.addPointAndSelect(new Complex(_panel.getWidth()/2, _panel.getHeight()/2));	
		_panel.invalidate();
		return true;
	}
	
	public boolean onClickAddP2Transformation(MenuItem item) {
		Bundle bundle = new Bundle();
		String[] labels = new String[_pointSet.getPoints().keySet().size()];
		_pointSet.getPoints().keySet().toArray(labels);
		bundle.putStringArray("labels", labels);
		DialogFragment tfsDialog = new TransformationDialog();
		tfsDialog.setArguments(bundle);
	    tfsDialog.show(getFragmentManager().beginTransaction(), "tfsDialog");
	    return true;
	}
	
	public boolean onClickAddInvTransformation(MenuItem item) {
		Bundle bundle = new Bundle();
		String[] labels = new String[_pointSet.getPoints().keySet().size()];
		_pointSet.getPoints().keySet().toArray(labels);
		bundle.putStringArray("labels", labels);
		DialogFragment tfsDialog = new InvTfsDialog();
		tfsDialog.setArguments(bundle);
	    tfsDialog.show(getFragmentManager().beginTransaction(), "tfsDialog");
	    return true;
	}
	
	public boolean onClickSettings(MenuItem item) {
		Intent myIntent = new Intent(getBaseContext(), SettingsActivity.class);
		startActivity(myIntent);
		return true;
	}
	
	public boolean addTransformation(Transformation tfs) {
		return _tfsSet.addTransformation(tfs);
	}
	
	public ComplexPointSet get_pointSet() {
		return _pointSet;
	}
	
	public IFSDrawingPanel get_panel() {
		return _panel;
	}
	
	public PointMovePanel get_pmPanel() {
		return _pmPanel;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences pref, String key) {
		// TODO Auto-generated method stub		
		if (key.equals("pref_numberOfSamples")) {
			int nsamples = pref.getInt(key, 100);			
			_sampler.setIterations(nsamples);
			_panel.calculateSample();
			_panel.invalidate();
		} else if (key.equals("pref_numberOfBurnins")) {
			int nburnin = pref.getInt(key, 100);			
			_sampler.setBurnin(nburnin);
			_panel.calculateSample();
			_panel.invalidate();
		} else if (key.equals("pref_nodeRadius")) {
			int radius = pref.getInt("pref_nodeRadius", 3);
			_panel.set_pointRadius(radius);
			_panel.invalidate();
		} else if (key.equals("pref_sampleRadius")) {
			int radius = pref.getInt("pref_sampleRadius", 3);
			_panel.set_sampleRadius(radius);
			_panel.invalidate();
		} else if (key.equals("pref_sampleColor")) { 
			int color = pref.getInt("pref_sampleColor", -256);
			_panel.set_sampleColor(color);
			_panel.invalidate();
		} else if (key.equals("pref_drawTrace")) {
			boolean drawTrace = pref.getBoolean("pref_drawTrace", false);
			_panel.set_drawTrace(drawTrace);
			_panel.invalidate();
		} else if (key.equals("pref_moveScale")) {
			int moveSize = pref.getInt("pref_moveScale", 500);
			_panel.set_moveScale(moveSize / 1000.0f);
		}
	}
}
