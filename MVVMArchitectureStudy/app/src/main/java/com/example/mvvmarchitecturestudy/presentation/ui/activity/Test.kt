package com.example.mvvmarchitecturestudy.presentation.ui.activity

/**
 * You can edit, run, and share this code.
 * play.kotlinlang.org
 */

var function_c_result = 0

fun main() {
    val sumResult = calculate(4,5, ::sum)
    println(sumResult)
    val mulResult = calculate(4,5) {a, b -> a*b}
    println(mulResult)

    val func = operation()
    println(func(2))

    val a = function_a(1, "one", "!!!", {x, str1 -> str1 + x})
    println(a)

    function_b(2, 3, {x, y -> x + y})
    function_b(2, 3) {x, y -> x - y}

    val func3 = function_c(1, 2)
    println(func3(1) + function_c_result)

    val func4 = function_d("Hello", "World", {a, b -> })
    val func4_1 = func4(1, "two", 3)
    println(func4_1(5, 10))
}

fun function_d(str1 : String, str2 : String, innerFunction : (String, String) -> Unit) : (Int, String, Int) -> ((Int, Int) -> String){
    val innerFuctionResult = if(innerFunction.equals("???")) "!!!" else "!!"
    println("${str1} $str2 ${innerFuctionResult}")
    return ::function_d_1
}

fun function_d_1(x: Int, str1:String, y:Int) : ((Int, Int) -> String) {
    println("$x $str1 $y")
    return ::function_d_2
}

fun function_d_2(x:Int, y:Int) : String = "x: ${x} y: ${y}"

fun function_c(x: Int, y:Int) : (Int) -> Int {
    function_c_result = x + y
    return ::function_c_1
}

fun function_c_1(x:Int) : Int {
    return x + x
}

fun function_b(x:Int, y:Int, op : (Int, Int) -> Int) {
    println("계산값은 ${op(x,y)}")
}

fun function_a(x:Int, str1:String, str2:String, innerFunction1 : (Int, String) -> String) : String {
    return innerFunction1(x, str1) + str2
}

fun operation() : (Int) -> Int {
    return ::square
}

fun square(x : Int) = x * x

fun sum(x:Int, y:Int) = x + y

fun calculate(x:Int, y:Int, operation:(Int, Int) -> Int) : Int {
    return operation(x,y)
}