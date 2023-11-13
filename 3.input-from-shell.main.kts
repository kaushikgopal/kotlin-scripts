#!/usr/bin/env kotlin

@file:Repository("https://jitpack.io")
@file:DependsOn("com.github.kaushikgopal:shell.main.kts:276950a346")

import sh.kau.shell.ShellConsole.Companion.ANSI_GRAY
import sh.kau.shell.ShellConsole.Companion.ANSI_GREEN
import sh.kau.shell.ShellConsole.Companion.ANSI_PURPLE
import sh.kau.shell.ShellConsole.Companion.ANSI_RESET
import sh.kau.shell.ShellConsole.Companion.ANSI_YELLOW
import sh.kau.shell.runInShell

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
    $>  kotlin 3.input-from-shell.main.kts
 *
 * Alternatively, make this script executable
    $>  chmod +x <script-name>.main.kts
    $>  ./<script-name>.main.kts <args>
 *
 * Read more about this at https://kau.sh/blog/kscript
*/

println("$ANSI_GRAY ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯  PROGRAM START$ANSI_RESET")
program(args)
println("$ANSI_GRAY  ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯ PROGRAM END $ANSI_RESET")

fun program(args: Array<String>) {
  println("$ANSI_GRAY[args]$ANSI_GREEN${args.joinToString()}$ANSI_RESET")

  // simple command - taking an input
  // print it back out

  print("\n\n$ANSI_PURPLE Enter your name: $ANSI_RESET")
  val name = readlnOrNull()
  println("\n\n$ANSI_GRAY Why hello $ANSI_YELLOW $name! $ANSI_RESET\n\n\n")


  // take a domain name
  // open social media links such as http://threads.<domain.name>

  print("Do you have a domain name? (y/N): ")
  val hasDomain = readlnOrNull()

  var domain: String? = null
  if (hasDomain == "y") {
    print("What is your domain name: ")
    domain = readlnOrNull()
  }

  domain = domain ?: "kau.sh"

  println("${ANSI_GRAY}🚧 Opening all the social profiles now!$ANSI_RESET")

  // execute a shell command
  listOf(
      "open https://threads.$domain",
      "open https://github.$domain",
      "open https://twitter.$domain",
      "open https://mastodon.$domain",
      "open https://instagram.$domain"
  ).forEach { it.runInShell() }
}
