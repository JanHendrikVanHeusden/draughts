package nl.jhvh.draughts.model.base

import nl.jhvh.draughts.formatting.textformat.TextFormattable
import nl.jhvh.draughts.formatting.textformat.FormattableList
import nl.jhvh.draughts.model.structure.Board

interface BoardElement: TextFormattable<BoardElement, FormattableList> {
    val board: Board
}