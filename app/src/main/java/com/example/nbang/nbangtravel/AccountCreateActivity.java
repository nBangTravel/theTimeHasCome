package com.example.nbang.nbangtravel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.ActionBar.LayoutParams;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AccountCreateActivity extends AppCompatActivity {
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

            int id = 0;
            CheckBox participation = new CheckBox(this);
            participation.setId(id);
            id++;
            participation.setTag(AccountActivity.listItemsac.get(j));
            ll.addView(participation);
            participation.setOnCheckedChangeListener(handleCheck(participation));


            EditText price = new EditText(this);
            price.setHint("가격을 입력해주세요");
            ll.addView(price);

            listC.addView(ll);
        }
    }

    public void onClickac(View view){
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
