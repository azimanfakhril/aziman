package com.example.yan_n.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.yan_n.R;
import com.example.yan_n.activity.activity.news.BusinessActivity;
import com.example.yan_n.activity.activity.news.EntertaimentActivity;
import com.example.yan_n.activity.activity.news.HeadLineActivity;
import com.example.yan_n.activity.activity.news.HealthActivity;
import com.example.yan_n.activity.activity.news.SportsNewsActivity;
import com.example.yan_n.activity.activity.news.TechnologyActivity;
import com.google.android.material.card.MaterialCardView;

public class kategori extends Fragment implements View.OnClickListener {

    MaterialCardView cvHead, cvSports, cvTechno, cvBusiness, cvHealth, cvEntertaiment;

    public kategori() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setTitle("Kategori");

        View v = inflater.inflate(R.layout.kategori, container, false);
        cvHead = v.findViewById(R.id.cardHeadLine);
        cvSports = v.findViewById(R.id.cardSports);
        cvTechno = v.findViewById(R.id.cardTechno);
        cvBusiness = v.findViewById(R.id.cardBusiness);
        cvHealth = v.findViewById(R.id.cardHealth);
        cvEntertaiment = v.findViewById(R.id.cardEnter);


        cvHead.setOnClickListener(this);
        cvSports.setOnClickListener(this);
        cvTechno.setOnClickListener(this);
        cvBusiness.setOnClickListener(this);
        cvHealth.setOnClickListener(this);
        cvEntertaiment.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cardHeadLine) {
            Intent intent = new Intent(getActivity(), HeadLineActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.cardSports) {
            Intent intent = new Intent(getActivity(), SportsNewsActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.cardTechno) {
            Intent intent = new Intent(getActivity(), TechnologyActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.cardBusiness) {
            Intent intent = new Intent(getActivity(), BusinessActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.cardHealth) {
            Intent intent = new Intent(getActivity(), HealthActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.cardEnter) {
            Intent intent = new Intent(getActivity(), EntertaimentActivity.class);
            startActivity(intent);
        }
    }
}
