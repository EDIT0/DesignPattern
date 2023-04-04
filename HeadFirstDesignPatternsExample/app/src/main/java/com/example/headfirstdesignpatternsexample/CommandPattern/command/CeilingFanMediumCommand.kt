package com.example.headfirstdesignpatternsexample.CommandPattern.command

import com.example.headfirstdesignpatternsexample.CommandPattern.receiver.CeilingFan

class CeilingFanMediumCommand(
    private val ceilingFan: CeilingFan
) : Command {
    private var prevSpeed: Int = 0
    override fun execute() {
        prevSpeed = ceilingFan.getSpeed()
        ceilingFan.medium()
    }

    override fun undo() {
        when(prevSpeed) {
            CeilingFan.HIGH -> {
                ceilingFan.high()
            }
            CeilingFan.MEDIUM -> {
                ceilingFan.medium()
            }
            CeilingFan.LOW -> {
                ceilingFan.low()
            }
            CeilingFan.OFF -> {
                ceilingFan.off()
            }
            else -> {

            }
        }
    }

}