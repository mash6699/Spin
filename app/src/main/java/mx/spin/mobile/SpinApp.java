package mx.spin.mobile;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;

import com.facebook.FacebookSdk;
import mx.spin.mobile.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class SpinApp extends Application {

	private static SpinApp instance;

    public static String BUNDLE_URL = "url";


    public static final int REQUEST_CODE_SELECTIMAGE = 9815;

    public static SpinApp getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance;
        // or return instance.getApplicationContext();
    }
	public void onCreate () {
		super.onCreate();


		instance = this;
		// Create default options which will be used for every
		//  displayImage(...) call if no options will be passed to this method


        FacebookSdk.sdkInitialize(this);


    }

    public static void initCargarImagen(Context context){
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                //.cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .showImageOnLoading(R.mipmap.ic_launcher)// resource or drawable
                .showImageForEmptyUri(R.mipmap.ic_launcher) // resource or drawable
                .showImageOnFail(R.mipmap.ic_launcher) // resource or drawable
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config); // Do it on Application start
    }



    public static void saveUsuario(String usuario) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getInstance()).edit();
        editor.putString("usuario", usuario);
        editor.apply();
    }

    public static void deleteUsuario() {
        saveUsuario("");
    }

    public static String getUsuario() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getInstance());
        return prefs.getString("usuario", "");
    }

    public static boolean hasUsuario() {
        String usr = getUsuario();
        return usr.length() > 0;
    }


}
