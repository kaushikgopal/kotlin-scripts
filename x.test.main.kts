#!/usr/bin/env kotlin

@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("org.jetbrains.kotlin:kotlin-test:1.3.72")

import kotlin.test.assertEquals

/*
 * This script is a template for quickly running tests
 *
 * To run kscripts do it like so:
    $>  brew install kotlin
    $>  chmod +x x.test.main.kts
    $>  ./x.test.main.kts
*/


fun isEditDistance1(string1: String, string2: String): Boolean {
  return false
}

assertEquals(true, isEditDistance1("cat", "cat"), "\uD83D\uDEA8")

assertEquals(true, isEditDistance1("cat", "cat"), "🚨")
assertEquals(false, isEditDistance1("cat", "dog"), "🚨")
assertEquals(true, isEditDistance1("cat", "mat"), "🚨")
assertEquals(true, isEditDistance1("cat", "chat"), "🚨")
assertEquals(false, isEditDistance1("cat", "act"), "🚨")
assertEquals(false, isEditDistance1("cat", "palindrome"), "🚨")
assertEquals(true, isEditDistance1("cat", "cats"), "🚨")


fun isEditDistance(maxDifference: Int, string1: String, string2: String): Boolean {
  return false
}

assertEquals(true, isEditDistance(0, "cat", "cat"), "\uD83D\uDEA8")
assertEquals(true, isEditDistance(0, "cat", "cat"), "🚨")
assertEquals(false, isEditDistance(1, "cat", "dog"), "🚨")
assertEquals(true, isEditDistance(1, "cat", "mat"), "🚨")
assertEquals(true, isEditDistance(1, "cat", "chat"), "🚨")
assertEquals(true, isEditDistance(2, "ban", "bhane"), "🚨")
assertEquals(false, isEditDistance(2, "pal", "palindrome"), "🚨")
assertEquals(false, isEditDistance(1, "cat", "act"), "🚨")
assertEquals(true, isEditDistance(1, "cats", "cat"), "🚨")
assertEquals(true, isEditDistance(2, "cat", "chats"), "🚨")
assertEquals(true, isEditDistance(3, "sitting", "kitten"), "🚨")
assertEquals(false, isEditDistance(2, "sitting", "kitten"), "🚨")

