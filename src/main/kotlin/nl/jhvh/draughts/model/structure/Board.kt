package nl.jhvh.draughts.model.structure

import nl.jhvh.draughts.model.base.BoardElement
import nl.jhvh.draughts.model.base.PlayableCoordinate

interface Board: BoardElement {

    val playableCoordinates: Set<PlayableCoordinate>

    val squares: Map<Pair<Int, Int>, Square>

}
