import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val input = Files.readAllLines(Paths.get("src/main/resources/day2.input.txt"))
    println(puzzle1(input))
    println(puzzle2(input))
}

private fun puzzle1(input: List<String>): Int {
    val cubes = mapOf(
        "red" to 12,
        "green" to 13,
        "blue" to 14
    )

    var sum = 0
    input.forEach { line ->
        val lineSplit = line.split(':')
        val gameInfo = lineSplit[0]
        val cubeInfo = lineSplit[1]

        val gameNumber = gameInfo.substring(5).toInt()
        val reveals = cubeInfo.split(';')
        val cubeMap = mapOfMaximumColorCubes(reveals)

        var matches = true
        for (cube in cubes) {
            if (cubeMap.containsKey(cube.key) && cubeMap[cube.key]!! > cube.value) {
                matches = false
                break
            }
        }

        if (matches)
            sum += gameNumber
    }
    return sum
}

private fun puzzle2(input: List<String>): Int {
    var sum = 0
    input.forEach { line ->
        val lineSplit = line.split(':')
        val cubeInfo = lineSplit[1]

        val reveals = cubeInfo.split(';')
        val cubeMap = mapOfMaximumColorCubes(reveals)

        var power = 1
        cubeMap.forEach {
            power *= it.value
        }
        sum += power
    }
    return sum
}

private fun mapOfMaximumColorCubes(reveals: List<String>): MutableMap<String, Int> {
    val cubeMap = mutableMapOf(
        "red" to 0,
        "green" to 0,
        "blue" to 0
    )

    reveals.forEach { reveal ->
        val cubeInfo = reveal.split(',')

        cubeInfo.forEach {
            val cube = it.trim().split(' ')
            val cubeNumber = cube[0].toInt()
            val cubeColor = cube[1]
            if (cubeMap[cubeColor]!! < cubeNumber)
                cubeMap[cubeColor] = cubeNumber
        }
    }
    return cubeMap
}
