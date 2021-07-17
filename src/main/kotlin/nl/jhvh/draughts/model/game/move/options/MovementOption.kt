package nl.jhvh.draughts.model.game.move.options

import nl.jhvh.draughts.model.base.PlayableCoordinate
import nl.jhvh.draughts.model.game.move.Capturing
import nl.jhvh.draughts.model.structure.Piece

/**
 * A [MovementOption] indicates a possible move, from the [parent]'s [coordinate] to `this` [coordinate].
 *
 * [MovementOption]s are modelled to form a tree structure, where the root's parent node [parent] is `null`.
 * In case of the parent node, the [coordinate] does indicate the current position.
 *
 * The [followingOptions] indicate the possibilities, if any, to move on from the current [coordinate];
 * applicable only in case of a capturing move (non-capturing moves in draughts are always only single moves).
 *
 * If the move is a capturing one, the enemy piece captured is stored in [capturing]
 */
internal sealed interface MovementOption: Capturing {

    /**
     * * If `this` [MovementOption] indicates a subsequent move, the previous move;
     * * `null` if this is not a subsequent move (`this` indicates the root in that case)
     */
    var parent: MovementOption?

    /**
     * * If not a subsequent move, [coordinate] holds the piece's current coordinate.
     * * If the [MovementOption] is a subsequent move, [coordinate] holds the destination of this [MovementOption]
     */
    val coordinate: PlayableCoordinate

    /**
     * [followingOptions] indicate the possibilities, if any, to move on from the current [coordinate];
     * applicable only in case of a capturing move (non-capturing moves in draughts are always only single moves).
     */
    val followingOptions: Collection<MovementOption>

    /** If the move is a capturing one, [capturing] indicates the enemy piece captured by this [MovementOption] */
    override var capturing: Piece?

    override fun toString(): String

}

/** @see [MovementOption] */
internal class PieceMovementOption(override val coordinate: PlayableCoordinate, override var parent: MovementOption? = null
) : MovementOption {

    override val followingOptions: MutableList<PieceMovementOption> = mutableListOf()

    override var capturing: Piece? = null

    override fun toString(): String =
        "${this.javaClass.simpleName}(coordinate=$coordinate, followingOptions=$followingOptions, capturing=$capturing)"

}