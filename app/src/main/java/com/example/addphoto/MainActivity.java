package com.example.addphoto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button button;
    LinearLayout linearlayout;
    int i = 0;
    int which_click =0;
    ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                1);

        button = findViewById(R.id.button);
        linearlayout = findViewById(R.id.linearlayout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                final ImageView imageView = new ImageView(MainActivity.this);
                imageView.setId(i);
                imageView.setImageResource(R.drawable.ic_launcher_background);
                addView(imageView,200,200);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        which_click = imageView.getId();
                        imageView2 = imageView;

                        showPictureDialog(which_click);
                    }
                });
            }
        });

    }

    private void showPictureDialog(final int which_click) {
        androidx.appcompat.app.AlertDialog.Builder pictureDialog = new androidx.appcompat.app.AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new android.content.DialogInterface.OnClickListener() {
                    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery(which_click);
                                break;
                            case 1:
                                takePhotoFromCamera(which_click);
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallery(int which_click) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, which_click);
    }

    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.M)
    private void takePhotoFromCamera(int which_click) {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, which_click);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Uri contentURI = data.getData();

            if(contentURI == null){  // untuk camera
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                Bitmap selected_image = Bitmap.createScaledBitmap(thumbnail, 920, 576, true);
                imageView2.setImageBitmap(selected_image);
            }else{  // untuk gallery
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    imageView2.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }

        }else{

        }

    }

    public void addView(ImageView imageView, int width, int height){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
        layoutParams.setMargins(0,10,0,10);
        imageView.setLayoutParams(layoutParams);
        linearlayout.addView(imageView);
    }


}
