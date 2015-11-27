package com.example.nataliajastrzebska.urbangame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by nataliajastrzebska on 27/11/15.
 */
public class DisplayHintDialog extends DialogFragment implements View.OnClickListener {

    TextView textView;
    String msg;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.dialog_display_hint, container, false);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        Button okButton=(Button)v.findViewById(R.id.btn_dialogDisplayHint_ok);
        okButton.setOnClickListener(this);

        textView = (TextView) v.findViewById(R.id.tv_dialogDisplayHint_msg);
        textView.setText(msg);
        return v;
    }

    public void setMessage(String msg){
        this.msg = msg;
    }


    @Override
    public void onClick(View v) {
        this.dismiss();
    }
}
