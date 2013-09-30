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
import armin.cifs.complex.InvTransformation;
import armin.cifs.complex.Transformation;

public class InvTfsDialog extends DialogFragment implements ColorChooserDialog.OnAmbilWarnaListener {
	
	private NumberPicker _pickerCenter;
	private NumberPicker _pickerThrough;
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
	    View view = inflater.inflate(R.layout.dialog_invtfs, null);
	    builder.setView(view)
	    	   .setMessage(R.string.add_tfs)
	           .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {	            	   
	            	   ComplexPoint A = _parent.get_pointSet().getPoint(labels[_pickerCenter.getValue()]);
	            	   ComplexPoint B = _parent.get_pointSet().getPoint(labels[_pickerThrough.getValue()]);	            	   
	            	   if (A != null && B != null) {
	            		   Transformation tfs = new InvTransformation(A, B);
	            		   tfs.setColor(_tfsColor);
	            		   _parent.addTransformation(tfs);
	            		   _parent.get_panel().calculateSample();
	            		   _parent.get_panel().invalidate();
	            	   }
	               }
	           })
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   InvTfsDialog.this.getDialog().cancel();
	               }
	           }); 
	    if (labels.length > 0) {
	    	_pickerCenter = (NumberPicker) view.findViewById(R.id.numberPickerCenter);
	    	_pickerThrough = (NumberPicker) view.findViewById(R.id.numberPickerThrough);	    	
	    	_pickerCenter.setMinValue(0);
	    	_pickerThrough.setMinValue(0);	    	
	    	_pickerCenter.setMaxValue(labels.length-1);
	    	_pickerThrough.setMaxValue(labels.length-1);	    	
	    	_pickerCenter.setDisplayedValues(labels);
	    	_pickerThrough.setDisplayedValues(labels);	    	
	    } 
	    // handle color button
	    _colorButton = (Button) view.findViewById(R.id.tfsColorButton);
	    _tfsColor = Color.BLUE;
	    _colorButton.setBackgroundColor(_tfsColor);
	    _colorButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorChooserDialog dialog = new ColorChooserDialog(_parent, Color.BLUE, InvTfsDialog.this);
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
