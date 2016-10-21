package com.example.ye_song.cameratest;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@SuppressWarnings("ALL")
public class CameratestActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    ImageView imageview;
    File file;
    Camera camera;
    Button btnTakePhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cameratest);
        btnTakePhoto = (Button) findViewById(R.id.bTakePhoto);
        SurfaceView mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
        SurfaceHolder mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.takePicture(null,null,pictureCallback);
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera = Camera.open();
        Camera.Parameters params = camera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        try{
            camera.setPreviewDisplay(surfaceHolder);
        }catch (IOException e){
            camera.release();
            camera = null;
        }
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            if(bytes != null){
                savePicture(bytes);
                camera.startPreview();
            }
        }
    };

    private void savePicture(byte[] bytes)  {
        try{
            String imageID = System.currentTimeMillis()+"";
            String pathName = android.os.Environment.getExternalStorageDirectory().getPath()+"/";
            File file = new File(pathName);
            if(!file.exists()){
                file.mkdirs();
            }
            pathName += imageID+".jpg";
            file = new File(pathName);
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();
            Toast.makeText(this, "已经保存在路径："+pathName, Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
