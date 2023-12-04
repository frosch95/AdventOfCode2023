import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val input = Files.readAllLines(Paths.get("src/main/resources/day4.txt"))
    println(puzzle1(input))
    println(puzzle2(input))
}

private fun puzzle1(input: List<String>): Int {
    var pointsInTotal = 0
    val cards = cards(input)
    cards.forEach { card ->
        var points = 0
        card.winningNumbers.forEach { winningNumber ->
            if (card.myNumbers.contains(winningNumber)) points = if (points == 0) 1 else points * 2
        }
        pointsInTotal += points
    }
    return pointsInTotal
}

private fun puzzle2(input: List<String>): Int {
    val cards = cards(input)

    val cardsMap = cards.associate { it.number to CardCount(it, 1) }
    val maxCardNumber = cards.last().number

    for (cardIndex in 1..maxCardNumber) {
        val current = cardsMap[cardIndex]!!
        val countOfWins = current.card.winningNumbers.count { current.card.myNumbers.contains(it) }
        val endOfCardsToCopy = minOf(cardIndex + countOfWins, maxCardNumber)
        for (processCopies in 1 .. current.numberOfCopies) {
            for (addNewCopiesTo in cardIndex + 1..endOfCardsToCopy) {
                cardsMap[addNewCopiesTo]!!.numberOfCopies++
            }
        }
    }
    return cardsMap.values.sumOf { it.numberOfCopies }
}

private fun cards(input: List<String>): List<Card> {
    return input
            .map { Card(cardNumber(it), winningNumbers(it), myNumbers(it)) }
            .toList()
}

private fun cardNumber(line: String): Int {
    return line
            .split(":")[0]
            .substring(4)
            .trim()
            .toInt()
}

private fun winningNumbers(line: String): List<Int> {
    return line
            .split(":")[1]
            .split("|")[0].trim()
            .split(" ")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { number -> number.toInt() }
            .toList()
}

private fun myNumbers(line: String): List<Int> {
    return line
            .split(":")[1]
            .split("|")[1].trim()
            .split(" ")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
            .toList()
}

private data class Card(val number: Int, val winningNumbers: List<Int>, val myNumbers: List<Int>)
private data class CardCount(val card: Card, var numberOfCopies: Int)