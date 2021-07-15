package nl.jhvh.draughts.model

/** The number of pieces (occupied squares) on a row when the game starts */
const val piecesPerRow = 5

/** The width of the board, counting both the accessible (dark) and inaccessible (light) squares */
const val boardWidth = piecesPerRow * 2

/** The depth (length) of the board, counting both the accessible (dark) and inaccessible (light) squares */
const val boardLength = 10 // with draughts and checkers, the board is always square, so same value as boardWidth

/** The total count of squares, both the accessible (dark) and inaccessible (light) squares */
const val squareCount = boardWidth * boardLength

/** The number of rows occupied initially per color */
const val initialOccupiedRowsPerColor = 4 // this number * 2 must always be less than boardLength, to allow space for moving

/** The initial number of pieces provided to each player at the start of the game */
const val piecesPerColor = initialOccupiedRowsPerColor * piecesPerRow

const val minPiecePositionNumber = 1
const val maxPiecePositionNumber = boardWidth * boardLength / 2

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
const val numberOfPiecesPerPlayer = piecesPerRow * initialOccupiedRowsPerColor
