/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.prsolution.winstrike.ui.data;


import android.databinding.ObservableInt;

import java.util.TimerTask;

import ru.prsolution.winstrike.ui.util.DefaultTimer;
import ru.prsolution.winstrike.ui.util.ObservableViewModel;

public class TimerViewModel extends ObservableViewModel {
    final Integer INITIAL_SECONDS_PER_WORK_SET = 30; // Seconds

    DefaultTimer timer;
    Boolean timerRunning;

    ObservableInt workTimeLeft = new ObservableInt(INITIAL_SECONDS_PER_WORK_SET * 10); // tenths
    ObservableInt timePerWorkSet = new ObservableInt(INITIAL_SECONDS_PER_WORK_SET * 10); // tenths

    private TimerStates state = TimerStates.STOPPED;


    public ObservableInt getWorkTimeLeft() {
        return workTimeLeft;
    }

    public Boolean getTimerRunning() {
        return timerRunning;
    }

    public void setTimerRunning(Boolean timerRunning) {
        this.timerRunning = timerRunning;
    }


    /**
     * Resets timers and state. Called from the UI.
     */
    public void stopButtonClicked() {
//        resetTimers();
//        numberOfSetsElapsed = 0
        state = TimerStates.STOPPED;
        timer.reset();

//        notifyPropertyChanged(BR.timerRunning);
//        notifyPropertyChanged(BR.inWorkingStage);
//        notifyPropertyChanged(BR.numberOfSets)
    }


    /**
     * Start the timer!
     */
    public void startButtonClicked() {
        state = TimerStates.STARTED;

        TimerTask task = new TimerTask() {
            public void run() {
                if (state == TimerStates.STARTED) {
                    updateCountdowns();
                }
            }
        };

        // Schedule timer every 100ms to update the counters.
        timer.start(task);
    }

    private void updateCountdowns() {
        if (state == TimerStates.STOPPED) {
//            resetTimers();
            return;
        }

        Long elapsed;

        if (state == TimerStates.PAUSED) {
            elapsed = timer.getPausedTime();
        } else {
            elapsed = timer.getElapsedTime();
        }

        // work
        updateWorkCountdowns(elapsed);

    }


    private void updateWorkCountdowns(Long elapsed) {
//        stage = StartedStages.WORKING
        Integer newTimeLeft = (int) ((timePerWorkSet.get() - (elapsed / 100)));
        if (newTimeLeft <= 0) {
            workoutFinished();
        }
        workTimeLeft.set(coerceAt(newTimeLeft,0));

    }

    private int coerceAt(Integer newTimeLeft, int minimumValue) {
        if (newTimeLeft >= minimumValue) {
            return newTimeLeft;
        } else {
            return 0;
        }
    }

    private void workoutFinished() {
        timer.resetStartTime();
//        stage = StartedStages.RESTING
//        notifyPropertyChanged(BR.inWorkingStage)
    }

    public TimerViewModel() {
        this.timer = DefaultTimer.INSTANCE;
    }

    enum TimerStates {STOPPED, STARTED, PAUSED}

}
