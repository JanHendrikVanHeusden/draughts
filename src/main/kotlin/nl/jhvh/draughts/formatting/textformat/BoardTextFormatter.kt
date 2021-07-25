package nl.jhvh.draughts.formatting.textformat

import nl.jhvh.draughts.concatAlignRight
import nl.jhvh.draughts.concatEach
import nl.jhvh.draughts.formatting.DraughtsFormatting
import nl.jhvh.draughts.isOdd
import nl.jhvh.draughts.model.base.boardLength
import nl.jhvh.draughts.model.base.piecesPerRow
import nl.jhvh.draughts.model.structure.Board
import nl.jhvh.draughts.model.structure.Square

internal class BoardTextFormatter(val squareFormatter: SquareTextFormatter) :
    DraughtsFormatting<Board, FormattableList> {

    override fun format(element: Board): FormattableList {
        val result = nakedBoardFormat(element)
        addLeftBorder(result)
        addIndices(result)
        addTopBorder(result)
        return FormattableList(result)
    }

    fun nakedBoardFormat(element: Board): MutableList<String> {
        val squares = element.squares
        val result = mutableListOf<String>()
        (0 until boardLength).reversed().forEach {
            result.addAll(formatRow(it, squares))
        }
        return result
    }

    fun addTopBorder(list: MutableList<String>) {
        if (list.isEmpty()) {
            return
        }
        // simply copy the bottom border - not too elegant, but works for this basic format
        list.add(0, list.last())
    }

    fun addLeftBorder(list: MutableList<String>) {
        if (list.isEmpty()) {
            return
        }
        // simply copy the right border - not too elegant, but works for this basic format
        list.forEachIndexed{ i, str -> list[i] = str.last() + str}
    }

    fun formatRow(y: Int, squares: Map<Pair<Int, Int>, Square>): List<String> {
        var textList = listOf("", "")
        squares.filter { it.key.second == y }.forEach { entry ->
            textList = textList.concatEach(squareFormatter.format(entry.value))
        }
        return textList
    }

    private fun addIndices(result: MutableList<String>) {
        val leftPositions: MutableList<String> = mutableListOf()
        val rightPositions: MutableList<String> = mutableListOf()
        (0 until boardLength).forEach { y ->
            val leftMostPos = y * piecesPerRow + 1
            leftPositions.add("$leftMostPos ..${if (y.isOdd()) "  " else ""}")
            leftPositions.add("")
            val rightMostPos = y * piecesPerRow + piecesPerRow
            rightPositions.add("$rightMostPos ${if (y.isOdd()) "  " else ""}")
            rightPositions.add("")
        }
        val indices = leftPositions.concatAlignRight(rightPositions)
        result.forEachIndexed { i, _ ->
            result[i] = indices[i] + result[i]
        }
    }

}

// Uncomment to test formatting!
//fun main() {
//    val board = DraughtsBoard()
//
//    val pieceFormatter = PieceTextFormatter()
//    val squareFormatter = SquareTextFormatter(pieceFormatter)
//    val boardFormatter = BoardTextFormatter(squareFormatter)
//
//    userInfo(boardFormatter.format(board))
//}