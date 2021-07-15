package nl.jhvh.draughts.service

import nl.jhvh.draughts.model.Board
import nl.jhvh.draughts.model.Coordinate
import nl.jhvh.draughts.model.move.Move

interface DraughtsServable {

    fun initBoard(): Board

    fun doMove(move: Move)

    fun allowedMoves(position: Coordinate): Collection<Move>

}