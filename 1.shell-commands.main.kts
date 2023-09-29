#!/usr/bin/env kotlin
@file:DependsOn("com.squareup.okio:okio:3.5.0")

import okio.buffer
import okio.source
import java.util.concurrent.TimeUnit

/*
 * This script <does something useful>
 *
 * To run kscripts do it like so:
    $>  brew install kotlin
    $>  kotlin <script-name>.main.kts <args>
 *
 * Alternatively, make this script executable
    $>  chmod +x <script-name>.main.kts
    $>  ./<script-name>.main.kts <args>
 *
 * Read more about this at https://kau.sh/blog/kscript
*/

val ANSI_RESET = "\u001B[0m"
val ANSI_GRAY = "\u001B[90m"   // use this mostly
val ANSI_PURPLE = "\u001B[35m" // input commands
val ANSI_GREEN = "\u001B[32m"  // highlighting values
val ANSI_RED = "\u001B[31m"    // error
val ANSI_YELLOW = "\u001B[33m" // important messages
//val ANSI_BLUE = "\u001B[34m"
//val ANSI_CYAN = "\u001B[36m"
//val ANSI_WHITE = "\u001B[37m"

val DEBUG = true

if (DEBUG) println("$ANSI_GRAY ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯  PROGRAM START$ANSI_RESET")
program(args)
if (DEBUG) println("$ANSI_GRAY  ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯ PROGRAM END $ANSI_RESET")

fun program(args: Array<String>) {

  if (DEBUG) println("$ANSI_GRAY[args]$ANSI_GREEN${args.joinToString()}$ANSI_RESET")

  // notice use of unix shell commands
  "find scratch.txt".exec()
  "rm -rf scratch.txt".exec()
  "find scratch.txt".exec()
  "touch scratch.txt".exec()

  // find all files ending with ".main.kts"
  // in this directory recursively
  // directory path must contain "res/values"
  // split the output by newline and filename in a list
  val fileList =
      "find . -type f -path '*.main.kts'"
          .exec()
          .inputStream
          .source()
          .buffer()
          .readUtf8()
          .split("\n")
          .filter { it.isNotBlank() }

  println("$ANSI_GRAY  Found the following files: $ANSI_RESET")
  fileList.forEach {
    println("$ANSI_GREEN  - $it $ANSI_RESET")
  }

}

fun String.exec(): Process {
  if (DEBUG) println("$ANSI_GRAY[command] $this $ANSI_RESET")
  val process = ProcessBuilder("/bin/bash", "-c", this).redirectErrorStream(true).start()
  val output = process.waitFor(3, TimeUnit.SECONDS)
  // if (DEBUG) println("$ANSI_GRAY *** [command] $output $ANSI_RESET")
  return process
}
