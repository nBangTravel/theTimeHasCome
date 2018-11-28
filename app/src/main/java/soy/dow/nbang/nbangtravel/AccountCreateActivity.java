package soy.dow.nbang.nbangtravel;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
        setContentView(soy.dow.nbang.nbangtravel.R.layout.activity_account_create);
        init_tables();

        final LinearLayout listC = (LinearLayout) findViewById(soy.dow.nbang.nbangtravel.R.id.listcontainer);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        for (int j = 0; j < AccountActivity.listItemsac.size(); j++) {
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            TextView participator = new TextView(this);
            participator.setText(AccountActivity.listItemsac.get(j));
            participator.setTextSize(20);
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

        spinner = (Spinner)findViewById(soy.dow.nbang.nbangtravel.R.id.spinner);
        ArrayAdapter spinnerAdapter;
        spinnerAdapter = new ArrayAdapter(this, soy.dow.nbang.nbangtravel.R.layout.support_simple_spinner_dropdown_item, currency);
        spinner.setAdapter(spinnerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(soy.dow.nbang.nbangtravel.R.menu.account_create_actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case soy.dow.nbang.nbangtravel.R.id.account_create_bar_save:
                onClickac();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void init_tables(){
        dbHelper = new DataBaseHelper(this);
    }

    public void onClickac(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        EditText title = (EditText) findViewById(soy.dow.nbang.nbangtravel.R.id.editText2);
        constantsCursor = db.rawQuery("SELECT " + "*" +
                " FROM " + AccountingContract.ConstantEntry.TABLE_NAME +
                " WHERE " + AccountingContract.ConstantEntry.COLUMN_NAME_TITLE + " = " + "\"" + title.getText() + "\"", null);
        constantsCursor.moveToFirst();
        if(constantsCursor.getCount() > 0){
            Toast.makeText(this, "같은 이름의 가계부가 존재합니다.", Toast.LENGTH_SHORT).show();
        }else{
            for (int j = 0; j < AccountActivity.listItemsac.size(); j++) {
                if(check_boxcheck[j] == 0){
                    continue;
                }
                ContentValues values = new ContentValues();
                Log.i("df", String.valueOf(ed[j].getText()));
                values.put(AccountingContract.ConstantEntry.COLUMN_NAME_DATE, ((TextView)findViewById(soy.dow.nbang.nbangtravel.R.id.account_create_date)).getText().toString());
                values.put(AccountingContract.ConstantEntry.COLUMN_NAME_TITLE, String.valueOf(title.getText()));
                values.put(AccountingContract.ConstantEntry.COLUMN_NAME_PARTICIPATOR, AccountActivity.listItemsac.get(j));
                if(TextUtils.isEmpty(ed[j].getText())){
                    values.put(AccountingContract.ConstantEntry.COLUMN_NAME_PRICE, (double)0);
                }else{
                    values.put(AccountingContract.ConstantEntry.COLUMN_NAME_PRICE, Double.parseDouble(String.valueOf(ed[j].getText())));
                }
                values.put(AccountingContract.ConstantEntry.COLUMN_NAME_CURRENCY, spinner.getSelectedItem().toString());
                values.put(AccountingContract.ConstantEntry.COLUMN_NAME_TRAVEL, DataBaseHelper.now_travel);
                db.insert(AccountingContract.ConstantEntry.TABLE_NAME, AccountingContract.ConstantEntry.COLUMN_NAME_TITLE, values);
                values.clear();
            }
            MainActivity activ = (MainActivity)MainActivity.activ;
            activ.finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            MainActivity.check_ac = 1;
            db.close();
            finish();
        }
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
        TextView textView = (TextView) findViewById(soy.dow.nbang.nbangtravel.R.id.account_create_date);
        textView.setText(date);
    }
}
