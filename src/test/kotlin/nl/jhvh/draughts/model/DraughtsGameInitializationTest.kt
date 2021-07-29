package nl.jhvh.draughts.model

import nl.jhvh.draughts.model.base.PlayableCoordinate
import nl.jhvh.draughts.model.base.PlayerType.SECOND_PLAYER
import nl.jhvh.draughts.model.base.PlayerType.STARTING_PLAYER
import nl.jhvh.draughts.model.base.boardLength
import nl.jhvh.draughts.model.base.boardWidth
import nl.jhvh.draughts.model.base.maxPiecePositionNumber
import nl.jhvh.draughts.model.base.piecesPerPlayer
import nl.jhvh.draughts.model.base.positionRange
import nl.jhvh.draughts.model.game.DraughtsGame
import nl.jhvh.draughts.model.game.Game
import nl.jhvh.draughts.model.structure.Board
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * This is an integration test in the sense that it also tests some behaviour of the comprising [BoardElement]s
 * that are constructed and initialized on construction of the [DraughtsGame], e.g. [DraughtsGame.startingPlayerPieces],
 * [DraughtsGame.secondPlayerPieces], [DraughtsGame.playerTypeInTurn] etc.; these are not mocked, so strictly not
 * a unit tests.
 * * Mocking the dependencies with constructor is possible with `mockk`, but quite awkward, and makes things overly
 *   complicated and less relevant compared with the straightforward tests in this class, even if these cover more classes.
 * * See [DraughtsGameTest] for tests of code that can be unit tested with dependencies mocked
 */
internal class DraughtsGameInitializationTest {

    private val subject: Game = DraughtsGame()

    @Test
    fun `get initial squares with or without pieces`() {
        val squares = subject.squares
        val expectedSquaresWithDarkPieces: Set<Pair<Int, Int>> = (1 .. piecesPerPlayer).map { PlayableCoordinate(it).xy }.toSet()
        val expectedSquaresWithWhitePieces: Set<Pair<Int, Int>> =
            (maxPiecePositionNumber - piecesPerPlayer+1..maxPiecePositionNumber).map { PlayableCoordinate(it).xy }.toSet()

        expectedSquaresWithDarkPieces.forEach { assertThat(squares[it]!!.piece!!.playerType).isEqualTo(SECOND_PLAYER) }
        expectedSquaresWithWhitePieces.forEach { assertThat(squares[it]!!.piece!!.playerType).isEqualTo(STARTING_PLAYER) }
        // squares without pieces
        (squares.keys - expectedSquaresWithDarkPieces - expectedSquaresWithWhitePieces)
            .forEach { assertThat(squares[it]!!.piece).isNull() }
    }

    @Test
    fun getDarkPieces() {
        val darkPieces = subject.allPiecesByPlayerType[SECOND_PLAYER]!!

        assertThat(darkPieces.size).isEqualTo(piecesPerPlayer)
        darkPieces.forEach { assertThat(it.playerType).isSameAs(SECOND_PLAYER) }
        assertThat(darkPieces.map { it.initialCoordinate.position })
            .containsExactlyInAnyOrderElementsOf(1..piecesPerPlayer)
    }

    @Test
    fun getLightPieces() {
        val lightPieces = subject.allPiecesByPlayerType[STARTING_PLAYER]!!
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
        assertThat(pieces).containsExactlyInAnyOrderElementsOf(subject.allPiecesByPlayerType.values.flatten())
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