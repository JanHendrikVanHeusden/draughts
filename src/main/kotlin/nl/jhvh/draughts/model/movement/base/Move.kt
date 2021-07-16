package nl.jhvh.draughts.model.movement.base

import nl.jhvh.draughts.model.base.Coordinate

interface Move {
    val from: Coordinate
    val to: Coordinate
}