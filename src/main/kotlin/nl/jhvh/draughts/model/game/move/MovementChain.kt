package nl.jhvh.draughts.model.game.move

import nl.jhvh.draughts.model.structure.Piece

/**
 * A chain (of 0 or more [Move]s) of a given [Piece].
 * The default sorting order [Comparable]<[MovementChain]> gives an ordering with descending [captureCount]
 * (so highest count first).
 */
interface MovementChain : Comparable<MovementChain> {
    val piece: Piece
    val moves: List<Move>
    val captureCount: Int
    val asPositions: List<Int>

    /**
     * This [equals] is meant to match purely by [asPositions]!
     * Take care to only use it within the context of a single turn, not over different turns / moves!
     * (which is anyway the span of this object)
     * FIXME: consider separate method matchByPositions()
     */
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int

    override fun toString(): String
}
