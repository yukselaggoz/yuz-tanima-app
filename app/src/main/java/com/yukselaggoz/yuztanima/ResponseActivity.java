package com.yukselaggoz.yuztanima;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_face;
import org.bytedeco.javacpp.opencv_objdetect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.bytedeco.javacpp.opencv_face.createEigenFaceRecognizer;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;

public class ResponseActivity extends AppCompatActivity {

    public static final String TAG = "ResponseActivity";

    ImageView imgPerson;
    Bitmap mBitmap;
    Uri mUri;

    Mat rgbaMat = new Mat();

    opencv_objdetect.CascadeClassifier faceDetector;

    opencv_face.FaceRecognizer faceRecognizer = createEigenFaceRecognizer();
    //opencv_face.FaceRecognizer faceRecognizer = createFisherFaceRecognizer();
    //opencv_face.FaceRecognizer faceRecognizer = createLBPHFaceRecognizer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);

        imgPerson = findViewById(R.id.img_person);

        mUri = getIntent().getData();
        Toast.makeText(ResponseActivity.this, "tesim yolu" + mUri.toString(), Toast.LENGTH_SHORT).show();
        imgPerson.setImageURI(mUri);
        InputStream pictureInputStream = null;
        try {
            pictureInputStream = getContentResolver().openInputStream(mUri);

            mBitmap = BitmapFactory.decodeStream(pictureInputStream);

            imgPerson.setImageBitmap(mBitmap);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    faceDetector = TrainHelper.loadClassifierCascade(ResponseActivity.this, R.raw.frontalface);
                    File folder = new File(getFilesDir(), TrainHelper.TRAIN_FOLDER);
                    File f = new File(folder, TrainHelper.EIGEN_FACES_CLASSIFIER);
                    faceRecognizer.load(f.getAbsolutePath());
                }catch (Exception e) {
                    Log.d(TAG, e.getLocalizedMessage(), e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }
        }.execute();

        //resimi matrise ayarlama
        rgbaMat = imread(mUri.getPath());
        if(rgbaMat == null){
            Toast.makeText(ResponseActivity.this, "matris atanmadı", Toast.LENGTH_SHORT).show();
        }
        /*
        Mat greyMat = new Mat(rgbaMat.rows(), rgbaMat.cols());
        cvtColor(rgbaMat, greyMat, CV_BGR2GRAY);
        opencv_core.RectVector faces = new opencv_core.RectVector();
        //faceDetector.detectMultiScale(greyMat, faces, 1.25f, 3, 1, new Size(mBitmap.getWidth(), mBitmap.getHeight()), new Size(4 * 92, 4 * 112));
        faceDetector.detectMultiScale(greyMat, faces);
        int x = faces.get(0).x();
        int y = faces.get(0).y();
        int w = faces.get(0).width();
        int h = faces.get(0).height();
        rectangle(rgbaMat, new opencv_core.Point(x, y), new opencv_core.Point(x + w, y + h), opencv_core.Scalar.GREEN, 2, LINE_8, 0);

        //Detection
        try {
            TrainHelper.takePhoto(getBaseContext(), 1, TrainHelper.qtdPhotos(getBaseContext()) + 1, rgbaMat.clone(), faceDetector);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Recognition
        Mat detectedFace = new Mat(greyMat, faces.get(0));
        resize(detectedFace, detectedFace, new Size(TrainHelper.IMG_SIZE,TrainHelper.IMG_SIZE));

        IntPointer label = new IntPointer(1);
        DoublePointer reliability = new DoublePointer(1);
        faceRecognizer.predict(detectedFace, label, reliability);
        int prediction = label.get(0);
        double acceptanceLevel = reliability.get(0);
        String name;
        if (prediction == -1 || acceptanceLevel >= ACCEPT_LEVEL) {
            name = "Kişi Bulunmadı";
        } else {
            name = "Suçlu kişiler arasında bulundu" + " - " + acceptanceLevel;
        }

        //Train
        try {
            TrainHelper.train(getBaseContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        */



    }
}
