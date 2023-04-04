package com.example.headfirstdesignpatternsexample.CommandPattern.command

import com.example.headfirstdesignpatternsexample.CommandPattern.command.Command
import com.example.headfirstdesignpatternsexample.CommandPattern.receiver.Stereo

class StereoOffWithCDCommand(
    private val stereo: Stereo
) : Command {
    override fun execute() {
        stereo.off()
    }

    override fun undo() {
        stereo.on()
        stereo.setCD()
        stereo.setVolume(11)
    }
}