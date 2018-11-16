package com.example.nbang.nbangtravel;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ChecklistActivity checklistActivity = new ChecklistActivity();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, checklistActivity).addToBackStack(null).commit();
    }

    public void onClick_account(View view){
        AccountActivity accountActivity = new AccountActivity();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, accountActivity).addToBackStack(null).commit();
    }

    public void onClick_check(View view){
        ChecklistActivity checklistActivity = new ChecklistActivity();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, checklistActivity).addToBackStack(null).commit();
    }

    public void onClick_diary(View view){
        DiaryActivity diaryActivity = new DiaryActivity();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, diaryActivity).addToBackStack(null).commit();
    }

    public void onClick_home(View view){
        //TODO: Home, First_Default & Default
    }

    public void onClick_camera(View view){
        //TODO: Camera Intent
    }


}