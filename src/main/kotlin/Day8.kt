import java.nio.file.Files
import java.nio.file.Paths
import kotlinx.coroutines.*

fun main() {
    val input = Files.readAllLines(Paths.get("src/main/resources/day8.txt"))
    //println(puzzle1(input))
    println(puzzle2(input))
}

private fun puzzle1(input: List<String>): Int {
    val instructions = input[0].toCharArray()
    val nodes = parseNodes(input.drop(2))
    val steps = walk(instructions, nodes)
    return steps
}

private fun puzzle2(input: List<String>): Long {
    val instructions = input[0].toCharArray()
    val nodes = parseNodes(input.drop(2))
    val steps = walk2(instructions, nodes)

    return steps
}

private fun parseNodes(input: List<String>): Map<String,Node> {
    val nodes = mutableMapOf<String, Node>()
    input.forEach { line ->
        val node = Node(line.substring(0, 3), line.substring(7, 10), line.substring(12, 15))
        nodes[node.name] = node
    }
    return nodes
}

private fun walk(
    instructions: CharArray,
    nodes: Map<String,Node>
): Int {

    var nextNode = "AAA"
    var currentSteps = 0
    var currentInstruction = 0
    while (nextNode != "ZZZ") {
        currentSteps++

        // find the next node
        val node = nodes[nextNode]!!

        // next direction
        val nextDirection = instructions[currentInstruction]

        currentInstruction++
        if (currentInstruction >= instructions.size)
            currentInstruction = 0

        // next node
        if (nextDirection == 'R') {
            nextNode = node.right
        } else
            nextNode = node.left

    }
    return currentSteps
}

private fun walk2(
    instructions: CharArray,
    nodes: Map<String,Node>
): Long {

    val nextNodes = nodes.keys
        .filter { it.endsWith('A') }
        .toMutableList()

    var currentSteps = 0L
    var currentInstruction = 0

    while (!end(nextNodes)) {
        currentSteps++

        // next direction
        val nextDirection = instructions[currentInstruction]

        currentInstruction++
        if (currentInstruction >= instructions.size)
            currentInstruction = 0


        runBlocking {

            val deferreds: List<Deferred<Unit>> = nextNodes.mapIndexed { index, nextNode ->
                async {
                    // find the next node
                    val node = nodes[nextNode]!!

                    // next node
                    if (nextDirection == 'R') {
                        nextNodes[index] = node.right
                    } else
                        nextNodes[index] = node.left
                }
            }
            deferreds.awaitAll()
        }
    }
    return currentSteps
}

private fun end(nodes: List<String>) : Boolean {
    return nodes.all { it.endsWith('Z') }
}

data class Node(val name: String, val left: String, val right: String)