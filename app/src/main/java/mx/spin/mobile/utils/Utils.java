package mx.spin.mobile.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import mx.spin.mobile.R;
import mx.spin.mobile.SpinApp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Utils {
	private static final String TAG = "Utils";
	private static Map<Character, Character> MAP_NORM;

	public static byte[] convertBitmapToBytes(Bitmap imagen) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		imagen.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		return byteArray;
	}

	public static String timeStringFromDate(java.util.Date fecha) {
		java.util.Date now = new java.util.Date();	    

		Double seconds = (double) (now.getTime() - fecha.getTime()) / 1000;
		if(seconds < 0) {
			seconds *= -1;
		}
		Double minutos = seconds / 60;
		Double horas = minutos / 60 ;
		Double dias = horas / 24;
		Double semanas = dias / 7;
		Double meses = dias / 30;
		Double anios = meses / 12;

		String tiempo = "Hace " + seconds.intValue() +" segundos";
		if (anios > 1) {
			tiempo = "Hace " + anios.intValue() +" años";
		}
		else if (meses > 1) {
			tiempo = "Hace " + meses.intValue() +" meses";
		}
		else if (semanas > 1) {
			tiempo = "Hace " + semanas.intValue() +" semanas";
		}
		else if (dias > 1) {
			tiempo = "Hace " + dias.intValue() +" dias";
		}
		else if (horas > 1) {
			tiempo = "Hace " + horas.intValue() +" horas";
		}
		else if (minutos > 1) {
			tiempo = "Hace " + minutos.intValue() +" minutos";
		}

		return tiempo;
	}

    public static String stringTimeFrom(int seconds) {
        int minutes = 0;
        if (seconds >= 60) {
            minutes = seconds / 60;
            seconds = seconds % 60;
        }

        return String.format("%02d:%02d", minutes, seconds);
    }

	public static String mysqlDateFromJavaDate(java.util.Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		String s = sdf.format(date);
		return s;	    
	}	

	public static String timeStringFromTimeDate(java.util.Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
		return sdf.format(date);
	}

	public static Calendar calendarFromTimeString(String time) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
			java.util.Date date = sdf.parse(time);

			Calendar calendar =  GregorianCalendar.getInstance();
			calendar.setTime(date);
			return calendar;
		} catch (ParseException e) {
			Log.e(TAG, "Error en parse calendarFromTimeString", e);
		}
		return null;
	}

	public static int minutesFromTimeString(String time) {
		return Utils.calendarFromTimeString(time).get(Calendar.MINUTE);
	}

	public static int hoursFromTimeString(String time) {
		return Utils.calendarFromTimeString(time).get(Calendar.HOUR_OF_DAY);
	}
/*
	public static void setupActionBar(ActionBar ab) {
		Utils.setupActionBar(ab, true);
	}

	public static void setupActionBar(ActionBar ab, boolean showBackButton) {
		ab.setCustomView(R.layout.action_bar);                
		ab.setDisplayShowCustomEnabled(true);

		ab.setDisplayShowHomeEnabled(showBackButton);
		ab.setDisplayHomeAsUpEnabled(showBackButton);
		ab.setDisplayShowTitleEnabled(false);
		ab.setIcon(R.drawable.empty);
		ab.setDisplayUseLogoEnabled(true);
	}

	public static void setupActionBarGallery(ActionBar ab) {
		ab.setCustomView(R.layout.action_bar_gallery);                
		ab.setDisplayShowCustomEnabled(true);

		ab.setDisplayShowHomeEnabled(true);
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowTitleEnabled(false);
		ab.setIcon(R.drawable.empty);
		ab.setDisplayUseLogoEnabled(true);
	}

	public static void setupActionBarMapaPerfil(ActionBar ab) {
		ab.setCustomView(R.layout.action_bar_mapa_perfil);                
		ab.setDisplayShowCustomEnabled(true);

		ab.setDisplayShowHomeEnabled(true);
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowTitleEnabled(false);
		ab.setIcon(R.drawable.empty);
		ab.setDisplayUseLogoEnabled(true);
	}

	public static void setupActionBarSearch(ActionBar ab, final AfueritaSearchViewFragment searchFragment) {
		ab.setCustomView(R.layout.action_bar_search);                
		ab.setDisplayShowCustomEnabled(true);

		ab.setDisplayShowHomeEnabled(false);
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowTitleEnabled(false);
		ab.setIcon(null);
		ab.setDisplayUseLogoEnabled(false);     

		View customView = ab.getCustomView();
		Button btnCancelar = (Button) customView.findViewById(R.id.btnCancelarBar);
		Button btnBuscar = (Button) customView.findViewById(R.id.btnBuscarBar);

		btnCancelar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchFragment.cancelar();
			}
		});
		btnBuscar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchFragment.buscar();
			}
		});

	}

	public static void setupActionBarContactanos(ActionBar ab, final ContactanosFragment contactanos) {
		ab.setCustomView(R.layout.action_bar_search);                
		ab.setDisplayShowCustomEnabled(true);

		ab.setDisplayShowHomeEnabled(false);
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowTitleEnabled(false);
		ab.setIcon(null);
		ab.setDisplayUseLogoEnabled(false);     

		View customView = ab.getCustomView();
		TextView txtTitle = (TextView) customView.findViewById(R.id.txtBarTitle);
		Button btnCancelar = (Button) customView.findViewById(R.id.btnCancelarBar);
		Button btnBuscar = (Button) customView.findViewById(R.id.btnBuscarBar);

		Resources res = PapayaApp.getContext().getResources();

		txtTitle.setText(res.getString(R.string.titleContactanos));
		btnBuscar.setText(res.getString(R.string.btnEnviar));

		btnCancelar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				contactanos.cancelar();
			}
		});
		btnBuscar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				contactanos.enviar();
			}
		});

	}

	public static void setupActionBarReportaNegocio(ActionBar ab, Negocio negocio, final ReportarFragment reportar) {
		ab.setCustomView(R.layout.action_bar_search);                
		ab.setDisplayShowCustomEnabled(true);

		ab.setDisplayShowHomeEnabled(false);
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowTitleEnabled(false);
		ab.setIcon(null);
		ab.setDisplayUseLogoEnabled(false);     

		View customView = ab.getCustomView();
		TextView txtTitle = (TextView) customView.findViewById(R.id.txtBarTitle);
		Button btnCancelar = (Button) customView.findViewById(R.id.btnCancelarBar);
		Button btnBuscar = (Button) customView.findViewById(R.id.btnBuscarBar);

		Resources res = PapayaApp.getContext().getResources();

		txtTitle.setText(negocio.getNombre());
		btnBuscar.setText(res.getString(R.string.btnEnviar));

		btnCancelar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reportar.cancelar();
			}
		});
		btnBuscar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reportar.reportarNegocio();
			}
		});

	}
*/

	//Alert Facebook
	/*
	public static void showAlertFbLogin(final Activity a, final FBLoginCompleted action) {
		AlertDialog.Builder builder = new AlertDialog.Builder(a);
		builder.setTitle("Espera!")
		.setMessage("Debes iniciar sesión para poder continuar")
		.setCancelable(true)
		.setNegativeButton("Cancelar", null)
		.setPositiveButton("Iniciar Sesión", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				FacebookLoginDelegate.fbLogin(a, true, action);
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
*/
	//MESSAGES
	public static void showSuccessMessage(String mensaje) {
		LayoutInflater inflater = LayoutInflater.from(SpinApp.getContext());
		View layout = inflater.inflate(R.layout.success_toast, null);

		TextView text = (TextView) layout.findViewById(R.id.txtSuccessMessage);
		text.setText(mensaje);

		Toast toast = new Toast(SpinApp.getContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}

	public static void showErrorMessage(String titulo, String mensaje) {
		LayoutInflater inflater = LayoutInflater.from(SpinApp.getContext());
		View layout = inflater.inflate(R.layout.error_toast, null);

		TextView title = (TextView) layout.findViewById(R.id.txtErrorTitle);
		TextView text = (TextView) layout.findViewById(R.id.txtErrorMessage);
		title.setText(titulo);
		text.setText(mensaje);

		Toast toast = new Toast(SpinApp.getContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}

	public static void sendEmail(File file, Fragment fragment) {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		//i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
		//i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
		//i.putExtra(Intent.EXTRA_TEXT   , "body of email");
		i.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.getAbsolutePath()));
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		fragment.startActivity(Intent.createChooser(i, "Enviando mail..."));
	}	

	public static void createAlert(final Activity activity, String mensaje) {
		Utils.createAlert(activity, null, mensaje);		
	}

	public static void createAlert(final Activity activity, String titulo, String mensaje) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(titulo)
		.setMessage(mensaje)
		.setCancelable(true)
		.setPositiveButton("OK", null);
		AlertDialog alert = builder.create();
		alert.show();
	}

	public static void showAlertNoLocation(final Activity activity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage("Los servicios de localizacion estan desactivados, ¿desea activarlos?")
		.setCancelable(false)
		.setPositiveButton("Si", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int id) {
				activity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();	    
	}
	
	public static String removeAccents(String value) {
	    if (MAP_NORM == null || MAP_NORM.size() == 0) {
	        MAP_NORM = new HashMap<Character, Character>();
	        MAP_NORM.put('À', 'A');
	        MAP_NORM.put('Á', 'A');
	        MAP_NORM.put('Â', 'A');
	        MAP_NORM.put('Ã', 'A');
	        MAP_NORM.put('Ä', 'A');
	        MAP_NORM.put('È', 'E');
	        MAP_NORM.put('É', 'E');
	        MAP_NORM.put('Ê', 'E');
	        MAP_NORM.put('Ë', 'E');
	        MAP_NORM.put('Í', 'I');
	        MAP_NORM.put('Ì', 'I');
	        MAP_NORM.put('Î', 'I');
	        MAP_NORM.put('Ï', 'I');
	        MAP_NORM.put('Ù', 'U');
	        MAP_NORM.put('Ú', 'U');
	        MAP_NORM.put('Û', 'U');
	        MAP_NORM.put('Ü', 'U');
	        MAP_NORM.put('Ò', 'O');
	        MAP_NORM.put('Ó', 'O');
	        MAP_NORM.put('Ô', 'O');
	        MAP_NORM.put('Õ', 'O');
	        MAP_NORM.put('Ö', 'O');
	        MAP_NORM.put('Ñ', 'N');
	        MAP_NORM.put('Ç', 'C');
	        MAP_NORM.put('ª', 'A');
	        MAP_NORM.put('º', 'O');
	        MAP_NORM.put('§', 'S');
	        MAP_NORM.put('³', '3');
	        MAP_NORM.put('²', '2');
	        MAP_NORM.put('¹', '1');
	        MAP_NORM.put('à', 'a');
	        MAP_NORM.put('á', 'a');
	        MAP_NORM.put('â', 'a');
	        MAP_NORM.put('ã', 'a');
	        MAP_NORM.put('ä', 'a');
	        MAP_NORM.put('è', 'e');
	        MAP_NORM.put('é', 'e');
	        MAP_NORM.put('ê', 'e');
	        MAP_NORM.put('ë', 'e');
	        MAP_NORM.put('í', 'i');
	        MAP_NORM.put('ì', 'i');
	        MAP_NORM.put('î', 'i');
	        MAP_NORM.put('ï', 'i');
	        MAP_NORM.put('ù', 'u');
	        MAP_NORM.put('ú', 'u');
	        MAP_NORM.put('û', 'u');
	        MAP_NORM.put('ü', 'u');
	        MAP_NORM.put('ò', 'o');
	        MAP_NORM.put('ó', 'o');
	        MAP_NORM.put('ô', 'o');
	        MAP_NORM.put('õ', 'o');
	        MAP_NORM.put('ö', 'o');
	        MAP_NORM.put('ñ', 'n');
	        MAP_NORM.put('ç', 'c');
	    }

	    if (value == null) {
	        return "";
	    }

	    StringBuilder sb = new StringBuilder(value);

	    for(int i = 0; i < value.length(); i++) {
	        Character c = MAP_NORM.get(sb.charAt(i));
	        if(c != null) {
	            sb.setCharAt(i, c.charValue());
	        }
	    }

	    return sb.toString();
	}

	public static boolean isEmailValid(String email) {
		//TODO: Replace this with your own logic
		return email.contains("@");
	}

	public static boolean isPasswordValid(String password) {
		//TODO: Replace this with your own logic
		return password.length() > 4;
	}

}
