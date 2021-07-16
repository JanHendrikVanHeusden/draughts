package nl.jhvh.draughts.model.movement.base

import nl.jhvh.draughts.model.base.PlayableCoordinate

interface Move: Capturing {
    val from: PlayableCoordinate
    val to: PlayableCoordinate
}