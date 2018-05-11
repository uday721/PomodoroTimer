package com.example.shanker.pomodorotimer.utility;

import android.os.SystemClock;
import com.example.shanker.pomodorotimer.types.SessionType;
import java.util.concurrent.TimeUnit;

public class Timer {

    private long mWorkTime;
    private long mShortBreak;
    private long mLongBreak;
    private long mCountDownTime;
    private long mPlaceholderCountDownTime;
    private int mSessionBeforeLongBreak;
    private int mPausedCountDownTime;
    private SessionType mCurrentSessionType;



//this is count down time plus system time.
    public void getCountDownTime(SessionType sessionType) {

        long currentTime = SystemClock.elapsedRealtime();
        mCurrentSessionType=sessionType;

        switch (sessionType) {
            case WORK:
                mCountDownTime = currentTime +TimeUnit.SECONDS.toMillis(mWorkTime) ;
                break;
            case SHORT_BREAK:
                mCountDownTime = currentTime +TimeUnit.SECONDS.toMillis(mShortBreak) ;
                break;
            case LONG_BREAK:
                mCountDownTime = currentTime +TimeUnit.SECONDS.toMillis(mLongBreak) ;
                break;
        }
    }

    public int getTimeRemaining(){
        return (int) (TimeUnit.MILLISECONDS.toSeconds(
                mCountDownTime - SystemClock.elapsedRealtime()));
    }

    public void pauseCountDownTimer(){
        mPausedCountDownTime = getTimeRemaining();
        mPlaceholderCountDownTime = mPausedCountDownTime;
    }

    public void unPauseCountDownTimer(SessionType sessionType){
        long currentTime = SystemClock.elapsedRealtime();
        if(sessionType==SessionType.WORK)
            mCountDownTime=TimeUnit.SECONDS.toMillis(mPausedCountDownTime)+ currentTime ;
        if(sessionType==SessionType.SHORT_BREAK)
            mCountDownTime=TimeUnit.SECONDS.toMillis(mPausedCountDownTime)+ currentTime;
        if(sessionType==SessionType.LONG_BREAK)
            mCountDownTime=TimeUnit.SECONDS.toMillis(mPausedCountDownTime)+ currentTime;
    }

    //Getters and Setters
    public SessionType getSessionType() {
        return mCurrentSessionType;
    }

    public int getRecurringLongBreak(){
        return mSessionBeforeLongBreak;
    }

    public void setWorkTime(long mWorkTime) {
        this.mWorkTime = mWorkTime;
    }

    public void setShortBreak(long mShortBreak) {
        this.mShortBreak = mShortBreak;
    }

    public void setLongBreak(long mLongBreak) {
        this.mLongBreak = mLongBreak;
    }

    public void setRecurringLongBreak(int mRecurringLongBreak) {
        this.mSessionBeforeLongBreak = mRecurringLongBreak;
    }
}
