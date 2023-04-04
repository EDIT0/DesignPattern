package com.example.headfirstdesignpatternsexample.CommandPattern.command

interface Command {
    fun execute()
    fun undo()
}