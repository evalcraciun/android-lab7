package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class SurfaceActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private final String TAG = "SurfaceViewActivity";
    private Camera camera;
    private SurfaceView surfaceView;
    private Button btn;
    private SurfaceHolder surfaceHolder;
    private Camera.PictureCallback pictureCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface);
        //surface
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        //button
        btn = findViewById(R.id.take_pic);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);



        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.e(TAG, "surface clicked!");
                camera.takePicture(null, null, pictureCallback);
            }
        });

        pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Bitmap cBitmap =  Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), null, true);
                String pathFileName = currentDateFormat();
                storePhotoToStorage(cBitmap, pathFileName);
                Toast.makeText(getApplicationContext(), "Saved Successfully!", Toast.LENGTH_LONG).show();
                SurfaceActivity.this.camera.startPreview();
            }
        };
    }

    private void storePhotoToStorage(Bitmap cBitmap, String pathFileName) {
        File outputFile = new File(Environment.getExternalStorageDirectory(), "/DCIM/" + "photo_" + pathFileName + ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            cBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String currentDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM_HH_mm_ss");
        return dateFormat.format(new Date());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open();
        } catch (Exception e) {}

        Camera.Parameters parameters;
        parameters = camera.getParameters();
        parameters.setPreviewFrameRate(20);
        parameters.setPreviewSize(352, 288);
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }
}
