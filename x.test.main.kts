#!/usr/bin/env kotlin

fun isEditDistance1(string1: String, string2: String): Boolean {
  return false
}

println("true → " + isEditDistance1("cat", "cat"))
println("false → " + isEditDistance1("cat", "dog"))
println("true → " + isEditDistance1("cat", "mat"))
println("true → " + isEditDistance1("cat", "chat"))
println("false → " + isEditDistance1("cat", "act"))
println("false → " + isEditDistance1("cat", "palindrome"))
println("true → " + isEditDistance1("cat", "cats"))


fun isEditDistance(maxDifference: Int, string1: String, string2: String): Boolean {
  return false
}

println("true → " + isEditDistance(0, "cat", "cat"))
println("true → " + isEditDistance(0,"cat" , "cat"))
println("false → " + isEditDistance(1,"cat" , "dog"))
println("true → " + isEditDistance(1,"cat" , "mat"))
println("true → " + isEditDistance(1,"cat" , "chat"))
println("true → " + isEditDistance(2,"ban" , "bhane"))
println("false → " + isEditDistance(2,"pal" , "palindrome"))
println("false → " + isEditDistance(1,"cat" , "act"))
println("true → " + isEditDistance(1,"cats" , "cat"))
println("true → " + isEditDistance(2,"cat" , "chats"))
println("true → " + isEditDistance(3,"sitting" , "kitten"))
println("false → " + isEditDistance(2,"sitting" , "kitten"))

