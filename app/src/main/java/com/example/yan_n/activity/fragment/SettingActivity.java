package com.example.yan_n.activity.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.yan_n.R;
import com.example.yan_n.activity.activity.setting.notification.NotificationBisnisReceiver;
import com.example.yan_n.activity.activity.setting.notification.NotificationDailyReceiver;
import com.example.yan_n.activity.activity.setting.notification.NotificationEntertaimenReceiver;
import com.example.yan_n.activity.activity.setting.notification.NotificationHeadlineReceiver;
import com.example.yan_n.activity.activity.setting.notification.NotificationKesehatanReceiver;
import com.example.yan_n.activity.activity.setting.notification.NotificationSportReceiver;
import com.example.yan_n.activity.activity.setting.notification.NotificationTeknologiReceiver;
import com.example.yan_n.activity.activity.setting.policy.policy;
import com.example.yan_n.unit.preference.SettingPreference;



public class SettingActivity extends Fragment implements View.OnClickListener {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchReminder;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchRelease;
    private NotificationDailyReceiver notificationDailyReceiver;
    private NotificationHeadlineReceiver cat1;
    private NotificationSportReceiver cat2;
    private NotificationTeknologiReceiver cat3;
    private NotificationBisnisReceiver cat4;
    private NotificationKesehatanReceiver cat5;
    private NotificationEntertaimenReceiver cat6;
    private SettingPreference settingPreference;

    public SettingActivity() {
    }
    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ActionBar mActionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setTitle("Setting");
        View v = inflater.inflate(R.layout.activity_setting, container, false);

        switchReminder = v.findViewById(R.id.swDailyReminder);
        switchRelease = v.findViewById(R.id.swReleaseToday);
        TextView textView = v.findViewById(R.id.policya);
        textView.setOnClickListener(this);


        notificationDailyReceiver = new NotificationDailyReceiver();
        cat1 = new NotificationHeadlineReceiver();
        cat2 = new NotificationSportReceiver();
        cat3 = new NotificationTeknologiReceiver();
        cat4 = new NotificationBisnisReceiver();
        cat5 = new NotificationKesehatanReceiver();
        cat6 = new NotificationEntertaimenReceiver();

        settingPreference = new SettingPreference(requireActivity());

        setSwitchRelease();
        setSwitchReminder();

        // Switch Reminder OnClick
        switchReminder.setOnClickListener(v1 -> {
            if (switchReminder.isChecked()) {
                notificationDailyReceiver.setDailyAlarm(requireActivity());
                settingPreference.setDailyReminder(true);
                Toast.makeText(requireActivity(), "Pengingat harian diaktifkan", Toast.LENGTH_SHORT).show();
            } else {
                notificationDailyReceiver.cancelAlarm(requireActivity());
                settingPreference.setDailyReminder(false);
                Toast.makeText(getActivity(), "Pengingat harian dinonaktifkan", Toast.LENGTH_SHORT).show();
            }
        });

        // Switch category OnClick
        switchRelease.setOnClickListener(v12 -> {
            if (switchRelease.isChecked()) {
                cat1.setReleaseAlarm(requireActivity());
                cat2.setReleaseAlarm(requireActivity());
                cat3.setReleaseAlarm(requireActivity());
                cat4.setReleaseAlarm(requireActivity());
                cat5.setReleaseAlarm(requireActivity());
                cat6.setReleaseAlarm(requireActivity());
                settingPreference.setReleaseReminder(true);
                Toast.makeText(requireActivity(), "Pengingat Kategori diaktifkan", Toast.LENGTH_SHORT).show();
            } else {
                cat1.cancelAlarm(requireActivity());
                cat2.cancelAlarm(requireActivity());
                cat3.cancelAlarm(requireActivity());
                cat4.cancelAlarm(requireActivity());
                cat5.cancelAlarm(requireActivity());
                cat6.cancelAlarm(requireActivity());
                settingPreference.setReleaseReminder(false);
                Toast.makeText(requireActivity(), "Pengingat Kategori dinonaktifkan", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    private void setSwitchReminder() {
        switchReminder.setChecked(settingPreference.getDailyReminder());
    }

    private void setSwitchRelease() {
        switchRelease.setChecked(settingPreference.getReleaseReminder());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), policy.class);
        startActivity(intent);
    }
}
