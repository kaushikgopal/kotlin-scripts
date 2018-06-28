#!/usr/bin/env kscript
//DEPS com.github.holgerbrandl:kscript:1.2
import java.io.File
import kscript.text.*

/**
 * This is a program that'll tabulate the results for Instacart's 2018 Summer Hackathon
 *
 *              ---------- Carrot Wars 2018 ---------------
 */
fun program(args: Array<String>) {

    if (args.isEmpty()) return

    var projectResults: List<HProjectResult> = emptyList()

    val lines: Sequence<String> = resolveArgFile(args)
    lines.forEachIndexed { index, line ->
        if (index == 0) {
            projectResults = processHeader(line)
        } else {
            processVote(projectResults, line)
        }

    }

    println("------ we got the list of projects $projectResults")
}


// run this program like so `kotlinc -script list_folders.kts ~/code`
println("******* PROGRAM START ***************** ")
program(args)
println("******* PROGRAM END ***************** ")


// ---------------------------------------------------------------------------------------------
// Internal Helpers

fun processVote(projectResults: List<HProjectResult>, line: String) {

}


fun processHeader(line: String): List<HProjectResult> {
    var results: MutableList<HProjectResult> = arrayListOf()

    val splitHeader: List<String> = line.split(",")

    /*println("---- Debugging Header processing")
    splitHeader
            .take(5)
            .forEachIndexed { index, value ->
                println("column ${value} is at ${index}")
            }*/

    println("---- Process CSV positions")
    splitHeader
            .take(5) // debugging
            .drop(2) // we don't want the username & timestamp just yet
            .forEachIndexed { index, headerColumn ->
//                println("column name ${headerColumn} at position ${index + 2}")
                val projectResult: HProjectResult = HProjectResult(
                        position = index + 2,
                        project = processProjectFromColumnHeader(headerColumn)
                )
//                println("processed $projectResult")
                results.add(projectResult)
            }

    return results
}

fun processProjectFromColumnHeader(headerColumn: String): HProject {
    val projectCategory: String = headerColumn.split("[")[0].trim()
    val projectName: String = headerColumn
            ?.let {
                it.substring(it.indexOf("[") + 1, it.indexOf("]")).trim()
            }
    return HProject(projectCategory, projectName)
}

// ---------------------------------------------------------------------------------------------
// Data structures


/**
 * Hackathon project
 */
data class HProject(
        val category: String,
        val name: String
)

/**
 * Hackathon project result row
 */
data class HProjectResult(
        val position: Int, // position in CSV
        val project: HProject,
        var points: Int = 0,
        var voters: List<String> = arrayListOf<String>()
)
