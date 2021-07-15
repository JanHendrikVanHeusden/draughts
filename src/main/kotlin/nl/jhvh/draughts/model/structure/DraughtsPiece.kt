package nl.jhvh.draughts.model.structure

import nl.jhvh.draughts.model.move.CapturingTreeMovable
import nl.jhvh.draughts.model.move.PieceMovementChain
import nl.jhvh.draughts.model.move.TreeMovable

interface DraughtsPiece: TreeMovable, BoardElement {

    val initialCoordinate: Coordinate

    /** The current [Coordinate] of this piece; or `null` if the piece has been captured */
    var currentCoordinate: Coordinate?

    var isCaptured: Boolean

    var isCrowned: Boolean

    fun move(chain: PieceMovementChain)

    fun movementTree(): CapturingTreeMovable

    override fun equals(other: Any?): Boolean

}