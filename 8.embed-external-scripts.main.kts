#!/usr/bin/env kotlin

@file:Import("common.main.kts")

import java.io.File

/*
 * This script demonstrates how you can import or embed external kts scripts
 * run this script like so:

    $>  brew install kotlin
    $>  kotlin 8.embed-external-scripts.main.kts
 *
 * alternatively if you want to make the script executable

    $>  chmod +x 8.embed-external-scripts.main.kts
    $>  ./8.embed-external-scripts.main.kts
*/

println("******* PROGRAM START ***************** ")
program(args)
println("******* PROGRAM END ***************** ")

fun program(args : Array<String>) {
    val person = Person(name = "Albert Einstein", age = 76)
    println("printing from class MyClassObject - ${person.name} is ${person.age} years old")
}

