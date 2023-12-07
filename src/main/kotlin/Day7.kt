import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val input = Files.readAllLines(Paths.get("src/main/resources/example.txt"))
    println(puzzle1(input))
    println(puzzle2(input))
}

private fun puzzle1(input: List<String>): Int {
    return solve(input, ::strengthOfHand, ::strengthOfCard)
}

private fun puzzle2(input: List<String>): Int {
    return solve(input, ::strengthOfHandWithJoker, ::strengthOfCardWithJoker)
}

private fun solve(input: List<String>,
                  handStrength: (Hand) -> Int,
                  cardStrength: (Char) -> Int): Int {
    val hands = parseHands(input)

    val sortedHands = hands.sortedWith(
            comparator = {
                hand1, hand2 ->
                val strength1 = handStrength(hand1)
                val strength2 = handStrength(hand2)

                if (strength1 == strength2) {
                    hand1.cards.forEachIndexed { index, card ->
                        val compared = cardStrength(card).compareTo(cardStrength(hand2.cards[index]))
                        if (compared != 0)
                            return@sortedWith compared
                    }
                }
                strength1.compareTo(strength2)
            }
    )

    return calculateTotalWins(sortedHands)
}

private fun calculateTotalWins(sortedHands: List<Hand>): Int =
    sortedHands.mapIndexed { index, hand ->
        val rank = index + 1
        rank * hand.bid
    }.sum()

private fun parseHands(input: List<String>) =
    input.map { it.split(" ") }
        .map { Hand(it[0], it[1].toInt()) }

private fun strengthOfHand(hand: Hand): Int =
    calculateStrength(hand, ::countCards)

private fun strengthOfHandWithJoker(hand: Hand): Int =
    calculateStrength(hand, ::countCardsWithJoker)

private fun calculateStrength(hand: Hand, cards: (Hand) -> Map<Char, Int>): Int {
    val chars = cards(hand)

    if (chars.keys.size == 1) {
        return 7 // five of a kind
    }

    if (chars.keys.size == 2) {
        if (chars.values.contains(4)) {
            return 6 // four of a kind
        }

        return 5 // full house because there must be 3 and 2 in the values
    }

    if (chars.keys.size == 3) {
        if (chars.values.contains(3)) {
            return 4 // three of a kind
        }
        return 3 // two paris because there must be 2, 2 and 1 in the values
    }

    if (chars.keys.size == 4) {
        return 2 // one pair
    }

    return 1 // high card
}

private fun countCards(hand: Hand): Map<Char, Int> =
    hand.cards
        .groupingBy { it }
        .eachCount()


private fun countCardsWithJoker(hand: Hand): Map<Char, Int> {
    val chars = countCards(hand).toMutableMap()

    if (chars.containsKey('J') && chars.keys.size > 1) {
        val sortedMap = chars.toList().sortedByDescending { it.second }.toMap()
        val highestChar = sortedMap.keys.first()

        if (highestChar == 'J') {
            // joker is highest
            val secondChar = sortedMap.keys.elementAt(1)
            chars[secondChar] = sortedMap[highestChar]!! + sortedMap[secondChar]!!
        } else {
            // joker is not the highest
            chars[highestChar] = sortedMap[highestChar]!! + sortedMap['J']!!
        }
        chars.remove('J')
    }

    return chars
}

private fun strengthOfCard(card: Char): Int =
    when (card) {
        'A' -> 14
        'K' -> 13
        'Q' -> 12
        'J' -> 11
        'T' -> 10
        else -> card.toString().toInt()
    }

private fun strengthOfCardWithJoker(card: Char): Int =
    when (card) {
        'A' -> 14
        'K' -> 13
        'Q' -> 12
        'J' -> 1 // joker is lowest in this case
        'T' -> 10
        else -> card.toString().toInt()
    }

private data class Hand(val cards: String, val bid: Int)
