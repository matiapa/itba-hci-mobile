package com.pi.gymapp.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CountDownTimer {
    private long remainingTime = 0;
    private long interval = 0;
    private boolean started, paused;
    private android.os.CountDownTimer countDownTimer;
    private final MutableLiveData<Status> countDownTimerStatus = new MutableLiveData<>();

    public void start(long time, long interval) {
        this.interval = interval;
        start(time);
        started = true;
    }

    public LiveData<Status> getStatus() {
        return countDownTimerStatus;
    }

    public void pause() {
        cancel();
        paused = true;
    }

    public void resume() {
        start(remainingTime);
        paused = false;
    }

    public void stop() {
        interval = 0;
        cancel();
        started = false;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isPaused() {
        return paused;
    }

    private void start(long time) {
        remainingTime=time;
        countDownTimerStatus.postValue(new Status(time, false));
        countDownTimer = new android.os.CountDownTimer(time, interval) {

            @Override
            public void onTick(long millisUntilFinished) {
                CountDownTimer.this.onTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                CountDownTimer.this.onFinish();
            }
        }.start();
    }

    private void cancel() {
        countDownTimer.cancel();
    }

    private void onTick(long millisUntilFinished) {
        remainingTime = millisUntilFinished;
        countDownTimerStatus.setValue(new Status(remainingTime / 1000, false));
    }

    private void onFinish() {
        countDownTimerStatus.setValue(new Status(0, true));
    }

    public static class Status {

        private final boolean isFinished;
        private final long remainingTime;

        public Status(long remainingTime, boolean isFinished) {
            this.remainingTime = remainingTime;
            this.isFinished = isFinished;
        }

        public long getRemainingTime() {
            return remainingTime;
        }


        public boolean isFinished() {
            return isFinished;
        }
    }
}
