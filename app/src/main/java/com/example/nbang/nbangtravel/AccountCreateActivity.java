package com.example.nbang.nbangtravel;

import android.app.ActionBar.LayoutParams;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class AccountCreateActivity extends AppCompatActivity implements DatePickerFragment.OnCompleteListener{

    private DataBaseHelper dbHelper = null;
    private Cursor constantsCursor = null;
    EditText ed[] = new EditText[AccountActivity.listItemsac.size()];
    int check_boxcheck[] = new int[AccountActivity.listItemsac.size()];
    private Spinner spinner;
    String[] currency = {"AED","AFN","ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN", "BAM",
            "BBD", "BDT", "BGN", "BHD", "BIF", "BMD", "BND", "BOB", "BRL", "BSD", "BTC", "BTN", "BWP",
            "BYN", "BZD", "CAD", "CDF", "CHF", "CLF", "CLP", "CNH", "CNY", "COP", "CRC", "CUC", "CUP",
            "CVE", "CZK", "DJF", "DKK", "DOP", "DZD", "EGP", "ERN", "ETB", "EUR", "FJD", "FKP", "GBP",
            "GEL", "GGP", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL", "HRK", "HTG", "HUF",
            "IDR", "ILS", "IMP", "INR", "IQD", "IRR", "ISK", "JEP", "JMD", "JOD", "JPY", "KES", "KGS",
            "KHR", "KMF", "KPW", "KRW", "KWD", "KYD", "KZT", "LAK", "LBP", "LKR", "LRD", "LSL", "LYD", "MAD",
            "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRO", "MRU", "MUR", "MVR", "MWK", "MXN", "MYR",
            "MZN", "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR",
            "PLN", "PYG", "QAR", "RON", "RSD", "RUB", "RWF", "SAR", "SBD", "SCR", "SDG", "SEK", "SGD",
            "SHP", "SLL", "SOS", "SRD", "SSP", "STD", "STN", "SVC", "SYP", "SZL", "THB", "TJS", "TMT",
            "TND", "TOP", "TRY", "TTD", "TWD", "TZS", "UAH", "UGX", "USD", "UYU", "UZS", "VEF", "VES",
            "VND", "VUV", "WST", "XAF", "XAG", "XAU", "XCD", "XDR", "XOF", "XPD", "XPF", "XPT", "YER",
            "ZAR", "ZMW", "ZWL"};


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("지출 일지");
        setContentView(R.layout.activity_account_create);
        init_tables();

        final LinearLayout listC = (LinearLayout) findViewById(R.id.listcontainer);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

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
            ed[j].setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
            ed[j].setHint("가격을 입력해주세요");
            ed[j].setSingleLine();
            ed[j].setImeOptions(EditorInfo.IME_ACTION_NEXT);
            ll.addView(ed[j]);
            listC.addView(ll);
        }

        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter spinnerAdapter;
        spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, currency);
        spinner.setAdapter(spinnerAdapter);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void init_tables(){
        dbHelper = new DataBaseHelper(this);
    }

    public void onClickac(View view){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        EditText title = (EditText) findViewById(R.id.editText2);
        for (int j = 0; j < AccountActivity.listItemsac.size(); j++) {
            if(check_boxcheck[j] == 0){
                continue;
            }
            ContentValues values = new ContentValues();
            Log.i("df", String.valueOf(ed[j].getText()));
            values.put(AccountingContract.ConstantEntry.COLUMN_NAME_DATE, ((TextView)findViewById(R.id.account_create_date)).getText().toString());
            values.put(AccountingContract.ConstantEntry.COLUMN_NAME_TITLE, String.valueOf(title.getText()));
            values.put(AccountingContract.ConstantEntry.COLUMN_NAME_PARTICIPATOR, AccountActivity.listItemsac.get(j));
            if(TextUtils.isEmpty(ed[j].getText())){
                values.put(AccountingContract.ConstantEntry.COLUMN_NAME_PRICE, 0);
            }else{
                values.put(AccountingContract.ConstantEntry.COLUMN_NAME_PRICE, Integer.parseInt(String.valueOf(ed[j].getText())));
            }
            values.put(AccountingContract.ConstantEntry.COLUMN_NAME_CURRENCY, spinner.getSelectedItem().toString());
            values.put(AccountingContract.ConstantEntry.COLUMN_NAME_TRAVEL, DataBaseHelper.now_travel);
            db.insert(AccountingContract.ConstantEntry.TABLE_NAME, AccountingContract.ConstantEntry.COLUMN_NAME_TITLE, values);
        }
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        startActivity(intent);
        MainActivity.check_ac = 1;
        db.close();
        finish();
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
