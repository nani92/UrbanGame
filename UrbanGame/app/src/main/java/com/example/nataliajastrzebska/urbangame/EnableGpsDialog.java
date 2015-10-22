package com.example.nataliajastrzebska.urbangame;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

/**
 * Created by nataliajastrzebska on 19/10/15.
 */
public class EnableGpsDialog extends DialogFragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.dialog_enable_gps, container, false);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);


        Button btnOk=(Button)v.findViewById(R.id.btn_enableGPSDialog_OK);
        btnOk.setOnClickListener(this);
        Button btnCancel=(Button)v.findViewById(R.id.btn_chooseEnableGPSDialog_CANCEL);
        btnCancel.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_enableGPSDialog_OK){
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
        }
        dismiss();
    }
}
