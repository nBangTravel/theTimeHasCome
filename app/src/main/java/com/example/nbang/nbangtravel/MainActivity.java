package com.example.nbang.nbangtravel;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Checklist List Update
       /* ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, "checklist example1");
        ListView listview = (ListView)findViewById(R.id.list_checklist);
        listview.setAdapter(adapter);*/


        //Fragment assign
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ChecklistActivity checklistActivity = new ChecklistActivity();

        fragmentTransaction.add(R.id.fragment_checklist, checklistActivity);
    }
}
