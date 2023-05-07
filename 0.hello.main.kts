#!/usr/bin/env kotlin

/*
 * This script is a starter template script.
 * It takes in whatever you send as arguments and prints them out.
 *
 * run this script like so:

    $>  brew install kotlin
    $>  kotlin 0.hello.main.kts Hello 🌎
 *
 * alternatively if you want to make the script executable

    $>  chmod +x 0.hello.main.kts
    $>  ./0.hello.main.kts Hello 🌎
*/

println("Hello from Kotlin Scripts!")
println("******* PROGRAM START ***************** ")
program(args)
println("******* PROGRAM END ***************** ")


fun program(args: Array<String>) {
    println("\uD83D\uDEE0️  This program received the following arguments: ${args.joinToString()} ")
}
