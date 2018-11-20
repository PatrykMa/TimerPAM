package com.example.patryk.pam_stoper

interface ISimpleStoperView {
    var startCouldDownMin:Int
    var startCouldDownSec:Int
    var displayMin:Int
    var displaySec:Int
    fun timeEnd()
}