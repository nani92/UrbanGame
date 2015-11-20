package com.example.nataliajastrzebska.urbangame.createTaskActivites;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.vrtoolkit.cardboard.*;


import com.example.nataliajastrzebska.urbangame.R;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CreateTaskFindAndAnswer_AddToScene_Cardboard extends CardboardActivity
        implements CardboardView.StereoRenderer, SurfaceTexture.OnFrameAvailableListener {

    CardboardView cardboardView;
    private SurfaceTexture surface;
    private Camera camera;
    private int texture;
    private static final int GL_TEXTURE_EXTERNAL_OES = 0x8D65;
    Context ctx;


    private float[] mView;
    private float[] mCamera;
    private float[] mModelCube;
    private int mProgram;
    private FloatBuffer vertexBuffer, textureVerticesBuffer;
    private ShortBuffer drawListBuffer;
    private int mPositionHandle;
    private int mTextureCoordHandle;
    private final int vertexStride = COORDS_PER_VERTEX * 4;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    private float[] color;

    private Triangle mTriangle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task_find_and_answer_addtoscene_cardboard);

        ctx = this;

        cardboardView = (CardboardView) findViewById(R.id.cardboardView_findAndAnswerCreateTask);
        cardboardView.setRestoreGLStateEnabled(false);
        cardboardView.setRenderer(this);
        setCardboardView(cardboardView);

        mCamera = new float[16];
        mView = new float[16];
        mModelCube = new float[16];

        color = new float[]{1.0f, 0.0f, 0.0f, 1.0f};

    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        this.cardboardView.requestRender();
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        Log.d ("Natalia", "new Frame");
        float[] mtx = new float[16];
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        surface.updateTexImage();
        surface.getTransformMatrix(mtx);
    }

    @Override
    public void onDrawEye(Eye eyeTransform) {
        Log.d("Natalia", "draw Eye");
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES20.glActiveTexture(GL_TEXTURE_EXTERNAL_OES);
        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, texture);


        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "position");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT,
                false, vertexStride, vertexBuffer);


        mTextureCoordHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");
        GLES20.glEnableVertexAttribArray(mTextureCoordHandle);
        GLES20.glVertexAttribPointer(mTextureCoordHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT,
                false, vertexStride, textureVerticesBuffer);

        mColorHandle = GLES20.glGetAttribLocation(mProgram, "s_texture");

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTextureCoordHandle);

        mTriangle.draw();

        Matrix.multiplyMM(mView, 0, eyeTransform.getEyeView(), 0, mCamera, 0);

    }

    @Override
    public void onFinishFrame(Viewport viewport) {

    }

    @Override
    public void onSurfaceChanged(int i, int i1) {
    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {
        showToast("Creating surface");

        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 0.5f); // Dark background so text shows up well

        ByteBuffer bb = ByteBuffer.allocateDirect(squareVertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareVertices);
        vertexBuffer.position(0);


        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);


        ByteBuffer bb2 = ByteBuffer.allocateDirect(textureVertices.length * 4);
        bb2.order(ByteOrder.nativeOrder());
        textureVerticesBuffer = bb2.asFloatBuffer();
        textureVerticesBuffer.put(textureVertices);
        textureVerticesBuffer.position(0);

        int vertexShader = loadGLShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadGLShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);

        mTriangle = new Triangle();

        texture = createTexture();
        startCamera(texture);
    }

    @Override
    public void onRendererShutdown() {

    }

    private final String vertexShaderCode =
            "attribute vec4 position;" +
                    "attribute vec2 inputTextureCoordinate;" +
                    "varying vec2 textureCoordinate;" +
                    "void main()" +
                    "{"+
                    "gl_Position = position;"+
                    "textureCoordinate = inputTextureCoordinate;" +
                    "}";

    private final String fragmentShaderCode =
            "#extension GL_OES_EGL_image_external : require\n"+
                    "precision mediump float;" +
                    "varying vec2 textureCoordinate;                            \n" +
                    "uniform samplerExternalOES s_texture;               \n" +
                    "void main(void) {" +
                    "  gl_FragColor = texture2D( s_texture, textureCoordinate );\n" +
                    //"  gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);\n" +
                    "}";

    static float squareVertices[] = { // in counterclockwise order:
            -1.0f, -1.0f,   // 0.left - mid
            1.0f, -1.0f,   // 1. right - mid
            -1.0f, 1.0f,   // 2. left - top
            1.0f, 1.0f,   // 3. right - top
    };

    static float textureVertices[] = {
            0.0f, 1.0f,  // A. left-bottom
            1.0f, 1.0f,  // B. right-bottom
            0.0f, 0.0f,  // C. left-top
            1.0f, 0.0f   // D. right-top
    };

    static final int COORDS_PER_VERTEX = 2;

    private short drawOrder[] =  {0, 2, 1, 1, 2, 3 }; // order to draw vertices
    private short drawOrder2[] = {2, 0, 3, 3, 0, 1};

    public void startCamera(int texture) {
        surface = new SurfaceTexture(texture);
        surface.setOnFrameAvailableListener(this);

        camera = Camera.open();

        try {
            camera.setPreviewTexture(surface);
            camera.startPreview();
        } catch (IOException ioe) {
            Log.w("MainActivity", "CAM LAUNCH FAILED");
        }
    }

    static private int createTexture() {
        int[] texture = new int[1];

        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES,
                        GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
                GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES,
                        GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES,
                        GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        return texture[0];
    }

    public void showToast(final String text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private int loadGLShader(int type, String code) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, code);
        GLES20.glCompileShader(shader);

        // Get the compilation status.
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        // If the compilation failed, delete the shader.
        if (compileStatus[0] == 0) {
            GLES20.glDeleteShader(shader);
            shader = 0;
        }

        if (shader == 0) {
            throw new RuntimeException("Error creating shader.");
        }

        return shader;
    }


    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }


}

