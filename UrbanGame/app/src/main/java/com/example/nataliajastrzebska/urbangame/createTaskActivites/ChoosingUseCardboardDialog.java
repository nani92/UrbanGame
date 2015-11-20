package com.example.nataliajastrzebska.urbangame.createTaskActivites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.nataliajastrzebska.urbangame.ChooseGameTaskTypeActivity;
import com.example.nataliajastrzebska.urbangame.R;

/**
 * Created by nataliajastrzebska on 18/11/15.
 */
public class ChoosingUseCardboardDialog extends DialogFragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.dialog_choose_cardboard_use, container, false);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        Button cardboardBtn=(Button)v.findViewById(R.id.btn_dialogUsingCardboard_yes);
        cardboardBtn.setOnClickListener(this);
        Button noCardboardBtn=(Button)v.findViewById(R.id.btn_btn_dialogUsingCardboard_no);
        noCardboardBtn.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_dialogUsingCardboard_yes){
            startActivity(new Intent(getActivity(), CreateTaskFindAndAnswer_AddToScene_Cardboard.class));
        }
        if(v.getId() == R.id.btn_chooseCreatingModeDialog_modeRemote){
            startActivity(new Intent(getActivity(), CreateTaskFindAndAnswer_AddToScene_Cardboard.class));
        }
        this.dismiss();
    }
}
