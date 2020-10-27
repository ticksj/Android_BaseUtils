package com.sj.android_base_utils;

import android.os.Bundle;

import com.sj.android_base_utils.utils_record.LogUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by SJ on 2020/8/17.
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.e("onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.e("onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e("onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.e("onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.e("onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e("onDestroy()");
    }
}
