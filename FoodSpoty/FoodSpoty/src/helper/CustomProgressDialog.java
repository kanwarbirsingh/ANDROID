package helper;

import com.isoft.foodspoty.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

public class CustomProgressDialog extends ProgressDialog {

public static ProgressDialog ctor(Context context) {
    CustomProgressDialog dialog = new CustomProgressDialog(context);
    dialog.setIndeterminate(true);
    dialog.setCancelable(false);
    return dialog;
  }

  public CustomProgressDialog(Context context) {
    super(context);
  }

  public CustomProgressDialog(Context context, int theme) {
    super(context, theme);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.custom_progress_dialog);
 
  }
 
}
