package nl.jhvh.draughts.model.game.move

import nl.jhvh.draughts.model.structure.Piece

/**
 * A chain (0 or more [Move]s) of a given [Piece].
 * The default sorting order [Comparable]<[MovementChain]> gives an ordering with descending [captureCount]
 * (so highest count first).
 */
internal class PieceMovementChain(override val piece: Piece, override val moves: List<PieceMove>) :
    MovementChain {

    // order by highest capture count first
    override fun compareTo(other: MovementChain): Int = other.captureCount - this.captureCount

    // Note: this value is determined on construction, and only once.
    // Make sure that the list of moves is never altered after construction, that would cause nasty bugs!
    override val captureCount: Int =
        this.moves.sumOf {
            // cast as Int is needed! Otherwise overload ambiguity (Int / Long) for sumOf()
            @Suppress("USELESS_CAST")
            if (it.capturing == null) 0 else 1 as Int
        }

    // lazy initialization, because for most instances we will never touch this
    override val asPositions: List<Int> by lazy {
        if (this.piece.isCaptured) emptyList()
        else listOf(this.piece.currentCoordinate!!.position) + this.moves.map { it.to.position }
    }

    override fun toString(): String {
        return """PieceMovementChain
            |piece=$piece
            |moves:${moves.joinToString("\n", "\n")}
            |captureCount=$captureCount)"""
            .trimMargin()
    }

}