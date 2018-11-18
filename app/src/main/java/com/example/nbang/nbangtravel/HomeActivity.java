package com.example.nbang.nbangtravel;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    static ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private static final int DELETE_ID = Menu.FIRST+1;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ListView listView = (ListView) findViewById(R.id.list);
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        registerForContextMenu(listView);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE,DELETE_ID, Menu.NONE, "삭제").setIcon(R.drawable.delete).setAlphabeticShortcut('d');
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                deleteList(info.id);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteList(final long rowId) {
        if (rowId >= 0) {
            new AlertDialog.Builder(HomeActivity.this).setTitle("삭제하시겠습니까?").setPositiveButton("네", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    listItems.remove((int)rowId);
                    adapter.notifyDataSetChanged();
                }
            }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //ignore
                }
            }).show();
        }
    }
}