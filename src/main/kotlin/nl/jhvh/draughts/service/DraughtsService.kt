package nl.jhvh.draughts.service

import nl.jhvh.draughts.log
import nl.jhvh.draughts.model.base.PlayerType
import nl.jhvh.draughts.model.game.DraughtsGame
import nl.jhvh.draughts.model.game.Game
import nl.jhvh.draughts.userInfo

class DraughtsService : DraughtsServable {

    override fun newGame(): Game = DraughtsGame()

    // FIXME: error handling!
    override fun doMove(game: Game, move: List<Int>): Boolean {
        try {
            require(move.isNotEmpty()) { "No position specified, can not do anything! Wanted move = $move" }
            require(move.size >= 2) { """No "to" position specified! Wanted move = $move""" }
            val currentPosition = move.first()
            val piece = game.getPiece(currentPosition)
            require(piece != null) { "There is no piece on position $currentPosition, cannot do a move! Wanted move = $move" }

            return game.move(piece, move)

        } catch (e: IllegalArgumentException) {
            userInfo { "The intended action is not allowed! \n${e.message ?: (e.javaClass.simpleName + ": " + e.stackTrace.first())}" }

        } catch (e: IllegalStateException) {
            userInfo { "Error: The game is in an unexpected state! \n${e.message ?: (e.javaClass.simpleName + ": " + e.stackTrace.first())}" }
            userInfo()
            throw e

        } catch (e: Exception) {
            log().error { "Unexpected error! \n${e.message} (at ${e.javaClass.simpleName + ": " + e.stackTrace.first()})" }
            userInfo()
            throw e
        }
        return false
    }

    override fun playerInTurn(game: Game): PlayerType = game.playerTypeInTurn()

}