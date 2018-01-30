package com.oo.valueanimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

public class ScrollerActivity extends AppCompatActivity {
    private final String TAG=getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroller);

        SeekBar seekBar=findViewById(R.id.seekbar);
        final PieView pieView=findViewById(R.id.pieview);
        seekBar.setMax(360);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i(TAG, "onProgressChanged: progress "+progress);
                pieView.setPieRotation(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
