package com.example.nbang.nbangtravel;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class DiaryLookActivity extends AppCompatActivity {
    public static int SHOW_DIARY = 0;
    private SQLiteDatabase db;
    private Cursor constantsCursor = null;
    public static String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_look);
        db = (new DataBaseHelper(this)).getWritableDatabase();
        Intent intent = getIntent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            ID= Objects.requireNonNull(intent.getExtras()).getString("this_id"); //ERROR
            TextView title = (TextView) findViewById(R.id.diary_look_title);
            title.setText(intent.getExtras().getString("this_title"));
            TextView date = (TextView) findViewById(R.id.diary_look_date);
            date.setText(intent.getExtras().getString("this_date"));
            TextView content = (TextView) findViewById(R.id.diary_look_content);
            content.setText(intent.getExtras().getString("this_content"));
            ImageView picture = (ImageView) findViewById(R.id.diary_look_picture);

            byte[] src = intent.getExtras().getByteArray("this_picture");
            Bitmap b = null;
            if (src != null) {
                b = BitmapFactory.decodeByteArray(src, 0, src.length);
            }
            picture.setImageBitmap(b);
        }
        //TODO:
        SHOW_DIARY = 0;

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
        String[] args = {ID};
        db.delete(DiaryContract.ConstantEntry.TABLE_NAME, "_ID=?", args);
        Toast.makeText(this, "삭제되었습니다.",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DiaryActivity.class);
        startActivity(intent);
    }

    public void edit() {
        //TODO
        //intent 로 화면을 create로 바꿔주고, 원래 내용 띄우기
    }

}
