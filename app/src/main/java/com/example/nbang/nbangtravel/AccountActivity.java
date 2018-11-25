package com.example.nbang.nbangtravel;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends Fragment{

    static ArrayList<String> listItemsac=new ArrayList<String>();
    private static SQLiteDatabase db = null;
    private static Cursor constantsCursor = null;
    private static final int DELETE_ID = Menu.FIRST+1;
    public static String s;
    boolean permessionCheck = false;
    private static final int REQUEST_EXTERNAL_STORAGE_CODE = 1;
    private String shout = DataBaseHelper.now_travel + " 가계부 결과입니다. \n";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_account, container, false);
        db = (new DataBaseHelper(getContext())).getWritableDatabase();
        constantsCursor = db.rawQuery("SELECT " + AccountingContract.ConstantEntry._ID + ", " +
                AccountingContract.ConstantEntry.COLUMN_NAME_DATE + ", " +
                AccountingContract.ConstantEntry.COLUMN_NAME_TITLE + ", " +
                AccountingContract.ConstantEntry.COLUMN_NAME_PARTICIPATOR + ", " +
                AccountingContract.ConstantEntry.COLUMN_NAME_PRICE + ", " +
                AccountingContract.ConstantEntry.COLUMN_NAME_CURRENCY +
                " FROM " + AccountingContract.ConstantEntry.TABLE_NAME +
                " WHERE " + AccountingContract.ConstantEntry.COLUMN_NAME_TRAVEL + " = " + "\"" +
                DataBaseHelper.now_travel + "\"" +
                " ORDER BY " + AccountingContract.ConstantEntry._ID + " DESC", null);


        final ListAdapter adapter = new SimpleCursorAdapter(getContext(), R.layout.listview_account, constantsCursor,
                new String[] {AccountingContract.ConstantEntry.COLUMN_NAME_TITLE, AccountingContract.ConstantEntry.COLUMN_NAME_PARTICIPATOR,
                AccountingContract.ConstantEntry.COLUMN_NAME_PRICE, AccountingContract.ConstantEntry.COLUMN_NAME_CURRENCY},
                new int[] {R.id.account_title, R.id.account_participator, R.id.account_price, R.id.account_currency}, 0);
        final ListView listView = (ListView) view.findViewById(R.id.list_accountlist);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                id = adapter.getItemId(position);
                Intent intent = new Intent(getActivity(), AccountLookActivity.class);
                constantsCursor = db.rawQuery("SELECT " + "*" +
                        " FROM " + AccountingContract.ConstantEntry.TABLE_NAME +
                        " WHERE " + AccountingContract.ConstantEntry.COLUMN_NAME_TRAVEL + " = " + "\"" +
                        DataBaseHelper.now_travel + "\"" +
                        " and " + AccountingContract.ConstantEntry._ID + " = " + id, null);
                constantsCursor.moveToFirst();
                intent.putExtra("this_ID", id);
                AccountLookActivity.ID = (int)id;
                intent.putExtra("this_date", constantsCursor.getString(1));
                intent.putExtra("this_title", constantsCursor.getString(2));
                intent.putExtra("this_participator", constantsCursor.getString(3));
                intent.putExtra("this_price", constantsCursor.getInt(4));
                intent.putExtra("this_currency", constantsCursor.getString(5));
                startActivity(intent);
            }
        });
        final FloatingActionButton plus = (FloatingActionButton) view.findViewById(R.id.kakaoShare);
        plus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    inserttoMap(view);
                    plus.setEnabled(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        FloatingActionButton add = (FloatingActionButton) view.findViewById(R.id.add2);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addAccount(); }
        });
        return view;
    }

    public void addAccount() {
        new AlertDialog.Builder(getContext()).setTitle("새 가계부를 작성하시겠습니까?").setPositiveButton("네", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                createAccount(getView());
                Log.i("check", "WORKING1");
            }
        }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //ignore
            }
        }).show();
    }

    public void createAccount(View view) {
        Intent intent = new Intent(view.getContext(), AccountCreateActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE,DELETE_ID, Menu.NONE, "삭제").setIcon(R.drawable.delete).setAlphabeticShortcut('d');
        }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                deleteList(info.id);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteList(final long rowId) {
        if (rowId > 0) {
            new AlertDialog.Builder(getContext()).setTitle("삭제하시겠습니까?").setPositiveButton("네", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    processDelete(rowId);
                }
            }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //ignore
                }
            }).show();
        }
    }

    private void processDelete(long rowId) {
        String[] args = {String.valueOf(rowId)};
        db.delete(AccountingContract.ConstantEntry.TABLE_NAME, "_ID=?", args);
        constantsCursor = db.rawQuery("SELECT " + AccountingContract.ConstantEntry._ID + ", " +
                AccountingContract.ConstantEntry.COLUMN_NAME_DATE + ", " +
                AccountingContract.ConstantEntry.COLUMN_NAME_TITLE + ", " +
                AccountingContract.ConstantEntry.COLUMN_NAME_PARTICIPATOR + ", " +
                AccountingContract.ConstantEntry.COLUMN_NAME_PRICE + ", " +
                AccountingContract.ConstantEntry.COLUMN_NAME_CURRENCY +
                " FROM " + AccountingContract.ConstantEntry.TABLE_NAME +
                " WHERE " + AccountingContract.ConstantEntry.COLUMN_NAME_TRAVEL + " = " + "\"" +
                DataBaseHelper.now_travel + "\"" +
                " ORDER BY " + AccountingContract.ConstantEntry._ID + " DESC", null);

        View view = getView();

        final ListAdapter adapter = new SimpleCursorAdapter(getContext(), R.layout.listview_account, constantsCursor,
                new String[] {AccountingContract.ConstantEntry.COLUMN_NAME_TITLE, AccountingContract.ConstantEntry.COLUMN_NAME_PARTICIPATOR,
                        AccountingContract.ConstantEntry.COLUMN_NAME_PRICE, AccountingContract.ConstantEntry.COLUMN_NAME_CURRENCY},
                new int[] {R.id.account_title, R.id.account_participator, R.id.account_price, R.id.account_currency}, 0);
        final ListView listView = (ListView) view.findViewById(R.id.list_accountlist);
        listView.setAdapter(adapter);
    }

    public void inserttoMap(View view) throws JSONException, InterruptedException {
        Toast.makeText(getContext(), "잠시만 기다려주세요. 계산중입니다.", Toast.LENGTH_LONG);
        new ShowCurrency.ExchangeRateTask().execute();
        new putmap().execute();
        Thread.sleep(3000);
        shareKakaoTalk();
        Log.d("result", shout);
        shout = DataBaseHelper.now_travel + " 가계부 결과입니다. \n";
    }

    private class putmap extends AsyncTask<Void, Void, Void> {

        @Override
        public void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
        }

        @Override
        public Void doInBackground(Void... voids){
            Map<String, Double> paid = new HashMap<String, Double>();
            Map<String, Double> topay = new HashMap<String, Double>();
            ArrayList<String> participators = new ArrayList<String>();
            String event = "";
            double eventpay = 0;

            Intent intent = new Intent(getActivity(), AccountLookActivity.class);
            constantsCursor = db.rawQuery("SELECT " + "*" +
                    " FROM " + AccountingContract.ConstantEntry.TABLE_NAME +
                    " WHERE " + AccountingContract.ConstantEntry.COLUMN_NAME_TRAVEL + " = " + "\"" +
                    DataBaseHelper.now_travel + "\"", null);
            int count = constantsCursor.getCount();
            constantsCursor.moveToFirst();

            try{
                Thread.sleep(2000);
                JSONObject object = new JSONObject(s);
                object = object.getJSONObject("rates");
                for(int i = 0; i < count; i++){

                    String title = constantsCursor.getString(2);
                    String participator = constantsCursor.getString(3);
                    int price = constantsCursor.getInt(4);
                    String currency = constantsCursor.getString(5);

                    //Fist paid
                    if(paid.containsKey(participator)){
                        double temp = paid.get(participator);
                        double tem1 = price/object.getDouble(currency);
                        double tem2 = tem1*object.getDouble("KRW");
                        temp += tem2;
                        paid.remove(participator);
                        paid.put(participator, temp);

                    }else{
                        double tem1 = price/object.getDouble(currency);
                        double tem2 = tem1*object.getDouble("KRW");
                        paid.put(participator, tem2);
                    }


                    //Second topay
                    if(event.equals(title)){
                        eventpay += ((double)price)/object.getDouble(currency)*object.getDouble("KRW");
                        participators.add(participator);

                    }else{
                        for(int j = 0; j<participators.size(); j++){
                            if(topay.containsKey(participators.get(j))){
                                double temp = topay.get(participators.get(j));
                                temp += eventpay/participators.size();
                                topay.remove(participators.get(j));
                                topay.put(participators.get(j), temp);
                            }else{
                                topay.put(participators.get(j), eventpay/participators.size());
                            }
                        }
                        eventpay = 0;
                        participators.clear();
                        event = title;
                        eventpay = ((double)price)/object.getDouble(currency)*object.getDouble("KRW");
                        participators.add(participator);
                    }
                    if(i == count-1){
                        for(int j = 0; j<participators.size(); j++){
                            if(topay.containsKey(participators.get(j))){
                                double temp = topay.get(participators.get(j));
                                temp += eventpay/participators.size();
                                topay.remove(participators.get(j));
                                topay.put(participators.get(j), temp);
                            }else{
                                topay.put(participators.get(j), eventpay/participators.size());
                            }
                        }
                    }
                    constantsCursor.moveToNext();
                }
            }catch(Exception e){

            }
            double[] divide = new double[listItemsac.size()];
            int chc = 0;
            for(String key : topay.keySet()){
                double value1 = topay.get(key);
                double value2 = paid.get(key);
                divide[chc] = value1 - value2;
                chc += 1;
            }

            int[][] service = new int[listItemsac.size()][listItemsac.size()];
            for(int i = 0; i<divide.length; i++){
                if(divide[i]<=0){
                    continue;
                }else{
                    for(int j = 0; j<divide.length; j++){
                        if(divide[j]>=0){
                            continue;
                        }else{
                            if(divide[i]+divide[j]>0){
                                service[i][j] += (-divide[j]);
                                divide[i] += divide[j];
                                divide[j] = 0;
                            }else{
                                service[i][j] += (divide[i]);
                                divide[j] += divide[i];
                                divide[i] = 0;
                            }
                        }
                    }
                }
            }

            for(int i = 0; i<listItemsac.size(); i++){
                for(int j = 0; j<listItemsac.size();j++){
                    if(service[i][j] != 0){
                        shout += paid.keySet().toArray()[i] + "님이 ";
                        shout += paid.keySet().toArray()[j] + "님에게 " + service[i][j] + "원을, ";
                    }
                }
            }
            shout += "주시면 됩니다.";
            return null;
        }
    }

    public void shareKakaoTalk(){
        onRequestPermission();
        if (permessionCheck) {
            Intent kakao = new Intent(Intent.ACTION_SEND);
            kakao.setType("text/plain");
            try {
                kakao.putExtra(Intent.EXTRA_TEXT, shout);
                kakao.setPackage("com.kakao.talk");
                startActivity(kakao);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getContext(), "카카오톡이 설치되지 않았습니다.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void onRequestPermission() {
        Toast.makeText(getContext(), "권한을 확인하는 중..", Toast.LENGTH_SHORT).show();
        int permissionReadStorage = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWriteStorage = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionReadStorage == PackageManager.PERMISSION_DENIED || permissionWriteStorage == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE_CODE);
        } else {
            permessionCheck = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE_CODE:
                for (int i = 0; i<permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if(permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(getContext(), "공유되었습니다.", Toast.LENGTH_SHORT).show();
                            permessionCheck= true;
                        } else {
                            Toast.makeText(getContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                            permessionCheck= false;
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        constantsCursor.close();
        db.close();
    }
}
