import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val input = Files.readAllLines(Paths.get("src/main/resources/day5.txt"))
    println(puzzle1(input))
    println(puzzle2(input))
}

private fun puzzle1(input: List<String>): Long {
    val seeds = input
        .asSequence()
        .filter { it.startsWith("seeds:") }
        .map { it.split(" ") }
        .flatten()
        .filterNot { it == "seeds:" }
        .map { it.toLong() }
        .toList()

    val mappings = createMappings(input)

    return findMinLocation(seeds, mappings)
}

private fun puzzle2(input: List<String>): Long {
    val s = input
        .asSequence()
        .filter { it.startsWith("seeds:") }
        .map { it.split(" ") }
        .flatten()
        .filterNot { it == "seeds:" }
        .map { it.toLong() }
        .toList()
    val seedRanges = s.chunked(2).map { it[0]..<it[0] + it[1] }

    val mappings = createMappings(input)

    var minLocation = Long.MAX_VALUE

    seedRanges.forEach { seeds ->
        minLocation = minOf(minLocation, findMinLocation(seeds, mappings))
    }

    return minLocation
}

private fun findMinLocation(iterable: Iterable<Long>, mappings: List<Mapping>): Long {
    var minLocation = Long.MAX_VALUE
    iterable.forEach { seed ->
        val soil = mappings[0].mapSourceToDestination(seed)
        val fertilizer = mappings[1].mapSourceToDestination(soil)
        val water = mappings[2].mapSourceToDestination(fertilizer)
        val light = mappings[3].mapSourceToDestination(water)
        val temperature = mappings[4].mapSourceToDestination(light)
        val humidity = mappings[5].mapSourceToDestination(temperature)
        val location = mappings[6].mapSourceToDestination(humidity)
        minLocation = minOf(minLocation, location)
    }
    return minLocation
}



private fun createMappings(input: List<String>): List<Mapping> {

    val mappings = mutableListOf<Mapping>()
    var currentMapName = ""

    input.forEach { line ->
        if (line.endsWith("map:")) {
            currentMapName = line.split(" ")[0]
        }
        else {
            if (line.trim().isNotEmpty() && !line.startsWith("seeds:")) {
                val mapLine = line.split(" ")
                val destinationRangeStart = mapLine[0].toLong()
                val sourceRangeStart = mapLine[1].toLong()
                val rangeLength = mapLine[2].toInt()

                if (mappings.isEmpty() || mappings.last().name != currentMapName)
                    mappings.add(Mapping(currentMapName, mutableListOf()))

                mappings.last().ranges.add(Ranges(destinationRangeStart , sourceRangeStart, rangeLength))
            }
        }
    }
    return mappings
}

private data class Mapping(val name: String, val ranges: MutableList<Ranges>) {

    fun mapSourceToDestination(number: Long): Long {
        ranges.forEach {
            if (number >= it.sourceRange && number < it.sourceRange + it.length)
                return it.destinationRange + number - it.sourceRange
        }
        return number
    }
}

private data class Ranges(val destinationRange: Long, val sourceRange: Long, val length: Int)

