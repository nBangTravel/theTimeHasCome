package com.example.nbang.nbangtravel;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

public class AccountCreateActivity extends AppCompatActivity {

    private SQLiteDatabase db = null;
    private Cursor constantsCursor = null;
    static int id = 0;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_create);

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
            participation.setId(id);
            participation.setTag(AccountActivity.listItemsac.get(j));
            ll.addView(participation);
            participation.setOnCheckedChangeListener(handleCheck(participation));

            EditText price = new EditText(this);
            price.setId(id);
            price.setHint("가격을 입력해주세요");
            ll.addView(price);

            id++;
            listC.addView(ll);
        }
    }
    //TODO 해야돼애애애 여기서부터 해애애애 editText받아오는거
    public void onClickac(View view){
        EditText title = (EditText) findViewById(R.id.editText2);
        for (int j = 0; j < AccountActivity.listItemsac.size(); j++) {
            ContentValues values = new ContentValues();
            values.put(AccountingContract.ConstantEntry.COLUMN_NAME_DATE, "2018.08.23");
            values.put(AccountingContract.ConstantEntry.COLUMN_NAME_TITLE, String.valueOf(title.getText()));
            values.put(AccountingContract.ConstantEntry.COLUMN_NAME_PARTICIPATOR, AccountActivity.listItemsac.get(j));
            values.put(AccountingContract.ConstantEntry.COLUMN_NAME_PRICE, "WON");
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
                    Toast.makeText(getApplicationContext(), "You unchecked " + chb.getTag(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "You checked " + chb.getTag(),
                            Toast.LENGTH_SHORT).show(); } } };
    }
}
