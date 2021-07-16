package nl.jhvh.draughts.model.structure

import nl.jhvh.draughts.model.base.BoardElement
import nl.jhvh.draughts.model.base.PlayerType
import nl.jhvh.draughts.model.base.PlayableCoordinate
import nl.jhvh.draughts.model.movement.base.PieceMoveChain
import nl.jhvh.draughts.model.movement.options.CapturingTreeMovable
import nl.jhvh.draughts.model.movement.options.TreeMovable

interface Piece: TreeMovable, BoardElement, Comparable<Piece> {

    val initialCoordinate: PlayableCoordinate

    val playerType: PlayerType

    /** The current [PlayableCoordinate] of this piece; or `null` if the piece has been captured */
    var currentCoordinate: PlayableCoordinate?

    var isCaptured: Boolean

    var isCrowned: Boolean

    fun move(chain: PieceMoveChain)

    fun movementTree(): CapturingTreeMovable

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int

    override fun toString(): String

}