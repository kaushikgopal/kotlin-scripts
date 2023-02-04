#!/usr/bin/env kotlin

@file:DependsOn("com.squareup.okio:okio:3.3.0")

import okio.FileSystem
import okio.IOException
import okio.Path
import okio.Path.Companion.toPath

@Throws(IOException::class)
fun processLinesFromFile(path: Path, lineAction: (String?) -> Unit) {
  FileSystem.SYSTEM.read(path) {
    while (true) {
      val line = readUtf8Line() ?: break
      lineAction.invoke(line)
    }
  }
}

val filePath = "star-wars-demo-results.csv".toPath()

println(" --- Start of file --- 🏁")
processLinesFromFile(filePath) { line ->
  println(line)
}
println("--- End of file --- ✅")
