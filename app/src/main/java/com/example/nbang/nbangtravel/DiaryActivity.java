package com.example.nbang.nbangtravel;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class DiaryActivity extends Fragment{

    private static SQLiteDatabase db = null;
    private static Cursor constantsCursor = null;
    private TooLargeDataFragment dataFragment;


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        db = (new DataBaseHelper(getContext())).getWritableDatabase();
        constantsCursor = db.rawQuery("SELECT " + DiaryContract.ConstantEntry._ID + ", " +
                DiaryContract.ConstantEntry.COLUMN_NAME_DATE + ", " +
                DiaryContract.ConstantEntry.COLUMN_NAME_TITLE +
                " FROM " + DiaryContract.ConstantEntry.TABLE_NAME +
                " ORDER BY " + DiaryContract.ConstantEntry._ID + " DESC", null);

        final ListAdapter adapter = new SimpleCursorAdapter(getContext(), R.layout.listview_diary, constantsCursor,
                new String[] {DiaryContract.ConstantEntry.COLUMN_NAME_DATE, DiaryContract.ConstantEntry.COLUMN_NAME_TITLE},
                new int[] {R.id.diary_date, R.id.diary_title}, 0);

        View view = inflater.inflate(R.layout.activity_diary, container, false);
        final ListView listView = (ListView) view.findViewById(R.id.list_diarylist);
        listView.setAdapter(adapter);



        //TODO show diary
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                id = adapter.getItemId(position);

                Intent intent = new Intent(getActivity(), DiaryLookActivity.class);

                constantsCursor = db.rawQuery("SELECT " + "*" +
                        " FROM " + DiaryContract.ConstantEntry.TABLE_NAME +
                        " WHERE " + DiaryContract.ConstantEntry._ID + " = " + id, null);

                constantsCursor.moveToFirst();
                intent.putExtra("this_ID", id);
                intent.putExtra("this_title", constantsCursor.getString(2));
                intent.putExtra("this_date", constantsCursor.getString(1));
                //얘는 너무 커서 별도의 프래그먼트가 전송해준다,,,
                //intent.putExtra("this_picture", constantsCursor.getBlob(3));
                DiaryLookActivity.cc = constantsCursor.getBlob(3);
                intent.putExtra("this_content", constantsCursor.getString(4));

               //TODO ERROR
                startActivity(intent);

                /*FragmentManager fm = getFragmentManager();
                dataFragment = (TooLargeDataFragment) fm.findFragmentByTag("data");


                Bundle bundle = new Bundle(1);

                dataFragment.setArguments(bundle);*/
            }
        });

 /*       FragmentManager fm = getFragmentManager();
        dataFragment = (TooLargeDataFragment) fm.findFragmentByTag("data");
        if (dataFragment == null) {
            // add the fragment
            dataFragment = new TooLargeDataFragment();
            fm.beginTransaction().add(dataFragment, "data").commit();
            // load the data from the web
            dataFragment.setData(loadMyData());
        }
*/
        FloatingActionButton add = (FloatingActionButton) view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addDiary();
            }
        });

        return view;
    }

    public void addDiary() {
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
        Intent intent = new Intent(view.getContext(), DiaryCreateActivity.class);
        startActivity(intent);
    }

}
