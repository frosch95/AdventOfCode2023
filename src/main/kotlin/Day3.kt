import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val input = Files.readAllLines(Paths.get("src/main/resources/day3.input.txt"))
    println(puzzle1(input))
    println(puzzle2(input))
}

private fun puzzle1(input: List<String>): Int {
    val symbols = findSymbols(input)
    val numbers = findNumbers(input)
    return numbers.sumOf { if (it.isAdjacentToAnySymbol(symbols)) it.number else 0 }
}

private fun puzzle2(input: List<String>): Int {
    val symbols = findSymbols(input)
    val numbers = findNumbers(input)

    val gearRatio = symbols
        .filter { it.isStar() }
        .sumOf { symbol ->
            val foundNumbers = numbers
                .filter { it.isAdjacentTo(symbol) }
                .map { it.number }
                .toList()

            // is a gear?
            if (foundNumbers.size == 2) foundNumbers[0] * foundNumbers[1] else 0
        }

    return gearRatio
}

private class Number(val number: Int, val position: Position) {
    fun isAdjacentToAnySymbol(listOfSymbols: List<Symbol>): Boolean {
        val length = "$number".length
        for (i in 0..<length) {
            val positionOfDigit = Position(position.row, position.column + i)
            for (symbolPosition in listOfSymbols) {
                if (positionOfDigit.isAdjacentTo(symbolPosition.position))
                    return true
            }
        }
        return false
    }

    fun isAdjacentTo(symbol: Symbol): Boolean {
        val length = "$number".length
        for (i in 0..<length) {
            val positionOfDigit = Position(position.row, position.column + i)
            if (positionOfDigit.isAdjacentTo(symbol.position))
                return true
        }
        return false
    }

    override fun toString(): String {
        return "$number at $position"
    }
}



private class Symbol(val symbol: Char, val position: Position) {

    fun isStar(): Boolean {
        return symbol == '*'
    }
    override fun toString(): String {
        return "$symbol at $position"
    }
}
private class Position(val row: Int, val column: Int) {

    fun isAdjacentTo(other: Position): Boolean {
        val left = row == other.row && column == other.column - 1
        val right = row == other.row && column == other.column + 1
        val above = row == other.row - 1 && column == other.column
        val below = row == other.row + 1 && column == other.column
        val aboveLeft = row == other.row - 1 && column == other.column - 1
        val aboveRight = row == other.row - 1 && column == other.column + 1
        val belowLeft = row == other.row + 1 && column == other.column - 1
        val belowRight = row == other.row + 1 && column == other.column + 1
        return left || right || above || below || aboveLeft || aboveRight || belowLeft || belowRight
    }

    override fun toString(): String {
        return "($row, $column)"
    }
}

private fun findSymbols(input: List<String>):List<Symbol> {
    val symbols = mutableListOf<Symbol>()
    var row = 0
    input.forEach{ line ->
        var column = 0
        line.forEach {
            if (!it.isLetterOrDigit() && it != '.')
                symbols.add(Symbol(it, Position(row, column)))
            column++
        }
        row++
    }
    return symbols
}

private fun findNumbers(input: List<String>): List<Number> {
    val numbers = mutableListOf<Number>()
    var row = 0

    input.forEach{ line ->
        var column = 0
        var partOfNumber = false
        var position = Position(0, 0)
        var number = ""

        line.forEach {
            if (it.isDigit() && !partOfNumber)
            {
                // start of a number
                partOfNumber = true
                number = "$it"
                position = Position(row, column)
            }
            else if (!it.isDigit() && partOfNumber)
            {
                // end of a number
                numbers.add(Number(number.toInt(), position))
                partOfNumber = false
            }
            else if (partOfNumber) {
                // part of a number
                number += it
            }
            column++
        }

        // number add the end of the line
        if (partOfNumber)
            numbers.add(Number(number.toInt(), position))

        row++
    }
    return numbers
}