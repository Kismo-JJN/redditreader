package net.kismo.redditreader.utils;

import java.util.List;
import net.kismo.redditreader.R;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager.BadTokenException;
import android.widget.Toast;

public class DialogUtil {
	private static volatile ProgressDialog progressDialog;
	private static AlertDialog alertDialog;

	public static void showErrorToast(Context ctx, String errorMsg) {
		showErrorAlertDialog(ctx, errorMsg);
	}

	public static void showErrorAlertDialog(Context ctx, String errorMsg) {
		
		try {
			if (alertDialog == null) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
				builder.setMessage(errorMsg).setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
	
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
				alertDialog = builder.create();
			} else {
				alertDialog.setMessage(errorMsg);
			}
	
			alertDialog.show();
		}

		catch (IllegalStateException ise) {
				Log.d("IllegalStateException", "Arrrggg");
	
		} catch (BadTokenException bte){
				Log.d("BadTokenException", "Arrrggggg");
		}

	}

	public static void showProgressDialog(Context context) {
		showProgressDialog(
				context,
				context.getResources().getString(
						R.string.msg_loading_posts));
	}

	public static void showProgressDialog(Context context, String msg) {
		synchronized (DialogUtil.class) {
			progressDialog = new ProgressDialog(context);
			progressDialog.setTitle(msg);
			progressDialog.show();
		}
	}
	
	
	public static void showProgressDialog(Context context, String msg, boolean cancelable, OnCancelListener listener) {
		synchronized (DialogUtil.class) {
			progressDialog = new ProgressDialog(context);
			progressDialog.setTitle(msg);
			progressDialog.setCancelable(cancelable);
			progressDialog.setOnCancelListener(listener);			
			progressDialog.show();
		}
	}
		

	public static void hideProgressDialog() {
		synchronized (DialogUtil.class) {
			if (progressDialog != null) {
				progressDialog.hide();
				progressDialog.dismiss();
			}
		}
	}

	/**
	 * A convenient method to display the error dialog when given a list of
	 * errors
	 */
	public static void displayErrors(Context ctx, List<String> errors) {

		if (ctx!=null && errors != null && !errors.isEmpty()) {
			StringBuilder errorMsg = new StringBuilder();
			errorMsg.append(ctx.getResources().getString(
					(R.string.msg_error_loading_posts)));
			for (String error : errors) {
				if (errorMsg.length() > 0) {
					errorMsg.append("\n");
				}
				errorMsg.append("- ");
				errorMsg.append(error);
			}
			showErrorToast(ctx, errorMsg.toString());
		}
	}

	public static void showProgressDialog(final Context ctx, String msg,
			long timeout, Runnable timeoutAction) {
		showProgressDialog(ctx, msg);
		Handler handler = new Handler();
		handler.postDelayed(timeoutAction, timeout);
	}

	public static void showChoiceDialog(final Context ctx, String title,
			String[] choices, DialogInterface.OnClickListener choiceListener) {
		new AlertDialog.Builder(ctx).setTitle(title)
				.setItems(choices, choiceListener).show();
	}

	public static void showSimpleToast(final Context ctx, String msg) {
		Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
	}
	
}
