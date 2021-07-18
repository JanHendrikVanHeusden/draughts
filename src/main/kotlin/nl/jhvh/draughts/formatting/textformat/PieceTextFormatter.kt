package nl.jhvh.draughts.formatting.textformat

import nl.jhvh.draughts.formatting.DraughtsFormatting
import nl.jhvh.draughts.model.structure.Piece

val noPieceFormat: FormattableList = FormattableList(listOf(" "))
const val startingPlayerPieceSymbol = "o"
const val secondPlayerPieceSymbol = "x"

class PieceTextFormatter: DraughtsFormatting<Piece, FormattableList> {

    override fun format(element: Piece): FormattableList {
        check(!element.isCaptured) { "Formatter should not be called for a piece that was captured already! piece = $element" }
        val formatted = if ( element.playerType.hasFirstTurn) startingPlayerPieceSymbol else secondPlayerPieceSymbol
        return FormattableList((listOf(if ( element.isCrowned) formatted.uppercase() else formatted)))
    }
}