package com.example.nbang.nbangtravel;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    //private SQLiteDatabase db = null;
    //private Cursor constantCursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*db=(new DataBaseHelper(this)).getWritableDatabase();
        constantCursor = db.rawQuery("SELECT " + Sample_CheckList.ConstantEntry._ID + ", " + Sample_CheckList.ConstantEntry.COLUMN_NAME_TITLE +
                " " + " FROM " + Sample_CheckList.ConstantEntry.TABLE_NAME +
                " ORDER BY " + Sample_CheckList.ConstantEntry._ID, null);

        ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.listview_checklist, constantCursor,
                new String[] {Sample_CheckList.ConstantEntry.COLUMN_NAME_TITLE},
                new int[] {R.id.title}, 0);

        List list = (List)findViewById(R.id.list_checklist);
        list.setListAdapter(adapter);
        registerForContextMenu(getListView());*/


        /*final ArrayList<String> items = new ArrayList<String>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.activity_list_item, items);

        final ListView listView = (ListView) findViewById(R.id.list_checklist);
        listView.setAdapter(adapter);*/

        ChecklistActivity checklistActivity = new ChecklistActivity();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, checklistActivity).addToBackStack(null).commit();
    }

    protected void onClick_account(View view){
        AccountActivity accountActivity = new AccountActivity();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, accountActivity).addToBackStack(null).commit();
    }

    protected void onClick_check(View view){
        ChecklistActivity checklistActivity = new ChecklistActivity();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, checklistActivity).addToBackStack(null).commit();
    }

    protected void onClick_diary(View view){
        DiaryActivity diaryActivity = new DiaryActivity();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, diaryActivity).addToBackStack(null).commit();
    }

    protected void onClick_home(View view){
        //TODO: Home, First_Default & Default
    }

    protected void onClick_camera(View view){
        //TODO: Camera Intent
    }


}