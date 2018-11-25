package com.example.nbang.nbangtravel;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
        FloatingActionButton plus = (FloatingActionButton) view.findViewById(R.id.kakaoShare);
        plus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    inserttoMap(view);
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

        new ShowCurrency.ExchangeRateTask().execute();
        new putmap().execute();
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
                Thread.sleep(3000);
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
                    for(String key : topay.keySet()){
                        double value = topay.get(key);
                        Log.d("map: ", key+" : "+value);
                    }
                    Log.d("check: ", "***********************");
                    constantsCursor.moveToNext();
                }
            }catch(Exception e){

            }
            double[] divide = new double[listItemsac.size()];
            for(String key : topay.keySet()){
                int j = 0;
                double value1 = topay.get(key);
                double value2 = topay.get(key);
                divide[j] = value1 - value2;
                j++;
            }
            int[][] service = new int[listItemsac.size()][listItemsac.size()];
            for(int i = 0; i<divide.length; i++){
                for(int j = 0; j<divide.length; j++){
                    if(divide[i]<=0){
                        break;
                    }
                    if(divide[j]<0){

                    }
                }
            }

            for(String key : paid.keySet()){
                double value = paid.get(key);
                Log.d("map: ", key+" : "+value);
            }
            for(String key : topay.keySet()){
                double value = topay.get(key);
                Log.d("map: ", key+" : "+value);
            }

            return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        constantsCursor.close();
        db.close();
    }
}
