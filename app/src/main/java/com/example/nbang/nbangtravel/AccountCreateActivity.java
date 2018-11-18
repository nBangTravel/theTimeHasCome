package com.example.nbang.nbangtravel;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.app.ActionBar.LayoutParams;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class AccountCreateActivity extends AppCompatActivity implements DatePickerFragment.OnCompleteListener{


    private DataBaseHelper dbHelper = null;
    private Cursor constantsCursor = null;
    EditText ed[] = new EditText[AccountActivity.listItemsac.size()];
    int check_boxcheck[] = new int[AccountActivity.listItemsac.size()];

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_create);
        init_tables();

        final LinearLayout listC = (LinearLayout) findViewById(R.id.listcontainer);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        for (int j = 0; j < AccountActivity.listItemsac.size(); j++) {
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);

            TextView participator = new TextView(this);
            participator.setText(AccountActivity.listItemsac.get(j));
            ll.addView(participator);

            CheckBox participation = new CheckBox(this);
            participation.setId(j);
            participation.setTag(AccountActivity.listItemsac.get(j));
            ll.addView(participation);
            participation.setOnCheckedChangeListener(handleCheck(participation));

            ed[j] = new EditText(this);
            ed[j].setId(j);
            ed[j].setHint("가격을 입력해주세요");
            ll.addView(ed[j]);

            listC.addView(ll);
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void init_tables(){
        dbHelper = new DataBaseHelper(this);
    }

    //TODO 해야돼애애애 여기서부터 해애애애 editText받아오는거
    public void onClickac(View view){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        EditText title = (EditText) findViewById(R.id.editText2);
        for (int j = 0; j < AccountActivity.listItemsac.size(); j++) {
            if(check_boxcheck[j] == 0){
                continue;
            }
            ContentValues values = new ContentValues();
            values.put(AccountingContract.ConstantEntry.COLUMN_NAME_DATE, ((TextView)findViewById(R.id.account_create_date)).getText().toString());
            values.put(AccountingContract.ConstantEntry.COLUMN_NAME_TITLE, String.valueOf(title.getText()));
            values.put(AccountingContract.ConstantEntry.COLUMN_NAME_PARTICIPATOR, AccountActivity.listItemsac.get(j));
            values.put(AccountingContract.ConstantEntry.COLUMN_NAME_PRICE, Integer.parseInt(String.valueOf(ed[j].getText())));
            values.put(AccountingContract.ConstantEntry.COLUMN_NAME_CURRENCY, "WON");
            db.insert(AccountingContract.ConstantEntry.TABLE_NAME, AccountingContract.ConstantEntry.COLUMN_NAME_TITLE, values);
        }
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        startActivity(intent);
        MainActivity.check_ac = 1;
    }


    private CompoundButton.OnCheckedChangeListener handleCheck (final CheckBox chb) {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(!isChecked){
                    check_boxcheck[chb.getId()] = 0;
                } else {
                    check_boxcheck[chb.getId()] = 1;
                }
            }
        };
    }

    @Override
    public void onComplete(String date) {
        TextView textView = (TextView) findViewById(R.id.account_create_date);
        textView.setText(date);
    }
}
