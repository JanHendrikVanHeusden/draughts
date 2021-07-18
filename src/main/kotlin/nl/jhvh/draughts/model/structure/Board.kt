package nl.jhvh.draughts.model.structure

import nl.jhvh.draughts.model.base.BoardElement
import nl.jhvh.draughts.model.base.PlayableCoordinate
import nl.jhvh.draughts.model.base.PlayerType

interface Board: BoardElement {

    val playableCoordinates: Set<PlayableCoordinate>

    val squares: Map<Pair<Int, Int>, Square>

    val allPieces: Set<Piece>

    val allPiecesByPlayerType: Map<PlayerType, Set<Piece>>

    fun getPiece(position: Int): Piece?

    fun getPiece(square: Square): Piece?

    fun getPiece(xy: Pair<Int, Int>): Piece?

    fun getPiecesByXY(): Map<Pair<Int, Int>, Piece>
}
