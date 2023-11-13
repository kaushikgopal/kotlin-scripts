#!/usr/bin/env kotlin

import java.io.BufferedReader
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

val ANSI_RESET = "\u001B[0m"
val ANSI_GRAY = "\u001B[90m"   // use this mostly
val ANSI_PURPLE = "\u001B[35m" // input commands
val ANSI_GREEN = "\u001B[32m"  // highlighting values
val ANSI_RED = "\u001B[31m"    // error
val ANSI_YELLOW = "\u001B[33m" // important messages
val ANSI_BLUE = "\u001B[34m"
val ANSI_CYAN = "\u001B[36m"
val ANSI_WHITE = "\u001B[37m"

fun String.exec(
  exitOnError: Boolean = false,
  verbose: Boolean = false,
): String {
  if (verbose) println("$ANSI_GRAY[command] $this $ANSI_RESET")
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
✗ [err: $exitCode] output:
------------------
${outputText.trim()}
$ANSI_RESET"""
    )
    if (exitOnError) {
      println("$ANSI_RED✗ Exiting... $ANSI_RESET")
      exitProcess(1)
    }
  }
  return outputText.trim()
}
