package nl.jhvh.draughts.model

import nl.jhvh.draughts.model.base.PlayableCoordinate
import nl.jhvh.draughts.model.base.PlayerType.SECOND_PLAYER
import nl.jhvh.draughts.model.base.PlayerType.STARTING_PLAYER
import nl.jhvh.draughts.model.base.boardLength
import nl.jhvh.draughts.model.base.boardWidth
import nl.jhvh.draughts.model.base.maxPiecePositionNumber
import nl.jhvh.draughts.model.base.piecesPerPlayer
import nl.jhvh.draughts.model.base.positionRange
import nl.jhvh.draughts.model.structure.Board
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * This is an integration test in the sense that it also tests some behaviour of the comprising [BoardElement]s
 * that are constructed and initialized on construction of the [DraughtsBoard], e.g. [DraughtsBoard.squares],
 * [DraughtsBoard.playableCoordinates], [DraughtsBoard.allPieces] etc.; these are not mocked, so strictly not
 * a unit tests.
 * * Mocking the dependencies with constructor is possible with `mockk`, but quite awkward, and makes things overly
 *   complicated and less relevant compared with the straightforward tests in this class, even if these cover more classes.
 * * See [DraughtsBoardTest] for tests of code that can be unit tested with dependencies mocked
 */
internal class DraughtsBoardInitializationTest {

    private val subject: Board = DraughtsBoard()

    @Test
    fun getPlayableCoordinates() {
        val coords = subject.playableCoordinates
        assertThat(coords.map { it.position }).containsExactlyInAnyOrderElementsOf(positionRange)
    }

    @Test
    fun getSquares() {
        val squares = subject.squares
        val expectedXY: Set<Pair<Int, Int>> = (0 until boardWidth)
            .map { x -> (0 until boardLength)
                .map { y -> Pair(x, y) }}
            .flatten().toSet()

        assertThat(squares.size).isEqualTo(boardWidth * boardLength)
        assertThat(squares.keys).isEqualTo(expectedXY)

        assertThat(squares.values.filter { it.squareType.playable }.size).isEqualTo(boardWidth * boardLength / 2)
        assertThat(squares.values.filter { !it.squareType.playable }.size).isEqualTo(boardWidth * boardLength / 2)

        squares.forEach {
            val square = it.value
            if (square.squareType.playable) {
                PlayableCoordinate(it.key) // playable, so should be possible
            }
            assertThat(it.key).isEqualTo(square.xy)
            assertThat(square.board).isSameAs(subject)
        }
    }

    @Test
    fun getDarkPieces() {
        val darkPieces = subject.darkPieces

        assertThat(darkPieces.size).isEqualTo(piecesPerPlayer)
        darkPieces.forEach { assertThat(it.playerType).isSameAs(SECOND_PLAYER) }
        assertThat(darkPieces.map { it.initialCoordinate.position })
            .containsExactlyInAnyOrderElementsOf(1..piecesPerPlayer)
    }

    @Test
    fun getLightPieces() {
        val lightPieces = subject.lightPieces
        val expectedPositions = (maxPiecePositionNumber - piecesPerPlayer + 1) .. maxPiecePositionNumber

        assertThat(lightPieces.size).isEqualTo(piecesPerPlayer)
        lightPieces.forEach { assertThat(it.playerType).isSameAs(STARTING_PLAYER) }
        assertThat(lightPieces.map { it.initialCoordinate.position })
            .containsExactlyInAnyOrderElementsOf(expectedPositions)
    }

    @Test
    fun getAllPieces() {
        val pieces = subject.allPieces
        assertThat(pieces.size).isEqualTo(piecesPerPlayer * 2)
        assertThat(pieces).containsExactlyInAnyOrderElementsOf(subject.lightPieces + subject.darkPieces)
    }

    @Test
    fun getBoard() {
        assertThat(subject.board).isSameAs(subject)
    }

    @Test
    fun `getPiece with Pair argument - occupied positions`() {
        (1..20).map { PlayableCoordinate(it).xy }.forEach { pair ->
            with(subject.getPiece(pair)!!) {
                assertThat(this.initialCoordinate.xy).isEqualTo(pair)
                assertThat(this.isCaptured).isFalse()
                // "black" is on positions 1..20
                assertThat(this.playerType).isSameAs(SECOND_PLAYER)
            }
        }
        (31..50).map { PlayableCoordinate(it).xy }.forEach { pair ->
            with(subject.getPiece(pair)!!) {
                assertThat(this.initialCoordinate.xy).isEqualTo(pair)
                assertThat(this.isCaptured).isFalse()
                // "white" is on positions 31..50
                assertThat(this.playerType).isSameAs(STARTING_PLAYER)
            }
        }
    }

    @Test
    fun `getPiece with Pair argument - non-occupied positions`() {
        (21..30).map { PlayableCoordinate(it).xy }.forEach { pair ->
            assertThat(subject.getPiece(pair)).isNull()
        }
    }

    @Test
    fun `getPiece with Pair argument - non-accessible positions`() {
        val nonAccessiblePositions: MutableSet<Pair<Int, Int>> = mutableSetOf()
        (0..9).forEach { x -> (0..9).forEach { y -> nonAccessiblePositions.add(Pair(x, y)) } }

        (1..20).forEach { nonAccessiblePositions.remove(PlayableCoordinate(it).xy) }
        (31..50).forEach { nonAccessiblePositions.remove(PlayableCoordinate(it).xy) }

        nonAccessiblePositions.forEach { pair ->
            assertThat(subject.getPiece(pair)).isNull()
        }
    }

}