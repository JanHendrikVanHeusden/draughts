package nl.jhvh.draughts.model.move

import nl.jhvh.draughts.model.structure.Coordinate

interface Move {
    val from: Coordinate
    val to: Coordinate
}