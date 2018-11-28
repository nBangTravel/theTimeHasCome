package soy.dow.nbang.nbangtravel;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AccountLookActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor constantsCursor = null;
    public static int ID;
    public static double prices = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("지출 일지");
        setContentView(R.layout.activity_account_look);
        db = (new DataBaseHelper(this)).getWritableDatabase();
        Intent intent = getIntent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TextView title = (TextView) findViewById(soy.dow.nbang.nbangtravel.R.id.title);
            title.setText(intent.getExtras().getString("this_title"));
            TextView date = (TextView) findViewById(soy.dow.nbang.nbangtravel.R.id.date);
            date.setText(intent.getExtras().getString("this_date"));
            TextView participator = (TextView) findViewById(soy.dow.nbang.nbangtravel.R.id.participator);
            participator.setText(intent.getExtras().getString("this_participator"));
            TextView price = (TextView) findViewById(soy.dow.nbang.nbangtravel.R.id.price);
            price.setText(String.valueOf(prices));
            TextView currency = (TextView) findViewById(soy.dow.nbang.nbangtravel.R.id.currency);
            currency.setText(intent.getExtras().getString("this_currency"));
        }
        db.close();
        prices = 0;
    }

    public void ask_delete(View view) {
        new AlertDialog.Builder(this).setTitle("삭제하시겠습니까?").setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                delete();
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //ignore
            }
        }).show();
    }

    public void delete() {
        db = (new DataBaseHelper(this)).getWritableDatabase();
        db.delete(AccountingContract.ConstantEntry.TABLE_NAME, "_ID = " + ID, null );
        Toast.makeText(this, "삭제되었습니다.",Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        MainActivity.check_ac=1;
        db.close();
        startActivity(intent);
    }

    public void edit(View view) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (constantsCursor != null){
            constantsCursor.close();
        }
        if (db != null){
            db.close();
        }
    }
}
