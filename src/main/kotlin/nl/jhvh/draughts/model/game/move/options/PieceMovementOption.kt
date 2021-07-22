package nl.jhvh.draughts.model.game.move.options

import nl.jhvh.draughts.model.base.PlayableCoordinate
import nl.jhvh.draughts.model.game.move.Capturing
import nl.jhvh.draughts.model.game.move.PieceMove
import nl.jhvh.draughts.model.game.move.PieceMovementChain
import nl.jhvh.draughts.model.structure.Piece
import java.util.Collections

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

    /** The [Piece] this [MovementOption] applies to */
    val piece: Piece

    /**
     * * If `this` [MovementOption] indicates a subsequent move, the previous move;
     * * `null` if this is not a subsequent move (`this` indicates the root in that case)
     */
    val parent: MovementOption?

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

    fun toMovementChains(): List<PieceMovementChain>

    override fun toString(): String

}

/** @see [MovementOption] */
internal class PieceMovementOption(override val piece: Piece, override val coordinate: PlayableCoordinate, override val parent: MovementOption? = null
) : MovementOption {

    override val followingOptions: MutableList<PieceMovementOption> = mutableListOf()

    init {
        if (parent != null) {
            (parent.followingOptions as MutableList).add(this)
        }
    }

    override var capturing: Piece? = null

    override fun toMovementChains(): List<PieceMovementChain> {
        check(this.parent == null) { "A valid movement chain must start at the piece's current position, so at the root level of the tree (so parent must be null), but parent is not null! this=$this, parent = $parent" }

        val chains: MutableList<PieceMovementChain> = mutableListOf()
        val leavesList: MutableSet<PieceMovementOption> = mutableSetOf()
        findLeafNodes(leavesList)

        leavesList.forEach {
            val moves: MutableList<PieceMove> = mutableListOf()
            var optionTo: PieceMovementOption? = it
            var optionFrom = it.parent as PieceMovementOption?
            while (optionFrom != null) {
                moves.add(0, PieceMove(from = optionFrom.coordinate, to = optionTo!!.coordinate, capturing = optionTo.capturing))
                with(optionFrom) {
                    optionTo = this
                    optionFrom = this.parent as PieceMovementOption?
                }
            }
            chains.add(PieceMovementChain(this.piece, Collections.unmodifiableList(moves)))
        }
        return chains
    }

    fun findLeafNodes(leavesList: MutableSet<PieceMovementOption>) {
        if (this.followingOptions.isEmpty()) {
            // It's a leaf node, add to output
            val isAdded = leavesList.add(this)
            check(isAdded) {"Error! Cycle in movement tree, this is a bug (but might happen in mocked test code)"}
        } else {
            // Not a leaf node, call recursively to search deeper
            followingOptions.forEach { nonLeafNode ->
                nonLeafNode.findLeafNodes(leavesList)
            }
        }
    }

    override fun toString(): String =
        "${this.javaClass.simpleName}(piece=$piece, coordinate=$coordinate, capturing=$capturing, followingOptions: size=${followingOptions.size})"

    // Any more "functional" equality check would need to include the followingOptions(), but it's not desirable to
    // walk the tree structure just for an advanced equality that we don't really need
    // So instead, we explicitly consider each instance to be unique, this override is meant to make this explicit
    override fun equals(other: Any?): Boolean = other === this

    @Suppress("RedundantOverride")
    override fun hashCode(): Int = super.hashCode()

}