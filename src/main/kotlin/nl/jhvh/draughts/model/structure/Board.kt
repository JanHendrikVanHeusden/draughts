package nl.jhvh.draughts.model.structure

import nl.jhvh.draughts.model.base.BoardElement
import nl.jhvh.draughts.model.base.PlayableCoordinate

interface Board: BoardElement {

    val playableCoordinates: Set<PlayableCoordinate>

    val squares: Map<Pair<Int, Int>, Square>

    val lightPieces: Set<Piece>

    val darkPieces: Set<Piece>

    val allPieces: Set<Piece>

    fun getPiece(square: Square): Piece?

    fun getPiece(xy: Pair<Int, Int>): Piece?

    fun getPiecesByXY(): Map<Pair<Int, Int>, Piece>

}
