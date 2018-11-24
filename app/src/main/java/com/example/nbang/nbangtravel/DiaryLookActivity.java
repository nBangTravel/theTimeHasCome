package com.example.nbang.nbangtravel;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DiaryLookActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    public static int ID;
    private static Cursor constantsCursor = null;

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
}