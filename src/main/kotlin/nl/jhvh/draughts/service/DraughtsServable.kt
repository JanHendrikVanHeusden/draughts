package nl.jhvh.draughts.service

import nl.jhvh.draughts.model.base.PlayableCoordinate
import nl.jhvh.draughts.model.movement.base.Move
import nl.jhvh.draughts.model.structure.Board

interface DraughtsServable {

    fun initBoard(): Board

    fun doMove(move: Move)

    fun allowedMoves(position: PlayableCoordinate): Collection<Move>

}