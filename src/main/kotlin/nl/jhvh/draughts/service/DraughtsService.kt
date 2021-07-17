package nl.jhvh.draughts.service

import nl.jhvh.draughts.model.base.PlayableCoordinate
import nl.jhvh.draughts.model.game.DraughtsGame
import nl.jhvh.draughts.model.game.Game
import nl.jhvh.draughts.model.game.move.Move

class DraughtsService: DraughtsServable {

    override fun initGame(): Game = DraughtsGame()

    override fun doMove(move: Move) {
        TODO("Not yet implemented")
    }

    override fun allowedMoves(position: PlayableCoordinate): Collection<Move> {
        TODO("Not yet implemented")
    }
}