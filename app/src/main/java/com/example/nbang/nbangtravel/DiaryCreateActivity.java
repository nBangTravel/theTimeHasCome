package com.example.nbang.nbangtravel;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Date;


public class DiaryCreateActivity extends AppCompatActivity implements DatePickerFragment.OnCompleteListener {

    private SQLiteDatabase db = null;
    private Cursor constantsCursor = null;
    private static final int DELETE_ID = Menu.FIRST+1;
    public static int checks = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private final int GALLERY_CODE=1112;
    private String date;
    private int DATE_REQ_CODE;

    public void onComplete(String date) {
        // After the dialog fragment completes, it calls this callback.
        // use the string here
        TextView textView = (TextView) findViewById(R.id.diary_create_date);
        textView.setText(date);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_create);
        if(checks == 1){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE); }
            checks = 0;
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
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ((ImageView) findViewById(R.id.diary_create_picture)).setImageBitmap(imageBitmap);
        }
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
    }

    public void save(View view){
        ContentValues values = new ContentValues(4);
        values.put(DiaryContract.ConstantEntry.COLUMN_NAME_DATE, ((TextView)findViewById(R.id.diary_create_date)).getText().toString());
        values.put(DiaryContract.ConstantEntry.COLUMN_NAME_TITLE, ((EditText)findViewById(R.id.diary_create_title)).getText().toString());
        //values.put(DiaryContract.ConstantEntry.COLUMN_NAME_PICTURE, DataBaseHelper.getBite(R.id.diary_create_picture));
        //TODO: EOROROROR
        values.put(DiaryContract.ConstantEntry.COLUMN_NAME_CONTENT, ((EditText)findViewById(R.id.diary_create_content)).getText().toString());

        db.insert(DiaryContract.ConstantEntry.TABLE_NAME, DiaryContract.ConstantEntry.COLUMN_NAME_TITLE, values);
        //저장 메세지 띄워주기
        Toast.makeText(this, "저장되었습니다", Toast.LENGTH_SHORT).show();
    }

}
