package com.example.headfirstdesignpatternsexample.CommandPattern.command

import com.example.headfirstdesignpatternsexample.CommandPattern.receiver.Light

class LightOffCommand(
    private val light: Light
) : Command {
    override fun execute() {
        light.off()
    }

    override fun undo() {
        light.on()
    }
}