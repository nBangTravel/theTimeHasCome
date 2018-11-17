package com.example.nbang.nbangtravel;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.ArrayList;

public class HomeActivity extends ListActivity {

    static ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        setListAdapter(adapter);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    public void addItems(View view) {
        EditText editText2 = (EditText)findViewById(R.id.participator);
        listItems.add(editText2.getText().toString());
        adapter.notifyDataSetChanged();
        editText2.setText("");
    }

    public void createtravel(View view){
        AccountActivity.listItemsac.clear();
        for(int i = 0; i < listItems.size(); i++){
            AccountActivity.listItemsac.add(listItems.get(i));
        }
        listItems.clear();
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        startActivity(intent);
    }
}