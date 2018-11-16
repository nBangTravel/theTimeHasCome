package com.example.nbang.nbangtravel;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;


public class DiaryCreateActivity extends AppCompatActivity {

    private SQLiteDatabase db = null;
    private Cursor constantsCursor = null;
    private static final int DELETE_ID = Menu.FIRST+1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_create);
        Intent intent = getIntent();

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void createDiary() {
        //TODO: 저장하기
    }

    public void openGallery() {

    }

    public void cameraIntent() {

    }
}
