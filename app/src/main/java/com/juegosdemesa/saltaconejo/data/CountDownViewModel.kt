package com.juegosdemesa.saltaconejo.data

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.juegosdemesa.saltaconejo.util.Utility
import com.juegosdemesa.saltaconejo.util.Utility.formatTime

class CountDownViewModel : ViewModel() {

    //region Properties
    private var countDownTimer: CountDownTimer? = null
    //endregion

    //region States
    private val _time = MutableLiveData(Utility.TIME_COUNTDOWN.formatTime())
    val time: LiveData<String> = _time

    private val _progress = MutableLiveData(1.00F)
    val progress: LiveData<Float> = _progress

    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    //hold data for timeUp view as boolean
    //private
    private val _isTimeUp = MutableLiveData(false)

    //accessed publicly
    val isTimeUp : LiveData<Boolean> get() =  _isTimeUp
    //endregion

    //region Private methods
    private fun timeIsUp() {
        countDownTimer?.cancel()
        handleTimerValues(false, 0L.formatTime(), 0.0F)
        _isTimeUp.postValue(true)
    }

    fun startTimer() {
        _isPlaying.value = true
        countDownTimer = object : CountDownTimer(Utility.TIME_COUNTDOWN, 100) {

            override fun onTick(millisRemaining: Long) {
                val progressValue = millisRemaining.toFloat() / Utility.TIME_COUNTDOWN
                handleTimerValues(true, millisRemaining.formatTime(), progressValue)
            }

            override fun onFinish() {
                timeIsUp()
            }
        }.start()
    }

    private fun handleTimerValues(isPlaying: Boolean, text: String, progress: Float) {
        _isPlaying.value = isPlaying
        _time.value = text
        _progress.value = progress
    }
    //endregion

    fun noMoreCards(){
        timeIsUp()
    }

}