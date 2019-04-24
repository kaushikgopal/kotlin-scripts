#!/usr/bin/env kscript

/*
 * run this script like so:

    $>  kscript 0-sample.kts
    $>  kscript 0-sample.kts arg1 arg2
 *
 *
 * if you wish to make this run in a bash environment, simply run this on your shell script:

    $> kscript --add-bootstrap-header 0-sample.kts
 *
 *
 *
 * if you wish to just have a stand alone binary, that you can simply run directly

    $> kscript --package 0-sample.kts
    $> ./0-sample --arg arg1 arg2
    $> ./0-sample arg1 arg2
 *
 *
*/

println("Hello from Kotlin!")
for (arg in args) {
    println("arg: $arg")
}
