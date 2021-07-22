package nl.jhvh.draughts.player

import nl.jhvh.draughts.formatting.DraughtsFormatting
import nl.jhvh.draughts.formatting.textformat.BoardTextFormatter
import nl.jhvh.draughts.formatting.textformat.FormattableList
import nl.jhvh.draughts.formatting.textformat.PieceTextFormatter
import nl.jhvh.draughts.formatting.textformat.PieceTextFormattingSymbols.secondPlayerSymbol
import nl.jhvh.draughts.formatting.textformat.PieceTextFormattingSymbols.startingPlayerSymbol
import nl.jhvh.draughts.formatting.textformat.SquareTextFormatter
import nl.jhvh.draughts.model.base.BoardElement
import nl.jhvh.draughts.model.base.PlayerType
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
    userInfo("*********************************************************************************************")
    userInfo("Welcome to Jan-Hendrik's draught game! Press N + [Enter] to quit, or just [Enter] to continue")
    userInfo("*********************************************************************************************")
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
    userInfo(""" The player's pieces symbols are: (${PlayerType.STARTING_PLAYER.color}) are indicated by "${startingPlayerSymbol.normal}"""")
    userInfo( """   ${PlayerType.STARTING_PLAYER.color.padEnd(8)} -  normal: ${startingPlayerSymbol.normal}  crowned: ${startingPlayerSymbol.crowned} """)
    userInfo( """   ${PlayerType.SECOND_PLAYER.color.padEnd(8)} -  normal: ${secondPlayerSymbol.normal}  crowned: ${secondPlayerSymbol.crowned} """)
    userInfo()
    userInfo("""Enter positions of move. First number = current position, second number = new position, e.g. "32 27" """)
    userInfo("""In case of multiple jumps, add all numbers on one line, e.g. "32 27 21" """)
    userInfo()
}

fun inputNumbers(): List<Int> {
    try {
        var input = input().trim()
        // When running jar file, java cannot find the Kotlin Regex class...
        // too bad... Just remove all duplicate spaces and tabs in these weird loops...
        while (input.contains("  ")) {
            input = input.replace("  ", " ")
        }
        while (input.contains("\t")) {
            input = input.replace("\t", " ")
        }
        return input.split(" ").map { it.toInt() }

    } catch (e: NumberFormatException) {
        userInfo("Please enter numbers only, divided by spaces!")
        return inputNumbers()
    }
}

fun playDraughts() {
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

// to run it from command line (jar file)
class Player {
    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            playDraughts()
        }
    }
}