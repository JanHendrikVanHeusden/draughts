package nl.jhvh.draughts.player

import nl.jhvh.draughts.formatting.textformat.PieceTextFormattingSymbols.secondPlayerSymbol
import nl.jhvh.draughts.formatting.textformat.PieceTextFormattingSymbols.startingPlayerSymbol
import nl.jhvh.draughts.model.base.PlayerType
import nl.jhvh.draughts.service.DraughtsServable
import nl.jhvh.draughts.service.DraughtsService
import nl.jhvh.draughts.userInfo

private const val emptyString: String = ""

private fun input(): String {
    val input: String? = readLine()
    return if (input == null || input.isBlank()) emptyString else input.trim()
}

private fun welcome(): Boolean {
    userInfo()
    userInfo("**********************************************************************************************")
    userInfo("Welcome to Jan-Hendrik's draughts game! Press N + [Enter] to quit, or just [Enter] to continue")
    userInfo("**********************************************************************************************")
    val quit = input().uppercase().equals("N")
    if (quit) {
        userInfo("Thanks for now! Maybe another time again!")
        return false
    }
    return true
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

private val whitespaceRegex: Regex = Regex("""\s+""")

private fun inputNumbers(): List<Int> {
    return try {
        input().split(whitespaceRegex).map { it.toInt() }
    } catch (e: NumberFormatException) {
        userInfo("Please enter numbers only, separated by spaces!")
        inputNumbers()
    }
}

fun playDraughts() {
    if (!welcome()) {
        return
    }
    val service: DraughtsServable = DraughtsService()
    val game = service.newGame()

    userInfo(service.formatAsText(game))
    explanation()
    userInfo("${service.playerInTurn(game).color} starts")
    userInfo("Enter move for ${service.playerInTurn(game).color}: ")

    while (true) {
        if (!service.doMove(game, inputNumbers())) {
            userInfo("Try again!")
        } else {
            userInfo(service.formatAsText(game))
            userInfo()
            userInfo("Enter move for ${service.playerInTurn(game).color}: ")
        }
    }
}

/** to run it from command line (jar file */
class Player {
    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            playDraughts()
        }
    }
}