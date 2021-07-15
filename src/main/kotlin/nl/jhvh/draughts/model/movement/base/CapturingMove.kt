package nl.jhvh.draughts.model.movement.base

import nl.jhvh.draughts.model.Coordinate

interface CapturingMove: Capturing {
    val from: Coordinate
    val to: Coordinate
}