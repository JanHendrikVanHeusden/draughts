package nl.jhvh.draughts.service

import nl.jhvh.draughts.formatting.DraughtsFormatting
import nl.jhvh.draughts.formatting.textformat.BoardTextFormatter
import nl.jhvh.draughts.formatting.textformat.FormattableList
import nl.jhvh.draughts.formatting.textformat.PieceTextFormatter
import nl.jhvh.draughts.formatting.textformat.SquareTextFormatter
import nl.jhvh.draughts.log
import nl.jhvh.draughts.model.base.BoardElement
import nl.jhvh.draughts.model.base.PlayerType
import nl.jhvh.draughts.model.game.DraughtsGame
import nl.jhvh.draughts.model.game.Game
import nl.jhvh.draughts.rule.ValidationException
import nl.jhvh.draughts.rule.validate
import nl.jhvh.draughts.summary
import nl.jhvh.draughts.userInfo
import java.lang.RuntimeException

class DraughtsService : DraughtsServable {

    override fun newGame(): Game = DraughtsGame()

    // FIXME: error handling!
    override fun doMove(game: Game, move: List<Int>): Boolean {
        try {
            validate(move.isNotEmpty()) { "No position specified, can not do anything! Wanted move = $move" }
            validate(move.size >= 2) { """No "to" position specified! Wanted move = $move""" }
            val currentPosition = move.first()
            val piece = game.getPiece(currentPosition)
            validate(piece != null) { "There is no piece on position $currentPosition, cannot do a move! Wanted move = $move" }

            return game.move(piece!!, move)

        } catch (e: ValidationException) {
            userInfo { "The intended action is not allowed! \n${e.message?.trim() ?: ""}" }

        } catch (e: IllegalArgumentException) {
            with("The intended action is not allowed! \n${e.summary()}") {
                log().warn { "PLEASE USE ValidationException instead of IllegalArgumentException! \n$this \ne" }
                userInfo(this)
                throw RuntimeException(this)
            }

        } catch (e: IllegalStateException) {
            with("Error: The game is in an unexpected state! \n${e.summary()}") {
                log().error(e) { this }
                userInfo { this }
                throw RuntimeException(this)
            }

        } catch (e: Exception) {
            with("Unexpected error! \n${e.summary()})") {
                log().error(e) { this }
                userInfo(this)
                throw RuntimeException(this)
            }
        }
        userInfo()
        return false
    }

    override fun playerInTurn(game: Game): PlayerType = game.playerTypeInTurn()

    override fun formatAsText(game: Game): String {
        return game.format(boardFormatter).toString()
    }

    companion object {
        private val pieceFormatter = PieceTextFormatter()
        private val squareFormatter = SquareTextFormatter(pieceFormatter)

        @Suppress("UNCHECKED_CAST")
        private val boardFormatter =
            BoardTextFormatter(squareFormatter) as DraughtsFormatting<BoardElement, FormattableList>

    }
}