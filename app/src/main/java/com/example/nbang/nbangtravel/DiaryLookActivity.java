package com.example.nbang.nbangtravel;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DiaryLookActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    public static int ID;
    private static Cursor constantsCursor = null;
    boolean permessionCheck = false;
    private static final int REQUEST_EXTERNAL_STORAGE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_look);

        Intent intent = getIntent();
        ID = intent.getExtras().getInt("this_ID");
        db = (new DataBaseHelper(this)).getWritableDatabase();
        constantsCursor = db.rawQuery("SELECT " + "*" +
                " FROM " + DiaryContract.ConstantEntry.TABLE_NAME +
                " WHERE " + DiaryContract.ConstantEntry._ID + " = " + ID, null);
        Log.i("------I DID NOT GET ONE", constantsCursor.getCount()+"");
        if(constantsCursor.getCount() == 1){
            Log.i("---------I GOT ONE", constantsCursor.getCount()+"");
            if(constantsCursor.moveToFirst()) {
                /*String sTitle = constantsCursor.getString(2);
                String sDate = constantsCursor.getString(1);
                String sContent = constantsCursor.getString(4);
                Log.i("-------TITLE", sTitle);
                Log.i("-------DATE", sDate);
                Log.i("-------CONTENT", sContent);*/
                TextView title = (TextView) findViewById(R.id.diary_look_title);
                title.setText(constantsCursor.getString(2));
                TextView date = (TextView) findViewById(R.id.diary_look_date);
                date.setText(constantsCursor.getString(1));
                TextView content = (TextView) findViewById(R.id.diary_look_content);
                content.setText(constantsCursor.getString(4));
                ImageView picture = (ImageView) findViewById(R.id.diary_look_picture);
                byte[] src = constantsCursor.getBlob(3);
                Bitmap b = null;
                if (src != null) {
                    b = BitmapFactory.decodeByteArray(src, 0, src.length);
                }
                picture.setImageBitmap(b);
            }
        }
        ImageButton edit = (ImageButton) findViewById(R.id.diary_look_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(view.getContext(), DiaryCreateActivity.class);
                DiaryCreateActivity.getEditIntent=1;
                editIntent.putExtra("edit_ID", ID);
                startActivity(editIntent);
            }
        });

    }

    public void ask_delete(View view) {
        new AlertDialog.Builder(this).setTitle("삭제하시겠습니까?").setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                delete();
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //ignore
            }
        }).show();
    }

    public void delete() {
        db.delete(DiaryContract.ConstantEntry.TABLE_NAME, "_ID = " + ID, null );
        Toast.makeText(this, "삭제되었습니다.",Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        MainActivity.check_ac=88;
        startActivity(intent);
    }

    public void shareInstagram(View view) {
        onRequestPermission();
        if (permessionCheck) {
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
            String getTime = simpleDateFormat.format(date);
            ImageView picture = (ImageView) findViewById(R.id.diary_look_picture);
            Bitmap bitmap = ((BitmapDrawable)picture.getDrawable()).getBitmap();
            String storage = Environment.getExternalStorageDirectory().getAbsolutePath();
            String fileName = "nbangtravel"+getTime+".png";
            String folder = "/nBangTravel/";
            String fullPath = storage+folder;
            File file;
            try {
                file = new File(fullPath);
                if(!file.isDirectory()){
                    file.mkdir();
                }
                FileOutputStream fos = new FileOutputStream(fullPath+fileName);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Intent share = new Intent(Intent.ACTION_SEND);
            Log.i("------INSTAGRAM","intent created-----------");
            share.setType("image/*");
            //Uri uri = Uri.fromFile(new File(fullPath, fileName));
            Uri uri = FileProvider.getUriForFile( this, "com.example.nbang.nbangtravel.fileprovider",new File(fullPath, fileName));

            try {
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.setPackage("com.instagram.android");
                startActivity(share);
                Log.i("------INSTAGRAM","intent started-----------");
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "인스타그램이 설치되지 않았습니다.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void shareKakaoTalk(View view){
        onRequestPermission();
        if (permessionCheck) {
            Intent kakao = new Intent(Intent.ACTION_SEND);
            Log.i("------KAKAOTALK","intent created-----------");
            String message = (String) ((TextView)findViewById(R.id.diary_look_content)).getText();
            kakao.setType("text/plain");


            try {
                kakao.putExtra(Intent.EXTRA_TEXT, message);
                kakao.setPackage("com.kakao.talk");
                startActivity(kakao);
                Log.i("------KAKAOTALK","intent started-----------");
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "카카오톡이 설치되지 않았습니다.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void onRequestPermission() {
        Toast.makeText(this, "권한을 확인하는 중..", Toast.LENGTH_SHORT).show();
        int permissionReadStorage = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWriteStorage = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionReadStorage == PackageManager.PERMISSION_DENIED || permissionWriteStorage == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE_CODE);
        } else {
            permessionCheck = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE_CODE:
                for (int i = 0; i<permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if(permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "공유되었습니다.", Toast.LENGTH_SHORT).show();
                            permessionCheck= true;
                        } else {
                            Toast.makeText(this, "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                            permessionCheck= false;
                        }
                    }
                }
                break;
        }
    }



    public void shareFacebook (View view) {

    }
}