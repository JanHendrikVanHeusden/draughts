package nl.jhvh.draughts.model

import io.mockk.clearMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import nl.jhvh.draughts.formatting.DraughtsFormatting
import nl.jhvh.draughts.formatting.textformat.FormattableList
import nl.jhvh.draughts.model.base.BoardElement
import nl.jhvh.draughts.model.base.PlayableCoordinate
import nl.jhvh.draughts.model.base.PlayerType
import nl.jhvh.draughts.model.structure.Board
import nl.jhvh.draughts.model.structure.Piece
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.test.assertFails

internal class DraughtsPieceTest {

    private val boardMock: Board = mockk()
    private val coordinateMock: PlayableCoordinate = mockk()

    @BeforeEach
    fun setUp() {
        clearMocks(boardMock, coordinateMock)
        every { coordinateMock.position }.returns(28)
    }

//    @Test
//    fun setCurrentCoordinate() {
//        // given
//        val subject: Piece = DraughtsPiece(boardMock, coordinateMock, PlayerType.SECOND_PLAYER)
//        assertThat(subject.currentCoordinate).isSameAs(coordinateMock)
//        val coordinateMock2: PlayableCoordinate = mockk()
//        // when
//        subject.currentCoordinate = coordinateMock2
//        // then
//        assertThat(subject.currentCoordinate).isSameAs(coordinateMock2)
//    }
//
//    @Test
//    fun `set currentCoordinate to null when not captured throws exception`() {
//        // given
//        val subject: Piece = DraughtsPiece(boardMock, coordinateMock, PlayerType.SECOND_PLAYER)
//        assertThat(subject.currentCoordinate).isEqualTo(coordinateMock)
//        assertThat(subject.isCaptured).isFalse()
//        // when
//        val exception = assertFails { subject.currentCoordinate = null }
//        // then
//        assertThat(exception is IllegalStateException).isTrue()
//        assertThat(subject.currentCoordinate).isEqualTo(coordinateMock)
//        assertThat(subject.isCaptured).isFalse()
//    }
//
//    @Test
//    fun `set currentCoordinate to non-null when already captured throws exception`() {
//        // given
//        val subject: Piece = DraughtsPiece(boardMock, coordinateMock, PlayerType.SECOND_PLAYER)
//        subject.isCaptured = true
//        assertThat(subject.currentCoordinate).isNull()
//        assertThat(subject.isCaptured).isTrue()
//        // when
//        val exception = assertFails { subject.currentCoordinate = mockk() }
//        // then
//        assertThat(exception is IllegalStateException).isTrue()
//        assertThat(subject.currentCoordinate).isNull()
//        assertThat(subject.isCaptured).isTrue()
//    }
//
//    @Test
//    fun `set currentCoordinate to null when captured succeeds`() {
//        // given
//        val subject: Piece = DraughtsPiece(boardMock, coordinateMock, PlayerType.SECOND_PLAYER)
//        subject.isCaptured = true
//        // when
//        subject.currentCoordinate = null
//        // then
//        assertThat(subject.currentCoordinate).isNull()
//        assertThat(subject.isCaptured).isTrue()
//    }
//
//    @Test
//    fun setCaptured() {
//        // given
//        val subject: Piece = DraughtsPiece(boardMock, coordinateMock, PlayerType.SECOND_PLAYER)
//        assertThat(subject.isCaptured).isFalse()
//        assertThat(subject.currentCoordinate).isEqualTo(coordinateMock)
//        // when
//        subject.isCaptured = true
//        // then
//        assertThat(subject.isCaptured).isTrue()
//        assertThat(subject.currentCoordinate).isNull()
//    }
//
//    @Test
//    fun `uncapturing throws exception`() {
//        // given
//        val subject: Piece = DraughtsPiece(boardMock, coordinateMock, PlayerType.SECOND_PLAYER)
//        subject.isCaptured = true
//        assertThat(subject.isCaptured).isTrue()
//        assertThat(subject.currentCoordinate).isNull()
//        // when
//        val exception = assertFails { subject.isCaptured = false }
//        // then
//        assertThat(exception is IllegalStateException).isTrue()
//        assertThat(subject.isCaptured).isTrue()
//        assertThat(subject.currentCoordinate).isNull()
//    }
//
//    @Test
//    @Disabled("Not yet implemented: test for isCrowned()")
//    fun isCrowned() {
//        // TODO
//    }
//
//    @Test
//    @Disabled("Not yet implemented: test for setCrowned()")
//    fun setCrowned() {
//        // TODO
//    }
//
//    @Test
//    @Disabled("Not yet implemented: test for compareTo()")
//    fun compareTo() {
//        // TODO
//    }
//
//    @Test
//    @Disabled("Not yet implemented: test for testToString()")
//    fun testToString() {
//        // TODO
//    }
//
//    @Test
//    @Disabled("Not yet implemented: test for getBoard()")
//    fun getBoard() {
//        // TODO
//    }
//
//    @Test
//    @Disabled("Not yet implemented: test for getInitialCoordinate()")
//    fun getInitialCoordinate() {
//        // TODO
//    }
//
//    @Test
//    @Disabled("Not yet implemented: test for getPlayerType()")
//    fun getPlayerType() {
//        // TODO
//    }
//
//    @Test
//    fun format() {
//        // given
//        val boardMock: Board = mockk()
//        val coordinateMock: PlayableCoordinate = mockk()
//        val subject: Piece = DraughtsPiece(boardMock, coordinateMock, PlayerType.SECOND_PLAYER)
//        val pieceFormatterMock: DraughtsFormatting<Piece, FormattableList> = mockk()
//        val expected: FormattableList = mockk()
//        every { pieceFormatterMock.format(subject) }.returns(expected)
//
//        // when
//        @Suppress("UNCHECKED_CAST")
//        val actual = subject.format(pieceFormatterMock as DraughtsFormatting<BoardElement, FormattableList>)
//
//        // then
//        verify { pieceFormatterMock.format(subject) }
//        confirmVerified(pieceFormatterMock)
//        assertThat(actual).isEqualTo(expected)
//
//    }
//
//    @Test
//    fun testEquals() {
//        // given
//        val boardMock: Board = mockk()
//        val initialCoordinateMock1: PlayableCoordinate = mockk()
//        val initialCoordinateMock2: PlayableCoordinate = mockk()
//
//        val subject: Piece = DraughtsPiece(boardMock, initialCoordinateMock1, PlayerType.SECOND_PLAYER)
//
//        val equal: Piece = DraughtsPiece(boardMock, initialCoordinateMock1, PlayerType.SECOND_PLAYER)
//        val equalInitialCoordinate: Piece = DraughtsPiece(boardMock, initialCoordinateMock1, PlayerType.STARTING_PLAYER)
//        val notSameBoard: Piece = DraughtsPiece(mockk(), initialCoordinateMock1, PlayerType.STARTING_PLAYER)
//        val notEqualInitialCoordinate: Piece = DraughtsPiece(boardMock, initialCoordinateMock2, PlayerType.SECOND_PLAYER)
//
//        // when, then
//        assertThat(subject).isEqualTo(equal)
//        assertThat(subject).isEqualTo(equalInitialCoordinate)
//        assertThat(subject).isNotEqualTo(notEqualInitialCoordinate)
//        assertThat(subject).isNotEqualTo(notSameBoard)
//    }
//
//    @Test
//    fun testHashCode() {
//        // given
//        val boardMock: Board = mockk()
//        val initialCoordinateMock1: PlayableCoordinate = mockk()
//        every { initialCoordinateMock1.hashCode() } returns 85
//        val initialCoordinateMock2: PlayableCoordinate = mockk()
//        every { initialCoordinateMock2.hashCode() } returns 17
//
//        val subject1: Piece = DraughtsPiece(boardMock, initialCoordinateMock1, PlayerType.SECOND_PLAYER)
//        val subject2: Piece = DraughtsPiece(boardMock, initialCoordinateMock2, PlayerType.STARTING_PLAYER)
//
//        // when, then
//        assertThat(subject1.hashCode()).isEqualTo(85)
//        assertThat(subject2.hashCode()).isEqualTo(17)
//    }

}