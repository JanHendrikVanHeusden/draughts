package nl.jhvh.draughts.model.structure

import nl.jhvh.draughts.model.base.BoardElement
import nl.jhvh.draughts.model.base.PlayableCoordinate
import nl.jhvh.draughts.model.base.PlayerType
import nl.jhvh.draughts.model.game.move.MovementChain

interface Piece: BoardElement, Comparable<Piece> {

    val initialCoordinate: PlayableCoordinate

    val playerType: PlayerType

    /** The current [PlayableCoordinate] of this piece; or `null` if the piece has been captured */
    var currentCoordinate: PlayableCoordinate?

    var isCaptured: Boolean

    var isCrowned: Boolean

    fun move(chain: MovementChain)

    /**
     * Determines all moves that are possible for this [Piece], regardless whether if and how many capturing.
     * Note that *not all of these moves may be allowed*: draughts rules allow only these moves with the highest capturing count.
     * @return the collection of move chains as determined; in case of multi-capture moves, the chains contain all required jumps
     * @see [allowedMoves]
     */
    fun possibleMoves(): Collection<MovementChain>

    /**
     * Determines all moves that are possible for this [Piece], and have the highest possible capturing count,
     * so these are allowed for international draughts
     * @return the collection of move chains as determined; in case of multi-capture moves, the chains contain all required jumps
     */
    fun allowedMoves(): Collection<MovementChain>

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int

    override fun toString(): String

}