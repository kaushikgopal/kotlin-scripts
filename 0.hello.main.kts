#!/usr/bin/env kotlin

/*
 * run this script like so:

    $>  brew install kotlin
    $>  kotlin 0.hello.main.kts Hello 🌎
 *
 * alternatively if you want to make the script executable

    $>  chmod +x 0.hello.main.kts
    $>  ./0.hello.main.kts Hello 🌎
*/

println("Hello from Kotlin!")
for (arg in args) {
    println("arg: $arg")
}
