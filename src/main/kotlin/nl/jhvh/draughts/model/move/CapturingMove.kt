package nl.jhvh.draughts.model.move

import nl.jhvh.draughts.model.Coordinate

interface CapturingMove: Capturing {
    val from: Coordinate
    val to: Coordinate
}