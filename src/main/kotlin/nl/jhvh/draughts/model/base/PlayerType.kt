package nl.jhvh.draughts.model.base

/**
 * Defines game related properties of the player
 * @param color The color of the pieces for this player; for international draughts that should be white or black
 * @param hasFirstTurn Is this the player who has the first turn of the game?
 */
enum class PlayerType(val color: String, val hasFirstTurn: Boolean) {
    STARTING_PLAYER(startingColor, true),
    SECOND_PLAYER(secondColor, false);

    override fun toString(): String = "PieceColor $color"

}