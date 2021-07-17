package nl.jhvh.draughts.model.game.move

import nl.jhvh.draughts.model.structure.Piece

interface Capturing {

    /** If an enemy piece is captured by some event, [capturing] holds a reference to that piece */
    var capturing: Piece?
}