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

    override fun toString(): String
}
