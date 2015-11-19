package com.ivanov.denis.rss_reader.dialog;

/**
 * Created by denis on 19.11.2015.
 */
public interface YesNoDialogListener {
    int YES_NO_DIALOG_DELETE_ITEM = 101;
    int YES_NO_DIALOG_DELETE_ALL = 102;

    void onYesNoDialogClicked(int dialogType, int result, int position);
    void onYesNoDialogCancelled();
}
