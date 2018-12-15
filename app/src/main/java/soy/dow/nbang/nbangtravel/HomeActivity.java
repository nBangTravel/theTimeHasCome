package soy.dow.nbang.nbangtravel;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    static ArrayList<String> listItems=new ArrayList<String>();
    String input_members = new String();
    ArrayAdapter<String> adapter;
    private static final int DELETE_ID = Menu.FIRST+1;
    private DataBaseHelper dbHelper = null;
    private Cursor constantsCursor = null;
    private SQLiteDatabase db = null;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("n빵 여행");
        setContentView(soy.dow.nbang.nbangtravel.R.layout.activity_home);
        init_tables();

        ListView listView = (ListView) findViewById(soy.dow.nbang.nbangtravel.R.id.list);
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        registerForContextMenu(listView);
    }



    private void init_tables(){
        dbHelper = new DataBaseHelper(this);
    }

    public void addItems(View view) {
        EditText editText2 = (EditText)findViewById(soy.dow.nbang.nbangtravel.R.id.participator);
        listItems.add(editText2.getText().toString());
        adapter.notifyDataSetChanged();
        editText2.setText("");
    }

    public void createtravel(View view){
        EditText travel = (EditText)findViewById(soy.dow.nbang.nbangtravel.R.id.editText);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        constantsCursor = db.rawQuery("SELECT " + "*" +
                " FROM " + HomeContract.ConstantEntry.TABLE_NAME +
                " WHERE " + HomeContract.ConstantEntry.COLUMN_NAME_TRAVEL + " = " + "\"" + travel.getText() + "\"", null);
        constantsCursor.moveToFirst();
        if(TextUtils.isEmpty(travel.getText())){
            Toast.makeText(this, "여행명이 없다면, 여행지라도 입력해주세요.", Toast.LENGTH_SHORT).show();
        }else if(listItems.size() == 0){
            Toast.makeText(this, "참여자 이름은 꼭 필요해요!", Toast.LENGTH_SHORT).show();
        }else if(constantsCursor.getCount() > 0){
            Toast.makeText(this, "이미 존재하는 여행명입니다. 벌써 까먹으셨나요?", Toast.LENGTH_SHORT).show();
        }else{ for(int i = 0; i < listItems.size(); i++){
                if(i == listItems.size()-1){
                    input_members = input_members + listItems.get(i);
                }else {
                    input_members = input_members + listItems.get(i) + ","; } }
            ContentValues values = new ContentValues();
            values.put(HomeContract.ConstantEntry.COLUMN_NAME_TRAVEL, String.valueOf(travel.getText()));
            values.put(HomeContract.ConstantEntry.COLUMN_NAME_MEMBERS, input_members);
            db.insert(HomeContract.ConstantEntry.TABLE_NAME, HomeContract.ConstantEntry.COLUMN_NAME_TRAVEL, values);
            input_members = null;
            listItems.clear();
            HomeListActivity activ = (HomeListActivity)HomeListActivity.activ;
            activ.finish();
            Intent intent = new Intent(view.getContext(), HomeListActivity.class);
            startActivity(intent);
            finish(); } }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE,DELETE_ID, Menu.NONE, "삭제").setIcon(soy.dow.nbang.nbangtravel.R.drawable.delete).setAlphabeticShortcut('d');
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (constantsCursor != null){
            constantsCursor.close();
        }
        if (db != null){
            db.close();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}