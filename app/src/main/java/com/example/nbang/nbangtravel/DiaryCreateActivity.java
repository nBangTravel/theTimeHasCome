package com.example.nbang.nbangtravel;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class DiaryCreateActivity extends AppCompatActivity implements DatePickerFragment.OnCompleteListener {

    private SQLiteDatabase db = null;
    public static int checks = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private final int GALLERY_CODE=1112;
    public static byte[] getByteArr = null;
    public static int editId;



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
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE); }
            checks = 0;
        }
        if (checks == 80) {
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
        ImageView image = (ImageView)findViewById(R.id.diary_create_picture);
        Bitmap image_bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ContentValues values = new ContentValues();
        values.put(DiaryContract.ConstantEntry.COLUMN_NAME_DATE, ((TextView)findViewById(R.id.diary_create_date)).getText().toString());
        values.put(DiaryContract.ConstantEntry.COLUMN_NAME_TITLE, ((EditText)findViewById(R.id.diary_create_title)).getText().toString());
        values.put(DiaryContract.ConstantEntry.COLUMN_NAME_PICTURE, getBite(image_bitmap));
        values.put(DiaryContract.ConstantEntry.COLUMN_NAME_CONTENT, ((EditText)findViewById(R.id.diary_create_content)).getText().toString());

        if(checks == 80) {
            db.update(DiaryContract.ConstantEntry.TABLE_NAME, values,"_id = "+ editId, null);
            Toast.makeText(this, "수정되었습니다", Toast.LENGTH_SHORT).show();
            checks = 0;
        } else {
            db.insert(DiaryContract.ConstantEntry.TABLE_NAME, DiaryContract.ConstantEntry.COLUMN_NAME_DATE, values);
            Toast.makeText(this, "저장되었습니다", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(this, MainActivity.class);
        MainActivity.check_ac=88;
        startActivity(intent);
    }

    public byte[] getBite(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        return  out.toByteArray();
    }
}
