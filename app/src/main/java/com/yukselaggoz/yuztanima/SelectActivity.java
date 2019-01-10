package com.yukselaggoz.yuztanima;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class SelectActivity extends AppCompatActivity {

    // Flag to indicate the request of the next task to be performed
    private static final int REQUEST_TAKE_PHOTO = 0;
    private static final int REQUEST_SELECT_IMAGE_IN_ALBUM = 1;

    // The URI of photo taken with camera. Fotoğraf makinesi ile çekilen URI
    private Uri mUriPhotoTaken;

    // The URI of the image selected to detect.
    private Uri mImageUri;

    // The image selected to detect.
    Bitmap mBitmap;

    Button btnSelectImg;
    Button btnCapturePhoto;
    Button btnRecognize;
    Button btnCameraPreview;

    ImageView imgSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        btnSelectImg = findViewById(R.id.btn_img_select);
        btnCapturePhoto = findViewById(R.id.btn_img_capture);
        btnRecognize = findViewById(R.id.btn_img_train);
        btnCameraPreview = findViewById(R.id.btn_cam_preview);

        imgSelected = findViewById(R.id.img_person);

        btnSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM);
            }
        });

        btnCapturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    File file = File.createTempFile("IMG_", ".jpg", storageDir);
                    mUriPhotoTaken = Uri.fromFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriPhotoTaken);
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }
        });

        btnCameraPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectActivity.this, RecognizeActivity.class);
                startActivity(intent);
            }
        });

        btnRecognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mImageUri != null) {
                    Toast.makeText(SelectActivity.this, "resim yüklendi...", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SelectActivity.this, ResponseActivity.class);

                    intent.setData(mImageUri);
                    startActivity(intent);

                    /*
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    Bundle extra = new Bundle();
                    extra.putString("imageFilename", mImageUri.toString());

                    intent.putExtra("image",byteArray);
                    startActivity(intent);

                    //intent.putExtra("imagePath", mImageUri.toString());
                    //startActivity(intent);
                    */

                } else {
                    Toast.makeText(SelectActivity.this, "resim yüklenmedi...", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    // Save the activity state when it's going to stop. Durdurulacağı zaman etkinlik durumunu kaydedin.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("ImageUri", mImageUri);
    }

    // Recover the saved state when the activity is recreated. Etkinlik yeniden oluşturulduğunda kaydedilen durumu kurtarın.
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mImageUri = savedInstanceState.getParcelable("ImageUri");
    }

    // Deal with the result of selection of the photos and faces.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO:
                    mImageUri = mUriPhotoTaken;
                    break;
                case REQUEST_SELECT_IMAGE_IN_ALBUM:
                    mImageUri = data.getData();
                    break;
                default:
                    break;
            }

            //convert URI to a stream. URI'yi bir akışa dönüştür
            /*
            InputStream pictureInputStream = null;
            try {
                pictureInputStream = getContentResolver().openInputStream(mImageUri);

                mBitmap = BitmapFactory.decodeStream(pictureInputStream);

                imgSelected.setImageBitmap(mBitmap);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            */
            imgSelected.setImageURI(mImageUri);

            Intent intent = new Intent();
            intent.setData(mImageUri);
            setResult(RESULT_OK, intent);
            setResult(RESULT_OK);
            Toast.makeText(SelectActivity.this, "set result tamam..." , Toast.LENGTH_SHORT).show();
            //finish();
        }
    }
}
