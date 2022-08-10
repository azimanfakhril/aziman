package com.example.yan_n.activity.activity.news;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.yan_n.R;
import com.example.yan_n.adapter.Adapter;
import com.example.yan_n.api.berita.ApiClient;
import com.example.yan_n.api.berita.ApiInterface;
import com.example.yan_n.models.berita.Article;
import com.example.yan_n.models.berita.News;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BusinessActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    // menetapkan data, tipe data, menetapkan komponen
    String country = "id";
    String category = "business";
    String language = "id";
    Call<News> call;

    public static final String API_KEY = "452752adf6044e299d803328c58b97c1";
    private RecyclerView recyclerView;
    private List<Article> articles = new ArrayList<>();
    private Adapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout errorLayout;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button btnRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mengambil layout untuk di tampilkan
        setContentView(R.layout.headline);

        //mengganti nama untuk navbar
        Objects.requireNonNull(getSupportActionBar()).setTitle("Berita Bisnis");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));

        //menampilkan tombol kembali/back pada navbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //menmanggil swipe Refres Layout dengan id
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        //menjalankan perintah swipe refres layout
        swipeRefreshLayout.setOnRefreshListener(this);

        //mengganti warna refres pada warna berputar
        swipeRefreshLayout.setColorSchemeResources(R.color.black);

        //menmanggil recyclerView dengan id
        recyclerView = findViewById(R.id.recyclerView);

        //memamngil layoutmanager untuk menyeting layout recyleview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BusinessActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        //animasi untuk recyleview
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //mengaktivkan/menonaktifkan scropll nested
        recyclerView.setNestedScrollingEnabled(false);

        //mengatur keyword untuk menampilkan data pertamakali
        onLoadingSwipeRefresh("");

        //menmanggil errorLayout dengan id
        errorLayout = findViewById(R.id.errorLayout);

        //menmanggil errorImage dengan id
        errorImage = findViewById(R.id.errorImage);

        //menmanggil errorTitle dengan id
        errorTitle = findViewById(R.id.errorTitle);

        //menmanggil errorMessage dengan id
        errorMessage = findViewById(R.id.errorMessage);

        //menmanggil btnRetry dengan id
        btnRetry = findViewById(R.id.btnRetry);

    }

    //menampilkan data dari api newsapi.org
    public void LoadJson(final String keyword) {

        //menyembunyikan error layout ketika memanggil data
        errorLayout.setVisibility(View.GONE);

        //refres pada saat load data
        swipeRefreshLayout.setRefreshing(true);

        //memanggil apiclien pada apiinface.class
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        //menampilkan data seuai dengan https://newsapi.org/v2/top-headlines?country=id&category=sports&apiKey=f3aa09e2f1d7494e9fded68f30d4e4c1 <-- contoh
        if (keyword.length() > 0) {
            call = apiInterface.getNewsSearch(keyword, language, category, API_KEY);
        } else {
            call = apiInterface.getNews(country, category, API_KEY);
        }

        //mengambil data dari api sesuai jenisdata yang di sediakan api
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(@NonNull Call<News> call, @NonNull Response<News> response) {
                if (response.isSuccessful() && Objects.requireNonNull(response.body()).getArticle() != null) {

                    if (!articles.isEmpty()) {
                        articles.clear();
                    }

                    articles = response.body().getArticle();
                    adapter = new Adapter(articles, BusinessActivity.this);
                    recyclerView.setAdapter(adapter);

                    initListener();

                    swipeRefreshLayout.setRefreshing(false);


                } else {

                    swipeRefreshLayout.setRefreshing(false);

                    String errorCode;

                    //menampilkan pesan error ketika gagal mengambil data dari api
                    switch (response.code()) {
                        case 404:
                            errorCode = "404 not found";
                            break;
                        case 500:
                            errorCode = "500 server broken";
                            break;
                        default:
                            errorCode = "unknown error";
                            break;
                    }

                    showErrorMessage(
                            R.drawable.no_result,
                            "No Result",
                            "Please Try Again!\n" +
                                    errorCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<News> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                showErrorMessage(
                        R.drawable.oops,
                        "Oops..",
                        "Tidak ada akses internet, Coba Kembali\n" +
                                t);
            }
        });
    }

    //memberikan data kepada layout lain
    private void initListener() {
        adapter.setOnItemClickListener((view, position) -> {
            Intent intent = new Intent(BusinessActivity.this, NewsDetailActivity.class);

            Article article = articles.get(position);
            intent.putExtra("url", article.getUrl());
            intent.putExtra("title", article.getTitle());
            intent.putExtra("img", article.getUrlToImage());
            intent.putExtra("date", article.getPublishedAt());
            intent.putExtra("source", article.getSource().getName());
            intent.putExtra("author", article.getAuthor());


            ActivityOptionsCompat optionsCompat;
            optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(BusinessActivity.this);

            startActivity(intent, optionsCompat.toBundle());
        });
    }

    //memberikan peritah back pada button back di navbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //settng search pada menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search_action).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        //memnberikan kalimat sebelum search
        searchView.setQueryHint("Cari Berita");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2) {
                    onLoadingSwipeRefresh(query);
                } else {
                    //menampilkan kalimat ketika huruf kurang
                    Toast.makeText(BusinessActivity.this, "Ketik lebih dari dua huruf!", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return true;
    }

    //menampilkan data ketika sselesai mengetikan dan menekan tombol search pada keyboard atau enter
    @Override
    public void onRefresh() {
        LoadJson("");
    }

    private void onLoadingSwipeRefresh(final String keyword) {

        swipeRefreshLayout.post(
                () -> LoadJson(keyword)
        );

    }

    private void showErrorMessage(int imageView, String title, String message) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
        }

        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);

        btnRetry.setOnClickListener(v -> onLoadingSwipeRefresh(""));
    }
}
