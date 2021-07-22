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
    fun `addNonCapturingCrownedMoves with 3 valid single square options`() {
        // given
        val coordinateMock: PlayableCoordinate = mockk()
        val currentX = 3
        val currentY = 5
        every { coordinateMock.x } returns currentX
        every { coordinateMock.y } returns currentY
        every { coordinateMock.xy } returns Pair(currentX, currentY)

        val movementOptionMock: PieceMovementOption = mockk()
        every { movementOptionMock.coordinate }.returns(coordinateMock)

        val subjectSpyk = spyk(DraughtsPiece(boardMock, coordinateMock, PlayerType.SECOND_PLAYER))

        every { subjectSpyk.isCrowned }.returns(true)

        every { subjectSpyk.enemyPiece(any()) }.returns(null)
        every { subjectSpyk.canMoveTo(any()) }.returns(false)
        every { subjectSpyk.canMoveTo(Pair(currentX-1, currentY-1)) }.returns(true)
        every { subjectSpyk.canMoveTo(Pair(currentX+1, currentY-1)) }.returns(true)
        every { subjectSpyk.canMoveTo(Pair(currentX+1, currentY+1)) }.returns(true)
        val followingOptions: MutableList<PieceMovementOption> = mutableListOf()
        every { movementOptionMock.followingOptions }.returns(followingOptions)

        // when
        subjectSpyk.addNonCapturingCrownedMoves(movementOptionMock)

        // then
        assertThat(followingOptions).hasSize(3)
        followingOptions.forEach {
            assertThat(it.capturing).isNull()
            assertThat(it.parent).isEqualTo(movementOptionMock)
            assertThat(it.followingOptions).isEmpty()
        }
        assertThat(followingOptions.filter { it.coordinate.xy == Pair(currentX-1, currentY-1) }).hasSize(1)
        assertThat(followingOptions.filter { it.coordinate.xy == Pair(currentX+1, currentY-1) }).hasSize(1)
        assertThat(followingOptions.filter { it.coordinate.xy == Pair(currentX+1, currentY+1) }).hasSize(1)
    }

    @Test
    fun `addNonCapturingCrownedMoves with options of different length`() {
        // given
        val coordinateMock: PlayableCoordinate = mockk()
        val currentX = 3
        val currentY = 5
        every { coordinateMock.x } returns currentX
        every { coordinateMock.y } returns currentY
        every { coordinateMock.xy } returns Pair(currentX, currentY)

        val movementOptionMock: PieceMovementOption = mockk()
        every { movementOptionMock.coordinate }.returns(coordinateMock)

        val subjectSpyk = spyk(DraughtsPiece(boardMock, coordinateMock, PlayerType.SECOND_PLAYER))

        every { subjectSpyk.isCrowned }.returns(true)

        every { subjectSpyk.enemyPiece(any()) }.returns(null)
        every { subjectSpyk.canMoveTo(any()) }.returns(false)
        every { subjectSpyk.canMoveTo(Pair(currentX-1, currentY-1)) }.returns(true)
        every { subjectSpyk.canMoveTo(Pair(currentX-2, currentY+2)) }.returns(true) // must not be chosen
        every { subjectSpyk.canMoveTo(Pair(currentX-2, currentY-2)) }.returns(true)
        every { subjectSpyk.canMoveTo(Pair(currentX-3, currentY-1)) }.returns(true) // must not be chosen
        every { subjectSpyk.canMoveTo(Pair(currentX+1, currentY+1)) }.returns(true)
        every { subjectSpyk.canMoveTo(Pair(currentX+2, currentY+2)) }.returns(true)
        every { subjectSpyk.canMoveTo(Pair(currentX+3, currentY+3)) }.returns(true)
        every { subjectSpyk.canMoveTo(Pair(currentX+1, currentY+3)) }.returns(true) // must not be chosen
        val followingOptions: MutableList<PieceMovementOption> = mutableListOf()
        every { movementOptionMock.followingOptions }.returns(followingOptions)

        // when
        subjectSpyk.addNonCapturingCrownedMoves(movementOptionMock)

        // then
        assertThat(followingOptions).hasSize(5)
        followingOptions.forEach {
            assertThat(it.capturing).isNull()
            assertThat(it.parent).isEqualTo(movementOptionMock)
            assertThat(it.followingOptions).isEmpty() // just simple moves of different lengths, no captures
        }
        assertThat(followingOptions.filter { it.coordinate.xy == Pair(currentX-1, currentY-1) }).hasSize(1)
        assertThat(followingOptions.filter { it.coordinate.xy == Pair(currentX-2, currentY-2) }).hasSize(1)
        assertThat(followingOptions.filter { it.coordinate.xy == Pair(currentX+1, currentY+1) }).hasSize(1)
        assertThat(followingOptions.filter { it.coordinate.xy == Pair(currentX+2, currentY+2) }).hasSize(1)
        assertThat(followingOptions.filter { it.coordinate.xy == Pair(currentX+3, currentY+3) }).hasSize(1)
    }

    @Test
    fun `addCapturingCrownedMoves with different capturing options`() {
        // given
        val coordinateMock: PlayableCoordinate = mockk()
        val currentX = 4
        val currentY = 0
        every { coordinateMock.x } returns currentX
        every { coordinateMock.y } returns currentY
        every { coordinateMock.xy } returns Pair(currentX, currentY)

        val subjectSpyk = spyk(DraughtsPiece(boardMock, coordinateMock, PlayerType.SECOND_PLAYER))

        every { subjectSpyk.isCrowned }.returns(true)

        every { subjectSpyk.enemyPiece(any()) }.returns(null)
        every { subjectSpyk.canMoveTo(any()) }.returns(false)

        // left forward - not returned, because not capturing
        every { subjectSpyk.canMoveTo(Pair(currentX-1, currentY+1)) }.returns(true)
        every { subjectSpyk.canMoveTo(Pair(currentX-2, currentY+2)) }.returns(true)
        every { subjectSpyk.enemyPiece(Pair(currentX+3, currentY+3)) }.returns(mockk()) // not captured
        // No open field beyond

        // right forward, 2 empty fields, then 1st capture
        every { subjectSpyk.canMoveTo(Pair(currentX+1, currentY+1)) }.returns(true)
        every { subjectSpyk.canMoveTo(Pair(currentX+2, currentY+2)) }.returns(true)
        val `enemyPiece+3+3`: Piece = let { mockk<Piece>(name = "enemyPiece+3+3") }
            .also { every { subjectSpyk.enemyPiece(Pair(currentX+3, currentY+3)) }.returns(it) }
        // 2 more empty fields beyond 1st capture
        every { subjectSpyk.canMoveTo(Pair(currentX+4, currentY+4)) }.returns(true) // Field beyond capture
        every { subjectSpyk.canMoveTo(Pair(currentX+5, currentY+5)) }.returns(true) // Field beyond capture

        // On first field beyond capture, a 2nd capture is possible, left forward
        val `enemyPiece+3+5`: Piece = let { mockk<Piece>(name = "enemyPiece+3+5") }
            .also { every { subjectSpyk.enemyPiece(Pair(currentX+3, currentY+5)) }.returns(it) }
        // 2 more empty fields beyond 2nd capture
        every { subjectSpyk.canMoveTo(Pair(currentX+2, currentY+6)) }.returns(true) // Field beyond capture
        every { subjectSpyk.canMoveTo(Pair(currentX+1, currentY+7)) }.returns(true) // Field beyond capture
        every { subjectSpyk.canMoveTo(Pair(currentX, currentY+8)) }.returns(true) // Field beyond capture

        val followingOptions: MutableList<PieceMovementOption> = mutableListOf()

        val movementOptionMock: PieceMovementOption = mockk()
        every { movementOptionMock.coordinate }.returns(coordinateMock)
        every { movementOptionMock.followingOptions }.returns(followingOptions)
        every { movementOptionMock.capturing }.returns(null)
        every { (movementOptionMock.parent) }.returns(null)

        // when
        subjectSpyk.addCapturingCrownedMoves(movementOptionMock)

        // then
        assertThat(followingOptions).hasSize(2)

        with (followingOptions.filter { it.coordinate.xy == Pair(currentX+4, currentY+4) }) {
            assertThat(this.first().capturing).isEqualTo(`enemyPiece+3+3`)
            val followingOptionsAfterCapture = this.first().followingOptions

            assertThat(followingOptionsAfterCapture).hasSize(3)
            with (followingOptionsAfterCapture.filter { it.coordinate.xy == Pair(currentX+2, currentY+6) }.first()) {
                assertThat(this.capturing).isEqualTo(`enemyPiece+3+5`)
                assertThat(this.followingOptions).isEmpty()
            }
            with (followingOptionsAfterCapture.filter { it.coordinate.xy == Pair(currentX+1, currentY+7) }.first()) {
                assertThat(this.capturing).isEqualTo(`enemyPiece+3+5`)
                assertThat(this.followingOptions).isEmpty()
            }
            with (followingOptionsAfterCapture.filter { it.coordinate.xy == Pair(currentX, currentY+8) }.first()) {
                assertThat(this.capturing).isEqualTo(`enemyPiece+3+5`)
                assertThat(this.followingOptions).isEmpty()
            }
        }

        with (followingOptions.filter { it.coordinate.xy == Pair(currentX+5, currentY+5) }) {
            assertThat(this.first().capturing).isEqualTo(`enemyPiece+3+3`)
            assertThat(this.first().followingOptions).isEmpty()
        }
    }

    @Test
    fun `addNonCapturingMoves with 2 valid options`() {
        // given
        val coordinateMock: PlayableCoordinate = mockk()
        val currentX = 3
        val currentY = 5
        every { coordinateMock.x } returns currentX
        every { coordinateMock.y } returns currentY
        every { coordinateMock.xy } returns Pair(currentX, currentY)

        val movementOptionMock: PieceMovementOption = mockk()
        every { movementOptionMock.coordinate }.returns(coordinateMock)

        val subjectSpyk = spyk(DraughtsPiece(boardMock, coordinateMock, PlayerType.SECOND_PLAYER))

        every { subjectSpyk.canMoveTo(Pair(currentX-1, currentY-1)) }.returns(true)
        every { subjectSpyk.canMoveTo(Pair(currentX+1, currentY-1)) }.returns(true)
        val followingOptions: MutableList<PieceMovementOption> = mutableListOf()
        every { movementOptionMock.followingOptions }.returns(followingOptions)

        // when
        subjectSpyk.addNonCapturingNonCrownedMoves(movementOptionMock)

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
        every { coordinateMock.xy } returns Pair(currentX, currentY)

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
        subjectSpyk.addNonCapturingNonCrownedMoves(movementOptionMock)

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
        every { coordinateMock.xy } returns Pair(currentX, currentY)

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
        subjectSpyk.addNonCapturingNonCrownedMoves(movementOptionMock)

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
        every { coordinateMock.position }.returns(PlayableCoordinate(currentX, currentY).position)
        val movementOptionMock: PieceMovementOption = mockk()

        val subjectSpyk = spyk(DraughtsPiece(boardMock, coordinateMock, PlayerType.STARTING_PLAYER))
        subjectSpyk.isCaptured = true

        // when
        subjectSpyk.addNonCapturingNonCrownedMoves(movementOptionMock)

        // then
        verify { subjectSpyk.isCaptured }
        verify { coordinateMock.position }
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
        every { coordinateMock.xy } returns Pair(currentX, currentY)

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
        subjectSpyk.addCapturingNonCrownedMoves(movementOptionMock)

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
        every { coordinateMock.xy } returns Pair(currentX, currentY)

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
        subjectSpyk.addCapturingNonCrownedMoves(movementOptionMock)

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
        every { coordinateMock.xy } returns Pair(currentX, currentY)

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
        subjectSpyk.addCapturingNonCrownedMoves(movementOptionMock)

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
        every { coordinateMock.xy } returns Pair(currentX, currentY)

        val movementOptionMock: PieceMovementOption = mockk()

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

        every { subjectSpyk.toString() }.returns("playingPiece")
        val enemyPieceMock1: Piece = mockk()
        val enemyPieceMock2: Piece = mockk()
        val enemyPieceMock3: Piece = mockk()
        val enemyPieceMock4: Piece = mockk()
        val enemyPieceMock5: Piece = mockk()
        every { enemyPieceMock1.toString() }.returns("enemyPiece1")
        every { enemyPieceMock2.toString() }.returns("enemyPiece2")
        every { enemyPieceMock3.toString() }.returns("enemyPiece3")
        every { enemyPieceMock4.toString() }.returns("enemyPiece4")
        every { enemyPieceMock5.toString() }.returns("enemyPiece5")

        every { subjectSpyk.enemyPiece(any()) }.returns(null)
        every { subjectSpyk.enemyPiece(Pair(currentX+1, currentY+1)) }.returns(enemyPieceMock1)
        every { subjectSpyk.enemyPiece(Pair(currentX+1, currentY-1)) }.returns(enemyPieceMock2)
        every { subjectSpyk.enemyPiece(Pair(currentX+3, currentY-3)) }.returns(enemyPieceMock3)
        every { subjectSpyk.enemyPiece(Pair(currentX+5, currentY-5)) }.returns(enemyPieceMock4)
        every { subjectSpyk.enemyPiece(Pair(currentX+5, currentY-3)) }.returns(enemyPieceMock5)

        val followingOptions: MutableList<PieceMovementOption> = mutableListOf()
        every { movementOptionMock.piece }.returns(subjectSpyk)
        every { movementOptionMock.parent }.returns(null)
        every { movementOptionMock.coordinate }.returns(coordinateMock)
        every { movementOptionMock.capturing }.returns(null)
        every { movementOptionMock.followingOptions }.returns(followingOptions)

        // when
        subjectSpyk.addCapturingNonCrownedMoves(movementOptionMock)

        // then
        assertThat(followingOptions).hasSize(2)
        followingOptions.forEach {
            assertThat(it.parent).isEqualTo(movementOptionMock)
        }

        // primary captures
        val primaryOption1 = followingOptions.filter { it.coordinate.xy == Pair(currentX + 2, currentY + 2) }.first()
        assertThat(primaryOption1.capturing).isEqualTo(enemyPieceMock1)
        val primaryOption2 = followingOptions.filter { it.coordinate.xy == Pair(currentX + 2, currentY - 2) }.first()
        assertThat(primaryOption2.capturing).isEqualTo(enemyPieceMock2)

        // secondary capture
        assertThat(primaryOption2.followingOptions).hasSize(1)
        val secondaryOption = primaryOption2.followingOptions.first()
        assertThat(secondaryOption.capturing).isEqualTo(enemyPieceMock3)
        assertThat(secondaryOption.coordinate.xy).isEqualTo(Pair(currentX+4, currentY-4))
        assertThat(secondaryOption.parent).isEqualTo(primaryOption2)

        // tertiary capture
        val tertiaryOptions: MutableList<PieceMovementOption> = secondaryOption.followingOptions
        assertThat(tertiaryOptions).hasSize(2)

        val tertiaryOption1 = tertiaryOptions.filter { it.coordinate.xy == Pair(currentX+6, currentY-6) }.first()
        assertThat(tertiaryOption1.capturing).isEqualTo(enemyPieceMock4)
        assertThat(tertiaryOption1.followingOptions).isEmpty()
        assertThat(tertiaryOption1.parent).isEqualTo(secondaryOption)

        val tertiaryOption2 = tertiaryOptions.filter { it.coordinate.xy == Pair(currentX+6, currentY-2) }.first()
        assertThat(tertiaryOption2.capturing).isEqualTo(enemyPieceMock5)
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
        every { coordinateMock.position }.returns(PlayableCoordinate(currentX, currentY).position)
        val movementOptionMock: PieceMovementOption = mockk()

        val subjectSpyk = spyk(DraughtsPiece(boardMock, coordinateMock, PlayerType.STARTING_PLAYER))
        subjectSpyk.isCaptured = true
        clearMocks(subjectSpyk)

        // when
        subjectSpyk.addCapturingNonCrownedMoves(movementOptionMock)

        // then
        verify { subjectSpyk.addCapturingNonCrownedMoves(movementOptionMock) }
        verify { subjectSpyk.isCaptured }
        verify { coordinateMock.position }

        confirmVerified(subjectSpyk, coordinateMock, movementOptionMock)
    }

}