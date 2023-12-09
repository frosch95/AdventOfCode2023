import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val input = Files.readAllLines(Paths.get("src/main/resources/day9.txt"))
    println(puzzle(input, ::findNextNumber))
    println(puzzle(input, :: findPreviousNumber))
}

private fun puzzle(input: List<String>, nextNumber: (List<Int>) -> Int): Int {

    val result = input.sumOf {
        val numbers = parse(it)
        nextNumber(numbers)
    }

    return result
}

private fun parse(line: String): List<Int> {
    return line.split(" ").map { it.toInt() }
}
private fun findNextNumber(numbers: List<Int>): Int {
    if (numbers.sum() == 0) return 0
    val newNumberList = generateNextNumberList(numbers)
    return numbers.last() + findNextNumber(newNumberList)
}

private fun findPreviousNumber(numbers: List<Int>): Int {
    if (numbers.sum() == 0) return 0
    val newNumberList = generateNextNumberList(numbers)
    return numbers.first() - findPreviousNumber(newNumberList)
}

private fun generateNextNumberList(numbers: List<Int>): List<Int> {
    val newNumberList = (0..<numbers.size - 1).map { i ->
        numbers[i + 1] - numbers[i]
    }
    return newNumberList
}