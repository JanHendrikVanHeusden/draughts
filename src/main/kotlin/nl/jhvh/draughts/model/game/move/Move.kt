package nl.jhvh.draughts.model.game.move

import nl.jhvh.draughts.model.base.PlayableCoordinate
import nl.jhvh.draughts.model.structure.Piece

interface Move: Capturing {
    val from: PlayableCoordinate
    val to: PlayableCoordinate
    override var capturing: Piece?
}