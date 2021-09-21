package com.converter.imagetotext_offline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    EditText textView;
    Dialog dDialogMore, dDialogChoose;
    AdManager adManager;
    AdView mAdView;
    String s;
    MyClipboardManager myClipboardManager;
   // WebView textView;
   Toast toastActivate;
   TextView description, tvtextView;
   View payout, chayout;
   private final int REQUEST_CODE_STOREGE_PERMESSION = 1;
   private final int REQUEST_CODE_SELECT_IMAGE = 2;
   private final int REQUEST_CODE_CAPTURE_IMAGE = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compart();
        myClipboardManager = new MyClipboardManager();
        //find imageview
        imageView = findViewById(R.id.imageView);
        //find textview
        tvtextView = findViewById(R.id.tvwebView);
        textView = findViewById(R.id.editwebView);
        dDialogMore = new Dialog(this);
        dDialogMore.setContentView(R.layout.custom_dialog_more);

        dDialogChoose = new Dialog(this);
        dDialogChoose.setContentView(R.layout.custom_dialog_choose);
     //   final Button btnRate = dDialogMore.findViewById(R.id.btnRate);
     //   final Button btnMoreShare = dDialogMore.findViewById(R.id.btnMoreShare);
//llll
        mAdView = findViewById(R.id.adView);
        adManager = new AdManager(this);
        adManager.LoadAdsBannerWithInterstitial(mAdView);

        //check app level permission is granted for Camera
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            //grant the permission
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
        }
    }

    public void btn_take(View view) {
      //  dDialogChoose.show();

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_STOREGE_PERMESSION);
        }else{
            selectImage();
        }

    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STOREGE_PERMESSION && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectImage();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK){
            if ( data != null){
                Uri selectImageUri = data.getData();
                if (selectImageUri != null){
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectImageUri);
                        Bitmap b = BitmapFactory.decodeStream(inputStream);
                        Bundle bundle = data.getExtras();
                        //from bundle, extract the image
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                       // imageView.setImageBitmap(b);
                        ml(b);

                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        else if (requestCode == REQUEST_CODE_CAPTURE_IMAGE && resultCode == RESULT_OK){

            assert data != null;
            Bundle bundle = data.getExtras();
            //from bundle, extract the image
            assert bundle != null;
            Bitmap bitmap = (Bitmap) bundle.get("data");
            // imageView.setImageBitmap(b);
            ml(bitmap);
        }

    }
    public void ml(Bitmap bitmap){
      /*
        Bundle bundle = data.getExtras();
        //from bundle, extract the image
        Bitmap bitmap = (Bitmap) bundle.get("data");
        //set image in imageview
*/

        imageView.setImageBitmap(bitmap);
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVision firebaseVision = FirebaseVision.getInstance();
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = firebaseVision.getOnDeviceTextRecognizer();
        Task<FirebaseVisionText> task = firebaseVisionTextRecognizer.processImage(firebaseVisionImage);
        task.addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                s = firebaseVisionText.getText();
                tvtextView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.INVISIBLE);
                tvtextView.setText(s);
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void compart(){
        if(!getPackageName().equals("com.converter."+"imagetotext_offline")){
            String error = null;
            error.getBytes();
        }
    }

    public void btnMore(View view) {
        final Button btnRate = dDialogMore.findViewById(R.id.btnRate);
        final Button btnMoreShare = dDialogMore.findViewById(R.id.btnMoreShare);

        dDialogMore.show();
    }
    public void onClickShare(View view) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Image to text - AI app on Google Play \n\n" + "https://play.google.com/store/apps/developer?id=SnapQueen";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, R.string.app_name);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void onClickRate(View view) {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    // share text
    public void onClickShareText(View view) {
        String text = textView.getText().toString();
        Intent i = new Intent();
        i.setAction(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_TEXT, text);
        i.setType("text/plain");
        i = Intent.createChooser(i, "Share By");
        startActivity(i);


    }

    public void onClickCopy(View view) {
                String text = textView.getText().toString();
                myClipboardManager.copyToClipboard(MainActivity.this, text);
               // Toast.makeText(MainActivity.this, R.string.copy_text_to_clipboard, Toast.LENGTH_SHORT).show();
                afficher(R.layout.custom_toast_activated, R.id.toast_layout_root, R.id.description,
                        R.string.c_txt_clioard);
    }

    public void onClickGallory(View view) {
    }

    public void onClickCamera(View view) {
       // open the camera => create an Intent object
          Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
          startActivityForResult(intent, REQUEST_CODE_CAPTURE_IMAGE);
    }

    public void onClickTranslate(View view) {
        String text = textView.getText().toString();
        Intent intent = new Intent(MainActivity.this, TranslationActivity.class);
        intent.putExtra("translate text", text);
        startActivity(intent);

    }
    public void  afficher(int ly, int ly_root, int desc, int stDEsc){
        LayoutInflater inflater = getLayoutInflater();
        View layoutD = inflater.inflate(ly,
                (ViewGroup) findViewById(ly_root));

        description = layoutD.findViewById(desc);
        description.setText(stDEsc);
// Create and show the Toast object
        toastActivate = new Toast(getApplicationContext());
        toastActivate.setGravity(Gravity.CENTER, 0, 0);
        toastActivate.setDuration(Toast.LENGTH_LONG);
        toastActivate.setView(layoutD);
        toastActivate.show();
    }

    public void onClickTextEdit(View view) {
        tvtextView.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.VISIBLE);
        textView.setText(s);
    }
}