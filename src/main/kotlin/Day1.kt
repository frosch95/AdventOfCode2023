import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val input = Files.readAllLines(Paths.get("src/main/resources/day1.input.txt"))
    println(puzzle1(input))
    println(puzzle2(input))
}

private fun puzzle1(input: List<String>): Int {
    var sum = 0
    input.forEach { line ->
        val firstDigit = line.first { c -> c.isDigit() }.digitToInt()
        val lastDigit = line.last { c -> c.isDigit() }.digitToInt()
        sum += firstDigit * 10 + lastDigit
    }
    return sum
}

val stringToDigit = mapOf(
    "0" to 0,
    "1" to 1,
    "2" to 2,
    "3" to 3,
    "4" to 4,
    "5" to 5,
    "6" to 6,
    "7" to 7,
    "8" to 8,
    "9" to 9,
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9
)

private fun puzzle2(input: List<String>): Int {
    var sum = 0
    input.forEach { line ->
        sum += addFirstDigit(line)
        sum += addSecondDigit(line)
    }
    return sum
}

private fun addFirstDigit(line: String): Int {
    var foundIndex = -1
    var foundNumber = 0
    stringToDigit.forEach {
        val index = line.indexOf(it.key)
        if (index != -1) {
            if (foundIndex == -1 || index < foundIndex) {
                foundIndex = index
                foundNumber = it.value
            }
        }
    }
    return foundNumber * 10
}

private fun addSecondDigit(line: String): Int {
    var foundIndex = -1
    var foundNumber = 0
    stringToDigit.forEach {
        val index = line.lastIndexOf(it.key)
        if (index != -1) {
            if (foundIndex == -1 || index > foundIndex) {
                foundIndex = index
                foundNumber = it.value
            }
        }
    }
    return foundNumber
}

