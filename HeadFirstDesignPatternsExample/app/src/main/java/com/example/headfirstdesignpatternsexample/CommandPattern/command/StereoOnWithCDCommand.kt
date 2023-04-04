package com.example.headfirstdesignpatternsexample.CommandPattern.command

import com.example.headfirstdesignpatternsexample.CommandPattern.command.Command
import com.example.headfirstdesignpatternsexample.CommandPattern.receiver.Stereo

class StereoOnWithCDCommand(
    private val stereo: Stereo
) : Command {
    override fun execute() {
        stereo.on()
        stereo.setCD()
        stereo.setVolume(11)
    }

    override fun undo() {
        stereo.off()
    }
}