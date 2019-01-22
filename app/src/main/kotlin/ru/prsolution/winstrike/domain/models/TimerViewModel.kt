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

package ru.prsolution.winstrike.domain.models


import java.util.TimerTask

import ru.prsolution.winstrike.common.utils.DefaultTimer

class TimerViewModel {
	val INITIAL_SECONDS_PER_WORK_SET = 30 // Seconds

	lateinit var listener: TimeFinishListener

	var timer: DefaultTimer
	var timerRunning: Boolean? = null
		get() {
			return state == TimerStates.STARTED
		}


//	var workTimeLeft = ObservableInt(INITIAL_SECONDS_PER_WORK_SET * 10)
//		set // tenths
//	var timePerWorkSet = ObservableInt(INITIAL_SECONDS_PER_WORK_SET * 10) // tenths

	var state = TimerStates.STOPPED

	interface TimeFinishListener {
		fun onTimeFinish()
	}


	init {
		this.timer = DefaultTimer
	}


	/**
	 * Resets timers and state. Called from the UI.
	 */
	fun stopButtonClicked() {
		//        resetTimers();
		state = TimerStates.STOPPED
		timer.reset()

		//        notifyPropertyChanged(BR.timerRunning);
		//        notifyPropertyChanged(BR.inWorkingStage);
		//        notifyPropertyChanged(BR.numberOfSets)
	}


	/**
	 * Start the timer!
	 */
	fun startButtonClicked() {
		state = TimerStates.STARTED

		val task = object : TimerTask() {
			override fun run() {
				if (state == TimerStates.STARTED) {
					updateCountdowns()
				}
			}
		}

		// Schedule timer every 100ms to update the counters.
		timer.start(task)
	}

	private fun updateCountdowns() {
		if (state == TimerStates.STOPPED) {
			//            resetTimers();
			return
		}

		val elapsed: Long?

		if (state == TimerStates.PAUSED) {
			elapsed = timer.getPausedTime()
		} else {
			elapsed = timer.getElapsedTime()
		}

		// work
		updateWorkCountdowns(elapsed)

	}


	private fun updateWorkCountdowns(elapsed: Long?) {
		//        stage = StartedStages.WORKING
//		val newTimeLeft = (timePerWorkSet.get() - elapsed!! / 100).toInt()
//		if (newTimeLeft <= 0) {
		workoutFinished()
	}
//		workTimeLeft.set(coerceAt(newTimeLeft, 0))

}

private fun coerceAt(newTimeLeft: Int?, minimumValue: Int): Int {
	return if (newTimeLeft!! >= minimumValue) {
		newTimeLeft
	} else {
		0
	}
}

private fun workoutFinished() {
//		this.listener.onTimeFinish()
	//        state = TimerStates.STOPPED;
//		timer.resetStartTime()
	//        stage = StartedStages.RESTING
	//        notifyPropertyChanged(BR.inWorkingStage)
}

enum class TimerStates {
	STOPPED, STARTED, PAUSED
}

