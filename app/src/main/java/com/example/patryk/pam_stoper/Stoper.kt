package com.example.patryk.pam_stoper

class Stoper() {
    fun onPeriod(period:Long, buz:()->Unit)
    {
        buz()
    }
}