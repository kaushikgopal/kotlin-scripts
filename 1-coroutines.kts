#!/usr/bin/env kscript

@file:MavenRepository("central", "https://repo.maven.apache.org/maven2/")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.2")

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

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

fun main3() = runBlocking {

    launch {
        repeat(100) {
            print("$it ")
            delay(150L)
        }
    }

//    repeat(1_000) {
//        launch {
//            print(".")
//            delay(100L)
//        }
//    }

}

fun main() = runBlocking {
    val time = measureTimeMillis {
       val one = doOne()
        val two = doTwo()
        println("One and Two is ${one + two}")
    }

    println("finished in $time")
}

suspend fun doOne(): Int {
    delay (500L)
    return 13
}

suspend fun doTwo(): Int {
    delay (1000L)
    return 29
}

main()