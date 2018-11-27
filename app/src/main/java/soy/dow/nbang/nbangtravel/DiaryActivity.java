package soy.dow.nbang.nbangtravel;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        ((MainActivity) getActivity())
                .setActionBarTitle("다이어리");
        db = (new DataBaseHelper(getContext())).getWritableDatabase();
        constantsCursor = db.rawQuery("SELECT " + DiaryContract.ConstantEntry._ID + ", " +
                DiaryContract.ConstantEntry.COLUMN_NAME_DATE + ", " +
                DiaryContract.ConstantEntry.COLUMN_NAME_TITLE +
                " FROM " + DiaryContract.ConstantEntry.TABLE_NAME +
                " WHERE " + DiaryContract.ConstantEntry.COLUMN_NAME_TRAVEL + " = " + "\"" +
                DataBaseHelper.now_travel + "\"" +
                " ORDER BY " + DiaryContract.ConstantEntry._ID + " DESC", null);

        final ListAdapter adapter = new SimpleCursorAdapter(getContext(), soy.dow.nbang.nbangtravel.R.layout.listview_diary, constantsCursor,
                new String[] {DiaryContract.ConstantEntry.COLUMN_NAME_DATE, DiaryContract.ConstantEntry.COLUMN_NAME_TITLE},
                new int[] {soy.dow.nbang.nbangtravel.R.id.diary_date, soy.dow.nbang.nbangtravel.R.id.diary_title}, 0);

        View view = inflater.inflate(soy.dow.nbang.nbangtravel.R.layout.activity_diary, container, false);
        final ListView listView = (ListView) view.findViewById(soy.dow.nbang.nbangtravel.R.id.list_diarylist);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.i("------SELECTED ID",adapter.getItemId(position)+"" );
                int selected = (int) adapter.getItemId(position);
                Intent intent = new Intent(getActivity(), DiaryLookActivity.class);
                intent.putExtra("this_ID", selected);
                startActivity(intent);
            }
        });

        FloatingActionButton add = (FloatingActionButton) view.findViewById(soy.dow.nbang.nbangtravel.R.id.add);
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
}
