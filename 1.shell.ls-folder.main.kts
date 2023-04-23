#!/usr/bin/env kotlin

import java.io.File

fun program(args : Array<String>) {

  if (args.isEmpty()) {
    return
  }

  val folders = File(args[0]).listFiles { file ->
    file.isDirectory()
  }

  folders?.forEach { folder -> println(folder) }
}


// run this program like so `kotlinc -script list_folders.kts ~/code`
println("******* PROGRAM START ***************** ")
program(args)
println("******* PROGRAM END ***************** ")
