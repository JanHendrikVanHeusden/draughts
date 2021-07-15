package nl.jhvh.draughts.model.move

import nl.jhvh.draughts.model.structure.DraughtsPiece

interface Capturing {

    val capturing: List<DraughtsPiece>
}