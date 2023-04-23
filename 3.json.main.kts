#!/usr/bin/env kotlin

@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("com.squareup.okhttp3:okhttp:4.10.0")
@file:DependsOn("com.squareup.okio:okio:3.0.0")
@file:DependsOn("com.squareup.moshi:moshi:1.13.0")
@file:DependsOn("com.squareup.moshi:moshi-adapters:1.13.0")
@file:DependsOn("com.squareup.moshi:moshi-kotlin:1.13.0")

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer
import java.util.*
import okhttp3.Request

val jsonFeedFilePath = "./feed.json".toPath()

/*
 * **********************
 * Load blog feed json
 * **********************
 */

data class Page(
    val title: String,
    @Json(name = "date_published")
    val publishedDate: Date,
    @Json(name = "file_path")
    val filePath: String,
    val description: String?,
)

data class Feed(
    val title: String,
    val description: String,
    @Json(name = "items")
    val pages: List<Page>
)

// download RSS feed file

val jsonFile: String = FileSystem.SYSTEM
    .source(jsonFeedFilePath)
    .buffer()
    .readUtf8()

val jsonParser: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .add(Date::class.java, Rfc3339DateJsonAdapter())
    .build()

var feed: Feed = jsonParser
    .adapter(Feed::class.java)
    .fromJson(jsonFile) as Feed

println("🤖 local feed - ${feed.pages.count()} pages")


// download RSS feed file
val jsonFeedUrl = "https://kau.sh/index.json"

val okhttpClient = OkHttpClient()
val request = Request.Builder()
    .url(jsonFeedUrl)
    .build()

okhttpClient.newCall(request).execute().use {response ->
  feed = jsonParser
      .adapter(Feed::class.java)
      .fromJson(response.body!!.source()) as Feed
}


println("🤖 remote found ${feed.pages.count()} pages")
