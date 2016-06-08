package mx.spin.mobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;

/**
 * Created by miguel_angel on 8/06/16.
 */
public class SpinTask extends AsyncTask<Void, Void, Void> {

    private ProgressDialog dialog;

    public SpinTask(Activity activity){
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
     //   dialog.setContentView(R.layout.costum_progress_dialog);
      //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
      //  dialog.setCancelable(false);
        dialog.setCancelable(false);
        dialog.setMessage("Generando Pdf");
        dialog.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try{
            Thread.sleep(5000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }
}
