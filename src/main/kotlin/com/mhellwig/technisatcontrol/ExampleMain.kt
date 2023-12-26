package com.mhellwig.technisatcontrol

import kotlinx.coroutines.runBlocking

fun main() {
    val technisatControl = TechnisatControl("192.168.0.10")

    runBlocking {
        technisatControl.sendControlEnums(listOf(ControlEnum.BTN_1, ControlEnum.BTN_1))
    }
}