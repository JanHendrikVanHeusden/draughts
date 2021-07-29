package nl.jhvh.draughts.model

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import nl.jhvh.draughts.formatting.DraughtsFormatting
import nl.jhvh.draughts.formatting.textformat.FormattableList
import nl.jhvh.draughts.model.base.BoardElement
import nl.jhvh.draughts.model.base.PlayerType
import nl.jhvh.draughts.model.base.boardLength
import nl.jhvh.draughts.model.base.boardWidth
import nl.jhvh.draughts.model.structure.Board
import nl.jhvh.draughts.model.structure.Piece
import nl.jhvh.draughts.model.structure.Square
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Unit tests for [DraughtsBoard]
 *
 * Note that a lot of stuff is done when constructing / initializing the [DraughtsBoard].
 * Tests covering construction and initialization are covered by a separate test [DraughtsBoardInitializationTest]
 */
internal class DraughtsGameTest {

    private val subject: Board = DraughtsBoard()

//    @Test
//    fun `getPiece by Square argument`() {
//        // spy, because the call we are testing is redirected to another method in the subject (SUT)
//        val subjectSpyk: DraughtsBoard = spyk(subject as DraughtsBoard)
//        val allSquarePositions: Set<Pair<Int, Int>> = (0 until boardWidth)
//            .map { x -> (0 until boardLength).map { y -> Pair(x, y) } }
//            .flatten().toSet()
//
//        allSquarePositions.forEach { pair ->
//            // given
//            val squareMock: Square = mockk()
//            val pieceMock: Piece = mockk()
//            every { squareMock.xy }.returns(pair)
//            every { subjectSpyk.getPiece(pair) }.returns(pieceMock)
//
//            // when
//            val actual = subjectSpyk.getPiece(squareMock)
//            // then
//            verify { squareMock.xy }
//            confirmVerified(squareMock, pieceMock)
//            assertThat(actual).isEqualTo(pieceMock)
//        }
//    }

    @Test
    fun format() {
        // given
        val boardFormatterMock: DraughtsFormatting<Board, FormattableList> = mockk()
        val expected: FormattableList = mockk()
        every { boardFormatterMock.format(subject) }.returns(expected)

        // when
        @Suppress("UNCHECKED_CAST")
        val actual = subject.format(boardFormatterMock as DraughtsFormatting<BoardElement, FormattableList>)

        // then
        verify { boardFormatterMock.format(subject) }
        confirmVerified(boardFormatterMock)
        assertThat(actual).isEqualTo(expected)
    }

//    @Test
//    fun crowningPositionForDarkPiece() {
//        // given
//        val darkPieceMock: Piece = mockk()
//        every { darkPieceMock.isCaptured }.returns(false)
//        every { darkPieceMock.currentCoordinate }.returns(mockk())
//        every { darkPieceMock.currentCoordinate!!.y }.returns(0)
//        every { darkPieceMock.playerType }.returns(PlayerType.SECOND_PLAYER)
//
//        // when, then
//        assertThat(subject.isCrowningPosition(darkPieceMock)).isTrue()
//
//
//        verify { darkPieceMock.isCaptured }
//        verify { darkPieceMock.playerType }
//        verify { darkPieceMock.currentCoordinate }
//        confirmVerified(darkPieceMock)
//    }

//    @Test
//    fun crowningPositionLightPiece() {
//        // given
//        val lightPieceMock: Piece = mockk()
//        every { lightPieceMock.isCaptured }.returns(false)
//        every { lightPieceMock.currentCoordinate }.returns(mockk())
//        every { lightPieceMock.currentCoordinate!!.y }.returns(9)
//        every { lightPieceMock.playerType }.returns(PlayerType.STARTING_PLAYER)
//
//        // when, then
//        assertThat(subject.isCrowningPosition(lightPieceMock)).isTrue()
//
//        verify { lightPieceMock.isCaptured }
//        verify { lightPieceMock.playerType }
//        verify { lightPieceMock.currentCoordinate }
//        confirmVerified(lightPieceMock)
//    }

}