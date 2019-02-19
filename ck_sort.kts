#!/usr/bin/env kscript
//DEPS com.github.holgerbrandl:kscript:1.2
import java.io.File
import kscript.text.*

fun program(args : Array<String>) {
    if (args.isEmpty()) return
    val lines: Sequence<String> = resolveArgFile(args)

    val pMap: MutableMap<Int, Int> = mutableMapOf()
    lines
        // .take(10)
        .map { it.toIntOrNull() }
        ?.filter { it != null }
        ?.map { it!! }
        .forEach { pid ->
            var frequency: Int? = pMap[pid]
            pMap[pid] = frequency?.let { it + 1 } ?: 1
        }

    pMap
        .filterValues { it > 1 }
        .forEach { k, v ->
            println("patient $k appears $v times")
        }

     println("processed ${pMap.keys.count()}")
}


// run this program like so `kotlinc -script list_folders.kts ~/code`
println("******* PROGRAM START ***************** ")
program(args)
println("******* PROGRAM END ***************** ")
