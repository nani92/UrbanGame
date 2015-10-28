package com.example.nataliajastrzebska.urbangame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

/**
 * Created by nataliajastrzebska on 06/10/15.
 */
public class ChooseCreatingModeDialog extends DialogFragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.dialog_choose_creating_mode, container, false);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        Button classicModeBtn=(Button)v.findViewById(R.id.btn_chooseCreatingModeDialog_modeClassic);
        classicModeBtn.setOnClickListener(this);
        Button remoteModeBtn=(Button)v.findViewById(R.id.btn_chooseCreatingModeDialog_modeRemote);
        remoteModeBtn.setOnClickListener(this);
        return v;
    }

    public void onRemoteModeClicked(){
        Intent i = new Intent(getActivity(), GameSettingsActivity.class).putExtra("mode", CreatingModeEnum.REMOTE);
        startActivity(i);
    }

    public void onClassicModeClicked(){
        Intent i = new Intent(getActivity(),CreateClassicGame.class);
        startActivity(i);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_chooseCreatingModeDialog_modeClassic){
            onClassicModeClicked();
        }
        if(v.getId() == R.id.btn_chooseCreatingModeDialog_modeRemote){
            onRemoteModeClicked();
        }
        this.dismiss();
    }
}
