package com.example.patryk.pam_stoper

import android.os.CountDownTimer
import java.lang.Exception
import java.util.*


class SimpleTimerPreseter(var view:ISimpleStoperView?, state:PresenterState?=null) {

    enum class State(val value:Int){NotRunning(0),Running(1),Stop(2);
        companion object {


    fun getfromValue(value:Int):State{State.values().forEach { if(it.value==value) return it };throw Exception()}}}
    var stop=Stoper()


    var stoper:CountDownTimer? = null
    //var isRunning:Boolean=false
    var state=State.NotRunning
    var left:Long=0
        get(){var wn = if(state==State.Running) field else view!!.displayMin*60_000.toLong()+ view!!.displaySec*1_000.toLong()
            return wn}
    init {
        if(state!=null) {
            this.state = State.getfromValue(state.isRunning)
            left=state.timeLeft
            display(state.timeLeft)
            if(this.state==State.Running)
                onSart()
        }
        else{
            this.state=State.NotRunning
            display(left)
        }
    }
    fun onSart()
    {
        //stop.onPeriod(100){print("rd")}
        if(view!=null) {
            if(state==State.Running&&stoper!=null)
                return
            state=State.Running
            stoper = object :
                CountDownTimer(view!!.startCouldDownMin * 60_000.toLong() + view!!.startCouldDownSec * 1_000, 1000) {


                override fun onTick(millisUntilFinished: Long) {
                    left=millisUntilFinished
                   display(millisUntilFinished)
                }

                override fun onFinish() {
                    view?.displaySec = 0
                    state=State.NotRunning
                    view?.timeEnd()
                }
            }.start()
        }
    }

    fun detache()
    {
        view=null
    }

    fun display(time:Long)
    {
        view?.displayMin = (time / 60_000).toInt()
        view?.displaySec = (time / 1_000).toInt() % 60.toInt()
    }
    fun getState():PresenterState
    {
        var state= PresenterState()
        state.isRunning=this.state.value
        state.timeLeft=left
        return state

    }
    fun onChangeTime(min:Int,sec:Int)
    {
        if(state==State.NotRunning)
        {
            view?.displayMin=min
            view?.displaySec= sec
        }
    }
    fun onRestart()
    {
        state=State.NotRunning
        stoper?.cancel()
        stoper=null
        left=0
        view?.displaySec=0
        view?.displayMin=0
    }

    fun onStop()
    {
        state=State.Stop
        stoper?.cancel()
    }
}

class PresenterState
{
    var timeLeft:Long=0
    var isRunning:Int=0
}