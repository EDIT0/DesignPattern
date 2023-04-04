package com.example.headfirstdesignpatternsexample.CommandPattern.command

class MacroCommand(
    private val commands: ArrayList<Command>
) : Command {
    override fun execute() {
        for(i in commands) {
            i.execute()
        }
    }

    override fun undo() {
//        for(i in commands.reversed()) {
//            i.undo()
//        }
        for(i in commands.size - 1 downTo 0) {
            commands[i].undo()
        }
    }
}