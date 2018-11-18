package com.example.nbang.nbangtravel;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class TooLargeDataFragment extends Fragment{
    private byte[] data;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

}
