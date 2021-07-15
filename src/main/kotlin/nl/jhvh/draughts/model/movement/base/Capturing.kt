package nl.jhvh.draughts.model.movement.base

import nl.jhvh.draughts.model.structure.Piece

interface Capturing {

    val capturing: List<Piece>
}