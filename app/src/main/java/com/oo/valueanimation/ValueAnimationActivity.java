package com.oo.valueanimation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ValueAnimationActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();


    private Button button;


    private ColumView columView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value_animation);
        button = findViewById(R.id.target);
        columView = findViewById(R.id.colum_view);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                columView.setValue(60);
            }
        });


    }
}
