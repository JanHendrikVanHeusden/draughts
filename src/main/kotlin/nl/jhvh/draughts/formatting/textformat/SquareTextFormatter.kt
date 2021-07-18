package nl.jhvh.draughts.formatting.textformat

import nl.jhvh.draughts.concatAlignCenter
import nl.jhvh.draughts.concatEach
import nl.jhvh.draughts.formatting.DraughtsFormatting
import nl.jhvh.draughts.model.base.BoardElement
import nl.jhvh.draughts.model.structure.Piece
import nl.jhvh.draughts.model.structure.Square

/**
 * @param pieceFormatter
 * @param pieces FIXME: Workaround: this is so we don't have to determine the piece position every again and again.
 *               Should be fixed in class [DraughtsBoard], so that this map isn't redetermined on every call.
 *               Parameter [pieces] should be removed then, the piece can then be retrieved by calling `element.board.getPiecesByXY()[xy]`
 *               Also see comments in that method
 */
class SquareTextFormatter(val pieceFormatter: PieceTextFormatter, val pieces: Map<Pair<Int, Int>, Piece>) : DraughtsFormatting<Square, FormattableList> {

    override fun format(element: Square): FormattableList {
        val xy = element.xy
        val piece = pieces[xy]

        @Suppress("UNCHECKED_CAST")
        val formattedPiece: FormattableList =
            piece?.format(pieceFormatter as DraughtsFormatting<BoardElement, FormattableList>) ?: noPieceFormat
        check(formattedPiece.size == 1) { "${this.javaClass.simpleName} expects a formatted piece as a list with 1 element (1 line), but is ${formattedPiece}" }

        return FormattableList( ArrayList(formattedPiece + "___").concatAlignCenter(FormattableList(listOf("│", " "))))
    }
}