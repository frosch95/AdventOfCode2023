import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val input = Files.readAllLines(Paths.get("src/main/resources/day7.txt"))
    println(puzzle1(input))
    println(puzzle2(input))
}

private fun puzzle1(input: List<String>): Int {
    val hands = parseHands(input)

    val sortedHands = hands.sortedWith(
        comparator = {
            hand1, hand2 ->
            val strength1 = strengthOfHand(hand1)
            val strength2 = strengthOfHand(hand2)
            if (strength1 == strength2) {
                hand1.cards.forEachIndexed { index, card ->
                    if (strengthOfCard(card) > strengthOfCard(hand2.cards[index])) {
                        return@sortedWith 1
                    } else if (strengthOfCard(card) < strengthOfCard(hand2.cards[index])) {
                        return@sortedWith -1
                    }
                }
            }
            strength1.compareTo(strength2)
        }
    )

    val wins = sortedHands.mapIndexed { index, hand ->
        val rank = index + 1
        rank * hand.bid
    }.sum()

    return wins
}

private fun puzzle2(input: List<String>): Int {
    val hands = parseHands(input)

    val sortedHands = hands.sortedWith(
            comparator = {
                hand1, hand2 ->
                val strength1 = strengthOfHandWithJoker(hand1)
                val strength2 = strengthOfHandWithJoker(hand2)
                if (strength1 == strength2) {
                    hand1.cards.forEachIndexed { index, card ->
                        if (strengthOfCardWithJoker(card) > strengthOfCardWithJoker(hand2.cards[index])) {
                            return@sortedWith 1
                        } else if (strengthOfCardWithJoker(card) < strengthOfCardWithJoker(hand2.cards[index])) {
                            return@sortedWith -1
                        }
                    }
                }
                strength1.compareTo(strength2)
            }
    )

    val wins = sortedHands.mapIndexed { index, hand ->
        val rank = index + 1
        rank * hand.bid
    }.sum()

    return wins
}

private fun parseHands(input: List<String>) = input.map { it.split(" ") }
        .map { Hand(it[0], it[1].toInt()) }

private fun strengthOfHand(hand: Hand): Int {
    val chars = countCards(hand)
    return calculateStrength(chars)
}

private fun strengthOfHandWithJoker(hand: Hand): Int {
    val chars = countCardsWithJoker(hand)
    return calculateStrength(chars)
}

private fun calculateStrength(chars: MutableMap<Char, Int>): Int {
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

private fun countCards(hand: Hand): MutableMap<Char, Int> {
    val chars = mutableMapOf<Char, Int>()
    hand.cards.forEach {
        if (chars.containsKey(it)) {
            chars[it] = chars[it]!! + 1
        } else {
            chars[it] = 1
        }
    }
    return chars
}

private fun countCardsWithJoker(hand: Hand): MutableMap<Char, Int> {

    // if there is no joker, just count the cards
    if (!hand.cards.contains('J'))
        return countCards(hand)

    val chars = countCards(hand)

    if (chars.keys.size == 1) {
        return chars // five of a kind
    }

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

    return chars
}

private fun strengthOfCard(card: Char): Int {
   return when (card) {
        'A' -> 14
        'K' -> 13
        'Q' -> 12
        'J' -> 11
        'T' -> 10
        else -> card.toString().toInt()
    }
}

private fun strengthOfCardWithJoker(card: Char): Int {
    return when (card) {
        'A' -> 14
        'K' -> 13
        'Q' -> 12
        'J' -> 1 // joker is lowest in this case
        'T' -> 10
        else -> card.toString().toInt()
    }
}

private data class Hand(val cards: String, val bid: Int)
