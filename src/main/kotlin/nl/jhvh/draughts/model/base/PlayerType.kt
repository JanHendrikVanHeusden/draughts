package nl.jhvh.draughts.model.base

enum class PlayerType(val color: String, val hasFirstTurn: Boolean) {
    STARTING_PLAYER(startingColor, true),
    SECOND_PLAYER(secondColor, false);

    override fun toString(): String = "PieceColor(color='$color', hasFirstTurn=$hasFirstTurn)"

}