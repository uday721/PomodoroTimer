package com.example.shanker.pomodorotimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HistoryActivity extends AppCompatActivity {

    private int _id;
    private String _activityName;
    private String _timeElapsed;

    //empty constructor
    public HistoryActivity(){}

    public HistoryActivity(String activityName, String timeElapsed) {
        this._activityName = activityName;
        this._timeElapsed = timeElapsed;
    }

    public void set_id(int id) {
        this._id = id;
    }

    public void set_activityName(String activityName) {
        this._activityName = activityName;
    }

    public void set_timeElapsed(String timeElapsed) {
        this._timeElapsed = timeElapsed;
    }

    public int get_id() {
        return _id;
    }

    public String get_activityName() {
        return _activityName;
    }

    public String get_timeElapsed() {
        return _timeElapsed;
    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }*/
}
