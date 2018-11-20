package com.example.patryk.pam_stoper

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.lang.Exception
import android.os.VibrationEffect
import android.os.Build
import android.content.Context.VIBRATOR_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.os.Vibrator



class MainActivity : AppCompatActivity(),ISimpleStoperView {

    var presenter:SimpleTimerPreseter?=null
    override var displayMin: Int
        get(){
            return try{
                findViewById<TextView>(R.id.textViewMinuteTensNumber).text.toString().toInt()*10+findViewById<TextView>(R.id.textViewMinuteUnitNumber).text.toString().toInt()
            }catch (e:Exception)
            {
                0
            }
        }
        set(value) {
            findViewById<TextView>(R.id.textViewMinuteTensNumber).text=(value/10).toInt().toString()
            findViewById<TextView>(R.id.textViewMinuteUnitNumber).text= (value%10).toString()
        }
    override var displaySec: Int
        get(){
            return try{
                findViewById<TextView>(R.id.textViewSecTensNumber).text.toString().toInt()*10+findViewById<TextView>(R.id.textViewSecUnitNumber).text.toString().toInt()
            }catch (e:Exception)
            {
                0
            }
        }
        set(value) {
            findViewById<TextView>(R.id.textViewSecTensNumber).text=(value/10).toInt().toString()
            findViewById<TextView>(R.id.textViewSecUnitNumber).text= (value%10).toString()
        }
    override var startCouldDownMin: Int
        get() = findViewById<TextView>(R.id.textViewMinuteTensNumber).text.toString().toInt()*10+findViewById<TextView>(R.id.textViewMinuteUnitNumber).text.toString().toInt()
        set(value) {}
    override var startCouldDownSec: Int
        get() = findViewById<TextView>(R.id.textViewSecTensNumber).text.toString().toInt()*10+findViewById<TextView>(R.id.textViewSecUnitNumber).text.toString().toInt()

        set(value) {}

    override fun timeEnd() {
        val v = getSystemService(VIBRATOR_SERVICE) as Vibrator
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(5000, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            //deprecated in API 26
            v.vibrate(5000)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter=SimpleTimerPreseter(this)
        findViewById<Button>(R.id.buttonMinuteTesNumberPlus).setOnClickListener {
            var value= findViewById<TextView>(R.id.textViewMinuteTensNumber).text.toString().toInt()
            value = (value+1)%6
            presenter?.onChangeTime(displayMin%10 + value*10,displaySec)
        }
        findViewById<Button>(R.id.buttonMinuteUnitNumberPlus).setOnClickListener{
            var value= findViewById<TextView>(R.id.textViewMinuteUnitNumber).text.toString().toInt()
            value = (value+1)%10
            presenter?.onChangeTime((displayMin/10)*10 + value,displaySec)
        }
        findViewById<Button>(R.id.buttonSecTesNumberPlus).setOnClickListener {
            var value= findViewById<TextView>(R.id.textViewSecTensNumber).text.toString().toInt()
            value = (value+1)%6
            presenter?.onChangeTime(displayMin,displaySec%10 + value*10)
        }
        findViewById<Button>(R.id.buttonSecUnitNumberPlus).setOnClickListener {
            var value= findViewById<TextView>(R.id.textViewSecUnitNumber).text.toString().toInt()
            value = (value+1)%10
            presenter?.onChangeTime(displayMin,(displaySec/10)*10 + value)

        }

        findViewById<Button>(R.id.buttonMinuteTesNumberMinus).setOnClickListener {
            var value=findViewById<TextView>(R.id.textViewMinuteTensNumber).text.toString().toInt()
            value = if(value==0)5 else (value-1)%6
            presenter?.onChangeTime(displayMin%10 + value*10,displaySec)
        }
        findViewById<Button>(R.id.buttonMinuteUnitNumberMinus).setOnClickListener{
            var value=findViewById<TextView>(R.id.textViewMinuteUnitNumber).text.toString().toInt()
            value = if(value==0)9 else (value-1)%10
            presenter?.onChangeTime((displayMin/10)*10 + value,displaySec)
        }
        findViewById<Button>(R.id.buttonSecTesNumberMinus).setOnClickListener {
            var value=findViewById<TextView>(R.id.textViewSecTensNumber).text.toString().toInt()
            value = if(value==0)5 else (value-1)%6
            presenter?.onChangeTime(displayMin,displaySec%10 + value*10)
        }
        findViewById<Button>(R.id.buttonSecUnitNumberMinus).setOnClickListener {
            var value=findViewById<TextView>(R.id.textViewSecUnitNumber).text.toString().toInt()
            value = if(value==0)9 else (value-1)%10
            presenter?.onChangeTime(displayMin,(displaySec/10)*10 + value)
        }

        findViewById<Button>(R.id.buttonStart).setOnClickListener {presenter?.onSart()  }
        findViewById<Button>(R.id.buttonStop).setOnClickListener {presenter?.onStop()  }
        findViewById<Button>(R.id.buttonRestart).setOnClickListener {presenter?.onRestart()   }

    }


    override fun onDestroy() {
        presenter?.detache()
        super.onDestroy()
    }


    override fun onSaveInstanceState( outState:Bundle){
        super.onSaveInstanceState(outState)
        //outState?.putInt("min", displayMin)
        //outState?.putInt("sec", displaySec)
        writePresenterToBundle(outState)

    }
    fun writePresenterToBundle(bundle:Bundle)
    {
        bundle.putLong("timeLeft",presenter!!.getState().timeLeft)
        bundle.putInt("isRunning",presenter!!.getState().isRunning)
    }

     override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
         displayMin= if(savedInstanceState!=null)savedInstanceState.getInt("min") else 0
         displaySec= if(savedInstanceState!=null)savedInstanceState.getInt("sec") else 0
         if (savedInstanceState!=null)
         {
             var state = PresenterState()
             state.isRunning=savedInstanceState.getInt("isRunning")
             state.timeLeft=savedInstanceState.getLong("timeLeft")
             presenter= SimpleTimerPreseter(this,state)
         }
         else presenter= SimpleTimerPreseter(this)
    }


    class AddListenerTens(var textView:TextView,var presentr:SimpleTimerPreseter?):View.OnClickListener
    {
        override fun onClick(v: View?) {
            var value=textView.text.toString().toInt()
            value = (value+1)%6
            //textView.text=value.toString()
            presentr

        }
    }

    class MinusListenerTens(var textView:TextView,var presentr:SimpleTimerPreseter?):View.OnClickListener
    {
        override fun onClick(v: View?) {
            var value=textView.text.toString().toInt()
            value = if(value==0)5 else (value-1)%6
            textView.text=if(value>0)value.toString() else (-value).toString()
        }
    }

    class AddListenerUnit(var textView:TextView,var presentr:SimpleTimerPreseter?):View.OnClickListener
    {
        override fun onClick(v: View?) {
            var value=textView.text.toString().toInt()
            value = (value+1)%10
            textView.text=value.toString()
        }
    }

    class MinusListenerUnit(var textView:TextView,var presentr: SimpleTimerPreseter?):View.OnClickListener
    {
        override fun onClick(v: View?) {

            var value=textView.text.toString().toInt()
            value = if(value==0)9 else (value-1)%10
            textView.text=if(value>0)value.toString() else (-value).toString()
        }
    }

    //todo zmienic w liczbie dziesątek min i sek max 5
}

