package nl.jhvh.draughts.service

import nl.jhvh.draughts.model.base.PlayerType
import nl.jhvh.draughts.model.game.Game

interface DraughtsServable {

    fun newGame(): Game

    fun doMove(game: Game, move: List<Int>)

    fun playerInTurn(game: Game): PlayerType

}