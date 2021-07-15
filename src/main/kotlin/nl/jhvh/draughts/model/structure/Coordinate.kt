package nl.jhvh.draughts.model.structure

import nl.jhvh.draughts.isEven
import nl.jhvh.draughts.model.boardLength
import nl.jhvh.draughts.model.boardWidth
import nl.jhvh.draughts.model.maxPiecePositionNumber
import nl.jhvh.draughts.model.squareCount

/**
 * A [Coordinate] defines the position of a playable (accessible) square on a board. Playable means that a piece can enter that field.
 * So on a normal draughts board (international draughts), only the dark fields have a [Coordinate].
 * See [Draughts board diagram with position numbers](https://upload.wikimedia.org/wikipedia/commons/d/da/Nummeringdambord.jpg)
 *
 * @constructor  Constructs a [Coordinate] by its position number.
 * @param position The position number, according to draughts numbering convention.
 * @throws IllegalArgumentException if [position] is outside the board boundaries
 */
data class Coordinate constructor(val position: Int) {

    /**
     * Constructs a [Coordinate] by x and y indices, zero based, with lower left square = (0,0) and upper right square = (9,9)
     * @param x zero based left-to-right index
     * @param y zero based bottom-to-top index
     * @throws IllegalArgumentException if combination of ([x], [y]) indicates a non-playable (non-accessible) field, or outside the board boundaries
     */
    constructor(x: Int, y: Int) : this (xyCoordinatesToPosition(x, y))

    val y: Int = boardLength - ((this.position*2-1) / boardLength) -1
    val x: Int = (this.position*2-1) % boardWidth - (if (y.isEven()) 1 else 0)

    init {
        validatePosition()
    }

    private fun validatePosition() {
        require(this.position > 0) { "position must be greater than zero, but is ${this.position}" }
        require(this.position <= maxPiecePositionNumber) { "position must be at most $maxPiecePositionNumber, but is ${this.position}" }
    }

    override fun toString() = "${this.javaClass.simpleName}(position=$position, x=$x, y=$y)"

}

@Throws(IllegalArgumentException::class)
private fun xyCoordinatesToPosition(x: Int, y: Int): Int {
    validateIndices(x, y)
    val tmpPosition = squareCount - boardLength * (y + 1) + (if (y.isEven()) (x + 2) else (x + 1))
    require(tmpPosition.isEven()) { "Given values x = $x and y = $y indicate a non-playable position" }
    return tmpPosition / 2
}

private fun validateIndices(x: Int, y: Int) {
    require(x >= 0) { "x coordinate should be zero or positive, but is $x" }
    require(x < boardWidth) { "x coordinate should be less than $boardWidth, but is $x" }
    require(y >= 0) { "y coordinate should be zero or positive, but is $y" }
    require(y < boardLength) { "y coordinate should be less than $boardLength, but is $y" }
}

// See the README.md file in this project (or Wikipedia etc.) for numbering convention of draughts.
// Below some conversions from position to (x, y) coordinates
//    1 = (1, 9)
//        (2, 9) -> not a playable field, doesn't have a number!!
//    2 = (3, 9)
//    3 = (5, 9)
//    4 = (7, 9)
//    5 = (9, 9)
//    6 = (0, 8)
//    7 = (2, 8)
//    ...
//   10 = (8, 8)
//   11 = (1, 7)
//   ...
//   41 = (1, 1)
//   42 = (3, 1)
//   ...
//   46 = (0, 0)
//   47 = (2, 0)
//   ...
//   49 = (6, 0)
//   50 = (8, 0)
