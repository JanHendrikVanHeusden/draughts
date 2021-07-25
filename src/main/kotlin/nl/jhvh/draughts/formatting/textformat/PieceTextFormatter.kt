package nl.jhvh.draughts.formatting.textformat

import nl.jhvh.draughts.formatting.DraughtsFormatting
import nl.jhvh.draughts.formatting.textformat.PieceTextFormattingSymbols.Companion.mapByPlayerType
import nl.jhvh.draughts.model.base.PlayerType
import nl.jhvh.draughts.model.structure.Piece

val noPieceFormat: FormattableList = FormattableList(listOf(" "))

enum class PieceTextFormattingSymbols(val playerType: PlayerType, val normal: String, val crowned: String) {
    startingPlayerSymbol(PlayerType.STARTING_PLAYER, "o", "8"),
    secondPlayerSymbol(PlayerType.SECOND_PLAYER, "x", "E" );

    companion object {
        val mapByPlayerType: Map<PlayerType, PieceTextFormattingSymbols> = values().map { it.playerType to it }.toMap()
    }
}

internal class PieceTextFormatter: DraughtsFormatting<Piece, FormattableList> {

    override fun format(element: Piece): FormattableList {
        check(!element.isCaptured) { "Formatter should not be called for a piece that was captured already! piece = $element" }
        val symbols = mapByPlayerType[element.playerType]!!
        return FormattableList(listOf(if (element.isCrowned) symbols.crowned else symbols.normal))
    }

}


