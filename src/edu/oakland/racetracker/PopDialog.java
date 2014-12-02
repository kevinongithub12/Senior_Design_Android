package edu.oakland.racetracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

public class PopDialog extends DialogFragment{
	private Context mContext;
	private AlertDialog alert;
	
public PopDialog(Context context){
	    mContext = context;
		alert = new AlertDialog.Builder(mContext).create();
	}

public AlertDialog getAlert(){
	return alert;
}
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return alert;
    }
}
