package soy.dow.nbang.nbangtravel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static int check_ac = 0;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(soy.dow.nbang.nbangtravel.R.layout.activity_main);

        switch (check_ac) {
            case 1:
                AccountActivity accountActivity = new AccountActivity();
                FragmentTransaction transaction_ac = getSupportFragmentManager().beginTransaction();
                transaction_ac.replace(soy.dow.nbang.nbangtravel.R.id.fragment_container, accountActivity).commit();
                check_ac = 0;
                break;

            case 88:
                DiaryActivity diaryActivity = new DiaryActivity();
                FragmentTransaction transaction_di = getSupportFragmentManager().beginTransaction();
                transaction_di.add(soy.dow.nbang.nbangtravel.R.id.fragment_container, diaryActivity).commit();
                check_ac = 0;
                break;

            default:
                ChecklistActivity checklistActivity = new ChecklistActivity();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(soy.dow.nbang.nbangtravel.R.id.fragment_container, checklistActivity).commit();
        }
    }

    public void onClick_account(View view){
        AccountActivity accountActivity = new AccountActivity();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(soy.dow.nbang.nbangtravel.R.id.fragment_container, accountActivity).commit();
    }

    public void onClick_check(View view){
        ChecklistActivity checklistActivity = new ChecklistActivity();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(soy.dow.nbang.nbangtravel.R.id.fragment_container, checklistActivity).commit();
    }

    public void onClick_diary(View view){
        DiaryActivity diaryActivity = new DiaryActivity();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(soy.dow.nbang.nbangtravel.R.id.fragment_container, diaryActivity).commit();
    }

    public void onClick_home(View view){
        Intent intent = new Intent(view.getContext(), HomeListActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClick_camera(View view){
        DiaryActivity diaryActivity = new DiaryActivity();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(soy.dow.nbang.nbangtravel.R.id.fragment_container, diaryActivity).commit();
        Intent intent = new Intent(view.getContext(), DiaryCreateActivity.class);
        startActivity(intent);
        DiaryCreateActivity.checks = 1;
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(this, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
}