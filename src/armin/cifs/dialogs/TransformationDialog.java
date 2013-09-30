package armin.cifs.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;
import armin.cifs.MyIFSActivity;
import armin.cifs.R;
import armin.cifs.complex.ComplexPoint;
import armin.cifs.complex.P2Transformation;
import armin.cifs.complex.Transformation;

public class TransformationDialog extends DialogFragment implements ColorChooserDialog.OnAmbilWarnaListener {
	
	private NumberPicker _pickerA;
	private NumberPicker _pickerB;
	private NumberPicker _pickerC;
	private NumberPicker _pickerD;
	private Button _colorButton;
	private int _tfsColor;
	private String[] labels; 
	MyIFSActivity _parent;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());	    
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    labels = getArguments().getStringArray("labels");	    
	    
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    View view = inflater.inflate(R.layout.dialog_transformation, null);
	    builder.setView(view)
	    	   .setMessage(R.string.add_tfs)
	           .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {	            	   
	            	   ComplexPoint A = _parent.get_pointSet().getPoint(labels[_pickerA.getValue()]);
	            	   ComplexPoint B = _parent.get_pointSet().getPoint(labels[_pickerB.getValue()]);
	            	   ComplexPoint C = _parent.get_pointSet().getPoint(labels[_pickerC.getValue()]);
	            	   ComplexPoint D = _parent.get_pointSet().getPoint(labels[_pickerD.getValue()]);
	            	   if (A != null && B != null && C != null && D != null) {
	            		   Transformation tfs = new P2Transformation(A, B, C, D);
	            		   tfs.setColor(_tfsColor);
	            		   _parent.addTransformation(tfs);
	            		   _parent.get_panel().calculateSample();
	            		   _parent.get_panel().invalidate();
	            	   }
	               }
	           })
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   TransformationDialog.this.getDialog().cancel();
	               }
	           }); 
	    if (labels.length > 0) {
	    	_pickerA = (NumberPicker) view.findViewById(R.id.numberPickerA);
	    	_pickerB = (NumberPicker) view.findViewById(R.id.numberPickerB);
	    	_pickerC = (NumberPicker) view.findViewById(R.id.numberPickerC);
	    	_pickerD = (NumberPicker) view.findViewById(R.id.numberPickerD);
	    	_pickerA.setMinValue(0);
	    	_pickerB.setMinValue(0);
	    	_pickerC.setMinValue(0);
	    	_pickerD.setMinValue(0);
	    	_pickerA.setMaxValue(labels.length-1);
	    	_pickerB.setMaxValue(labels.length-1);
	    	_pickerC.setMaxValue(labels.length-1);
	    	_pickerD.setMaxValue(labels.length-1);
	    	_pickerA.setDisplayedValues(labels);
	    	_pickerB.setDisplayedValues(labels);
	    	_pickerC.setDisplayedValues(labels);
	    	_pickerD.setDisplayedValues(labels);	    		    	
	    	
	    } 
	    // handle color button
	    _colorButton = (Button) view.findViewById(R.id.tfsColorButton);
	    _tfsColor = Color.BLUE;
	    _colorButton.setBackgroundColor(_tfsColor);
	    _colorButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorChooserDialog dialog = new ColorChooserDialog(_parent, Color.BLUE, TransformationDialog.this);
				dialog.show();
			}
		});	    
	    
	 	return builder.create();
	}
		    
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try { 
			_parent = (MyIFSActivity) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " invalid parent");
		}
	}


	@Override
	public void onCancel(ColorChooserDialog dialog) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onOk(ColorChooserDialog dialog, int color) {
		// TODO Auto-generated method stub
		_colorButton.setBackgroundColor(color);
		_tfsColor = color;
	}
}
