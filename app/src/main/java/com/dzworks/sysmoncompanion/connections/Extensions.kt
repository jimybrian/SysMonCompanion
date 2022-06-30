package com.dzworks.sysmoncompanion.connections

import androidx.databinding.BindingAdapter
import com.github.lzyzsd.circleprogress.CircleProgress
import com.mackhartley.roundedprogressbar.RoundedProgressBar

class Extensions {
}

object BindingAdapters{


    @JvmStatic
    @BindingAdapter("gaugeValue")
    fun circleGaugeValue(circleProgress: CircleProgress, value: String?) {
        var progressVal = 0
        if(!value.isNullOrEmpty()){
            progressVal = value.toInt()
        }
        circleProgress.progress = progressVal
    }

    @JvmStatic
    @BindingAdapter("barValue")
    fun barGaugeValue(barProgress: RoundedProgressBar, value: String?){
        var progressVal = 0.0
        if(!value.isNullOrEmpty()){
            progressVal = value.toDouble()
        }
        barProgress.setProgressPercentage(progressVal, true)
    }

}