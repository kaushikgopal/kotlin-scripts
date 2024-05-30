#!/usr/bin/env kotlin

@file:DependsOn("com.squareup.okio:okio:3.3.0")

import okio.FileSystem
import okio.IOException
import okio.Path
import okio.Path.Companion.toPath
import java.io.File

/*
 * ---------------- Carrot Wars 2018 -----------------------------------
 * This script was used in Instacart's 2018 Summer Hackathon
 *  it tabulates the results from a CSV file populated by Google Forms
 *  and outputs the results of votes
 *
 * See the [accompanying blog post](https://tech.instacart.com/free-hackathon-vote-tabulation-using-google-forms-kotlin-3c7b7080ea) for more details.
 *
 * run this script like so:

    $>  brew install kotlin
    $>  kotlin 7.carrot-wars-tabulate.main.kts star-wars-demo-results.csv
 */


println("******* PROGRAM START ***************** ")
program(args)
println("******* PROGRAM END ***************** ")

fun program(args: Array<String>) {
  if (checkValidCsvFile(args)) return

  var voterCount: Int = 0
  var projectCsv: Map<Int, HProjectVoteResult> = emptyMap()
  var projectCsvWithResults: Map<Int, HProjectVoteResult> = emptyMap()

  val filePath = args[0].toPath()

  processLinesFromFile(filePath) { line, index ->
    line ?: return@processLinesFromFile

    if (index == 0) {
      projectCsv = processHeaderAndBuildMap(line)
    } else {
      projectCsvWithResults = processVote(projectCsv, line)
      voterCount += 1
    }
  }

  println("$voterCount folk(s) voted!")

  printResults(projectCsvWithResults)
}
// ---------------------------------------------------------------------------------------------
// Internal Helpers

fun printResults(projectCsvColumn: Map<Int, HProjectVoteResult>) {

  val categoryComparator: Comparator<HProjectVoteResult> =
      compareBy<HProjectVoteResult> { it.project.category }
  val categoryAndMaxPointsComparator: Comparator<HProjectVoteResult> =
      categoryComparator.thenByDescending { it.points }

  val orderedResults = projectCsvColumn.values
      .filter { it.points > 0 }
      .sortedWith(categoryAndMaxPointsComparator)

  orderedResults
      .groupBy { it.project.category }
      .forEach { map: Map.Entry<String, List<HProjectVoteResult>> ->
        println("-----------------------\nCategory: ${map.key}\n----")
        map.value.forEach { println(it) }
      }
}


/**
 * A typical row response from Google Forms looks like this:
 *
 *  (HEADER) "Timestamp","Username","Best Team Collaboration [1. Project 1]","Best Team Collaboration [2. Project 2]","Best Team Collaboration [3. Project 3]","Best Team Collaboration [4. Project 4]","Best Team Collaboration [5. Project 5]","Best Team Collaboration [6. Project 6]","Best Team Collaboration [7. Project 7]","Best Team Collaboration [8. Project 8]","Best Team Collaboration [9. Project 9]","Best Team Collaboration [10. Project 10]","Best Team Collaboration [11. Project 11]","Best Team Collaboration [12. Project 12]","Best Team Collaboration [13. Project 13]","Best Team Collaboration [14. Project 14]","Best Team Collaboration [15. Project 15]","Best Team Collaboration [16. Project 16]","Best Team Collaboration [17. Project 17]","Best Team Collaboration [18. Project 18]","Best Team Collaboration [19. Project 19]","Best Team Collaboration [20. Project 20]","Best Team Collaboration [21. Project 21]","Best Team Collaboration [22. Project 22]","Best Team Collaboration [23. Project 23]","Best Team Collaboration [24. Project 24]","Best Team Collaboration [25. Project 25]","Fixed most annoying longstanding issue [1. Project 1]","Fixed most annoying longstanding issue [2. Project 2]","Fixed most annoying longstanding issue [3. Project 3]","Fixed most annoying longstanding issue [4. Project 4]","Fixed most annoying longstanding issue [5. Project 5]","Fixed most annoying longstanding issue [6. Project 6]","Fixed most annoying longstanding issue [7. Project 7]","Fixed most annoying longstanding issue [8. Project 8]","Fixed most annoying longstanding issue [9. Project 9]","Fixed most annoying longstanding issue [10. Project 10]","Fixed most annoying longstanding issue [11. Project 11]","Fixed most annoying longstanding issue [12. Project 12]","Fixed most annoying longstanding issue [13. Project 13]","Fixed most annoying longstanding issue [14. Project 14]","Fixed most annoying longstanding issue [15. Project 15]","Fixed most annoying longstanding issue [16. Project 16]","Fixed most annoying longstanding issue [17. Project 17]","Fixed most annoying longstanding issue [18. Project 18]","Fixed most annoying longstanding issue [19. Project 19]","Fixed most annoying longstanding issue [20. Project 20]","Fixed most annoying longstanding issue [21. Project 21]","Fixed most annoying longstanding issue [22. Project 22]","Fixed most annoying longstanding issue [23. Project 23]","Fixed most annoying longstanding issue [24. Project 24]","Fixed most annoying longstanding issue [25. Project 25]","Greatest business impact [1. Project 1]","Greatest business impact [2. Project 2]","Greatest business impact [3. Project 3]","Greatest business impact [4. Project 4]","Greatest business impact [5. Project 5]","Greatest business impact [6. Project 6]","Greatest business impact [7. Project 7]","Greatest business impact [8. Project 8]","Greatest business impact [9. Project 9]","Greatest business impact [10. Project 10]","Greatest business impact [11. Project 11]","Greatest business impact [12. Project 12]","Greatest business impact [13. Project 13]","Greatest business impact [14. Project 14]","Greatest business impact [15. Project 15]","Greatest business impact [16. Project 16]","Greatest business impact [17. Project 17]","Greatest business impact [18. Project 18]","Greatest business impact [19. Project 19]","Greatest business impact [20. Project 20]","Greatest business impact [21. Project 21]","Greatest business impact [22. Project 22]","Greatest business impact [23. Project 23]","Greatest business impact [24. Project 24]","Greatest business impact [25. Project 25]","Saves the most time [1. Project 1]","Saves the most time [2. Project 2]","Saves the most time [3. Project 3]","Saves the most time [4. Project 4]","Saves the most time [5. Project 5]","Saves the most time [6. Project 6]","Saves the most time [7. Project 7]","Saves the most time [8. Project 8]","Saves the most time [9. Project 9]","Saves the most time [10. Project 10]","Saves the most time [11. Project 11]","Saves the most time [12. Project 12]","Saves the most time [13. Project 13]","Saves the most time [14. Project 14]","Saves the most time [15. Project 15]","Saves the most time [16. Project 16]","Saves the most time [17. Project 17]","Saves the most time [18. Project 18]","Saves the most time [19. Project 19]","Saves the most time [20. Project 20]","Saves the most time [21. Project 21]","Saves the most time [22. Project 22]","Saves the most time [23. Project 23]","Saves the most time [24. Project 24]","Saves the most time [25. Project 25]","Most innovative solution [1. Project 1]","Most innovative solution [2. Project 2]","Most innovative solution [3. Project 3]","Most innovative solution [4. Project 4]","Most innovative solution [5. Project 5]","Most innovative solution [6. Project 6]","Most innovative solution [7. Project 7]","Most innovative solution [8. Project 8]","Most innovative solution [9. Project 9]","Most innovative solution [10. Project 10]","Most innovative solution [11. Project 11]","Most innovative solution [12. Project 12]","Most innovative solution [13. Project 13]","Most innovative solution [14. Project 14]","Most innovative solution [15. Project 15]","Most innovative solution [16. Project 16]","Most innovative solution [17. Project 17]","Most innovative solution [18. Project 18]","Most innovative solution [19. Project 19]","Most innovative solution [20. Project 20]","Most innovative solution [21. Project 21]","Most innovative solution [22. Project 22]","Most innovative solution [23. Project 23]","Most innovative solution [24. Project 24]","Most innovative solution [25. Project 25]"
 *  (ROW 1) "2018/06/28 10:29:47 AM MDT","hackathon@hotmail.com","3","","","","","","","","2","","","","1","","","","","","","","","","","","","","","","","","","","","","","","","","","","","2","","1","","3","","","","","","","","","","1","","2","","","","","","","","3","","","","","","","","","","3","","","","","","","","","2","","1","","","","","","","","","","","","","","1","2","","","","","3","","","","","","","","","","","","","","","","","",""
 *
 *   We go through each row
 *   From the map position, we query the project info
 *   We update the points standing based on the vote value (weighted)
 *
 *   return a new map that has project info + summary of all points
 */
fun processVote(
  tableOfResults: Map<Int, HProjectVoteResult>, vote: String
): Map<Int, HProjectVoteResult> {

  val projectVoteResults: Map<Int, HProjectVoteResult> = tableOfResults
  val splitVote: List<String> = vote.split(",")
  splitVote
      .drop(2) // not doing anything with the date
      .forEachIndexed loop@{ index, voteResultColumn ->

        val voteResultWithoutQuote = voteResultColumn.removeSurrounding("\"")
        if (voteResultWithoutQuote.isBlank()) return@loop
        val voteResult: Int = voteResultWithoutQuote.toInt()

        val position = index + 2 // we dropped 2 indices

        val projectVoteResult: HProjectVoteResult = projectVoteResults[position]
          ?: return@loop

        val project: HProject = projectVoteResult.project
        val voter: String = splitVote[1].removeSurrounding("\"")

        projectVoteResult.points += voteResult.voteWeight()

        projectVoteResult.voters.add(voter)
        projectVoteResult.votes.add(voteResult)
        // println("project '${project.name}' was voted '$voteResult' [${voteResult.voteWeight()}] in category '${project.category}' by '$voter'")
      }

  return projectVoteResults
}

/**
 * Process the header alone and initialize with 0 points everywhere
 * Google forms generates all combinations of entries
 * and creates a header column for all
 *
 * A typical header form the Google Forms response looks like this:
 *
 *  (HEADER) "Timestamp","Username","Best Team Collaboration [1. Project 1]","Best Team Collaboration [2. Project 2]","Best Team Collaboration [3. Project 3]","Best Team Collaboration [4. Project 4]","Best Team Collaboration [5. Project 5]","Best Team Collaboration [6. Project 6]","Best Team Collaboration [7. Project 7]","Best Team Collaboration [8. Project 8]","Best Team Collaboration [9. Project 9]","Best Team Collaboration [10. Project 10]","Best Team Collaboration [11. Project 11]","Best Team Collaboration [12. Project 12]","Best Team Collaboration [13. Project 13]","Best Team Collaboration [14. Project 14]","Best Team Collaboration [15. Project 15]","Best Team Collaboration [16. Project 16]","Best Team Collaboration [17. Project 17]","Best Team Collaboration [18. Project 18]","Best Team Collaboration [19. Project 19]","Best Team Collaboration [20. Project 20]","Best Team Collaboration [21. Project 21]","Best Team Collaboration [22. Project 22]","Best Team Collaboration [23. Project 23]","Best Team Collaboration [24. Project 24]","Best Team Collaboration [25. Project 25]","Fixed most annoying longstanding issue [1. Project 1]","Fixed most annoying longstanding issue [2. Project 2]","Fixed most annoying longstanding issue [3. Project 3]","Fixed most annoying longstanding issue [4. Project 4]","Fixed most annoying longstanding issue [5. Project 5]","Fixed most annoying longstanding issue [6. Project 6]","Fixed most annoying longstanding issue [7. Project 7]","Fixed most annoying longstanding issue [8. Project 8]","Fixed most annoying longstanding issue [9. Project 9]","Fixed most annoying longstanding issue [10. Project 10]","Fixed most annoying longstanding issue [11. Project 11]","Fixed most annoying longstanding issue [12. Project 12]","Fixed most annoying longstanding issue [13. Project 13]","Fixed most annoying longstanding issue [14. Project 14]","Fixed most annoying longstanding issue [15. Project 15]","Fixed most annoying longstanding issue [16. Project 16]","Fixed most annoying longstanding issue [17. Project 17]","Fixed most annoying longstanding issue [18. Project 18]","Fixed most annoying longstanding issue [19. Project 19]","Fixed most annoying longstanding issue [20. Project 20]","Fixed most annoying longstanding issue [21. Project 21]","Fixed most annoying longstanding issue [22. Project 22]","Fixed most annoying longstanding issue [23. Project 23]","Fixed most annoying longstanding issue [24. Project 24]","Fixed most annoying longstanding issue [25. Project 25]","Greatest business impact [1. Project 1]","Greatest business impact [2. Project 2]","Greatest business impact [3. Project 3]","Greatest business impact [4. Project 4]","Greatest business impact [5. Project 5]","Greatest business impact [6. Project 6]","Greatest business impact [7. Project 7]","Greatest business impact [8. Project 8]","Greatest business impact [9. Project 9]","Greatest business impact [10. Project 10]","Greatest business impact [11. Project 11]","Greatest business impact [12. Project 12]","Greatest business impact [13. Project 13]","Greatest business impact [14. Project 14]","Greatest business impact [15. Project 15]","Greatest business impact [16. Project 16]","Greatest business impact [17. Project 17]","Greatest business impact [18. Project 18]","Greatest business impact [19. Project 19]","Greatest business impact [20. Project 20]","Greatest business impact [21. Project 21]","Greatest business impact [22. Project 22]","Greatest business impact [23. Project 23]","Greatest business impact [24. Project 24]","Greatest business impact [25. Project 25]","Saves the most time [1. Project 1]","Saves the most time [2. Project 2]","Saves the most time [3. Project 3]","Saves the most time [4. Project 4]","Saves the most time [5. Project 5]","Saves the most time [6. Project 6]","Saves the most time [7. Project 7]","Saves the most time [8. Project 8]","Saves the most time [9. Project 9]","Saves the most time [10. Project 10]","Saves the most time [11. Project 11]","Saves the most time [12. Project 12]","Saves the most time [13. Project 13]","Saves the most time [14. Project 14]","Saves the most time [15. Project 15]","Saves the most time [16. Project 16]","Saves the most time [17. Project 17]","Saves the most time [18. Project 18]","Saves the most time [19. Project 19]","Saves the most time [20. Project 20]","Saves the most time [21. Project 21]","Saves the most time [22. Project 22]","Saves the most time [23. Project 23]","Saves the most time [24. Project 24]","Saves the most time [25. Project 25]","Most innovative solution [1. Project 1]","Most innovative solution [2. Project 2]","Most innovative solution [3. Project 3]","Most innovative solution [4. Project 4]","Most innovative solution [5. Project 5]","Most innovative solution [6. Project 6]","Most innovative solution [7. Project 7]","Most innovative solution [8. Project 8]","Most innovative solution [9. Project 9]","Most innovative solution [10. Project 10]","Most innovative solution [11. Project 11]","Most innovative solution [12. Project 12]","Most innovative solution [13. Project 13]","Most innovative solution [14. Project 14]","Most innovative solution [15. Project 15]","Most innovative solution [16. Project 16]","Most innovative solution [17. Project 17]","Most innovative solution [18. Project 18]","Most innovative solution [19. Project 19]","Most innovative solution [20. Project 20]","Most innovative solution [21. Project 21]","Most innovative solution [22. Project 22]","Most innovative solution [23. Project 23]","Most innovative solution [24. Project 24]","Most innovative solution [25. Project 25]"
 *  (ROW 1) "2018/06/28 10:29:47 AM MDT","hackathon@hotmail.com","3","","","","","","","","2","","","","1","","","","","","","","","","","","","","","","","","","","","","","","","","","","","2","","1","","3","","","","","","","","","","1","","2","","","","","","","","3","","","","","","","","","","3","","","","","","","","","2","","1","","","","","","","","","","","","","","1","2","","","","","3","","","","","","","","","","","","","","","","","",""
 *
 * We try to parse the header and build an empty map with the
 *          position as the map's key -> when going through each row column, we'd know which project the vote was for
 *          value -> project vote result (basically project + category + current points standing)
 */
fun processHeaderAndBuildMap(line: String): Map<Int, HProjectVoteResult> {
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

  splitHeader
      .drop(2) // we don't want the username & timestamp just yet; so ignore columns
      .forEachIndexed { index, headerColumn ->
        // println("column name ${headerColumn} at position ${index + 2}")
        results[index + 2] = HProjectVoteResult(processProjectFromColumnHeader(headerColumn))
        // println("processed $projectResult")
      }

  return results
}

fun processProjectFromColumnHeader(headerColumn: String): HProject {
  val projectCategory: String = headerColumn.split("[")[0].trim()
  val projectName: String = headerColumn.let {
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
  val project: HProject,
  var points: Int = 0,
  var voters: MutableList<String> = arrayListOf<String>(),
  var votes: MutableList<Int> = arrayListOf<Int>()
) {
  override fun toString(): String {
    return "${project.name.padEnd(50)} ----> ${points}    <- ${votes}"
  }
}

fun checkValidCsvFile(args: Array<String>): Boolean {
  if (args.isEmpty()) {
    println("⚠️  We need a csv file to proceed!")
    return true
  }

  // make sure one argument provided and it is a csv file
  val file = File(args[0])

  if (!file.exists()) {
    println("⚠️  File ${file.absolutePath} does not exist!")
    return true
  }

  if (!file.isFile) {
    println("⚠️  File ${file.absolutePath} is not a file!")
    return true
  }
  if (!file.canRead()) {
    println("⚠️  File ${file.absolutePath} is not readable!")
    return true
  }
  if (!file.extension.equals("csv", ignoreCase = true)) {
    println("⚠️  File ${file.absolutePath} is not a CSV file!")
    return true
  }
  return false
}

@Throws(IOException::class)
fun processLinesFromFile(path: Path, lineAction: (String?, Int) -> Unit) {
  FileSystem.SYSTEM.read(path) {
    var lineCount = 0
    while (true) {
      val line = readUtf8Line() ?: break
      lineAction.invoke(line, lineCount)
      lineCount += 1
    }
  }
}
