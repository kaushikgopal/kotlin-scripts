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

println("------ first program ")
println("------   two coroutines run in series")
main()
fun main() = runBlocking {
  val time = measureTimeMillis {
    val one = doOne()
    val two = doTwo()
    println("One and Two is ${one + two}")
  }

  println("finished in $time")
}



println("------ second program ")
println("------   multiple coroutines non-waiting")
main2()
fun main2() = runBlocking {

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



println("\n\n\n\n------ next program ")
println("------   100s of coroutines")
main3()
fun main3() = runBlocking {

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
