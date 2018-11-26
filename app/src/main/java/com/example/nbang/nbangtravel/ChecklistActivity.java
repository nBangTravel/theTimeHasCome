package com.example.nbang.nbangtravel;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

public class ChecklistActivity extends Fragment {

    private SQLiteDatabase db = null;
    private Cursor constantsCursor = null;
    private static final int DELETE_ID = Menu.FIRST+1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) getActivity())
                .setActionBarTitle("체크리스트");

        db = (new DataBaseHelper(getContext())).getWritableDatabase();
        constantsCursor = db.rawQuery("SELECT " + CheckListContract.ConstantEntry._ID + ", " + CheckListContract.ConstantEntry.COLUMN_NAME_TITLE +
                " FROM " + CheckListContract.ConstantEntry.TABLE_NAME +
                " WHERE " + CheckListContract.ConstantEntry.COLUMN_NAME_TRAVEL + " = " + "\"" +
                DataBaseHelper.now_travel + "\"" +
                " ORDER BY " + CheckListContract.ConstantEntry._ID + " DESC", null);

        final ListAdapter adapter = new SimpleCursorAdapter(getContext(), R.layout.listview_checklist, constantsCursor,
                new String[] {CheckListContract.ConstantEntry.COLUMN_NAME_TITLE},
                new int[] {R.id.item_list}, 0);

        View view = inflater.inflate(R.layout.activity_checklist, container, false);
        final ListView listView = (ListView) view.findViewById(R.id.list_checklist);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        FloatingActionButton add = (FloatingActionButton) view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addList();
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (constantsCursor != null){
            constantsCursor.close();
        }
        if (db != null){
            db.close();
        }
    }

    public void addList() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View addView = inflater.inflate(R.layout.checklist_add, null);
        final ChecklistAddActivity wrapper = new ChecklistAddActivity(addView);
        new AlertDialog.Builder(getContext()).setTitle("새로운 할 일").setView(addView).setPositiveButton("추가", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                processAdd(wrapper);
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //ignore
            }
        }).show();
    }

    private void processAdd(ChecklistAddActivity wrapper) {
        ContentValues values = new ContentValues(1);
        values.put(CheckListContract.ConstantEntry.COLUMN_NAME_TITLE, wrapper.getTitle());
        values.put(CheckListContract.ConstantEntry.COLUMN_NAME_TRAVEL, DataBaseHelper.now_travel);
        db.insert(CheckListContract.ConstantEntry.TABLE_NAME, CheckListContract.ConstantEntry.COLUMN_NAME_TITLE, values);
        constantsCursor = db.rawQuery("SELECT " + CheckListContract.ConstantEntry._ID + ", " + CheckListContract.ConstantEntry.COLUMN_NAME_TITLE +
                " FROM " + CheckListContract.ConstantEntry.TABLE_NAME +
                " WHERE " + CheckListContract.ConstantEntry.COLUMN_NAME_TRAVEL + " = " + "\"" +
                DataBaseHelper.now_travel + "\"" +
                " ORDER BY " + CheckListContract.ConstantEntry._ID + " DESC", null);

        ListAdapter adapter = new SimpleCursorAdapter(getContext(), R.layout.listview_checklist, constantsCursor,
                new String[] {CheckListContract.ConstantEntry.COLUMN_NAME_TITLE},
                new int[] {R.id.item_list}, 0);

        View view = getView();
        ListView listView = (ListView) view.findViewById(R.id.list_checklist);
        listView.setAdapter(adapter);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE,DELETE_ID, Menu.NONE, "삭제").setIcon(R.drawable.delete).setAlphabeticShortcut('d');

    }

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
        db.delete(CheckListContract.ConstantEntry.TABLE_NAME, "_ID=?", args);
        constantsCursor = db.rawQuery("SELECT " + CheckListContract.ConstantEntry._ID + ", " + CheckListContract.ConstantEntry.COLUMN_NAME_TITLE +
                " FROM " + CheckListContract.ConstantEntry.TABLE_NAME +
                " WHERE " + CheckListContract.ConstantEntry.COLUMN_NAME_TRAVEL + " = " + "\"" +
                DataBaseHelper.now_travel + "\"" +
                " ORDER BY " + CheckListContract.ConstantEntry._ID + " DESC", null);

        ListAdapter adapter = new SimpleCursorAdapter(getContext(), R.layout.listview_checklist, constantsCursor,
                new String[] {CheckListContract.ConstantEntry.COLUMN_NAME_TITLE},
                new int[] {R.id.item_list}, 0);

        View view = getView();
        ListView listView = (ListView) view.findViewById(R.id.list_checklist);
        listView.setAdapter(adapter);
    }

}
