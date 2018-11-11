package com.example.nbang.nbangtravel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class ListViewAdapter extends ArrayAdapter implements View.OnClickListener{

    public interface ListViewButtonListener {
        void onListViewButtonClicked(int position);
    }

    int resourceID;
    private ListViewButtonListener listViewButtonListener;

    public ListViewAdapter(@NonNull Context context, int resource, ArrayList<ListViewItem> list, ListViewButtonListener listner) {
        super(context, resource, list);
        this.resourceID = resource;
        this.listViewButtonListener=listner;
    }

    @Override
    public void onClick(View view) {
        if(this.listViewButtonListener != null) {
            this.listViewButtonListener.onListViewButtonClicked((int)view.getTag());
        }

    }
}
