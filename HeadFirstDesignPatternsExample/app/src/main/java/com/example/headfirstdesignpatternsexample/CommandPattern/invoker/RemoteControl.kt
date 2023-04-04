package com.example.headfirstdesignpatternsexample.CommandPattern.invoker

import com.example.headfirstdesignpatternsexample.CommandPattern.command.Command
import com.example.headfirstdesignpatternsexample.CommandPattern.command.NoCommand

class RemoteControl {
    private var onCommands: ArrayList<Command> = ArrayList<Command>(7)
    private var offCommands: ArrayList<Command> = ArrayList<Command>(7)
    private var undoCommand: Command

    init {

        val noCommand = NoCommand()
        for(i in 0 until 7) {
            onCommands.add(noCommand)
            offCommands.add(noCommand)
        }
        undoCommand = noCommand
    }

    fun setCommand(slot: Int, onCommand: Command, offCommand: Command) {
        onCommands[slot] = onCommand
        offCommands[slot] = offCommand
    }

    fun onButtonWasPushed(slot: Int) {
        onCommands[slot].execute()
        undoCommand = onCommands[slot]
    }

    fun offButtonWasPushed(slot: Int) {
        offCommands[slot].execute()
        undoCommand = offCommands[slot]
    }

    fun undoButtonWasPushed() {
        undoCommand.undo()
    }

    override fun toString(): String {
        var stringBuff = StringBuffer()
        stringBuff.append("\n------ 리모컨 ------\n")
        for(i in 0 until onCommands.size) {
            stringBuff.append("[slot${i}]" + onCommands[i].javaClass.simpleName + "   " + offCommands[i].javaClass.simpleName + "\n")
        }
        stringBuff.append("${undoCommand.javaClass.simpleName}\n")
        return stringBuff.toString()
    }
}