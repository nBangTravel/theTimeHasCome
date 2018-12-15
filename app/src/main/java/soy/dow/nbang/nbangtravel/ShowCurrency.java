package soy.dow.nbang.nbangtravel;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShowCurrency extends Activity {
    public static JSONObject object;
    static String s = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);}
    public void OnClick(View view) {
        new ExchangeRateTask().execute();
    }

    static class ExchangeRateTask extends AsyncTask<Void, Void, Void> {
        private String CLIENT_ID_EXCHANGE_RATE = "b9a8c1f1075e4fe883cfb9d2cc376d80";

        @Override
        public void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
        }

        @Override
        public Void doInBackground(Void... voids){
            String urlString = "https://openexchangerates.org/api/latest.json?app_id=" + CLIENT_ID_EXCHANGE_RATE;
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(urlString)
                    .build();
            client.newCall(request).enqueue(new Callback(){

                @Override
                public void onFailure(Call call, IOException e) { }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    AccountActivity.s = response.body().string();
                    try{object = new JSONObject(AccountActivity.s);
                        object = object.getJSONObject("rates");
                    }catch(JSONException e){
                        e.printStackTrace(); } } });
            return null;
        } }
}