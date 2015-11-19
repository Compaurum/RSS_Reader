package com.example.compaurum.rss_reader.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.compaurum.rss_reader.MainActivity;
import com.example.compaurum.rss_reader.R;

public class YesNoDialog {
    private AlertDialog.Builder adb;
    private Context mContext;
    private int position;
    private int dialogType;

    public YesNoDialog(Context context, int dialogType, int position) {
        this.dialogType = dialogType;
        if (dialogType == YesNoDialogListener.YES_NO_DIALOG_DELETE_ITEM) {
            this.mContext = (MainActivity) context;
            this.position = position;
            adb = new AlertDialog.Builder(context);
            adb.setTitle(R.string.delete)
                    .setMessage(R.string.questionDelete)
                    .setIcon(R.drawable.ic_warning_black_36dp)
                    .setPositiveButton(R.string.yes, answerListener)
                    .setNegativeButton(R.string.no, answerListener)
                    .setCancelable(true);
        }
    }

    public YesNoDialog(Context context, int dialogType){
        this.dialogType = dialogType;
        if (dialogType == YesNoDialogListener.YES_NO_DIALOG_DELETE_ALL) {
            this.mContext = (MainActivity) context;
            adb = new AlertDialog.Builder(context);
            adb.setTitle(R.string.delete)
                    .setMessage(R.string.questionDeleteAll)
                    .setIcon(R.drawable.ic_warning_black_36dp)
                    .setPositiveButton(R.string.yes, answerListener)
                    .setNegativeButton(R.string.no, answerListener)
                    .setCancelable(true);
        }
    }

    public AlertDialog create() {
        return adb.create();
    }

    DialogInterface.OnClickListener answerListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            ((YesNoDialogListener) mContext).onYesNoDialogClicked(dialogType, which, position);
        }
    };

}
