#!/usr/bin/env kotlin

@file:DependsOn("com.squareup.okio:okio:3.5.0")

import okio.BufferedSink
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toOkioPath
import okio.Path.Companion.toPath
import okio.appendingSink
import okio.buffer
import okio.source
import java.io.File


/*
 * This script shows how to deal with files in kotlin scripts.

 * In this file you'll see how to:
 *    1. read each line from a file
 *    2. write to a file
 *    3. download a file from the web
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
val ANSI_GRAY = "\u001B[90m" // use this mostly
val ANSI_PURPLE = "\u001B[35m" // input commands
val ANSI_GREEN = "\u001B[32m" // highlighting values
val ANSI_RED = "\u001B[31m" // error
val ANSI_YELLOW = "\u001B[33m" // important messages
// val ANSI_BLUE = "\u001B[34m"
// val ANSI_CYAN = "\u001B[36m"
// val ANSI_WHITE = "\u001B[37m"

val DEBUG = true

if (DEBUG) println("$ANSI_GRAY ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯  PROGRAM START$ANSI_RESET")
program(args)
if (DEBUG) println("$ANSI_GRAY  ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯ PROGRAM END $ANSI_RESET")

fun program(args: Array<String>) {
  if (DEBUG) println("$ANSI_GRAY[args]$ANSI_GREEN${args.joinToString()}$ANSI_RESET")

  readEachLine()
  println("\n\n")
  createFileWithContent()
  println("\n\n")
  appendContentToFile()

}

fun readEachLine() {
    println("${ANSI_GRAY}🚧 printing first 5 lines of feed.json $ANSI_RESET")
    // print the first 10 lines of a file
    forEachLine("feed.json".toPath()) { text: String, index: Int ->
       // if (index !in 1424..1505) return@forEachLine
       if(index > 10) return@forEachLine
       println("$ANSI_GRAY Line $index: $ANSI_GREEN $text $ANSI_RESET")
    }
}

fun createFileWithContent() {
   val filename = "scratch.txt"
   val file = File(filename)

   if (file.exists()) {
     println("${ANSI_GRAY}🚧 $file already exists. going to delete it $ANSI_RESET")
     file.delete()
   }

  println("${ANSI_GRAY}🚧 creating $file $ANSI_RESET")
  file.createNewFile()

  writeToFile(file.toOkioPath()) { sink ->
    sink.writeUtf8("---- head of file ---- \n")
    sink.writeUtf8("... riveting content\n")
    sink.writeUtf8("---- tail of file ---- \n")
  }

  println("$ANSI_YELLOW ✓ open $file to read some riveting content $ANSI_RESET")
}


fun appendContentToFile() {
  val filename = "scratch.txt"
  val file = File(filename)
  file.appendingSink().buffer().use {
    it.writeUtf8("*** appended content ***")
  }
  println("$ANSI_YELLOW ✓ appended content to $file  $ANSI_RESET")
}

/**
 * Okio recipe to read a file line by line
 * https://square.github.io/okio/recipes/#read-a-text-file-line-by-line-javakotlin
 */
fun forEachLine(path: Path, lineAction: (String, Int) -> Unit) {
  var index: Int = 0
  FileSystem.SYSTEM.read(path) {
    loop@ while (true) {
      val line = readUtf8Line() ?: break
      lineAction.invoke(line, index)
      index += 1
    }
  }
}

/**
 * Okio recipe to write to a file
 * https://square.github.io/okio/recipes/#read-a-text-file-line-by-line-javakotlin
 */
fun writeToFile(path: Path, writingSink: (BufferedSink) -> Unit) {
  FileSystem.SYSTEM.write(path) { writingSink.invoke(this) }
}
