package com.example.headfirstdesignpatternsexample.CommandPattern

import com.example.headfirstdesignpatternsexample.CommandPattern.command.*
import com.example.headfirstdesignpatternsexample.CommandPattern.invoker.RemoteControl
import com.example.headfirstdesignpatternsexample.CommandPattern.receiver.CeilingFan
import com.example.headfirstdesignpatternsexample.CommandPattern.receiver.Light
import com.example.headfirstdesignpatternsexample.CommandPattern.receiver.Stereo
import com.example.headfirstdesignpatternsexample.Utility

/**
 * 커맨드 패턴
 * 요청하는 객체와 수행하는 객체를 분리할 수 있다.
 * 분리하는 과정의 중심에 커맨드 객체(CeilingFanHighCommand, LightOnCommand 등)가 있고, 커맨드 객체가 행동이 들어있는 리시버(CeilingFan, Light, Stereo 등)를 캡슐화한다.
 * 인보커(RemoteControl)는 무언가를 요청할 때 커맨드 객체의 함수를 호출하면 된다.
 * 기능 추가 시 OCP 원칙에 위배되지 않는다.
 * */
fun main() {
    val TAG = "RemoteLoader"

    val remoteControl = RemoteControl()

    val livingRoomLight = Light("Living Room")
    val kitchenLight = Light("Kitchen")
    val livingRoomStereo = Stereo("Living Room")
    val kitchenStereo = Stereo("Kitchen")
    val ceilingFan = CeilingFan("CeilingFan")

    val livingRoomLightOn = LightOnCommand(livingRoomLight)
    val livingRoomLightOff = LightOffCommand(livingRoomLight)
    val kitchenLightOn = LightOnCommand(kitchenLight)
    val kitchenLightOff = LightOffCommand(kitchenLight)
    val livingRoomStereoOnWithCDCommand = StereoOnWithCDCommand(livingRoomStereo)
    val livingRoomStereoOffWithCDCommand = StereoOffWithCDCommand(livingRoomStereo)
    val kitchenStereoOnWithCDCommand = StereoOnWithCDCommand(kitchenStereo)
    val kitchenStereoOffWithCDCommand = StereoOffWithCDCommand(kitchenStereo)
    val ceilingFanOnCommand = CeilingFanOnCommand(ceilingFan)
    val ceilingFanOffCommand = CeilingFanOffCommand(ceilingFan)

    val ceilingFanMediumCommand = CeilingFanMediumCommand(ceilingFan)
    val ceilingFanHighCommand = CeilingFanHighCommand(ceilingFan)

    val partyOn : ArrayList<Command> = ArrayList()
    partyOn.apply {
        add(livingRoomLightOn)
        add(kitchenLightOn)
        add(livingRoomStereoOnWithCDCommand)
        add(kitchenStereoOnWithCDCommand)
    }
    val partyOff: ArrayList<Command> = ArrayList()
    partyOff.apply {
        add(livingRoomLightOff)
        add(kitchenLightOff)
        add(livingRoomStereoOffWithCDCommand)
        add(kitchenStereoOffWithCDCommand)
    }

    val macroCommandOn = MacroCommand(partyOn)
    val macroCommandOff = MacroCommand(partyOff)

    remoteControl.setCommand(0, livingRoomLightOn, livingRoomLightOff)
    remoteControl.setCommand(1, kitchenLightOn, kitchenLightOff)
    remoteControl.setCommand(2, livingRoomStereoOnWithCDCommand, livingRoomStereoOffWithCDCommand)
    remoteControl.setCommand(3, kitchenStereoOnWithCDCommand, kitchenStereoOffWithCDCommand)
//    remoteControl.setCommand(4, ceilingFanOnCommand, ceilingFanOffCommand) // ceilingFanOnCommand 사용 못함

    remoteControl.setCommand(4, macroCommandOn, macroCommandOff)

    remoteControl.setCommand(5, ceilingFanMediumCommand, ceilingFanOffCommand)
    remoteControl.setCommand(6, ceilingFanHighCommand, ceilingFanOffCommand)

    Utility.Log(TAG, remoteControl.toString())

    remoteControl.apply {
        /* 일반 리모컨 예제 예제 */
//        onButtonWasPushed(0)
//        offButtonWasPushed(0)
//        undoButtonWasPushed()
//        onButtonWasPushed(0)
//        offButtonWasPushed(0)
//        onButtonWasPushed(1)
//        offButtonWasPushed(1)
//        onButtonWasPushed(2)
//        offButtonWasPushed(2)
//        onButtonWasPushed(3)
//        offButtonWasPushed(3)
//        undoButtonWasPushed()
//        onButtonWasPushed(4)
//        offButtonWasPushed(4)
//
//        onButtonWasPushed(5)
//        offButtonWasPushed(5)
//        undoButtonWasPushed()
//        onButtonWasPushed(6)
//        undoButtonWasPushed()
//        onButtonWasPushed(6)


        /* MacroCommand 예제 */
        onButtonWasPushed(4)
        undoButtonWasPushed()
        offButtonWasPushed(4)
        undoButtonWasPushed()
    }

}