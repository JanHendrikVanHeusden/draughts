package nl.jhvh.draughts.model.game.move

import nl.jhvh.draughts.model.structure.Piece

/**
 * A chain (of at least 1 [Move]) of a given [Piece].
 * The default sorting order [Comparable]<[MovementChain]> gives an ordering with descending [captureCount]
 * (so highest count first).
 */
interface MovementChain : Comparable<MovementChain> {
    val piece: Piece
    val moves: List<Move>
    val captureCount: Int
    override fun toString(): String
}
