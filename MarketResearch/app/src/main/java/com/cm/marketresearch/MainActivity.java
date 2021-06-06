package com.cm.marketresearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cm.marketresearch.base.BaseActivity;
import com.cm.marketresearch.databinding.ActivityMainBinding;
import com.cm.marketresearch.ui.SearchActivity;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected int layoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onViewCreated() {

        //서칭화면으로 전환
        binding.btnSearchArea.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SearchActivity.class)));
    }
}