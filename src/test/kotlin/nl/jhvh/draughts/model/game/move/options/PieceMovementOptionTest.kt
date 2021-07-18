package nl.jhvh.draughts.model.game.move.options

import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import nl.jhvh.draughts.model.DraughtsPiece
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFails

internal class PieceMovementOptionTest {

    private val pieceMock: DraughtsPiece = mockk()
    private val subject = PieceMovementOption(pieceMock, mockk())

    private val primaryOption1 = PieceMovementOption(pieceMock, mockk(), subject)
    private val secondaryOption1_1 = PieceMovementOption(pieceMock, mockk(), primaryOption1)

    private val primaryOption2 = PieceMovementOption(pieceMock, mockk(), subject)

    private val secondaryOption2_1 = PieceMovementOption(pieceMock, mockk(), primaryOption2)
    private val tertiaryOption2_1_2 = PieceMovementOption(pieceMock, mockk(), secondaryOption2_1)

    private val secondaryOption2_2 = PieceMovementOption(pieceMock, mockk(), primaryOption2)

    private val primaryOption3 = PieceMovementOption(pieceMock, mockk(), subject)

    @BeforeEach
    fun setUp() {
        resetMocks()

        secondaryOption1_1.capturing = mockk()
        primaryOption2.capturing = mockk()
        secondaryOption2_1.capturing = mockk()
        tertiaryOption2_1_2.capturing = mockk()
        secondaryOption2_2.capturing = mockk()

        // to help trouble finding in case of test failures
        every { subject.coordinate.toString() }.returns("optionsRoot")
        every { primaryOption1.coordinate.toString() }.returns("primaryOption1")
        every { secondaryOption1_1.coordinate.toString() }.returns("secondaryOption1_1")
        every { primaryOption2.coordinate.toString() }.returns("primaryOption2")
        every { secondaryOption2_1.coordinate.toString() }.returns("secondaryOption2_1")
        every { tertiaryOption2_1_2.coordinate.toString() }.returns("tertiaryOption2_1_2")
        every { secondaryOption2_2.coordinate.toString() }.returns("secondaryOption2_2")
        every { primaryOption3.coordinate.toString() }.returns("primaryOption3")
    }

    @Test
    fun toMovementChains() {
        // given, when
        val result = subject.toMovementChains()
        // then
        assertThat(result).hasSize(4)

        val sortedResult = result.sorted()
        assertThat(sortedResult.map { it.captureCount }.toList()).isEqualTo(listOf(3, 2, 1, 0))

        var movementChain = result.filter { chain -> chain.moves.map { move -> move.to }.contains(secondaryOption1_1.coordinate) }.first()
        assertThat(movementChain.captureCount).isEqualTo(1)
        assertThat(movementChain.moves).hasSize(2)
        assertThat(movementChain.moves[0].from).isEqualTo(subject.coordinate)
        assertThat(movementChain.moves[0].to).isEqualTo(primaryOption1.coordinate)
        assertThat(movementChain.moves[0].capturing).isNull()
        assertThat(movementChain.moves[1].from).isEqualTo(primaryOption1.coordinate)
        assertThat(movementChain.moves[1].to).isEqualTo(secondaryOption1_1.coordinate)
        assertThat(movementChain.moves[1].capturing).isNotNull().isEqualTo(secondaryOption1_1.capturing)

        movementChain = result.filter { chain -> chain.moves.map { move -> move.to }.contains(tertiaryOption2_1_2.coordinate) }.first()
        assertThat(movementChain.captureCount).isEqualTo(3)
        assertThat(movementChain.moves).hasSize(3)
        assertThat(movementChain.moves[0].from).isEqualTo(subject.coordinate)
        assertThat(movementChain.moves[0].to).isEqualTo(primaryOption2.coordinate)
        assertThat(movementChain.moves[0].capturing).isNotNull().isEqualTo(primaryOption2.capturing)
        assertThat(movementChain.moves[1].from).isEqualTo(primaryOption2.coordinate)
        assertThat(movementChain.moves[1].to).isEqualTo(secondaryOption2_1.coordinate)
        assertThat(movementChain.moves[1].capturing).isNotNull().isEqualTo(secondaryOption2_1.capturing)
        assertThat(movementChain.moves[2].from).isEqualTo(secondaryOption2_1.coordinate)
        assertThat(movementChain.moves[2].to).isEqualTo(tertiaryOption2_1_2.coordinate)
        assertThat(movementChain.moves[2].capturing).isNotNull().isEqualTo(tertiaryOption2_1_2.capturing)

        movementChain = result.filter { chain -> chain.moves.map { move -> move.to }.contains(secondaryOption2_2.coordinate) }.first()
        assertThat(movementChain.captureCount).isEqualTo(2)
        assertThat(movementChain.moves).hasSize(2)
        assertThat(movementChain.moves[0].from).isEqualTo(subject.coordinate)
        assertThat(movementChain.moves[0].to).isEqualTo(primaryOption2.coordinate)
        assertThat(movementChain.moves[0].capturing).isNotNull().isEqualTo(primaryOption2.capturing)
        assertThat(movementChain.moves[1].from).isEqualTo(primaryOption2.coordinate)
        assertThat(movementChain.moves[1].to).isEqualTo(secondaryOption2_2.coordinate)
        assertThat(movementChain.moves[1].capturing).isNotNull().isEqualTo(secondaryOption2_2.capturing)

        movementChain = result.filter { chain -> chain.moves.map { move -> move.to }.contains(primaryOption3.coordinate) }.first()
        assertThat(movementChain.captureCount).isEqualTo(0)
        assertThat(movementChain.moves).hasSize(1)
        assertThat(movementChain.moves[0].from).isEqualTo(subject.coordinate)
        assertThat(movementChain.moves[0].capturing).isNull()
    }

    @Test
    fun findLeafNodes() {
        // given
        val leafNodes: MutableSet<PieceMovementOption> = mutableSetOf()
        // when
        subject.findLeafNodes(leafNodes)
        // then
        assertThat(leafNodes).containsExactlyInAnyOrder(
            primaryOption3, secondaryOption1_1, tertiaryOption2_1_2, secondaryOption2_2)
    }

    @Test
    fun `when toMovementChains is called on a non-root option an exception is thrown`() {
        subject.toMovementChains() // OK
        assertThat ((assertFails { primaryOption3.toMovementChains() }) is IllegalStateException).isTrue()
        assertThat ((assertFails { secondaryOption2_1.toMovementChains() }) is IllegalStateException).isTrue()
    }

    private fun resetMocks() {
        clearMocks(
            pieceMock,
            subject.coordinate,
            primaryOption1.coordinate,
            secondaryOption1_1.coordinate,
            primaryOption2.coordinate,
            secondaryOption2_1.coordinate,
            tertiaryOption2_1_2.coordinate,
            secondaryOption2_2.coordinate,
            primaryOption3.coordinate,

            answers = true,
            recordedCalls = true
        )
    }

}