package nl.jhvh.draughts.player

import nl.jhvh.draughts.formatting.DraughtsFormatting
import nl.jhvh.draughts.formatting.textformat.BoardTextFormatter
import nl.jhvh.draughts.formatting.textformat.FormattableList
import nl.jhvh.draughts.formatting.textformat.PieceTextFormatter
import nl.jhvh.draughts.formatting.textformat.SquareTextFormatter
import nl.jhvh.draughts.model.base.BoardElement
import nl.jhvh.draughts.model.structure.Board
import nl.jhvh.draughts.service.DraughtsServable
import nl.jhvh.draughts.service.DraughtsService
import nl.jhvh.draughts.userInfo

const val emptyString: String = ""

fun input(): String {
    val input: String? = readLine()
    return if (input == null || input.isBlank()) emptyString else input.trim()
}

private fun welcome(): Boolean {
    userInfo()
    userInfo("Welcome to Jan-Hendrik's draught game! Press N + [Enter] to quit, or just [Enter] to continue")
    val quit = input().uppercase().equals("N")
    if (quit) {
        userInfo("Thanks for now! Maybe another time again!")
        return false
    }
    return true
}

fun displayBoard(board: Board, boardTextFormatter: BoardTextFormatter) {
    @Suppress("UNCHECKED_CAST")
    userInfo(board.format(boardTextFormatter as DraughtsFormatting<BoardElement, FormattableList>))
}

private fun explanation() {
    userInfo()
    userInfo("""Enter positions of move. First number = current position, second number = new position, e.g. "32 27" """)
    userInfo("""In case of multiple jumps, add all numbers on one line, e.g. "32 27 21" """)
    userInfo()
}

private val whitespaceRegex: Regex = Regex("""\s+""")

fun inputNumbers(): List<Int> {
    try {
        return input().split(whitespaceRegex).map { it.toInt() }
    } catch (e: NumberFormatException) {
        userInfo("Please enter numbers only, divided by spaces!")
        return inputNumbers()
    }
}

fun main() {
    if (!welcome()) {
        return
    }
    val service: DraughtsServable = DraughtsService()
    val game = service.newGame()

    val pieceFormatter = PieceTextFormatter()
    val squareFormatter = SquareTextFormatter(pieceFormatter)
    val boardFormatter = BoardTextFormatter(squareFormatter)

    displayBoard(game, boardFormatter)
    userInfo("${service.playerInTurn(game).color} starts")
    explanation()

    while (true) {
        if (!service.doMove(game, inputNumbers())) {
            userInfo("Try again!")
        } else {
            displayBoard(game, boardFormatter)
        }
    }
}