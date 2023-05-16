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
 *
 * I live coded another version of this script using Github Copilot:
 *  You can read more [about it here](https://kau.sh/kscript-copilot/)
 *  or watch the [youtube video here](https://www.youtube.com/watch?v=Tr2YBmecdw4)
*/

import java.io.File


println("******* PROGRAM START ***************** ")
program(args)
println("******* PROGRAM END ***************** ")

fun program(args: Array<String>) {

  if (args.isEmpty()) {
    println(" ⚠️ ERROR: a path is required ")
    return
  }

  // return if we can't find atleast one .md file
  // also check any subdirectories
  val files = File(args[0]).walkTopDown().filter { file -> file.name.endsWith(".md") }.toList()
  if (files.isEmpty()) {
    println(" ⚠️ ERROR: no markdown files found ")
    return
  }

  println(" ℹ️  found ${files.size} markdown files to process")

  // return if second argument is not present
  if (args.size < 2) {
    println(" ⚠️ ERROR: second argument is required")
    return
  }

  val items: List<String> = when (args[1]) {
    "tags", "categories" -> {
      collectTagsOrCategories(files, args[1])
    }

    "duplicates", "duplicate" -> {
      findDuplicates(files)
    }

    else -> {
      println(" ⚠️ ERROR: second argument must be either 'tags', 'categories' or 'duplicate(s)'")
      return
    }
  }

  justifiedPrint(items)
}

fun collectTagsOrCategories(files: List<File>, tagOrCategorySearch: String): List<String> {
  // read every line of file and pluck the line that has "tags: [" in it
  return files
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
}

// This function collects all categories and tags from all files
// then prints out the words that are both categories and tags
fun findDuplicates(files: List<File>): List<String> {
  val tags = collectTagsOrCategories(files, "tags")
  val categories = collectTagsOrCategories(files, "categories")
  return tags.filter { categories.contains(it) }
}

/**
 * This function takes a list of strings and
 * prints them out in a justified format
 */
fun justifiedPrint(words: List<String>) {
  if(words.isEmpty()) {
    println(" \uD83E\uDEB9️ nothing to print")
    return
  }

  val maxWordLength = words.maxBy { it.length }.length ?: 0
  val maxWordsPerLine = 80 / (maxWordLength + 2)

  var line = ""
  words.forEach { word ->
    if (line.length + word.length + 2 > 80) {
      println(line)
      line = ""
    }
    line += word.padEnd(maxWordLength + 2)
  }
  println(line)
}
