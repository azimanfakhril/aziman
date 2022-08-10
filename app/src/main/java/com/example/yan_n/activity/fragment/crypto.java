package com.example.yan_n.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;

import com.example.yan_n.R;
import com.example.yan_n.activity.activity.crypto.CryptoDetailsActivity;
import com.example.yan_n.adapter.RecyclerViewCryptoAdapter;
import com.example.yan_n.api.berita.CryptoApi;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class crypto extends Fragment implements RecyclerViewCryptoAdapter.OnCoinListener, OnRefreshListener {

    // membuat api interface
    private CryptoApi cryptoApi;

    private SwipeRefreshLayout swipeRefreshLayout;

    // mengambil data dari
    private final ArrayList<String> mCoinPercentChange = new ArrayList<>();
    private final ArrayList<String> mCoins = new ArrayList<>();
    private final ArrayList<String> mCoinPrices = new ArrayList<>();
    private final ArrayList<String> mImageUrls = new ArrayList<>();
    private final ArrayList<String> mCoinRanks = new ArrayList<>();
    private final ArrayList<String> mIDs = new ArrayList<>();
    private final ArrayList<String> mSymbols = new ArrayList<>();


    private RecyclerView recyclerView;


    public crypto() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setTitle("Crypto List");
        View v = inflater.inflate(R.layout.crypto, container, false);

        //memncari id swipe refres
        swipeRefreshLayout = v.findViewById(R.id.swipe);

        //
        swipeRefreshLayout.setOnRefreshListener(this);


        Retrofit retrofit = new Builder()
                .baseUrl("http://api.coingecko.com/api/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // makes the interface we have
        cryptoApi = retrofit.create(CryptoApi.class);


        //getCoins();
        //getCoinData();
        getPosts(1);

        //initCoins();
        recyclerView = v.findViewById(R.id.recycle_view);
        return v;
    }

    private void initRecyclerView() {
        RecyclerViewCryptoAdapter adapter = new RecyclerViewCryptoAdapter(mCoins, getActivity(), mCoinPrices, mImageUrls, mCoinPercentChange, mIDs, mSymbols, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void getPosts(final int page) {


        Map<String, String> parameters = new HashMap<>();
        parameters.put("vs_currency", "idr");
        parameters.put("order", "market_cap_desc");
        parameters.put("per_page", "250");
        parameters.put("page", Integer.toString(page));
        //parameters.put("_order", "desc");

        Call<JsonArray> call = cryptoApi.getCoins(parameters);

        // runs it in a background thread
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                // checks if the http response is not one of those 404 ones
                if (!response.isSuccessful()) {
                    // textView.setText("Code:" + response.code());
                    return;
                }

                for (int i = 0; i < 250; i++) {
                    String change = (response.body().get(i).getAsJsonObject().get("price_change_percentage_24h").toString());

                    mCoinPercentChange.add(change);
                    /*if (change.indexOf('-') != -1) {
                        mCoinPercentChange.add(change + "%");
                    } else {
                        mCoinPercentChange.add(change.substring(0, 4) + "%");
                    }*/

                    // ranks


                    // Name of coins
                    String coinname = (response.body().get(i).getAsJsonObject().get("name").toString());
                    coinname = coinname.substring(1, coinname.length() - 1);
                    mCoins.add(coinname);


                    String price = (response.body().get(i).getAsJsonObject().get("current_price").toString());
                    try {
                        mCoinPrices.add("Rp. " + price.substring(0, 6));
                    } catch (Exception e) {
                        mCoinPrices.add("Rp. " + price);
                    }

                    // urls
                    String url = (response.body().get(i).getAsJsonObject().get("image").toString());
                    url = url.substring(1, url.length() - 1);
                    mImageUrls.add(url);

                    //IDs'
                    mIDs.add(response.body().get(i).getAsJsonObject().get("id").toString());

                    //Symbols
                    mSymbols.add(response.body().get(i).getAsJsonObject().get("symbol").toString());

                }


                if (page == 1) {
                    getPosts(2);
                    initRecyclerView();
                } else if (page == 2) {
                    getPosts(3);
                    initRecyclerView();
                } else if (page == 3) {
                    getPosts(4);
                    initRecyclerView();
                }

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

            }


            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                // textView.setText(t.getMessage());
            }
        });

    }


    @Override
    public void OnCoinClick(int position) {

        Intent intent = new Intent(getActivity(), CryptoDetailsActivity.class);
        String id = mIDs.get(position).substring(1, mIDs.get(position).length() - 1);
        intent.putExtra("id", id);
        //Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        //Toast.makeText(this, "Hello u have refreshed", Toast.LENGTH_SHORT).show();
        getPosts(1);
    }

}
