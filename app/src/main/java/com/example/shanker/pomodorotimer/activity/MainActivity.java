package com.example.shanker.pomodorotimer.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.shanker.pomodorotimer.HistoryActivity;
import com.example.shanker.pomodorotimer.R;
import com.example.shanker.pomodorotimer.SettingsFragment;
import com.example.shanker.pomodorotimer.utility.Timer;
import com.example.shanker.pomodorotimer.types.TimerState;
import com.example.shanker.pomodorotimer.types.SessionType;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_TAG = 2;
    private static final int MINUTES = 60;
    private static final int SECONDS = 60;
    private static final int ONE_SECOND = 1000;
    private static final int FRAG_DELAY = 500;
    private static final String START_TEXT = "Start";
    private static final String STOP_TEXT = "Stop";
    private static final String CANCEL_TEXT = "Cancel";
    private static final String BREAK_COMPLETE_MSG = "Break Complete";
    private static final String WORK_COMPLETE_MSG = "Session Complete";
    private static final String START_BREAK_TEXT = "Start Break";
    private static final String SKIP_BREAK_TEXT = "Skip Break";
    private static final String START_SESSION_TEXT = "Start Session";
    private static final String WORK_TIME_TEXT = "Work Time!";
    private static final String BREAK_TIME_TEXT = "Short Break!";
    private static final String LONG_BREAK_TIME_TEXT = "long Break!";
    private static final String EMPTY_STRING = "";
    private static final String DEFAULT_ACTIVITY_NAME = "Default Activity";

    private TextView mCountDownText, mActivityName, mTotalElapsedTime, mHistoryDisplay;
    private Button mCountDownButton;
    private Timer mTimer;
    private AlertDialog mAlertDialog;
    private int mNumOfWorkSessions=0;
    private int totalSeconds=0;
    private boolean mStartStop = true;
    private MediaPlayer mTimePauseSound;
    private MediaPlayer mSessionCompleteSound;
    private Handler mHandler;
    private TimerState mTimerState;
    private SessionType mSessionType;
    private EditText mActivityInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting all required references
        mCountDownText = findViewById(R.id.countdown_text_view);
        mCountDownButton = findViewById(R.id.countdown_button);
        mActivityName= findViewById(R.id.activity_name);
        mActivityInput = findViewById(R.id.activity_input);
        mTotalElapsedTime = findViewById(R.id.total_elapsed_time);
        mTimerState = TimerState.INACTIVE;
        mTimePauseSound = MediaPlayer.create(this,R.raw.beep);
        mSessionCompleteSound = MediaPlayer.create(this,R.raw.oringz);
        mHandler = new Handler();

        mCountDownText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startPauseCountDown();
                mTimePauseSound.start();
            }
        });

        mCountDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStopTimer();
            }
        });

        setSupportActionBar( (Toolbar) findViewById(R.id.toolbar) );
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if(item.getItemId()== R.id.action_settings){
            Toast.makeText(MainActivity.this,"you have clicked on settings menu",Toast.LENGTH_SHORT).show();
        } else if (item.getItemId()== R.id.action_history){
            Intent historyIntent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(historyIntent);
            Toast.makeText(MainActivity.this,"you have clicked on history menu",Toast.LENGTH_SHORT).show();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    //this method is called when the button is clicked
    private void startStopTimer() {
        if (mStartStop) { startTimer(); }
        else { stopTimer(); }
    }

    //resets the timer app
    private void stopTimer() {
        mSessionType = SessionType.WORK;
        mStartStop = true;
        final SettingsFragment settingFrag = (SettingsFragment)getSupportFragmentManager().findFragmentById(R.id.settings_fragment);
        getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .show(settingFrag)
            .commit();
        pauseCountdown();
        mCountDownText.setText(EMPTY_STRING);
        mTotalElapsedTime.setText(EMPTY_STRING);
        mActivityName.setText(EMPTY_STRING);
        mCountDownButton.setText(START_TEXT);
        settingFrag.setHistoryDisplay(START_TEXT);
    }

    //starts the timer with assigned work, break, long break settings
    private void startTimer() {
        mSessionType=SessionType.WORK;
        mTimerState=TimerState.RUNNING;
        final SettingsFragment settingFrag = (SettingsFragment)getSupportFragmentManager().findFragmentById(R.id.settings_fragment);
        mTimer = new Timer();
        mTimer.setWorkTime(TimeUnit.MINUTES.toSeconds(settingFrag.getWorkTime()));
        mTimer.setShortBreak(TimeUnit.MINUTES.toSeconds(settingFrag.getBreakTime()));
        mTimer.setLongBreak(TimeUnit.MINUTES.toSeconds(settingFrag.getLongBreakTime()));
        mTimer.setRecurringLongBreak(settingFrag.getRecurringCount());

        totalElapsedTime();
        mTimer.getCountDownTime(mSessionType);
        getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .hide(settingFrag)
            .commit();
        if(!settingFrag.isHidden()){
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startCountdown();
                    mActivityName.setText(WORK_TIME_TEXT);
                }
            }, 500);
        } else {
            startCountdown();
        }
        mCountDownButton.setText(STOP_TEXT);
        mStartStop = false;

       // if(!TextUtils.isEmpty(mActivityInput.getText().toString())){
         //  mActivityName.setText(mActivityInput.getText().toString());
        //}else mActivityName.setText(DEFAULT_ACTIVITY_NAME);
    }

    //using this to calculate the total time a certain activity. It will be used to create history
    //once History icon is clicked it should display all the previous activities along with time spent on that particular activity
    //this feature is not yet implemented.
    private void totalElapsedTime() {
        final Runnable elapsedTimeRunnable = new Runnable() {
            @Override
            public void run() {
                if(!mStartStop) {
                    mHandler.postDelayed(this, ONE_SECOND);

                    int seconds, minutes, hours, totalMinutes;
                    seconds = totalSeconds % SECONDS;
                    totalMinutes = totalSeconds / MINUTES;
                    hours = totalMinutes / MINUTES;
                    minutes = totalMinutes % MINUTES;
                    String elapsedTime = (hours > 0 ? hours : EMPTY_STRING) +
                            (hours > 0 ? ":" : EMPTY_STRING) + (minutes > 0 ? minutes : EMPTY_STRING) +
                            (minutes > 0 ? ":" : EMPTY_STRING) +
                            format(Locale.US, "%02d", seconds);
                    mTotalElapsedTime.setText(elapsedTime);
                    totalSeconds++;
                }else{
                    totalSeconds=0;
                }
            }
        };
        mHandler.postDelayed(elapsedTimeRunnable, FRAG_DELAY);
    }

    //this method is called when clicked on countdown text
    public void startPauseCountDown(){
        if(mTimerState == TimerState.RUNNING){
            pauseCountdown();
        }
        else{
            startCountdown();
        }
    }

    //simple loop to count down the time
    public void startCountdown(){
        if(mTimerState == TimerState.PAUSE){
            mTimer.unPauseCountDownTimer(mSessionType);
        }
        final Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                if(mTimer.getTimeRemaining()>=0&&mTimerState==TimerState.RUNNING) {
                    mHandler.postDelayed(this,ONE_SECOND);
                    updateTimer();
                }
                else if(mTimerState!=TimerState.PAUSE) {
                    onCountDownFinished();
                }
            }
        };
        mHandler.postDelayed(mRunnable, 0);
        mTimerState = TimerState.RUNNING;
        if(mSessionType == SessionType.WORK){
            mNumOfWorkSessions++;
        }
    }

    //this method is called when countdown becomes zero
    private void onCountDownFinished() {
        mSessionCompleteSound.start();
        switch (mTimer.getSessionType()){
            case WORK:
                    mAlertDialog = breakSessionDialog();
                    mAlertDialog.setCanceledOnTouchOutside(false);
                    mAlertDialog.show();
                break;
            case SHORT_BREAK:
            case LONG_BREAK:
                if(mNumOfWorkSessions>mTimer.getRecurringLongBreak()){
                    resetWorkSessionsCount();
                }
                mAlertDialog = startSessionDialog();
                mAlertDialog.setCanceledOnTouchOutside(false);
                mAlertDialog.show();
        }
    }

    //starts long break or short break based on the current session type
    public void startBreak(){
        if(mNumOfWorkSessions == mTimer.getRecurringLongBreak()){
            mSessionType = SessionType.LONG_BREAK;
            resetWorkSessionsCount();
            mActivityName.setText(LONG_BREAK_TIME_TEXT);
        }
        else{
            mSessionType = SessionType.SHORT_BREAK;
            mActivityName.setText(BREAK_TIME_TEXT);
        }
        startCountdown();
    }

    //once the work time session equals to the recurring session count this method is called to reset.
    private void resetWorkSessionsCount() {
        mNumOfWorkSessions=0;
    }

    //this alert box is created when break time is completed
    private AlertDialog startSessionDialog() {
        return new AlertDialog.Builder(this)
                .setTitle(BREAK_COMPLETE_MSG)
                .setPositiveButton(START_SESSION_TEXT, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeCompletionNotification();
                        mSessionType=SessionType.WORK;
                        mTimer.getCountDownTime(mSessionType);
                        mActivityName.setText(WORK_TIME_TEXT);
                        startCountdown();
                    }
                })
                .setNegativeButton(CANCEL_TEXT, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeCompletionNotification();
                        stopTimer();
                    }
                })
                .create();
    }

    //this alert dialog is called when work session is completed
    private AlertDialog breakSessionDialog() {
        return new AlertDialog.Builder(this)
                .setTitle(WORK_COMPLETE_MSG)
                .setPositiveButton(
                        START_BREAK_TEXT,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeCompletionNotification();
                                startBreak();
                                mTimer.getCountDownTime(mSessionType);
                            }
                        }
                )
                .setNegativeButton(
                        SKIP_BREAK_TEXT,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which
                            ) {
                                removeCompletionNotification();
                                mSessionType = SessionType.WORK;
                                mActivityName.setText(WORK_TIME_TEXT);
                                startCountdown();
                                mTimer.getCountDownTime(mSessionType);
                            }
                        }
                )
                .setNeutralButton(CANCEL_TEXT, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeCompletionNotification();
                        stopTimer();
                    }
                })
                .create();
    }

    //this method is called to remove the alert dialog
    private void removeCompletionNotification() {
        NotificationManager notificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_TAG);
    }

    //method is called to pause the timer
    public void pauseCountdown(){
        mTimer.pauseCountDownTimer();
        mTimerState = TimerState.PAUSE;
    }

    //the count down text is updated in this method
    public void updateTimer(){
        int timeRemaining = mTimer.getTimeRemaining();
        int minutes = timeRemaining / SECONDS;
        int seconds = timeRemaining % SECONDS;

        String currentTick = (minutes > 0 ? minutes : EMPTY_STRING) +
                (minutes > 0 ? ":" : EMPTY_STRING)  +
                format(Locale.US, "%02d", seconds);

        mCountDownText.setText(currentTick);
    }
}


