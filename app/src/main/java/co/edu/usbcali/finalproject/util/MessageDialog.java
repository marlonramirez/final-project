package co.edu.usbcali.finalproject.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Marlon.Ramirez on 2/03/2018.
 */

public class MessageDialog extends DialogFragment {
    private String message;
    private Class classNav;
    private Activity activity;

    @SuppressLint("ValidFragment")
    public MessageDialog(String message) {
        this.message = message;
    }

    @SuppressLint("ValidFragment")
    public MessageDialog(String message, Class classNav, Activity activity) {
        this.message = message;
        this.classNav = classNav;
        this.activity = activity;
    }

    public MessageDialog() {
    }

    public Dialog onCreateDialog(Bundle instance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setClass(activity, classNav);
                startActivity(intent);
                activity.finish();
                getDialog().dismiss();
            }
        });
        return builder.create();
    }
}
