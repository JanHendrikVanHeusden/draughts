package nl.jhvh.draughts.model.game.move

interface PieceMovementChain {
    val moves: List<PieceMove>
    val captureCount: Int
}
