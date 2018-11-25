package com.example.nbang.nbangtravel;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class AccountLookActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor constantsCursor = null;
    public static int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_look);
        db = (new DataBaseHelper(this)).getWritableDatabase();
        Intent intent = getIntent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TextView title = (TextView) findViewById(R.id.title);
            title.setText("제목: " + intent.getExtras().getString("this_title"));
            TextView date = (TextView) findViewById(R.id.date);
            date.setText("날짜: " + intent.getExtras().getString("this_date"));
            TextView participator = (TextView) findViewById(R.id.participator);
            participator.setText("참여자: " + intent.getExtras().getString("this_participator"));
            TextView price = (TextView) findViewById(R.id.price);
            price.setText("가격: " + intent.getExtras().getInt("this_price"));
            TextView currency = (TextView) findViewById(R.id.currency);
            currency.setText("통화: " + intent.getExtras().getString("this_currency"));
        }
        db.close();
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
        db = (new DataBaseHelper(this)).getWritableDatabase();
        db.delete(AccountingContract.ConstantEntry.TABLE_NAME, "_ID = " + ID, null );
        Toast.makeText(this, "삭제되었습니다.",Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        MainActivity.check_ac=1;
        db.close();
        startActivity(intent);
    }

    public void edit(View view) {
    }
}
