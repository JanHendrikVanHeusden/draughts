package nl.jhvh.draughts.formatting.textformat

import nl.jhvh.draughts.concatAlignCenter
import nl.jhvh.draughts.formatting.DraughtsFormatting
import nl.jhvh.draughts.model.base.BoardElement
import nl.jhvh.draughts.model.structure.Square

class SquareTextFormatter(val pieceFormatter: PieceTextFormatter) : DraughtsFormatting<Square, FormattableList> {

    override fun format(element: Square): FormattableList {
        val xy = element.xy
        // FIXME !! For every Square to be formatted, the board is required to build a new map of pieces!
        //          Should be fixed in class [DraughtsBoard], so that this map isn't redetermined on every call.
        val piece = element.board.getPiecesByXY()[xy]

        @Suppress("UNCHECKED_CAST")
        val formattedPiece: FormattableList =
            piece?.format(pieceFormatter as DraughtsFormatting<BoardElement, FormattableList>) ?: noPieceFormat
        check(formattedPiece.size == 1) { "${this.javaClass.simpleName} expects a formatted piece as a list with 1 element (1 line), but is ${formattedPiece}" }

        return FormattableList( ArrayList(formattedPiece + "___").concatAlignCenter(FormattableList(listOf("│", " "))))
    }
}