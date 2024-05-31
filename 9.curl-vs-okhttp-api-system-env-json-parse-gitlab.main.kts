#!/usr/bin/env kotlin

@file:Repository("https://repo.maven.apache.org/maven2/") @file:DependsOn("com.squareup.okhttp3:okhttp:4.10.0") @file:DependsOn(
    "com.squareup.okio:okio:3.9.0",
) @file:DependsOn("com.squareup.moshi:moshi-kotlin:1.15.1") @file:DependsOn("com.squareup.moshi:moshi-adapters:1.13.0") @file:DependsOn(
    "dev.zacsweers.moshix:moshi-metadata-reflect:0.27.1",
)

@file:Repository("https://jitpack.io") @file:DependsOn("com.github.kaushikgopal:shell.main.kts:276950a346")

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import dev.zacsweers.moshix.reflect.MetadataKotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.buffer
import java.util.*
import okio.source
import java.io.File
import java.io.IOException

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
val gitlabApiToken =
    System.getenv("GITLAB_API_TOKEN") ?: throw IllegalStateException("PRIVATE_TOKEN must be set")

val moshi: Moshi = Moshi.Builder().add(MetadataKotlinJsonAdapterFactory())
    .add(Date::class.java, Rfc3339DateJsonAdapter()).build()

val client = OkHttpClient()

println("$ANSI_GRAY----$ANSI_RESET")
program(args)
println("$ANSI_GRAY----$ANSI_RESET")

fun program(args: Array<String>) {
  if (DEBUG) println("$ANSI_GRAY[args]$ANSI_GREEN${args.joinToString()}$ANSI_RESET")

  getProjects().forEach { project ->
        println(
            """
$ANSI_GRAY[${project.id}]$ANSI_GREEN ${project.name}
      """.trimMargin(),
        )
      }

  getMergeRequests().forEach { mr ->
        println(
            """
$ANSI_GRAY[$ANSI_YELLOW${mr.id}$ANSI_GRAY]: ${ANSI_GREEN}${mr.title} - ${ANSI_GREEN}${mr.author.name}
          """.trimMargin(),
        )
      }
}

private fun getProjects(): List<GitlabProject> {

  val listType = Types.newParameterizedType(List::class.java, GitlabProject::class.java)
  val adapter = moshi.adapter<List<GitlabProject>>(listType)

  // Command to execute the API call
  val command = listOf(
      "curl",
      "-H", "PRIVATE-TOKEN: $gitlabApiToken",
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

private fun getMergeRequests(): List<MergeRequest> {
  val listType = Types.newParameterizedType(List::class.java, MergeRequest::class.java)
  val adapter = moshi.adapter<List<MergeRequest>>(listType)

  // Build the request
  val request = Request.Builder()
      .url("https://gitlab.com/api/v4/projects/40196776/merge_requests?state=opened")
      .addHeader("PRIVATE-TOKEN", gitlabApiToken).build()

  return try {
    // Execute the request
    val response = client.newCall(request).execute()
    response.use {
      if (!it.isSuccessful) throw IOException("Unexpected code $it")
      val jsonOutput = it.body!!.source()
      adapter.fromJson(jsonOutput) ?: emptyList()
    }
  } catch (e: IOException) {
    e.printStackTrace()
    emptyList()
  }
}

data class GitlabProject(val name: String, val id: Int)

data class MergeRequest(
  val id: Int,
  val title: String,
  @Json(name = "created_at") val created: Date,
  @Json(name = "web_url") val webUrl: String,
  val author: User,
  val assignees: List<User>,
  val assignee: User?,
  val reviewers: List<User>,
)

class User(
  val id: Int,
  val username: String,
  val name: String,
)
