package com.example.stopwatchapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView timerText;
    private Button startButton, stopButton, holdButton;

    private Handler handler = new Handler();
    private long startTime = 0L;
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updateTime = 0L;

    private boolean isRunning = false;
    private boolean isPaused = false;

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = System.currentTimeMillis() - startTime;
            updateTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updateTime / 1000);
            int mins = secs / 60;
            int hrs = mins / 60;
            secs = secs % 60;
            mins = mins % 60;

            timerText.setText(String.format("%02d:%02d:%02d", hrs, mins, secs));
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerText = findViewById(R.id.timer_text);
        startButton = findViewById(R.id.start_button);
        stopButton = findViewById(R.id.stop_button);
        holdButton = findViewById(R.id.hold_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRunning) {
                    startTime = System.currentTimeMillis();
                    handler.postDelayed(updateTimerThread, 0);
                    isRunning = true;
                    isPaused = false;
                    holdButton.setText("Hold");
                }
            }
        });

        holdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRunning) {
                    if (!isPaused) {
                        timeSwapBuff += timeInMilliseconds;
                        handler.removeCallbacks(updateTimerThread);
                        isPaused = true;
                        holdButton.setText("Play");
                    } else {
                        startTime = System.currentTimeMillis();
                        handler.postDelayed(updateTimerThread, 0);
                        isPaused = false;
                        holdButton.setText("Pause");
                    }
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(updateTimerThread);
                isRunning = false;
                isPaused = false;
                startTime = 0L;
                timeInMilliseconds = 0L;
                timeSwapBuff = 0L;
                updateTime = 0L;
                timerText.setText("00:00:00");
                holdButton.setText("Hold");
            }
        });
    }
}
