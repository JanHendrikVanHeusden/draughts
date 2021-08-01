package nl.jhvh.draughts.model

import io.mockk.*
import nl.jhvh.draughts.formatting.DraughtsFormatting
import nl.jhvh.draughts.formatting.textformat.FormattableList
import nl.jhvh.draughts.model.base.BoardElement
import nl.jhvh.draughts.model.base.PlayableCoordinate
import nl.jhvh.draughts.model.base.PlayerType
import nl.jhvh.draughts.model.game.Game
import nl.jhvh.draughts.model.structure.Board
import nl.jhvh.draughts.model.structure.Piece
import nl.jhvh.draughts.model.structure.Square
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.test.assertFails

internal class DraughtsPieceTest {

    private val gameMock: Game = mockk("gameMock")
    private val boardMock: Board = mockk("boardMock")
    private val coordinateMock: PlayableCoordinate = mockk("coordinateMock")

    @BeforeEach
    fun setUp() {
        clearMocks(gameMock, coordinateMock)

        val coor28 = PlayableCoordinate(28)
        every { coordinateMock.position } returns(coor28.position)
        every { coordinateMock.xy } returns(coor28.xy)

        every { gameMock.board } returns(boardMock)
        every { boardMock.squares } returns emptyMap()  // override in tests when needed
    }

    @Test
    fun setCurrentCoordinate() {
        // given
        val subject: Piece = DraughtsPiece(gameMock, coordinateMock, PlayerType.SECOND_PLAYER)
        assertThat(subject.currentCoordinate).isSameAs(coordinateMock)

        val xy37 = Pair(3, 7)
        val coordinateMock2: PlayableCoordinate = mockk("coordinateMock2")
        every { coordinateMock2.xy } returns xy37

        val squareMock: Square = mockk("squareMock")
        every { squareMock.occupyingPiece } returns subject
        every { squareMock setProperty "occupyingPiece" value(subject) } just Runs
        every { subject.board.squares } returns mapOf(xy37 to squareMock)

        // when
        subject.currentCoordinate = coordinateMock2
        // then
        assertThat(subject.currentCoordinate).isSameAs(coordinateMock2)
    }

    @Test
    fun `set currentCoordinate to null when not captured should throw exception`() {
        // given
        val subject: Piece = DraughtsPiece(gameMock, coordinateMock, PlayerType.SECOND_PLAYER)
        assertThat(subject.currentCoordinate).isEqualTo(coordinateMock)
        assertThat(subject.isCaptured).isFalse()
        // when
        val exception = assertFails { subject.currentCoordinate = null }
        // then
        assertThat(exception is IllegalStateException).isTrue()
        assertThat(subject.currentCoordinate).isEqualTo(coordinateMock)
        assertThat(subject.isCaptured).isFalse()
    }

    @Test
    fun `set currentCoordinate to non-null when already captured should throw exception`() {
        // given
        val subject: Piece = DraughtsPiece(gameMock, coordinateMock, PlayerType.SECOND_PLAYER)
        subject.isCaptured = true
        assertThat(subject.currentCoordinate).isNull()
        assertThat(subject.isCaptured).isTrue()
        // when
        val exception = assertFails { subject.currentCoordinate = mockk() }
        // then
        assertThat(exception is IllegalStateException).isTrue()
        assertThat(subject.currentCoordinate).isNull()
        assertThat(subject.isCaptured).isTrue()
    }

    @Test
    fun `set currentCoordinate to null when captured succeeds`() {
        // given
        val subject: Piece = DraughtsPiece(gameMock, coordinateMock, PlayerType.SECOND_PLAYER)
        subject.isCaptured = true
        // when
        subject.currentCoordinate = null
        // then
        assertThat(subject.currentCoordinate).isNull()
        assertThat(subject.isCaptured).isTrue()
    }

    @Test
    fun setCaptured() {
        // given
        val subject: Piece = DraughtsPiece(gameMock, coordinateMock, PlayerType.SECOND_PLAYER)
        assertThat(subject.isCaptured).isFalse()
        assertThat(subject.currentCoordinate).isEqualTo(coordinateMock)
        // when
        subject.isCaptured = true
        // then
        assertThat(subject.isCaptured).isTrue()
        assertThat(subject.currentCoordinate).isNull()
    }

    @Test
    fun `uncapturing should throw exception`() {
        // given
        val subject: Piece = DraughtsPiece(gameMock, coordinateMock, PlayerType.SECOND_PLAYER)
        subject.isCaptured = true
        assertThat(subject.isCaptured).isTrue()
        assertThat(subject.currentCoordinate).isNull()
        // when
        val exception = assertFails { subject.isCaptured = false }
        // then
        assertThat(exception is IllegalStateException).isTrue()
        assertThat(subject.isCaptured).isTrue()
        assertThat(subject.currentCoordinate).isNull()
    }

    @Test
    fun isEnemyPiece() {
        val firstPlayerPiece: Piece = DraughtsPiece(gameMock, coordinateMock, PlayerType.STARTING_PLAYER)
        val firstPlayerPieceMock: Piece = mockk()
        every { firstPlayerPieceMock.playerType } returns PlayerType.STARTING_PLAYER

        val secondPlayerPiece: Piece = DraughtsPiece(gameMock, coordinateMock, PlayerType.SECOND_PLAYER)
        val secondPlayerPieceMock: Piece = mockk()
        every { secondPlayerPieceMock.playerType } returns PlayerType.SECOND_PLAYER

        assertThat(firstPlayerPiece.isEnemyPiece(other = secondPlayerPiece)).isTrue()
        assertThat(firstPlayerPiece.isEnemyPiece(other = firstPlayerPieceMock)).isFalse()
        verify { firstPlayerPieceMock.playerType }

        assertThat(secondPlayerPiece.isEnemyPiece(other = firstPlayerPiece)).isTrue()
        assertThat(secondPlayerPiece.isEnemyPiece(other = secondPlayerPieceMock)).isFalse()
        verify { secondPlayerPieceMock.playerType }

        assertThat(firstPlayerPiece.isEnemyPiece(other = null)).isFalse()
        assertThat(secondPlayerPiece.isEnemyPiece(other = null)).isFalse()

        confirmVerified(firstPlayerPieceMock, secondPlayerPieceMock)
    }

    @Test
    @Disabled("Not yet implemented: test for isCrowned()")
    fun isCrowned() {
        // TODO
    }

    @Test
    @Disabled("Not yet implemented: test for setCrowned()")
    fun setCrowned() {
        // TODO
    }

    @Test
    @Disabled("Not yet implemented: test for compareTo()")
    fun compareTo() {
        // TODO
    }

    @Test
    @Disabled("Not yet implemented: test for testToString()")
    fun testToString() {
        // TODO
    }

    @Test
    @Disabled("Not yet implemented: test for getInitialCoordinate()")
    fun getInitialCoordinate() {
        // TODO
    }

    @Test
    @Disabled("Not yet implemented: test for getPlayerType()")
    fun getPlayerType() {
        // TODO
    }

    // This test may fail on some JDK's (e.g. Adopt OpenJDK 11 Hotspot and Adopt OpenJDK 16 OpenJ9)
    // with this error message:
    //   io.mockk.MockKException: Class cast exception happened. Probably type information was erased.
    //   In this case use `hint` before call to specify exact return type of a method.
    @Test
    fun format() {
        // given
        val gameMock: Game = mockk()
        val boardMock: Board = mockk()
        every { gameMock.board } returns(boardMock)
        val coordinateMock: PlayableCoordinate = mockk()
        val subject: Piece = DraughtsPiece(gameMock, coordinateMock, PlayerType.SECOND_PLAYER)
        val pieceFormatterMock: DraughtsFormatting<Piece, FormattableList> = mockk()
        val expected: FormattableList = mockk()
        every { pieceFormatterMock.format(subject) } returns(expected)

        // when
        @Suppress("UNCHECKED_CAST")
        val actual = subject.format(pieceFormatterMock as DraughtsFormatting<BoardElement, FormattableList>)

        // then
        verify { pieceFormatterMock.format(subject) }
        confirmVerified(pieceFormatterMock)
        assertThat(actual).isEqualTo(expected)

    }

    @Test
    fun testEquals() {
        // given
        val gameMock: Game = mockk("gameMock")
        val boardMock: Board = mockk("boardMock")
        every { gameMock.board } returns(boardMock)
        every { boardMock.squares } returns emptyMap()
        val initialCoordinateMock1: PlayableCoordinate = mockk()
        val initialCoordinateMock2: PlayableCoordinate = mockk()

        val subject: Piece = DraughtsPiece(gameMock, initialCoordinateMock1, PlayerType.SECOND_PLAYER)

        val equal: Piece = DraughtsPiece(gameMock, initialCoordinateMock1, PlayerType.SECOND_PLAYER)
        val equalInitialCoordinate: Piece = DraughtsPiece(gameMock, initialCoordinateMock1, PlayerType.STARTING_PLAYER)
        val notEqualInitialCoordinate: Piece = DraughtsPiece(gameMock, initialCoordinateMock2, PlayerType.SECOND_PLAYER)

        val anotherBoardMock: Board = mockk("another Board")
        val anotherGameMock: Game = mockk("another Game")
        every { anotherGameMock.board } returns anotherBoardMock
        val notSameBoard: Piece = DraughtsPiece(anotherGameMock, initialCoordinateMock1, PlayerType.STARTING_PLAYER)

        // when, then
        assertThat(subject).isEqualTo(equal)
        assertThat(subject).isEqualTo(equalInitialCoordinate)
        assertThat(subject).isNotEqualTo(notEqualInitialCoordinate)
        assertThat(subject).isNotEqualTo(notSameBoard)
    }

    @Test
    fun testHashCode() {
        // given
        val gameMock: Game = mockk()
        val boardMock: Board = mockk()
        every { gameMock.board } returns(boardMock)
        every { boardMock.squares } returns emptyMap()
        val initialCoordinateMock1: PlayableCoordinate = mockk()
        every { initialCoordinateMock1.hashCode() } returns 85
        val initialCoordinateMock2: PlayableCoordinate = mockk()
        every { initialCoordinateMock2.hashCode() } returns 17

        val subject1: Piece = DraughtsPiece(gameMock, initialCoordinateMock1, PlayerType.SECOND_PLAYER)
        val subject2: Piece = DraughtsPiece(gameMock, initialCoordinateMock2, PlayerType.STARTING_PLAYER)

        // when, then
        assertThat(subject1.hashCode()).isEqualTo(85)
        assertThat(subject2.hashCode()).isEqualTo(17)
    }

}