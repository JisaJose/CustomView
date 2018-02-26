package com.example.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.customview.view.CustomView;

public class DashBoardActivity extends AppCompatActivity {
    private CustomView mcustomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        mcustomView = findViewById(R.id.customView);
        findViewById(R.id.customViewButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcustomView.swapColor();
            }
        });
    }
}
