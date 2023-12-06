import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val input = Files.readAllLines(Paths.get("src/main/resources/day6.txt"))
    println(puzzle1(input))
    println(puzzle2(input))
}

private fun puzzle1(input: List<String>): Long {
    val times = input[0]
        .substring("Time:".length)
        .split(' ')
        .filter { it.isNotBlank() }
        .map { it.toLong() }
    
   
    val distances = input[1]
        .substring("Distance:".length)
        .split(' ')
        .filter { it.isNotBlank() }
        .map { it.toLong() }
    
    return marginOfError(times, distances)
}

private fun puzzle2(input: List<String>): Long {
    val times = input[0]
        .substring("Time:".length)
        .replace(" ", "")
        .toLong()



    val distances = input[1]
        .substring("Distance:".length)
        .replace(" ", "")
        .toLong()
    return marginOfError(listOf(times), listOf(distances))
}

private fun marginOfError(times: List<Long>, distances: List<Long>): Long {
    val marginOfError = times.foldIndexed(1) { index, marginOfError: Long, time ->
        val winningRaces = (0 .. time).count {
            movedDistance(it, time) > distances[index]    
        }
        marginOfError * winningRaces
    }

    return marginOfError
}

private fun movedDistance(pressedButtonTime: Long, time: Long): Long {
    val speed = pressedButtonTime
    val distance = (time - pressedButtonTime) * speed
    return distance
}
