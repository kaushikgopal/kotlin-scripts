#!/usr/bin/env kotlin

@file:Repository("https://jitpack.io")
@file:DependsOn("com.github.yschimke:okurl-script:2.1.0")

import com.baulsupp.okscript.client
import com.baulsupp.okscript.queryList
import com.baulsupp.okscript.runScript
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.*
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer

val jsonFeedUrl = "./feed.json".toPath()

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
    .source(jsonFeedUrl)
    .buffer()
    .readUtf8()

val jsonParser: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .add(Date::class.java, Rfc3339DateJsonAdapter())
    .build()

val feed: Feed = jsonParser
    .adapter(Feed::class.java)
    .fromJson(jsonFile) as Feed

println("🤖 found ${feed.pages.count()} pages")
