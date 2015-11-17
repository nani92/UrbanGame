package com.example.nataliajastrzebska.urbangame.createTaskActivites;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;

import com.google.vrtoolkit.cardboard.*;


import com.example.nataliajastrzebska.urbangame.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TaskFindAndAnswer extends CardboardActivity implements CardboardView.StereoRenderer, SurfaceTexture.OnFrameAvailableListener {

    CardboardView cardboardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_find_and_answer);
        cardboardView = (CardboardView) findViewById(R.id.cardboardView_findAndAnswerCreateTask);
        cardboardView.setRenderer(this);
        setCardboardView(cardboardView);

    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {

    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
    }

    @Override
    public void onDrawEye(EyeTransform eyeTransform) {
    }

    @Override
    public void onFinishFrame(Viewport viewport) {

    }

    @Override
    public void onSurfaceChanged(int i, int i1) {

    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {
    }

    private int loadGLShader(int type, String code) {
    }

    @Override
    public void onRendererShutdown() {

    }
[]

}
