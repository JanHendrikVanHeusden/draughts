package nl.jhvh.draughts.model.game.move

import nl.jhvh.draughts.model.base.PlayableCoordinate
import nl.jhvh.draughts.model.structure.Piece

class PieceMove(
    override val from: PlayableCoordinate,
    override val to: PlayableCoordinate,
    override var capturing: Piece?
) : Move, Capturing {

    override fun toString(): String {
        return "${this.javaClass.simpleName}(from=$from, to=$to, capturing=$capturing)"
    }
}