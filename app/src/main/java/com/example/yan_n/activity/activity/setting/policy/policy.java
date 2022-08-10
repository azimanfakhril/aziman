package com.example.yan_n.activity.activity.setting.policy;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yan_n.R;

import java.util.Objects;

public class policy extends AppCompatActivity {
    TextView line1,line2;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mengambil layout untuk di tampilkan
        setContentView(R.layout.policy);

        //mengganti nama untuk navbar
        Objects.requireNonNull(getSupportActionBar()).setTitle("Policy");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));

        //menampilkan tombol kembali/back pada navbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linkgps();
    }

    private void linkgps() {
        line1 = findViewById(R.id.googlePlayService);
        line1.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
