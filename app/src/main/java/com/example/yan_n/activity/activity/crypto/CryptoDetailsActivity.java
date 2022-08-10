package com.example.yan_n.activity.activity.crypto;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yan_n.R;
import com.example.yan_n.api.berita.CryptoApi;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CryptoDetailsActivity extends AppCompatActivity {

    private CryptoApi cryptoApi;
    String mID = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_details);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Info Crypto");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.coingecko.com/api/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        cryptoApi = retrofit.create(CryptoApi.class);



        getIncomingIntent();
    }

    private void getIncomingIntent() {
        if(getIntent().hasExtra("id")) {
            String id = getIntent().getStringExtra("id");
            //Toast.makeText(this, id + "e", Toast.LENGTH_SHORT).show();
            mID = id;
            setInfo(id);
        }

    }

    private void setInfo(String id) {

        final TextView priceText = findViewById(R.id.priceContent);
        final TextView topText = findViewById(R.id.title);
        final TextView priceBTCText = findViewById(R.id.priceBtcContent);
        final TextView marketCapText = findViewById(R.id.marketCapContent);
        final TextView volumeText = findViewById(R.id.volumeContent);
        final TextView percent24 = findViewById(R.id.percent24Content);
        final TextView percent7d = findViewById(R.id.percent7dContent);
        final TextView priceBtcLabel = findViewById(R.id.priceBtcLabel);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("localisation", "false");
        parameters.put("market_data", "true");
        parameters.put("community_data", "false");
        parameters.put("developer_data", "false");


        Call<JsonObject> call = cryptoApi.getDetails(id, parameters);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(!response.isSuccessful()) {
                    return;
                }
                topText.setText(response.body().getAsJsonObject().get("name").toString().substring(1,response.body().getAsJsonObject().get("name").toString().length() - 1));
                priceBtcLabel.setText(response.body().getAsJsonObject().get("name").toString().substring(1,response.body().getAsJsonObject().get("name").toString().length() - 1));

                String name = response.body().getAsJsonObject().get("market_data").getAsJsonObject().get("current_price").getAsJsonObject().get("idr").toString();
                if (name.length() > 6) {
                    name = name.substring(0,6);
                }
                priceText.setText("Rp. " + name);



                String pricebtctext = (response.body().getAsJsonObject().get("market_data").getAsJsonObject().get("current_price").getAsJsonObject().get("btc").toString());
                if (pricebtctext.length() > 10) {
                    priceBTCText.setText(pricebtctext.substring(0,10));
                } else {
                    priceBTCText.setText(pricebtctext);
                }

                marketCapText.setText(response.body().getAsJsonObject().get("market_data").getAsJsonObject().get("market_cap").getAsJsonObject().get("idr").toString());

                volumeText.setText(response.body().getAsJsonObject().get("market_data").getAsJsonObject().get("total_volume").getAsJsonObject().get("idr").toString());

                percent24.setText(response.body().getAsJsonObject().get("market_data").getAsJsonObject().get("price_change_percentage_24h").toString());

                percent7d.setText(response.body().getAsJsonObject().get("market_data").getAsJsonObject().get("price_change_percentage_7d").toString());

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
