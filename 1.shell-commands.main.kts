#!/usr/bin/env kotlin
import java.io.BufferedReader
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

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
  "find scratch.txt".exec(exitOnError = false)
  "rm -rf scratch.txt".exec(exitOnError = false)
  "find scratch.txt".exec(exitOnError = false)
  "touch scratch.txt".exec(exitOnError = false)

  // find all files ending with ".main.kts"
  // in this directory recursively
  // directory path must contain "res/values"
  // split the output by newline and filename in a list
  val fileList =
      "find . -type f -path '*.main.kts'"
          .exec()
          .split("\n")
          .filter { it.isNotBlank() }

  println("$ANSI_GRAY  Found the following files: $ANSI_RESET")
  fileList.forEach {
    println("$ANSI_GREEN  - $it $ANSI_RESET")
  }

}

fun String.exec(
  exitOnError: Boolean = false,
): String {
  if (DEBUG) println("$ANSI_GRAY[command] $this $ANSI_RESET")
  val process =
      ProcessBuilder()
          // .directory(workingDirectory)
          .redirectErrorStream(true)
          .redirectOutput(ProcessBuilder.Redirect.PIPE)
          .redirectError(ProcessBuilder.Redirect.PIPE)
          // the /bin/bash -c -l is necessary if you use programs like "find" etc.
          .command("/bin/bash", "-c", "-l", this)
          .start()
  process.waitFor(3, TimeUnit.MINUTES)
  return process.retrieveOutput(exitOnError)
}

private fun Process.retrieveOutput(exitOnError: Boolean): String {
  val outputText = inputStream.bufferedReader().use(BufferedReader::readText)
  val exitCode = exitValue()
  if (exitCode != 0) {
    val errorText = errorStream.bufferedReader().use(BufferedReader::readText)
    println(
        """$ANSI_RED
✗ err: $exitCode
✗ output:
----------
${outputText.trim()}
$ANSI_RED✗ error:
----------
${errorText.trim()}$ANSI_RESET"""
    )
    if (exitOnError) {
      println("$ANSI_RED ✗ Exiting... $ANSI_RESET")
      exitProcess(1)
    }
  }
  return outputText.trim()
}
