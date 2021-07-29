package nl.jhvh.draughts.model.structure

import nl.jhvh.draughts.model.base.BoardElement
import nl.jhvh.draughts.model.base.SquareType

interface Square: BoardElement, Comparable<Square> {

    val xy: Pair<Int, Int>

    val squareType: SquareType

    var piece: Piece?

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int

}