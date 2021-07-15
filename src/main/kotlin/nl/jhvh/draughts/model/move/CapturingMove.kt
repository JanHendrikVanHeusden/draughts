package nl.jhvh.draughts.model.move

import nl.jhvh.draughts.model.structure.Coordinate

interface CapturingMove: Capturing {
    val from: Coordinate
    val to: Coordinate
}