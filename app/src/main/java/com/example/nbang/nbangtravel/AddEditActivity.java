package com.example.nbang.nbangtravel;

import android.view.View;
import android.widget.EditText;

public class AddEditActivity {

    EditText titleField = null;

    View base = null;

    AddEditActivity(View base) {
        this.base = base;
    }

    String getTitle() { return getTitleField().getText().toString();}

    private EditText getTitleField() {
        if(titleField == null) {
            titleField = (EditText)base.findViewById(R.id.title);
        }
        return titleField;
    }


}
