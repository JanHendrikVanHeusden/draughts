package nl.jhvh.draughts.formatting

import nl.jhvh.draughts.model.base.BoardElement

interface DraughtsFormatting<out T: BoardElement, out R: Any> {

    fun format(element: BoardElement): R
}

