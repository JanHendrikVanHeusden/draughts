package nl.jhvh.draughts.model.move

import nl.jhvh.draughts.model.Coordinate

interface Move {
    val from: Coordinate
    val to: Coordinate
}