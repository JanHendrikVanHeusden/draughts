package nl.jhvh.draughts.model.base

/** The number of pieces (occupied squares) on a row when the game starts */
const val piecesPerRow: Int = 5

/** The width of the board, counting both the accessible (dark) and inaccessible (light) squares */
const val boardWidth: Int = piecesPerRow * 2

/** The depth (length) of the board, counting both the accessible (dark) and inaccessible (light) squares */
const val boardLength: Int = 10 // with draughts and checkers, the board is always square, so same value as boardWidth

/** The total count of squares, both the accessible (dark) and inaccessible (light) squares */
const val squareCount: Int = boardWidth * boardLength

/** The number of rows occupied initially per color */
const val initialOccupiedRowsPerColor: Int = 4 // this number * 2 must be less than boardLength, to allow space for moving the pieces

const val minPiecePositionNumber: Int = 1
const val maxPiecePositionNumber: Int = boardWidth * boardLength / 2

/**
 * In the common notation of "international draughts", the playable positions (the dark squares) are numbered
 * starting from the leftmost dark square of the top row, to the rightmost dark square of the bottom row.
 * For the common `International draughts` this range is 1 to 50 (up to and including).
 * Note that the light squares, which are inaccessible (not playable) are not assigned positions.
 *
 * See the README.md file in this project for more details.
 */
val positionRange: IntRange = minPiecePositionNumber..maxPiecePositionNumber

/** The initial number of pieces for each player */
const val piecesPerPlayer: Int = piecesPerRow * initialOccupiedRowsPerColor

const val startingColor: String = "white"
const val secondColor: String = "black"

const val playableSquareColor: String = "dark"
const val nonPlayableSquareColor: String = "light"