package nl.jhvh.draughts.formatting

import nl.jhvh.draughts.model.base.BoardElement

interface DraughtsFormatting<in T: BoardElement, out R: Any> {

    fun format(element: T): R
}

