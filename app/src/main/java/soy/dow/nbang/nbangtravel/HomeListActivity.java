package soy.dow.nbang.nbangtravel;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class HomeListActivity extends AppCompatActivity {

    private SQLiteDatabase db = null;
    private static Cursor constantsCursor = null;
    private static final int DELETE_ID = Menu.FIRST+1;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    public static Activity activ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(soy.dow.nbang.nbangtravel.R.layout.activity_homelist);
        activ = this;


        db = (new DataBaseHelper(this)).getWritableDatabase();
        constantsCursor = db.rawQuery("SELECT " + HomeContract.ConstantEntry._ID + ", " +
                HomeContract.ConstantEntry.COLUMN_NAME_TRAVEL + ", " +
                HomeContract.ConstantEntry.COLUMN_NAME_MEMBERS +
                " FROM " + HomeContract.ConstantEntry.TABLE_NAME +
                " ORDER BY " + HomeContract.ConstantEntry._ID + " DESC", null);

        final ListAdapter adapter = new SimpleCursorAdapter(this, soy.dow.nbang.nbangtravel.R.layout.listview_home, constantsCursor,
                new String[] {HomeContract.ConstantEntry.COLUMN_NAME_TRAVEL},
                new int[] {soy.dow.nbang.nbangtravel.R.id.travel}, 0);
        ListView listView = (ListView) findViewById(soy.dow.nbang.nbangtravel.R.id.list_homelist);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                id = adapter.getItemId(position);
                constantsCursor = db.rawQuery("SELECT " + "*" +
                        " FROM " + HomeContract.ConstantEntry.TABLE_NAME +
                        " WHERE " + HomeContract.ConstantEntry._ID + " = " + id, null);
                constantsCursor.moveToFirst();
                String travel = constantsCursor.getString(1);
                String members = constantsCursor.getString(2);
                DataBaseHelper.now_travel = travel;
                AccountActivity.listItemsac.clear();
                int memlen = members.split(",").length;
                for(int i=0;i<memlen;i++){
                    AccountActivity.listItemsac.add(members.split(",")[i]);
                }
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        FloatingActionButton add = (FloatingActionButton) findViewById(soy.dow.nbang.nbangtravel.R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HomeActivity.class);
                startActivity(intent); }
        });
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
        if (rowId > 0) {
            new AlertDialog.Builder(this).setTitle("삭제하시겠습니까?").setPositiveButton("네", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    processDelete(rowId);
                }
            }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //ignore
                }
            }).show();
        }
    }

    public void onClick(View view){
        Intent intent = new Intent(view.getContext(), ShowCurrency.class);
        startActivity(intent);
        finish();
    }

    private void processDelete(long rowId) {
        String[] args = {String.valueOf(rowId)};
        Cursor constantsCursor_a = db.rawQuery("SELECT " + "*" +
                " FROM " + HomeContract.ConstantEntry.TABLE_NAME +
                " WHERE " + HomeContract.ConstantEntry._ID + " = " + rowId, null);
        constantsCursor_a.moveToFirst();
        String travel = constantsCursor_a.getString(1);

        db.delete(HomeContract.ConstantEntry.TABLE_NAME, "_ID=?", args);
        db.delete(AccountingContract.ConstantEntry.TABLE_NAME, "travelname=?", new String[]{travel});
        db.delete(DiaryContract.ConstantEntry.TABLE_NAME, "travelname=?", new String[]{travel});
        db.delete(CheckListContract.ConstantEntry.TABLE_NAME, "travelname=?", new String[]{travel});

        constantsCursor = db.rawQuery("SELECT " + HomeContract.ConstantEntry._ID + ", " +
                HomeContract.ConstantEntry.COLUMN_NAME_TRAVEL + ", " +
                HomeContract.ConstantEntry.COLUMN_NAME_MEMBERS +
                " FROM " + HomeContract.ConstantEntry.TABLE_NAME +
                " ORDER BY " + HomeContract.ConstantEntry._ID + " DESC", null);

        final ListAdapter adapter = new SimpleCursorAdapter(this, soy.dow.nbang.nbangtravel.R.layout.listview_home, constantsCursor,
                new String[] {HomeContract.ConstantEntry.COLUMN_NAME_TRAVEL},
                new int[] {soy.dow.nbang.nbangtravel.R.id.travel}, 0);
        final ListView listView = (ListView) findViewById(soy.dow.nbang.nbangtravel.R.id.list_homelist);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        constantsCursor.close();
        db.close();
    }
}