package com.example.nbang.nbangtravel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    static int check_ac = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switch (check_ac) {
            case 1:
                AccountActivity accountActivity = new AccountActivity();
                FragmentTransaction transaction_ac = getSupportFragmentManager().beginTransaction();
                transaction_ac.replace(R.id.fragment_container, accountActivity).addToBackStack(null).commit();
                check_ac = 0;
                break;

            case 88:
                DiaryActivity diaryActivity = new DiaryActivity();
                FragmentTransaction transaction_di = getSupportFragmentManager().beginTransaction();
                transaction_di.add(R.id.fragment_container, diaryActivity).addToBackStack(null).commit();
                check_ac = 0;
                break;

            default:
                ChecklistActivity checklistActivity = new ChecklistActivity();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_container, checklistActivity).addToBackStack(null).commit();
        }

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
        Intent intent = new Intent(view.getContext(), HomeActivity.class);
        startActivity(intent);
    }

    public void onClick_camera(View view){
        Intent intent = new Intent(view.getContext(), DiaryCreateActivity.class);
        startActivity(intent);
        DiaryCreateActivity.checks = 1;
    }



}