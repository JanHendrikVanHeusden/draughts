package nl.jhvh.draughts.model.structure

import nl.jhvh.draughts.model.BoardElement
import nl.jhvh.draughts.model.Coordinate

interface Square: BoardElement {
    val coordinate: Coordinate
}