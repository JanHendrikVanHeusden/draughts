package nl.jhvh.draughts.model

import nl.jhvh.draughts.model.base.Coordinate
import nl.jhvh.draughts.model.base.boardLength
import nl.jhvh.draughts.model.base.boardWidth
import nl.jhvh.draughts.model.base.maxPiecePositionNumber
import nl.jhvh.draughts.model.base.minPiecePositionNumber
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class CoordinateTest {

    @BeforeEach
    fun setUp() {
        assertThat(boardWidth).isEqualTo(10).`as`("Tests in this class assume that board width = 10")
        assertThat(boardLength).isEqualTo(10).`as`("Tests in this class assume that board length = 10")
    }

    @Test
    fun `test Coordinates of playable fields`() {
        assertThat(Coordinate(1, 9).position).isEqualTo(1)
        assertThat(Coordinate(3, 9).position).isEqualTo(2)
        assertThat(Coordinate(7, 9).position).isEqualTo(4)
        assertThat(Coordinate(2, 8).position).isEqualTo(7)
        assertThat(Coordinate(8, 8).position).isEqualTo(10)
        assertThat(Coordinate(0, 8).position).isEqualTo(6)
        assertThat(Coordinate(1, 7).position).isEqualTo(11)
        assertThat(Coordinate(1, 1).position).isEqualTo(41)
        assertThat(Coordinate(3, 1).position).isEqualTo(42)
        assertThat(Coordinate(6, 0).position).isEqualTo(49)
        assertThat(Coordinate(8, 0).position).isEqualTo(50)
    }

    @Test
    fun `constructing Coordinate of non-playable fields should throw IllegalArgumentException`() {
        var exception: IllegalArgumentException

        exception = assertThrows { Coordinate(0, 9) }
        assertThat(exception.message).isEqualTo("Given values x = 0 and y = 9 indicate a non-playable position")
        exception = assertThrows { Coordinate(4, 1) }
        assertThat(exception.message).isEqualTo("Given values x = 4 and y = 1 indicate a non-playable position")

        assertThrows<IllegalArgumentException> { Coordinate(4, 9) }
        assertThrows<IllegalArgumentException> { Coordinate(8, 9) }
        assertThrows<IllegalArgumentException> { Coordinate(1, 8) }
        assertThrows<IllegalArgumentException> { Coordinate(7, 8) }
        assertThrows<IllegalArgumentException> { Coordinate(1, 8) }
        assertThrows<IllegalArgumentException> { Coordinate(0, 7) }
        assertThrows<IllegalArgumentException> { Coordinate(2, 1) }
        assertThrows<IllegalArgumentException> { Coordinate(7, 0) }
        assertThrows<IllegalArgumentException> { Coordinate(9, 0) }
    }

    @Test
    fun `constructing Coordinate with indexes outside board should throw IllegalArgumentException`() {
        var exception: IllegalArgumentException

        exception = assertThrows { Coordinate(-1, 5) }
        assertThat(exception.message).isEqualTo("x coordinate should be zero or positive, but is -1" )
        exception = assertThrows { Coordinate(-1, -9) }
        assertThat(exception.message).isEqualTo("x coordinate should be zero or positive, but is -1" )
        exception = assertThrows { Coordinate(4, -1) }
        assertThat(exception.message).isEqualTo("y coordinate should be zero or positive, but is -1")
        exception = assertThrows { Coordinate(-8, 3) }
        assertThat(exception.message).isEqualTo("x coordinate should be zero or positive, but is -8" )
        exception = assertThrows { Coordinate(5, -17) }
        assertThat(exception.message).isEqualTo("y coordinate should be zero or positive, but is -17")
    }

    @Test
    fun `constructing Coordinate with position outside board should throw IllegalArgumentException`() {
        var exception: IllegalArgumentException

        exception = assertThrows { Coordinate(0) }
        assertThat(exception.message).isEqualTo("position must be greater than zero, but is 0" )
        exception = assertThrows { Coordinate(minPiecePositionNumber -1) }
        assertThat(exception.message).isEqualTo("position must be greater than zero, but is 0" )
        exception = assertThrows { Coordinate(-1) }
        assertThat(exception.message).isEqualTo("position must be greater than zero, but is -1" )
        exception = assertThrows { Coordinate(-10) }
        assertThat(exception.message).isEqualTo("position must be greater than zero, but is -10" )
        exception = assertThrows { Coordinate(51) }
        assertThat(exception.message).isEqualTo("position must be at most $maxPiecePositionNumber, but is 51")
        exception = assertThrows { Coordinate(maxPiecePositionNumber + 1) }
        assertThat(exception.message).isEqualTo("position must be at most $maxPiecePositionNumber, but is ${maxPiecePositionNumber +1}")
        exception = assertThrows { Coordinate(maxPiecePositionNumber + 30) }
        assertThat(exception.message).isEqualTo("position must be at most $maxPiecePositionNumber, but is ${maxPiecePositionNumber +30}" )
    }

    @Test
    fun `test indices of all valid positions`() {
        var previous: Coordinate? = null
        (1..maxPiecePositionNumber).forEach { position ->
            val newCoord = Coordinate(position)
            // assert that Coordinate constructed by position is equal to Coordinate constructed by corresponding (x, y) indices
            assertThat(newCoord).isEqualTo(Coordinate(newCoord.x, newCoord.y))
            // also test inequality
            assertThat(newCoord).isNotEqualTo(previous)
            previous = newCoord
        }
    }

}