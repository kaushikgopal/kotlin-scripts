#!/usr/bin/env kotlin

/*
 * This script demonstrates how you can take inputs from the user
 * through an interactive way by prompting users for inputs step by step.
 *
 * There's a few concepts being demonstrated here:
 *  1. how to print things in color
 *  2. how to take user inputs from the command line
 *  3. how to execute shell commands from within a kotlin script
 *
 * To run kscripts do it like so:
    $>  brew install kotlin
    $>  kotlin 9.input-from-user-color-command.main.kts
 *
 * Alternatively, make this script executable
    $>  chmod +x <script-name>.main.kts
    $>  ./<script-name>.main.kts <args>
 *
 * Read more about this at https://kau.sh/blog/kscript
*/

println("******* PROGRAM START ***************** ")
main(args)
println("******* PROGRAM END ***************** ")


fun main(args: Array<String>) {

  val ANSI_BLACK = "\u001B[30m"
  val ANSI_RED = "\u001B[31m"
  val ANSI_RESET = "\u001B[0m"
  val ANSI_GREEN = "\u001B[32m"
  val ANSI_YELLOW = "\u001B[33m"
  val ANSI_BLUE = "\u001B[34m"
  val ANSI_PURPLE = "\u001B[35m"
  val ANSI_CYAN = "\u001B[36m"
  val ANSI_WHITE = "\u001B[37m"

  print("$ANSI_WHITE Enter your name: ")
  val name = readlnOrNull()
  println("$ANSI_BLUE Why hello $ANSI_PURPLE $name! $ANSI_RESET")

  print("Do you have a domain name? (y/N): ")
  val hasDomain = readlnOrNull()

  var domain: String? = null
  if (hasDomain == "y") {
    print("What is your domain name: ")
    domain = readlnOrNull()
  }

  domain = domain ?: "kau.sh"

  println("Opening all the social profiles now! ")

  // execute a shell command
  "open https://threads.$domain".exec()
  "open https://github.$domain".exec()
  "open https://twitter.$domain".exec()
  "open https://mastodon.$domain".exec()
  "open https://instagram.$domain".exec()
}

fun String.exec() {
  val process = Runtime.getRuntime().exec(this)
  process.waitFor()
}
