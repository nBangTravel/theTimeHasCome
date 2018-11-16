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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class DiaryActivity extends Fragment{

    private SQLiteDatabase db = null;
    private Cursor constantsCursor = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        db = (new DataBaseHelper(getContext())).getWritableDatabase();
        constantsCursor = db.rawQuery("SELECT " + DiaryContract.ConstantEntry._ID + ", " +
                DiaryContract.ConstantEntry.COLUMN_NAME_DATE + ", " +
                DiaryContract.ConstantEntry.COLUMN_NAME_TITLE +
                " FROM " + DiaryContract.ConstantEntry.TABLE_NAME +
                " ORDER BY " + DiaryContract.ConstantEntry._ID, null);

        final ListAdapter adapter = new SimpleCursorAdapter(getContext(), R.layout.listview_diary, constantsCursor,
                new String[] {DiaryContract.ConstantEntry.COLUMN_NAME_DATE, DiaryContract.ConstantEntry.COLUMN_NAME_TITLE},
                new int[] {R.id.diary_date, R.id.diary_title}, 0);

        View view = inflater.inflate(R.layout.activity_diary, container, false);
        final ListView listView = (ListView) view.findViewById(R.id.list_diarylist);
        listView.setAdapter(adapter);

        FloatingActionButton add = (FloatingActionButton) view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addDiary();
            }
        });
        return view;
    }

    public void addDiary() {//다이어리 진짜 만들꺼니?
        new AlertDialog.Builder(getContext()).setTitle("새 다이어리를 작성하시겠습니까?").setPositiveButton("네", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                createDiary(getView());

                Log.i("check", "WORKING1");
            }
        }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //ignore
            }
        }).show();
    }

    public void createDiary(View view) {
        //TODO: 작성화면 xml 뿌리기, 입력받은 데이터를 테이블에 추가하기.
        Intent intent = new Intent(view.getContext(), DiaryCreateActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //TODO: 선택된 로우 아이디 겟
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        showDiary(info.id);

        return super.onContextItemSelected(item);
    }

    public void showDiary(long id) {
        //TODO: 겟 한 로우 아이디로 데베에서 내용을 가져와 xml에 뿌려준다. (작성된 다이어리 화면으로 이동)
        //DiaryLookActivity 에 구현해야하나...?
    }

}
