package nl.jhvh.draughts.model.move

import nl.jhvh.draughts.model.DraughtsPiece

interface Capturing {

    val capturing: List<DraughtsPiece>
}