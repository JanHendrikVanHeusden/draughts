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
 * Note that a lot of stuff is done when constructing / initializing the [DraughtsBoard] and [DraughtsGame].
 * Tests covering construction and initialization are covered by separate tests [DraughtsBoardInitializationTest]
 * and [DraughtsGameInitializationTest]
 */
internal class DraughtsBoardTest {

    private val subject: Board = DraughtsBoard()

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

}