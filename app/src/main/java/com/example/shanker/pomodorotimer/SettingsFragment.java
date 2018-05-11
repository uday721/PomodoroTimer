package com.example.shanker.pomodorotimer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsFragment extends Fragment {
    private SeekBar workTimeSeekBar;
    private SeekBar breakTimeSeekBar;
    private SeekBar longBreakSeekBar;
    private SeekBar recurringCountSeekBar;
    private TextView workTimeText;
    private TextView breakTimeText;
    private TextView longBreakTimeText;
    private TextView sessionCountText, historyDisplay;
    private EditText editActivityText;
    private int workTime;
    private int breakTime;
    private int longBreakTime;
    private int recurringCount;
    private static final String MINUTES_TEXT = " Minutes";
    private static final String SESSION_TEXT = " Sessions";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_settings,container,false);

        //Setting all the necessary references
        workTimeSeekBar = view.findViewById(R.id.work_seek_bar);
        breakTimeSeekBar = view.findViewById(R.id.break_seek_bar);
        longBreakSeekBar = view.findViewById(R.id.long_break_seek_bar);
        recurringCountSeekBar = view.findViewById(R.id.recurring_bar);

        workTimeText = view.findViewById(R.id.work_text_view);
        breakTimeText = view.findViewById(R.id.break_text_view);
        longBreakTimeText = view.findViewById(R.id.long_break_text_view);
        sessionCountText =view.findViewById(R.id.recurring_text_view);
        historyDisplay = view.findViewById(R.id.history_display_text);

        editActivityText = view.findViewById(R.id.activity_input);

        seekBar();
        return view;
    }

    //accessing the values from seek bar
    private void seekBar() {
        //Default time for work, break and long break

        //work time default values
        workTimeText.setText(workTimeSeekBar.getProgress()+MINUTES_TEXT);
        workTime =workTimeSeekBar.getProgress();

        //break time default values
        breakTimeText.setText((breakTimeSeekBar.getProgress()+MINUTES_TEXT));
        breakTime =breakTimeSeekBar.getProgress();

        //long break default values
        longBreakTimeText.setText(longBreakSeekBar.getProgress()+MINUTES_TEXT);
        longBreakTime =longBreakSeekBar.getProgress();

        //recurring session count default values
        sessionCountText.setText(recurringCountSeekBar.getProgress()+SESSION_TEXT);
        recurringCount =recurringCountSeekBar.getProgress();

        workTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressValue=i;
                workTimeText.setText(progressValue+MINUTES_TEXT);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                workTimeText.setText(progressValue+MINUTES_TEXT);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                workTimeText.setText(progressValue+MINUTES_TEXT);
                workTime =progressValue;
            }
        });

        breakTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressValue=i;
                breakTimeText.setText(progressValue+MINUTES_TEXT);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                breakTimeText.setText(progressValue+MINUTES_TEXT);
                breakTime =progressValue;
            }
        });

        longBreakSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressValue=i;
                longBreakTimeText.setText(progressValue+MINUTES_TEXT);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                longBreakTimeText.setText(progressValue+MINUTES_TEXT);
                longBreakTime =progressValue;
            }
        });

        recurringCountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressValue=i;
                sessionCountText.setText(progressValue+SESSION_TEXT);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sessionCountText.setText(progressValue+SESSION_TEXT);
                recurringCount =progressValue;
            }
        });
    }

    //Getters and Setters
    public int getWorkTime() {
        return workTime;
    }

    public int getBreakTime() {
        return breakTime;
    }

    public int getLongBreakTime() {
        return longBreakTime;
    }

    public int getRecurringCount() {
        return recurringCount;
    }
    public String getEditActivityText() {
        return editActivityText.getText().toString();
    }

    public void setHistoryDisplay( String name) {
        this.historyDisplay.setText(name);
    }
}
