package nl.jhvh.draughts.model.base

enum class SquareType(val color: String, val playable: Boolean) {
    PLAYABLE (playableSquareColor, true),
    NON_PLAYABLE(nonPlayableSquareColor, false);

    override fun toString(): String = "$color (playable: $playable)"
}