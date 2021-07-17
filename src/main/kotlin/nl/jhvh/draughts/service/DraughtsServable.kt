package nl.jhvh.draughts.service

import nl.jhvh.draughts.model.base.PlayableCoordinate
import nl.jhvh.draughts.model.game.Game
import nl.jhvh.draughts.model.game.move.Move

interface DraughtsServable {

    fun initGame(): Game

    fun doMove(move: Move)

    fun allowedMoves(position: PlayableCoordinate): Collection<Move>

}