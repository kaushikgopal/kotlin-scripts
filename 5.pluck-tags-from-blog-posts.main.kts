#!/usr/bin/env kotlin

/*
 *
 * This script goes through a specified folder
 *  searches through all blog posts (markdown files)
 *  plucks out the tags/categories from the front-matter of each post (should be in the "tags: []" format)
 *  then prints out a list of all the tags/categories
 *
 * run this script like so:
    $>  brew install kotlin
    $>  kotlin 5.pluck-tags-from-blog-posts.main.kts ~/kau.sh/content/blog tags
    $>  kotlin 5.pluck-tags-from-blog-posts.main.kts ~/kau.sh/content/blog categories
*/

import java.io.File

fun program(args: Array<String>) {

  if (args.isEmpty()) {
    println(" ⚠️ ERROR: a path is required ")
    return
  }

  // return if we can't find atleast one .md file
  val files = File(args[0]).listFiles { file ->
    file.isFile() && file.name.endsWith(".md")
  } ?: kotlin.run {
    println(" ⚠️ ERROR: no markdown files found ")
    return
  }

  println(" ℹ️ found ${files.size} markdown files to process")

  // return if second argument is not either tags or categories
  if (args.size < 2 || (args[1] != "tags" && args[1] != "categories")) {
    println(" ⚠️ ERROR: second argument must be either 'tags' or 'categories' ")
    return
  }

  val tagOrCategorySearch = args[1]

  // read every line of file and pluck the line that has "tags: [" in it
  val tags = files
      .map { file ->
        file.readLines()
            .filter { it.contains("${tagOrCategorySearch}: [") }
            .map { it.replace("${tagOrCategorySearch}: [", "").replace("]", "") }
            .map { it.split(",") }
            .flatten()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
      }
      .flatten()
      .distinct()
      .sorted()

  // print the tags to the console on separate lines
  tags.forEach { tag ->
    println(tag)
  }
}


println("******* PROGRAM START ***************** ")
program(args)
println("******* PROGRAM END ***************** ")
