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

    var projectCsvColumn: Map<Int, HProjectVoteResult> = emptyMap()

    val lines: Sequence<String> = resolveArgFile(args)
    lines.forEachIndexed { index, line ->
        if (index == 0) {
            projectCsvColumn = processHeader(line)
//            println("------ Project CSV Column parsed - Before Voting $projectCsvColumn")
        } else {
            processVote(projectCsvColumn, line)
        }
    }

    printResults(projectCsvColumn)

//            println("------  Project CSV Column parsed - After Voting $projectCsvColumn")
}


// run this program like so `kotlinc -script list_folders.kts ~/code`
println("******* PROGRAM START ***************** ")
program(args)
println("******* PROGRAM END ***************** ")


// ---------------------------------------------------------------------------------------------
// Internal Helpers

fun printResults(projectCsvColumn: Map<Int, HProjectVoteResult>) {

    val c: Comparator<HProjectVoteResult> = compareBy<HProjectVoteResult> { it.project.category }
    val ct = c.thenByDescending { it.points }

    val orderedResults = projectCsvColumn.values
            .filter { it.points > 0 }
            .sortedWith(ct)

    orderedResults.forEach { println(it) }
}


fun processVote(projectVoteResults: Map<Int, HProjectVoteResult>, vote: String) {

    val splitVote: List<String> = vote.split(",")
    splitVote
            .drop(2) // not doing anything with the date
            .forEachIndexed loop@{ index, voteResultColumn ->

                val voteResultWithoutQuote = voteResultColumn.removeSurrounding("\"")
                if (voteResultWithoutQuote.isBlank()) return@loop

                val position = index + 2 // we dropped 2 indices

                val projectVoteResult: HProjectVoteResult = projectVoteResults[position]
                        ?: return@loop
                val project: HProject = projectVoteResult.project
                val voteResult: Int = voteResultWithoutQuote.toInt()
                val voter: String = splitVote[1].removeSurrounding("\"")

                projectVoteResult.points += voteResult.voteWeight()
                projectVoteResult.voters.add(voter)

//                println("project '${project.name}' was voted '$voteResult' [${voteResult.voteWeight()}] in category '${project.category}' by '$voter'")
            }

    // update projectVoteResults
}


fun processHeader(line: String): Map<Int, HProjectVoteResult> {
    var results: MutableMap<Int, HProjectVoteResult> = mutableMapOf()

    val splitHeader: List<String> = line.split(",")

    /*
    println("---- Debugging Header processing")
    splitHeader
            .take(5)
            .forEachIndexed { index, value ->
                println("column ${value} is at ${index}")
            }
    */

//    println("---- Process CSV positions")
    splitHeader
            .drop(2) // we don't want the username & timestamp just yet
            .forEachIndexed { index, headerColumn ->
                //                println("column name ${headerColumn} at position ${index + 2}")
                results[index + 2] = HProjectVoteResult(processProjectFromColumnHeader(headerColumn))
//                println("processed $projectResult")
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

fun Int.voteWeight(): Int {
    return when (this) {
        1 -> 6
        2 -> 3
        3 -> 2
        else -> throw RuntimeException("there can be only 3")
    }
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
data class HProjectVoteResult(
//        val position: Int, // position in CSV
        val project: HProject,
        var points: Int = 0,
        var voters: MutableList<String> = arrayListOf<String>()
) {
    override fun toString(): String {
        return "[${project.category}] ${project.name} ----> ${points}"
    }
}


