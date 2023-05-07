#!/usr/bin/env kotlin

import java.io.File

/*
 * run this script like so:

    $>  brew install kotlin
    $>  kotlin 1.shell.ls-folder.main.kts <directory>
 *
 * alternatively if you want to make the script executable

    $>  chmod +x 1.shell.ls-folder.main.kts
    $>  ./1.shell.ls-folder.main.kts <directory>
*/

println("******* PROGRAM START ***************** ")
program(args)
println("******* PROGRAM END ***************** ")



fun program(args : Array<String>) {

  if (args.isEmpty()) return

  val folders = File(args[0]).listFiles { file ->
    file.isDirectory()
  }

  folders?.forEach { folder -> println(folder) }
}
