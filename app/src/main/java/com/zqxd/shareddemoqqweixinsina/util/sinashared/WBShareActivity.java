package com.zqxd.shareddemoqqweixinsina.util.sinashared;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class WBShareActivity extends AppCompatActivity {
    private static final String TAG="WBShareActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Toast.makeText(this,"新浪分享成功",Toast.LENGTH_SHORT).show();
        finish();
        super.onCreate(savedInstanceState);

    }
}
