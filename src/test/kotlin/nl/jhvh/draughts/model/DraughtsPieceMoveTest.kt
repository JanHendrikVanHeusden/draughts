package nl.jhvh.draughts.model

import io.mockk.clearMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import nl.jhvh.draughts.model.base.PlayableCoordinate
import nl.jhvh.draughts.model.base.PlayerType
import nl.jhvh.draughts.model.game.move.options.PieceMovementOption
import nl.jhvh.draughts.model.structure.Board
import nl.jhvh.draughts.model.structure.Piece
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class DraughtsPieceMoveTest {

    private val boardMock: Board = mockk()
    private val coordinateMock: PlayableCoordinate = mockk()

    @BeforeEach
    fun setUp() {
        clearMocks(boardMock, coordinateMock)
    }

    @Test
    @Disabled("Not yet implemented: test for move()")
    fun move() {
        // TODO
    }

    @Test
    @Disabled("Not yet implemented: test for allowedMoves()")
    fun allowedMoves() {
        // TODO
    }

    @Test
    @Disabled("Not yet implemented: test for possibleMoves()")
    fun possibleMoves() {
        // TODO
    }

    @Test
    @Disabled("Not yet implemented: test for addNonCapturingCrownedMoves()")
    fun addNonCapturingCrownedMoves() {
        // TODO
    }

    @Test
    @Disabled("Not yet implemented: test for addCapturingCrownedMoves()")
    fun addCapturingCrownedMoves() {
        // TODO
    }

    @Test
    fun `addNonCapturingMoves with 2 valid options`() {
        // given
        val coordinateMock: PlayableCoordinate = mockk()
        val currentX = 3
        val currentY = 5
        every { coordinateMock.x } returns currentX
        every { coordinateMock.y } returns currentY

        val movementOptionMock: PieceMovementOption = mockk()
        every { movementOptionMock.coordinate }.returns(coordinateMock)

        val subjectSpyk = spyk(DraughtsPiece(boardMock, coordinateMock, PlayerType.SECOND_PLAYER))

        every { subjectSpyk.canMoveTo(Pair(currentX-1, currentY-1)) }.returns(true)
        every { subjectSpyk.canMoveTo(Pair(currentX+1, currentY-1)) }.returns(true)
        val followingOptions: MutableList<PieceMovementOption> = mutableListOf()
        every { movementOptionMock.followingOptions }.returns(followingOptions)

        // when
        subjectSpyk.addNonCapturingMoves(movementOptionMock)

        // then
        assertThat(followingOptions).hasSize(2)
        followingOptions.forEach {
            assertThat(it.capturing).isNull()
            assertThat(it.parent).isEqualTo(movementOptionMock)
            assertThat(it.coordinate.y).isEqualTo(currentY -1)
        }
        assertThat(followingOptions.filter { it.coordinate.x == currentX - 1 }).hasSize(1)
        assertThat(followingOptions.filter { it.coordinate.x == currentX + 1 }).hasSize(1)
    }

    @Test
    fun `addNonCapturingMoves with 1 valid option`() {
        // given
        val coordinateMock: PlayableCoordinate = mockk()
        val currentX = 7
        val currentY = 3
        every { coordinateMock.x } returns currentX
        every { coordinateMock.y } returns currentY

        val movementOptionMock: PieceMovementOption = mockk()
        every { movementOptionMock.coordinate }.returns(coordinateMock)

        val subjectSpyk = spyk(DraughtsPiece(boardMock, coordinateMock, PlayerType.STARTING_PLAYER))

        every { subjectSpyk.canMoveTo(Pair(currentX-1, currentY+1)) }.returns(true)
        every { subjectSpyk.canMoveTo(Pair(currentX+1, currentY+1)) }.returns(false)
        val followingOptions: MutableList<PieceMovementOption> = mutableListOf()
        // to make sure it is not emptied somehow, we populate some stuff already
        val originalSize = 3
        repeat(originalSize) { followingOptions.add(mockk()) }
        every { movementOptionMock.followingOptions }.returns(followingOptions)

        // when
        subjectSpyk.addNonCapturingMoves(movementOptionMock)

        // then
        assertThat(followingOptions).hasSize(originalSize + 1)
        with(followingOptions[originalSize]) {
            assertThat(this.capturing).isNull()
            assertThat(this.parent).isEqualTo(movementOptionMock)
            assertThat(this.coordinate.y).isEqualTo(currentY + 1)
            assertThat(this.coordinate.x).isEqualTo(currentX-1)
        }
    }

    @Test
    fun `addNonCapturingMoves with no valid options`() {
        // given
        val coordinateMock: PlayableCoordinate = mockk()
        val currentX = 5
        val currentY = 1
        every { coordinateMock.x } returns currentX
        every { coordinateMock.y } returns currentY

        val movementOptionMock: PieceMovementOption = mockk()
        every { movementOptionMock.coordinate }.returns(coordinateMock)

        val subjectSpyk = spyk(DraughtsPiece(boardMock, coordinateMock, PlayerType.STARTING_PLAYER))

        every { subjectSpyk.canMoveTo(any()) }.returns(false)
        val followingOptions: MutableList<PieceMovementOption> = mutableListOf()
        // to make sure it is not emptied somehow, we populate some stuff already
        val originalSize = 5
        repeat(originalSize) { followingOptions.add(mockk()) }
        val reference = ArrayList(followingOptions)
        every { movementOptionMock.followingOptions }.returns(followingOptions)

        // when
        subjectSpyk.addNonCapturingMoves(movementOptionMock)

        // then
        assertThat(followingOptions).isEqualTo(reference) // nothing added or changed
    }

    @Test
    fun `addNonCapturingMoves on captured piece should do nothing`() {
        // given
        val coordinateMock: PlayableCoordinate = mockk()
        val currentX = 5
        val currentY = 1
        every { coordinateMock.x } returns currentX
        every { coordinateMock.y } returns currentY
        val movementOptionMock: PieceMovementOption = mockk()

        val subjectSpyk = spyk(DraughtsPiece(boardMock, coordinateMock, PlayerType.STARTING_PLAYER))
        subjectSpyk.isCaptured = true

        // when
        subjectSpyk.addNonCapturingMoves(movementOptionMock)

        // then
        verify { subjectSpyk.isCaptured }
        confirmVerified(coordinateMock, movementOptionMock)
    }

    @Test
    fun `addCapturingMoves with 4 valid options and no subsequent captures`() {
        // given
        val coordinateMock: PlayableCoordinate = mockk()
        val currentX = 3
        val currentY = 5
        every { coordinateMock.x } returns currentX
        every { coordinateMock.y } returns currentY

        val movementOptionMock: PieceMovementOption = mockk()
        every { movementOptionMock.coordinate }.returns(coordinateMock)

        val subjectSpyk = spyk(DraughtsPiece(boardMock, coordinateMock, PlayerType.SECOND_PLAYER))

        // 4 times true
        every { subjectSpyk.canMoveTo(any()) }.returns(false)
        every { subjectSpyk.canMoveTo(Pair(currentX+2, currentY+2)) }.returns(true)
        every { subjectSpyk.canMoveTo(Pair(currentX-2, currentY+2)) }.returns(true)
        every { subjectSpyk.canMoveTo(Pair(currentX+2, currentY-2)) }.returns(true)
        every { subjectSpyk.canMoveTo(Pair(currentX-2, currentY-2)) }.returns(true)

        every { subjectSpyk.enemyPiece(Pair(currentX+1, currentY+1)) }.returns(mockk())
        every { subjectSpyk.enemyPiece(Pair(currentX-1, currentY+1)) }.returns(mockk())
        every { subjectSpyk.enemyPiece(Pair(currentX+1, currentY-1)) }.returns(mockk())
        every { subjectSpyk.enemyPiece(Pair(currentX-1, currentY-1)) }.returns(mockk())

        val followingOptions: MutableList<PieceMovementOption> = mutableListOf()
        every { movementOptionMock.followingOptions }.returns(followingOptions)
        every { movementOptionMock.capturing }.returns(null)
        every { movementOptionMock.parent }.returns(null)

        // when
        subjectSpyk.addCapturingMoves(movementOptionMock)

        // then
        assertThat(followingOptions).hasSize(4)
        followingOptions.forEach {
            assertThat(it.capturing is Piece).isTrue()
            assertThat(it.parent).isEqualTo(movementOptionMock)
        }
        assertThat(followingOptions
            .filter { it.coordinate.x == currentX - 2 && it.coordinate.y == currentY + 2 })
            .hasSize(1)
        assertThat(followingOptions
            .filter { it.coordinate.x == currentX + 2 && it.coordinate.y == currentY + 2 })
            .hasSize(1)
        assertThat(followingOptions
            .filter { it.coordinate.x == currentX - 2 && it.coordinate.y == currentY - 2 })
            .hasSize(1)
        assertThat(followingOptions
            .filter { it.coordinate.x == currentX - 2 && it.coordinate.y == currentY - 2 })
            .hasSize(1)
    }

    @Test
    fun `addCapturingMoves with 3 accessible squares and 2 capturable enemy pieces`() {
        // given
        val coordinateMock: PlayableCoordinate = mockk()
        val currentX = 4
        val currentY = 4
        every { coordinateMock.x } returns currentX
        every { coordinateMock.y } returns currentY

        val movementOptionMock: PieceMovementOption = mockk()
        every { movementOptionMock.coordinate }.returns(coordinateMock)

        val subjectSpyk = spyk(DraughtsPiece(boardMock, coordinateMock, PlayerType.SECOND_PLAYER))

        every { subjectSpyk.canMoveTo(any()) }.returns(false)
        every { subjectSpyk.canMoveTo(Pair(currentX+2, currentY+2)) }.returns(true)
        every { subjectSpyk.canMoveTo(Pair(currentX-2, currentY+2)) }.returns(true)
        every { subjectSpyk.canMoveTo(Pair(currentX+2, currentY-2)) }.returns(true)

        val enemyPiece1: Piece = mockk()
        val enemyPiece2: Piece = mockk()
        every { subjectSpyk.enemyPiece(any()) }.returns(null)
        every { subjectSpyk.enemyPiece(Pair(currentX+1, currentY+1)) }.returns(enemyPiece1)
        every { subjectSpyk.enemyPiece(Pair(currentX+1, currentY-1)) }.returns(enemyPiece2)

        val followingOptions: MutableList<PieceMovementOption> = mutableListOf()
        every { movementOptionMock.followingOptions }.returns(followingOptions)
        every { movementOptionMock.capturing }.returns(null)
        every { movementOptionMock.parent }.returns(null)

        // when
        subjectSpyk.addCapturingMoves(movementOptionMock)

        // then
        assertThat(followingOptions).hasSize(2)
        followingOptions.forEach {
            assertThat(it.parent).isEqualTo(movementOptionMock)
        }
        assertThat(followingOptions.filter { it.coordinate.xy == Pair(currentX+2, currentY+2) }.first().capturing)
            .isEqualTo(enemyPiece1)
        assertThat(followingOptions.filter { it.coordinate.xy == Pair(currentX+2, currentY-2) }.first().capturing)
            .isEqualTo(enemyPiece2)
    }

    @Test
    fun `addCapturingMoves with 2 primary and 1 subsequent capture`() {
        // given
        val coordinateMock: PlayableCoordinate = mockk()
        val currentX = 4
        val currentY = 4
        every { coordinateMock.x } returns currentX
        every { coordinateMock.y } returns currentY

        val movementOptionMock: PieceMovementOption = mockk()
        every { movementOptionMock.coordinate }.returns(coordinateMock)

        val subjectSpyk = spyk(DraughtsPiece(boardMock, coordinateMock, PlayerType.SECOND_PLAYER))

        every { subjectSpyk.canMoveTo(any()) }.returns(false)
        // captures enemyPiece1
        every { subjectSpyk.canMoveTo(Pair(currentX+2, currentY+2)) }.returns(true)
        // no capture
        every { subjectSpyk.canMoveTo(Pair(currentX-2, currentY+2)) }.returns(true)
        // captures enemyPiece2 and subsequently enemyPiece3
        every { subjectSpyk.canMoveTo(Pair(currentX+2, currentY-2)) }.returns(true)
        every { subjectSpyk.canMoveTo(Pair(currentX+4, currentY-4)) }.returns(true)

        val enemyPiece1: Piece = mockk()
        val enemyPiece2: Piece = mockk()
        val enemyPiece3: Piece = mockk()
        every { subjectSpyk.enemyPiece(any()) }.returns(null)
        every { subjectSpyk.enemyPiece(Pair(currentX+1, currentY+1)) }.returns(enemyPiece1)
        every { subjectSpyk.enemyPiece(Pair(currentX+1, currentY-1)) }.returns(enemyPiece2)
        every { subjectSpyk.enemyPiece(Pair(currentX+3, currentY-3)) }.returns(enemyPiece3)

        val followingOptions: MutableList<PieceMovementOption> = mutableListOf()
        every { movementOptionMock.followingOptions }.returns(followingOptions)
        every { movementOptionMock.capturing }.returns(null)
        every { movementOptionMock.parent }.returns(null)

        // when
        subjectSpyk.addCapturingMoves(movementOptionMock)

        // then
        assertThat(followingOptions).hasSize(2)
        followingOptions.forEach {
            assertThat(it.parent).isEqualTo(movementOptionMock)
        }
        val firstCaptureOption = followingOptions.filter { it.coordinate.xy == Pair(currentX + 2, currentY + 2) }.first()
        assertThat(firstCaptureOption.capturing)
            .isEqualTo(enemyPiece1)
        val secondCaptureOption = followingOptions.filter { it.coordinate.xy == Pair(currentX + 2, currentY - 2) }.first()
        assertThat(secondCaptureOption.capturing)
            .isEqualTo(enemyPiece2)

        // subsequent capture
        assertThat(secondCaptureOption.followingOptions).hasSize(1)
    }

    @Test
    fun `addCapturingMoves with 2 primary, 1 secondary and 2 tertiary captures`() {
        // given
        val coordinateMock: PlayableCoordinate = mockk()
        val currentX = 3
        val currentY = 7
        every { coordinateMock.x } returns currentX
        every { coordinateMock.y } returns currentY

        val movementOptionMock: PieceMovementOption = mockk()
        every { movementOptionMock.coordinate }.returns(coordinateMock)

        val subjectSpyk = spyk(DraughtsPiece(boardMock, coordinateMock, PlayerType.SECOND_PLAYER))

        every { subjectSpyk.canMoveTo(any()) }.returns(false)
        // captures enemyPiece1
        every { subjectSpyk.canMoveTo(Pair(currentX+2, currentY+2)) }.returns(true)
        // no capture
        every { subjectSpyk.canMoveTo(Pair(currentX-2, currentY+2)) }.returns(true)
        // captures enemyPiece2 ...
        every { subjectSpyk.canMoveTo(Pair(currentX+2, currentY-2)) }.returns(true)
        // ... then enemyPiece3 ...
        every { subjectSpyk.canMoveTo(Pair(currentX+4, currentY-4)) }.returns(true)
        // ... then enemyPiece4 or enemyPiece5
        every { subjectSpyk.canMoveTo(Pair(currentX+6, currentY-6)) }.returns(true)
        every { subjectSpyk.canMoveTo(Pair(currentX+6, currentY-2)) }.returns(true)

        val enemyPiece1: Piece = mockk()
        val enemyPiece2: Piece = mockk()
        val enemyPiece3: Piece = mockk()
        val enemyPiece4: Piece = mockk()
        val enemyPiece5: Piece = mockk()
        every { subjectSpyk.enemyPiece(any()) }.returns(null)
        every { subjectSpyk.enemyPiece(Pair(currentX+1, currentY+1)) }.returns(enemyPiece1)
        every { subjectSpyk.enemyPiece(Pair(currentX+1, currentY-1)) }.returns(enemyPiece2)
        every { subjectSpyk.enemyPiece(Pair(currentX+3, currentY-3)) }.returns(enemyPiece3)
        every { subjectSpyk.enemyPiece(Pair(currentX+5, currentY-5)) }.returns(enemyPiece4)
        every { subjectSpyk.enemyPiece(Pair(currentX+5, currentY-3)) }.returns(enemyPiece5)

        val followingOptions: MutableList<PieceMovementOption> = mutableListOf()
        every { movementOptionMock.followingOptions }.returns(followingOptions)
        every { movementOptionMock.capturing }.returns(null)
        every { movementOptionMock.parent }.returns(null)

        // when
        subjectSpyk.addCapturingMoves(movementOptionMock)

        // then
        assertThat(followingOptions).hasSize(2)
        followingOptions.forEach {
            assertThat(it.parent).isEqualTo(movementOptionMock)
        }

        // primary captures
        val primaryOption1 = followingOptions.filter { it.coordinate.xy == Pair(currentX + 2, currentY + 2) }.first()
        assertThat(primaryOption1.capturing).isEqualTo(enemyPiece1)
        val primaryOption2 = followingOptions.filter { it.coordinate.xy == Pair(currentX + 2, currentY - 2) }.first()
        assertThat(primaryOption2.capturing).isEqualTo(enemyPiece2)

        // secondary capture
        assertThat(primaryOption2.followingOptions).hasSize(1)
        val secondaryOption = primaryOption2.followingOptions.first()
        assertThat(secondaryOption.capturing).isEqualTo(enemyPiece3)
        assertThat(secondaryOption.coordinate.xy).isEqualTo(Pair(currentX+4, currentY-4))
        assertThat(secondaryOption.parent).isEqualTo(primaryOption2)

        // tertiary capture
        val tertiaryOptions: MutableList<PieceMovementOption> = secondaryOption.followingOptions
        assertThat(tertiaryOptions).hasSize(2)

        val tertiaryOption1 = tertiaryOptions.filter { it.coordinate.xy == Pair(currentX+6, currentY-6) }.first()
        assertThat(tertiaryOption1.capturing).isEqualTo(enemyPiece4)
        assertThat(tertiaryOption1.followingOptions).isEmpty()
        assertThat(tertiaryOption1.parent).isEqualTo(secondaryOption)

        val tertiaryOption2 = tertiaryOptions.filter { it.coordinate.xy == Pair(currentX+6, currentY-2) }.first()
        assertThat(tertiaryOption2.capturing).isEqualTo(enemyPiece5)
        assertThat(tertiaryOption2.followingOptions).isEmpty()
        assertThat(tertiaryOption2.parent).isEqualTo(secondaryOption)
    }

    @Test
    fun `addCapturingMoves on captured piece should do nothing`() {
        // given
        val coordinateMock: PlayableCoordinate = mockk()
        val currentX = 3
        val currentY = 7
        every { coordinateMock.x } returns currentX
        every { coordinateMock.y } returns currentY
        val movementOptionMock: PieceMovementOption = mockk()

        val subjectSpyk = spyk(DraughtsPiece(boardMock, coordinateMock, PlayerType.STARTING_PLAYER))
        subjectSpyk.isCaptured = true
        clearMocks(subjectSpyk)

        // when
        subjectSpyk.addCapturingMoves(movementOptionMock)

        // then
        verify { subjectSpyk.addCapturingMoves(movementOptionMock) }
        verify { subjectSpyk.isCaptured }

        confirmVerified(subjectSpyk, coordinateMock, movementOptionMock)
    }

}