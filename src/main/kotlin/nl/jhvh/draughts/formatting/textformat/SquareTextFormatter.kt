package nl.jhvh.draughts.formatting.textformat

import nl.jhvh.draughts.concatAlignCenter
import nl.jhvh.draughts.formatting.DraughtsFormatting
import nl.jhvh.draughts.model.base.BoardElement
import nl.jhvh.draughts.model.structure.Square

internal class SquareTextFormatter(val pieceFormatter: PieceTextFormatter) : DraughtsFormatting<Square, FormattableList> {

    override fun format(element: Square): FormattableList {
        val piece = element.piece

        @Suppress("UNCHECKED_CAST")
        val formattedPiece: FormattableList =
            piece?.format(pieceFormatter as DraughtsFormatting<BoardElement, FormattableList>) ?: noPieceFormat

        return FormattableList( ArrayList(formattedPiece + "___").concatAlignCenter(FormattableList(listOf("│", " "))))
    }
}