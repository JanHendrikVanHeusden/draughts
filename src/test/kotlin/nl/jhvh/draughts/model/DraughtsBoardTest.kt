package nl.jhvh.draughts.model

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import nl.jhvh.draughts.formatting.DraughtsFormatting
import nl.jhvh.draughts.formatting.textformat.FormattableList
import nl.jhvh.draughts.model.base.BoardElement
import nl.jhvh.draughts.model.structure.Board
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

    // This test may fail on some JDK's (e.g. Adopt OpenJDK 11 Hotspot and Adopt OpenJDK 16 OpenJ9)
    // with this error message:
    //   io.mockk.MockKException: Class cast exception happened. Probably type information was erased.
    //   In this case use `hint` before call to specify exact return type of a method.
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