package nl.jhvh.draughts.model.movement.base

import nl.jhvh.draughts.model.base.Coordinate

interface CapturingMove: Capturing {
    val from: Coordinate
    val to: Coordinate
}