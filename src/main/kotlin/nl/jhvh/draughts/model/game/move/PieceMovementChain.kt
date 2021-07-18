package nl.jhvh.draughts.model.game.move

import nl.jhvh.draughts.model.DraughtsPiece
import nl.jhvh.draughts.model.structure.Piece

/**
 * A chain (of at least 1 [Move]) of a given [Piece].
 * The default sorting order [Comparable]<[MovementChain]> gives an ordering with descending [captureCount]
 * (so highest count first).
 */
internal class PieceMovementChain(override val piece: DraughtsPiece, override val moves: List<PieceMove>) :
    MovementChain {

    // order by highest capture count first
    override fun compareTo(other: MovementChain): Int = other.captureCount - this.captureCount

    override fun toString(): String {
        return "PieceMovementChain(piece=$piece, moves=$moves, captureCount=$captureCount)"
    }

    override val captureCount: Int by lazy {
        this.moves.sumOf {
            // cast as Int is needed! Otherwise overload ambiguity (Int / Long)
            @Suppress("USELESS_CAST") if (it.capturing == null) 0 else 1 as Int
        }
    }

}