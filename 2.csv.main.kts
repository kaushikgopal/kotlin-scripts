#!/usr/bin/env kotlin

@file:DependsOn("com.squareup.okio:okio:3.3.0")

import okio.FileSystem
import okio.IOException
import okio.Path
import okio.Path.Companion.toPath

/*
 * This script shows you how you can read a csv file.
 * run this script like so:

    $>  brew install kotlin
    $>  chmod +x 2.csv.main.kts
    $>  ./2.csv.main.kts
 */

println("\uD83D\uDEE0️ ******  Reading from CSV File ****** \uD83D\uDEE0")

val filePath = "star-wars-demo-results.csv".toPath()

println(" --- Start of file --- 🏁")
forEachLine(filePath) { line ->
  println(line)
}
println("--- End of file --- ✅")

@Throws(IOException::class)
fun forEachLine(path: Path, lineAction: (String) -> Unit) {
  FileSystem.SYSTEM.read(path) {
    while (true) {
      val line = readUtf8Line() ?: break
      lineAction.invoke(line)
    }
  }
}



println("\uD83D\uDEE0️ ******  Writing to a CSV File ****** \uD83D\uDEE0")

val tootsFile = "./toots.csv".toPath()
var tooted = mutableListOf<Tooted>()

println(" --- ☝\uD83C\uDFFD Reading from another existing CSV file ")
forEachLine(tootsFile) { line ->
  val (path, tootId) = line.split(',', ignoreCase = false, limit = 2)
  tooted.add(Tooted(path, tootId))
}
println("existing CSV file has ${tooted.count()} toots/lines")

println("--- Writing back to the CSV file --- ✅")
FileSystem.SYSTEM.write(tootsFile) {
  tooted.forEach { tooted ->
    writeUtf8(tooted.postId)
    writeUtf8(",")
    writeUtf8(tooted.tootId)
    writeUtf8("\n")

    // simply write into it again (so you can see the change in file)
    writeUtf8(tooted.postId)
    writeUtf8(",")
    writeUtf8(tooted.tootId)
    writeUtf8("\n")
  }
}

data class Tooted(
  val postId: String,
  val tootId: String,
)
