package nl.jhvh.draughts.model

import nl.jhvh.draughts.formatting.DraughtsFormatting
import nl.jhvh.draughts.formatting.textformat.FormattableList
import nl.jhvh.draughts.model.base.BoardElement
import nl.jhvh.draughts.model.base.SquareType
import nl.jhvh.draughts.model.structure.Board
import nl.jhvh.draughts.model.structure.Piece
import nl.jhvh.draughts.model.structure.Square

internal data class BoardSquare(override val board: Board, override val xy: Pair<Int, Int>, override val squareType: SquareType) : Square {

    override fun getPiece(): Piece? {
        if (!this.squareType.playable) {
            return null
        }
        return board.getPiece(this)
    }

    override fun format(draughtsFormatter: DraughtsFormatting<BoardElement, FormattableList>): FormattableList {
        return draughtsFormatter.format(this)
    }

    override fun compareTo(other: Square): Int = xy.first + xy.first * xy.second

    override fun equals(other: Any?): Boolean =
        other === this || (other is BoardSquare && other.board === this.board && other.xy == this.xy)

    override fun hashCode(): Int = this.xy.hashCode()

}