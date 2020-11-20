package com.pi.gymapp.utils;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private final CountDownTimer countDownTimer = new CountDownTimer();

    public CountDownTimer getCountDownTimer() {
        return countDownTimer;
    }
}
