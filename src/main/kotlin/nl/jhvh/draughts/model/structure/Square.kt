package nl.jhvh.draughts.model.structure

import nl.jhvh.draughts.model.base.BoardElement
import nl.jhvh.draughts.model.base.Coordinate

interface Square: BoardElement {
    val coordinate: Coordinate
}