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
        val expectedXY: Set<Pair<Int, Int>> =
            (0 until boardWidth).map { x -> (0 until boardLength).map { y -> Pair(x, y) }}
                .flatten()
                .toSet()

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
    fun getBoard() {
        assertThat(subject.board).isSameAs(subject)
    }

}