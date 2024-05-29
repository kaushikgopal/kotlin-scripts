#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("com.squareup.okhttp3:okhttp:4.10.0")
@file:DependsOn("com.squareup.okio:okio:3.0.0")
@file:DependsOn("com.squareup.moshi:moshi:1.13.0")
@file:DependsOn("com.squareup.moshi:moshi-adapters:1.13.0")
@file:DependsOn("com.squareup.moshi:moshi-kotlin:1.13.0")

@file:Repository("https://jitpack.io")
@file:DependsOn("com.github.kaushikgopal:shell.main.kts:276950a346")

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okio.buffer
import java.util.*
import okio.IOException
import okio.source
import java.io.File

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

println("$ANSI_GRAY----$ANSI_RESET")
program(args)
println("$ANSI_GRAY----$ANSI_RESET")

fun program(args: Array<String>) {
  if (DEBUG) println("$ANSI_GRAY[args]$ANSI_GREEN${args.joinToString()}$ANSI_RESET")
  getProjects()
      .forEach { println("$ANSI_GRAY Project Name: $ANSI_PURPLE${it.name}$ANSI_GRAY , Project ID: $ANSI_GREEN${it.id}$ANSI_RESET") }

}

private fun getProjects(): List<GitlabProject> {
  val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory())
      .add(Date::class.java, Rfc3339DateJsonAdapter()).build()

  val listType = Types.newParameterizedType(List::class.java, GitlabProject::class.java)
  val adapter = moshi.adapter<List<GitlabProject>>(listType)

  // Command to execute the API call
  val privateToken =
      System.getenv("GITLAB_API_TOKEN") ?: throw IllegalStateException("PRIVATE_TOKEN must be set")
  val command = listOf(
      "curl",
      "-H", "PRIVATE-TOKEN: $privateToken",
      "https://gitlab.com/api/v4/groups/caperai/projects",
  )

  // Temporarily store output in a file
  val tempFile = File.createTempFile("response", ".json")

  val projects = try {

    val process =
        ProcessBuilder(command).redirectOutput(ProcessBuilder.Redirect.to(tempFile)).start()
    process.waitFor()

    // Read the output
    val source = tempFile.source().buffer()
    val jsonOutput = source.readUtf8()
    source.close()

    // Parse JSON to Kotlin object
    adapter.fromJson(jsonOutput) ?: emptyList()

  } catch (e: IOException) {
    e.printStackTrace()
    emptyList()
  } finally {
    // Clean up the temporary file
    tempFile.delete()
  }

  return projects
}

data class GitlabProject(val name: String, val id: Int)
