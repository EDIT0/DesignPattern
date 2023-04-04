package com.example.headfirstdesignpatternsexample.CommandPattern.command

import com.example.headfirstdesignpatternsexample.CommandPattern.receiver.CeilingFan

class CeilingFanOnCommand(
    private val ceilingFan: CeilingFan
) : Command {
    override fun execute() {
//        ceilingFan.on()
    }

    override fun undo() {
        ceilingFan.off()
    }
}