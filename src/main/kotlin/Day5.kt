import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val input = Files.readAllLines(Paths.get("src/main/resources/example.txt"))
    println(puzzle1(input))
    println(puzzle2(input))
}

private val mapOfMaps = mutableMapOf<String, MutableMap<Int, Int>>()

private fun puzzle1(input: List<String>): Int {
    seeds(input)
    return -1
}

private fun puzzle2(input: List<String>): Int {
    return -1
}

private fun seeds(input: List<String>): List<Seed> {
    createMaps(input)

    input.forEach { line ->
        if (line.startsWith("seeds:")) {
            val seedLine = line.split(" ")
            (0..<seedLine.size).forEach {
                println("$it: ${seedLine[it]}")




            }

            //val seed = Seed(seedNumber, seedSoil, seedFertilizer, seedWater, seedLight, seedTemperature, seedHumidity, seedLocation)

           // println(seed)
        }
    }

    return listOf()
}

private fun createMaps(input: List<String>) {

    var mapSectionStart = false
    var currentMapName = ""

    input.forEach { line ->
        if (line.endsWith("map:")) {
            mapSectionStart = true
            currentMapName = line.split(" ")[0]
            mapOfMaps[currentMapName] = mutableMapOf()
        }
        else {
            if (mapSectionStart) {
                if (line.trim().isEmpty()) {
                    mapSectionStart = false
                } else {
                    val mapLine = line.split(" ")
                    val destinationRangeStart = mapLine[0].toInt()
                    val sourceRangeStart = mapLine[1].toInt()
                    val rangeLength = mapLine[2].toInt()

                    for (i in 0..rangeLength) {
                        val mapKey = destinationRangeStart + i
                        val mapValue = sourceRangeStart + i
                        mapOfMaps[currentMapName]!![mapKey] = mapValue
                    }
                }
            }
        }
    }
}



private data class Seed(val number: Int, val soil: Int, val fertilizer: Int, val water: Int, val light: Int, val temperature: Int, val humidity: Int, val location: Int)