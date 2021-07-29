package nl.jhvh.draughts.model

import nl.jhvh.draughts.formatting.DraughtsFormatting
import nl.jhvh.draughts.formatting.textformat.FormattableList
import nl.jhvh.draughts.model.base.BoardElement
import nl.jhvh.draughts.model.base.PlayableCoordinate
import nl.jhvh.draughts.model.base.PlayerType
import nl.jhvh.draughts.model.base.PlayerType.SECOND_PLAYER
import nl.jhvh.draughts.model.base.PlayerType.STARTING_PLAYER
import nl.jhvh.draughts.model.base.SquareType
import nl.jhvh.draughts.model.base.SquareType.NON_PLAYABLE
import nl.jhvh.draughts.model.base.SquareType.PLAYABLE
import nl.jhvh.draughts.model.base.boardLength
import nl.jhvh.draughts.model.base.boardWidth
import nl.jhvh.draughts.model.base.piecesPerPlayer
import nl.jhvh.draughts.model.base.positionRange
import nl.jhvh.draughts.model.structure.Board
import nl.jhvh.draughts.model.structure.Piece
import nl.jhvh.draughts.model.structure.Square

internal class DraughtsBoard: Board {

    override val playableCoordinates: Set<PlayableCoordinate> = positionRange.map { pos -> PlayableCoordinate(pos) }.toSet()

    override val squares: Map<Pair<Int, Int>, Square> = initSquares()

    override val board: Board = this

    private fun initSquares(): Map<Pair<Int, Int>, Square> {
        val playableXYs = playableCoordinates.map { it.xy }
        val playable: (Pair<Int, Int>) -> SquareType = { xy -> if (xy in playableXYs) PLAYABLE else NON_PLAYABLE}
        return (0 until boardWidth)
            .map { x -> (0 until boardLength).map { y -> Pair(x, y) } }
            .flatten()
            .map { it to BoardSquare(this, it, playable(it)) }
            .toMap()
    }

    override fun format(draughtsFormatter: DraughtsFormatting<BoardElement, FormattableList>): FormattableList {
        return draughtsFormatter.format(this)
    }

}