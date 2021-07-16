package nl.jhvh.draughts.model

import nl.jhvh.draughts.formatting.DraughtsFormatting
import nl.jhvh.draughts.formatting.textformat.FormattableList
import nl.jhvh.draughts.model.base.BoardElement
import nl.jhvh.draughts.model.base.PlayerType
import nl.jhvh.draughts.model.base.PlayableCoordinate
import nl.jhvh.draughts.model.movement.base.PieceMoveChain
import nl.jhvh.draughts.model.movement.options.CapturingTreeMovable
import nl.jhvh.draughts.model.movement.options.TreeMovable
import nl.jhvh.draughts.model.structure.Board
import nl.jhvh.draughts.model.structure.Piece

class DraughtsPiece(override val board: Board, override val initialCoordinate: PlayableCoordinate, override val playerType: PlayerType) : Piece {

    override var currentCoordinate: PlayableCoordinate? = initialCoordinate
        set(value) {
            check(value != null || isCaptured) { """Can set the current coordinate to null only if piece was captured, but it wasn't captured! (piece: "$field")"""}
            check(!(field == null && value != null)) { """Piece "this" is captured already, can not be moved to $value""" }
            field = value
        }

    override var isCaptured: Boolean = false
        set(value) {
            field = value
            if (value) {
                this.currentCoordinate = null
            }
        }

    override var isCrowned: Boolean = false

    override fun move(chain: PieceMoveChain) {
        TODO("Not yet implemented")
    }

    override fun movementTree(): CapturingTreeMovable {
        TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean =
        other === this || (other is DraughtsPiece && other.board === this.board && this.initialCoordinate == other.initialCoordinate)

    override fun hashCode(): Int = this.initialCoordinate.hashCode()


    override val options: Collection<TreeMovable>
        get() = TODO("Not yet implemented")

    override fun format(draughtsFormatter: DraughtsFormatting<BoardElement, FormattableList>): FormattableList {
        TODO("Not yet implemented")
    }

    override fun compareTo(other: Piece) = this.initialCoordinate.position

    override fun toString(): String {
        return "${this.javaClass.simpleName}(initialCoordinate=$initialCoordinate, playerType=$playerType, currentCoordinate=$currentCoordinate, isCaptured=$isCaptured, isCrowned=$isCrowned)"
    }

}