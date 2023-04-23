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


data class Tooted(
    val postId: String,
    val tootId: String,
)

val tootsFile = "./toots.csv".toPath()
var tooted = mutableListOf<Tooted>()

FileSystem.SYSTEM.read(tootsFile) {
  while (true) {
    val line = readUtf8Line() ?: break
    val (path, tootId) = line.split(',', ignoreCase = false, limit = 2)
    tooted.add(Tooted(path, tootId))
  }
}

println("🤖 tooted ${tooted.count()} times before")

// write back into the file

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
