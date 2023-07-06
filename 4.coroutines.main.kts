#!/usr/bin/env kotlin

@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.2")

import kotlin.system.measureTimeMillis
import kotlinx.coroutines.*


/*
 * This script shows you how you can use coroutines to do async programming
 *
 * run this script like so:

    $>  brew install kotlin
    $>  chmod +x 4.coroutines.main.kts
    $>  ./4.coroutines.main.kts
 */

//main()
fun main() = runBlocking {
  println("------ first program ")
  println("------   two coroutines run in series")

  val time = measureTimeMillis {
    val one = doOne()
    val two = doTwo()
    println("One and Two is ${one + two}")
  }

  println("finished in $time")
}



//main2()
fun main2() = runBlocking {
  println("------ second program ")
  println("------   multiple coroutines non-waiting")

  launch {
    delay(200L)
    println("---- A")
  }

  coroutineScope {
    launch {
      delay(500L)
      println("---- B")
    }

    delay(100L)
    println("---- C")
  }

  println("---- D")
}



//main3()
fun main3() = runBlocking {
  println("\n\n\n\n------ next program ")
  println("------   100s of coroutines")

  // these will "wait" on the next one
  launch {
    println("\n\n")

    repeat(1_00) {
      print("$it ")
      delay(150L)
    }
  }

  // these will all run in "parallel"
  repeat(1_00) {
      println("\n\n")
      launch {
          delay(100L)
          print("-${it}-")
      }
  }

}

suspend fun doOne(): Int {
  delay(500L)
  return 13
}

suspend fun doTwo(): Int {
  delay(1000L)
  return 29
}



main4()

fun main4() = runBlocking {
  println("\n\n\n\n------ next program ")
  println("------   coroutines sequencing")

  launch {
    println("This is executed before the first delay <1> [${Thread.currentThread().name}]")
    println("This is executed before the first delay <2> [${Thread.currentThread().name}]")
    stallForTime()
    println("This is executed after the first delay [${Thread.currentThread().name}]")
  }

  launch {
    println("This is executed before the second delay [${Thread.currentThread().name}]")
    stallForTime()
    println("This is executed after the second delay [${Thread.currentThread().name}]")
  }

  println("This is executed immediately [${Thread.currentThread().name}]")
}

suspend fun stallForTime() {
  println("before delay [${Thread.currentThread().name}]")
//  withContext(Dispatchers.IO) {
//    println("delaying on [${Thread.currentThread().name}]")
    delay(4000L)
//  }
}
