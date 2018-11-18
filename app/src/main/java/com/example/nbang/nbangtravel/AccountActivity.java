package com.example.nbang.nbangtravel;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class AccountActivity extends Fragment{

    static ArrayList<String> listItemsac=new ArrayList<String>();
    private static SQLiteDatabase db = null;
    private static Cursor constantsCursor = null;
    private static final int DELETE_ID = Menu.FIRST+1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_account, container, false);
        db = (new DataBaseHelper(getContext())).getWritableDatabase();
        constantsCursor = db.rawQuery("SELECT " + AccountingContract.ConstantEntry._ID + ", " +
                AccountingContract.ConstantEntry.COLUMN_NAME_DATE + ", " +
                AccountingContract.ConstantEntry.COLUMN_NAME_TITLE + ", " +
                AccountingContract.ConstantEntry.COLUMN_NAME_PARTICIPATOR + ", " +
                AccountingContract.ConstantEntry.COLUMN_NAME_PRICE + ", " +
                AccountingContract.ConstantEntry.COLUMN_NAME_CURRENCY +
                " FROM " + AccountingContract.ConstantEntry.TABLE_NAME +
                " ORDER BY " + AccountingContract.ConstantEntry._ID + " DESC", null);

        final ListAdapter adapter = new SimpleCursorAdapter(getContext(), R.layout.listview_account, constantsCursor,
                new String[] {AccountingContract.ConstantEntry.COLUMN_NAME_TITLE, AccountingContract.ConstantEntry.COLUMN_NAME_PARTICIPATOR,
                AccountingContract.ConstantEntry.COLUMN_NAME_PRICE, AccountingContract.ConstantEntry.COLUMN_NAME_CURRENCY},
                new int[] {R.id.account_title, R.id.account_participator, R.id.account_price, R.id.account_currency}, 0);
        final ListView listView = (ListView) view.findViewById(R.id.list_accountlist);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        FloatingActionButton add = (FloatingActionButton) view.findViewById(R.id.add2);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addAccount(); }
        });
        return view;
    }

    public void addAccount() {
        new AlertDialog.Builder(getContext()).setTitle("새 가계부를 작성하시겠습니까?").setPositiveButton("네", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                createAccount(getView());
                Log.i("check", "WORKING1");
            }
        }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //ignore
            }
        }).show();
    }

    public void createAccount(View view) {
        Intent intent = new Intent(view.getContext(), AccountCreateActivity.class);
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
        if (rowId > 0) {
            new AlertDialog.Builder(getContext()).setTitle("삭제하시겠습니까?").setPositiveButton("네", new DialogInterface.OnClickListener() {
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

    private void processDelete(long rowId) {
        String[] args = {String.valueOf(rowId)};
        db.delete(AccountingContract.ConstantEntry.TABLE_NAME, "_ID=?", args);
        constantsCursor = db.rawQuery("SELECT " + AccountingContract.ConstantEntry._ID + ", " +
                AccountingContract.ConstantEntry.COLUMN_NAME_DATE + ", " +
                AccountingContract.ConstantEntry.COLUMN_NAME_TITLE + ", " +
                AccountingContract.ConstantEntry.COLUMN_NAME_PARTICIPATOR + ", " +
                AccountingContract.ConstantEntry.COLUMN_NAME_PRICE + ", " +
                AccountingContract.ConstantEntry.COLUMN_NAME_CURRENCY +
                " FROM " + AccountingContract.ConstantEntry.TABLE_NAME +
                " ORDER BY " + AccountingContract.ConstantEntry._ID + " DESC", null);

        View view = getView();
        ListAdapter adapter = new SimpleCursorAdapter(getContext(), R.layout.listview_checklist, constantsCursor,
                new String[] {CheckListContract.ConstantEntry.COLUMN_NAME_TITLE},
                new int[] {R.id.item_list}, 0);

        final ListAdapter adapter2 = new SimpleCursorAdapter(getContext(), R.layout.listview_account, constantsCursor,
                new String[] {AccountingContract.ConstantEntry.COLUMN_NAME_TITLE, AccountingContract.ConstantEntry.COLUMN_NAME_PARTICIPATOR,
                        AccountingContract.ConstantEntry.COLUMN_NAME_PRICE, AccountingContract.ConstantEntry.COLUMN_NAME_CURRENCY},
                new int[] {R.id.account_title, R.id.account_participator, R.id.account_price, R.id.account_currency}, 0);
        final ListView listView = (ListView) view.findViewById(R.id.list_accountlist);
        listView.setAdapter(adapter2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        constantsCursor.close();
        db.close();
    }
}
