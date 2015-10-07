package com.example.nataliajastrzebska.urbangame;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Created by nataliajastrzebska on 06/10/15.
 */
public class ChooseCreatingModeDialog extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.dialog_choose_creating_mode, container, false);
        //dialogStyling(getDialog());
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return v;
    }

    void dialogStyling(Dialog dialog){
        dialog.setTitle(R.string.dialog_chooseCreatingMode_title);
    }

}
