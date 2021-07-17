package nl.jhvh.draughts.model.structure

import nl.jhvh.draughts.model.base.BoardElement
import nl.jhvh.draughts.model.base.PlayableCoordinate
import nl.jhvh.draughts.model.base.PlayerType
import nl.jhvh.draughts.model.game.move.PieceMovementChain

interface Piece: BoardElement, Comparable<Piece> {

    val initialCoordinate: PlayableCoordinate

    val playerType: PlayerType

    /** The current [PlayableCoordinate] of this piece; or `null` if the piece has been captured */
    var currentCoordinate: PlayableCoordinate?

    var isCaptured: Boolean

    var isCrowned: Boolean

    fun move(chain: PieceMovementChain)

    /**
     * Determines all moves that are possible, regardless whether if and how many capturing.
     * Note that not all moves may be allowed: draughts rules allow only these moves with the highest capturing count.
     * @return the moves as determined
     * @see [allowedMoves]
     */
    fun possibleMoves(): Collection<PieceMovementChain>

    /**
     * Determines all moves that are possible and have the highest possible capturing count,
     * so these are allowed for international draughts
     * @return the moves as determined
     */
    fun allowedMoves(): Collection<PieceMovementChain>

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int

    override fun toString(): String

}