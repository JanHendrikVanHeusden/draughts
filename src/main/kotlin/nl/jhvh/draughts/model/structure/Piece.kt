package nl.jhvh.draughts.model.structure

import nl.jhvh.draughts.model.BoardElement
import nl.jhvh.draughts.model.Coordinate
import nl.jhvh.draughts.model.movement.base.PieceMoveChain
import nl.jhvh.draughts.model.movement.options.CapturingTreeMovable
import nl.jhvh.draughts.model.movement.options.TreeMovable

interface Piece: TreeMovable, BoardElement {

    val initialCoordinate: Coordinate

    /** The current [Coordinate] of this piece; or `null` if the piece has been captured */
    var currentCoordinate: Coordinate?

    var isCaptured: Boolean

    var isCrowned: Boolean

    fun move(chain: PieceMoveChain)

    fun movementTree(): CapturingTreeMovable

    override fun equals(other: Any?): Boolean

}