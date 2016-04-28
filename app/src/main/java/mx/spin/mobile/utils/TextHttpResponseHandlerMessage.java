package mx.spin.mobile.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.loopj.android.http.TextHttpResponseHandler;

import mx.spin.mobile.R;

public abstract class TextHttpResponseHandlerMessage extends TextHttpResponseHandler {
	private ProgressDialog dialog;
	
	protected void showMessage(Context c, String message) {
		dialog = new ProgressDialog(c);
		dialog.setIndeterminate(true);
        dialog.setCancelable(false); 
		dialog.setMessage(message);
		dialog.show();
	}
	
	protected void hideMessage() {
		if(dialog != null) {
			dialog.dismiss();
		}
	}

}
