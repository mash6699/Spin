package mx.spin.mobile.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.loopj.android.http.TextHttpResponseHandler;

import mx.spin.mobile.R;

public abstract class TextHttpResponseHandlerMessage extends TextHttpResponseHandler {
	private ProgressDialog dialog;
	
	protected void showMessage(Context c, String message) {
		dialog = new ProgressDialog(c);
		dialog.show();
		dialog.setContentView(R.layout.costum_progress_dialog);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
	}
	
	protected void hideMessage() {
		if(dialog != null) {
			dialog.dismiss();
		}
	}
}
