package com.example.nbang.nbangtravel;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DiaryCreateActivity extends AppCompatActivity implements DatePickerFragment.OnCompleteListener {

    private SQLiteDatabase db = null;
    public static int checks = 0;
    public static int editDiary = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int GET_GOOD_PIC = 1;
    private final int GALLERY_CODE=1112;
    public static byte[] getByteArr = null;
    public static int editId;
    private String pictureImagePath = "";

    public void onComplete(String date) {
        TextView textView = (TextView) findViewById(R.id.diary_create_date);
        textView.setText(date);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        db = (new DataBaseHelper(this)).getWritableDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_create);

        if(checks == 1){
            /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }*/
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = timeStamp + ".jpg";
            File storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
            File file = new File(pictureImagePath);
            Uri outputFileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".fileprovider", file);
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            if(cameraIntent.resolveActivity(getPackageManager())!= null) {
                startActivityForResult(cameraIntent, GET_GOOD_PIC);
            }
            checks = 0;

        }
        if (editDiary == 1) {
            Intent editInent = getIntent();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                editId=editInent.getExtras().getInt("this_ID");
                TextView title = (TextView) findViewById(R.id.diary_create_title);
                title.setText(editInent.getExtras().getString("this_title"));
                TextView date = (TextView) findViewById(R.id.diary_create_date);
                date.setText(editInent.getExtras().getString("this_date"));
                TextView content = (TextView) findViewById(R.id.diary_create_content);
                content.setText(editInent.getExtras().getString("this_content"));
                ImageView picture = (ImageView) findViewById(R.id.diary_create_picture);
                //byte[] src = intent.getExtras().getByteArray("this_picture");
                Bitmap b = null;
                if (getByteArr != null) {
                    b = BitmapFactory.decodeByteArray(getByteArr, 0, getByteArr.length);
                }
                picture.setImageBitmap(b);
            }
        }
    }


    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void selectGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }

    public void onClickcamera(View view){
        /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }*/
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".fileprovider", file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        if(cameraIntent.resolveActivity(getPackageManager())!= null) {
            startActivityForResult(cameraIntent, GET_GOOD_PIC);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Do the file write
            if (requestCode == 1) {
                File imgFile = new File(pictureImagePath);
                try {
                    Log.i("FILEPATH-------------", pictureImagePath);
                    imgFile.createNewFile();
                    Log.i("HERE----------------", "IMAGE FILE EXISTS");
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ((ImageView) findViewById(R.id.diary_create_picture)).setImageBitmap(myBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Request permission from the user
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        }
        //갤러리에서 가져오기
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            InputStream in = null;
            try {
                in = getContentResolver().openInputStream(data.getData());
                Bitmap imageBitmap = BitmapFactory.decodeStream(in);
                in.close();
                ((ImageView) findViewById(R.id.diary_create_picture)).setImageBitmap(imageBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (requestCode == 1) {
                    File imgFile = new File(pictureImagePath);
                    try {
                        Log.i("FILEPATH-------------", pictureImagePath);
                        imgFile.createNewFile();
                        Log.i("HERE----------------", "IMAGE FILE EXISTS");
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        ((ImageView) findViewById(R.id.diary_create_picture)).setImageBitmap(myBitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //사진찍어서 가져오기
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
           Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ((ImageView) findViewById(R.id.diary_create_picture)).setImageBitmap(imageBitmap);
        }
        //갤러리에서 가져오기
        else if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            InputStream in = null;
            try {
                in = getContentResolver().openInputStream(data.getData());
                Bitmap imageBitmap = BitmapFactory.decodeStream(in);
                in.close();
                ((ImageView)findViewById(R.id.diary_create_picture)).setImageBitmap(imageBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

    public void save(View view){
        ImageView image = (ImageView)findViewById(R.id.diary_create_picture);

        Bitmap image_bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        byte[] img_data = getBite(image_bitmap);
        ContentValues values = new ContentValues();
        values.put(DiaryContract.ConstantEntry.COLUMN_NAME_DATE, ((TextView)findViewById(R.id.diary_create_date)).getText().toString());
        values.put(DiaryContract.ConstantEntry.COLUMN_NAME_TITLE, ((EditText)findViewById(R.id.diary_create_title)).getText().toString());
        values.put(DiaryContract.ConstantEntry.COLUMN_NAME_PICTURE, img_data);
        values.put(DiaryContract.ConstantEntry.COLUMN_NAME_CONTENT, ((EditText)findViewById(R.id.diary_create_content)).getText().toString());
        values.put(DiaryContract.ConstantEntry.COLUMN_NAME_TRAVEL, DataBaseHelper.now_travel);
        if(editDiary == 1) {
            db.update(DiaryContract.ConstantEntry.TABLE_NAME, values,"_id = "+ editId, null);
            Toast.makeText(this, "수정되었습니다", Toast.LENGTH_SHORT).show();
            editDiary = 0;
        } else {
            db.insert(DiaryContract.ConstantEntry.TABLE_NAME, DiaryContract.ConstantEntry.COLUMN_NAME_DATE, values);
            Toast.makeText(this, "저장되었습니다", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(this, MainActivity.class);
        MainActivity.check_ac=88;
        startActivity(intent);
        ImageButton save = (ImageButton)findViewById(R.id.diary_create_save);
        save.setEnabled(false);
    }

    public byte[] getBite(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        return  out.toByteArray();
    }
}
