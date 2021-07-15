package nl.jhvh.draughts.model

import nl.jhvh.draughts.model.move.CapturingTreeMovable
import nl.jhvh.draughts.model.move.PieceMovementChain
import nl.jhvh.draughts.model.move.TreeMovable

interface DraughtsPiece: TreeMovable, BoardElement {

    val initialCoordinate: Coordinate

    var currentCoordinate: Coordinate?

    var isCaptured: Boolean

    var isCrowned: Boolean

    fun move(chain: PieceMovementChain)

    fun movementTree(): CapturingTreeMovable

    override fun equals(other: Any?): Boolean

}