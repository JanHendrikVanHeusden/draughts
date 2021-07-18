package nl.jhvh.draughts.model

import nl.jhvh.draughts.formatting.DraughtsFormatting
import nl.jhvh.draughts.formatting.textformat.FormattableList
import nl.jhvh.draughts.model.base.BoardElement
import nl.jhvh.draughts.model.base.PlayableCoordinate
import nl.jhvh.draughts.model.base.PlayerType
import nl.jhvh.draughts.model.game.move.MovementChain
import nl.jhvh.draughts.model.game.move.options.PieceMovementOption
import nl.jhvh.draughts.model.structure.Board
import nl.jhvh.draughts.model.structure.Piece
import nl.jhvh.draughts.userInfo

internal class DraughtsPiece(
    override val board: Board,
    override val initialCoordinate: PlayableCoordinate,
    override val playerType: PlayerType
) : Piece {

    override var currentCoordinate: PlayableCoordinate? = initialCoordinate
        set(value) {
            if (value == field) {
                return
            }
            check(value != null || isCaptured) { """Can set the current coordinate to null only if piece was captured, but it wasn't captured! (piece: "$field")""" }
            check(!(field == null && value != null)) { """Piece "$this" is captured already, can not be moved to $value""" }
            field = value
        }

    override var isCaptured: Boolean = false
        set(value) {
            if (value == field) {
                return
            }
            check(!(field && !value)) { """Piece "$this" is captured already, can not be uncaptured""" }
            if (value && !field) {
                userInfo("Piece on position ${this.currentCoordinate!!.position} (${this.playerType.color}) is captured")
            }
            field = value
            if (value) {
                this.currentCoordinate = null
            }
        }

    override var isCrowned: Boolean = false

    override fun move(chain: MovementChain) {
        check(chain.piece == this) { "A movement for piece=${chain.piece} can not be applied to this piece = $this" }
        require(!isCaptured || chain.moves.isEmpty()) { "This piece was captured already, it cannot be moved! piece = $this" }
        // move to new position
        this.currentCoordinate = chain.moves.last().to
        // capture the enemy's pieces
        chain.moves.forEach{ it.capturing?.isCaptured = true }
    }

    override fun allowedMoves(): Collection<MovementChain> {
        if (isCaptured) {
            return emptyList()
        }
        val possibleMoves = possibleMoves()
        val maxCaptureCount = possibleMoves.maxOf { it.captureCount }
        return possibleMoves.filter { it.captureCount == maxCaptureCount }
    }

    override fun possibleMoves(): Collection<MovementChain> {
        if (isCaptured) {
            return emptyList()
        }
        with(PieceMovementOption(this, currentCoordinate!!)) {
            addCapturingMoves(this)
            addCapturingCrownedMoves(this) // does nothing yet!
            addNonCapturingMoves(this)
            addNonCapturingCrownedMoves(this) // does nothing yet!

            return this.toMovementChains()
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun addNonCapturingCrownedMoves(parentMove: PieceMovementOption) {
        // TODO: Not implemented yet
    }

    @Suppress("UNUSED_PARAMETER")
    fun addCapturingCrownedMoves(parentMove: PieceMovementOption) {
        // TODO: Not implemented yet
    }

    fun addNonCapturingMoves(parentMove: PieceMovementOption) {
        if (isCaptured) {
            return
        }
        val currentX = parentMove.coordinate.x
        val currentY = parentMove.coordinate.y

        val handleMove: (Pair<Int, Int>) -> Unit = { destination ->
            if (canMoveTo(destination)) {
                PieceMovementOption(this, PlayableCoordinate(destination), parent = parentMove)
            }
        }

        // playerType.hasFirstTurn -> "white" player
        val leftMoveTo = if (this.playerType.hasFirstTurn) Pair(currentX - 1, currentY + 1) else Pair(currentX + 1, currentY - 1)
        handleMove(leftMoveTo)
        val rightMoveTo = if (this.playerType.hasFirstTurn) Pair(currentX + 1, currentY + 1) else Pair(currentX - 1, currentY - 1)
        handleMove(rightMoveTo)
    }

    fun addCapturingMoves(parentMove: PieceMovementOption) {
        if (isCaptured) {
            return
        }

        val handlePossibleCapture: (Pair<Int, Int>, Pair<Int, Int>) -> Unit = { destination, capturePos ->
            if (canMoveTo(destination)) {
                val enemyPiece = enemyPiece(capturePos)
                if (enemyPiece != null) {
                    // Avoid cycles! According to draughts rules, you can not jump the same piece twice
                    // That's nice: programmers don't like endless loops either ;-)
                    var parent: PieceMovementOption? = parentMove
                    var alreadyCaptured = false
                    do {
                        if (parent!!.capturing == enemyPiece) {
                            alreadyCaptured = true
                            break
                        }
                        parent = parent.parent as PieceMovementOption?
                    } while (parent != null)
                    if (!alreadyCaptured) {
                        val newMove = PieceMovementOption(this, PlayableCoordinate(destination), parent = parentMove)
                        newMove.capturing = enemyPiece
                        // recursive call to handle any subsequent captures
                        addCapturingMoves(newMove)
                    }
                }
            }
        }

        val currentX = parentMove.coordinate.x
        val currentY = parentMove.coordinate.y

        // playerType.hasFirstTurn -> "white" player
        val leftForwardMoveTo = if (this.playerType.hasFirstTurn) Pair(currentX - 2, currentY + 2) else Pair(currentX + 2, currentY - 2)
        val leftForwardCapturePos = if (this.playerType.hasFirstTurn) Pair(currentX - 1, currentY + 1) else Pair(currentX + 1, currentY - 1)
        handlePossibleCapture(leftForwardMoveTo, leftForwardCapturePos)

        val rightForwardMoveTo = if (this.playerType.hasFirstTurn) Pair(currentX + 2, currentY + 2) else Pair(currentX - 2, currentY - 2)
        val rightForwardCapturePos = if (this.playerType.hasFirstTurn) Pair(currentX + 1, currentY + 1) else Pair(currentX - 1, currentY - 1)
        handlePossibleCapture(rightForwardMoveTo, rightForwardCapturePos)

        val leftBackMoveTo = if (this.playerType.hasFirstTurn) Pair(currentX - 2, currentY - 2) else Pair(currentX + 2, currentY + 2)
        val leftBackCapturePos = if (this.playerType.hasFirstTurn) Pair(currentX - 1, currentY - 1) else Pair(currentX + 1, currentY + 1)
        handlePossibleCapture(leftBackMoveTo, leftBackCapturePos)

        val rightBackMoveTo = if (this.playerType.hasFirstTurn) Pair(currentX + 2, currentY - 2) else Pair(currentX - 2, currentY + 2)
        val rightBackCapturePos = if (this.playerType.hasFirstTurn) Pair(currentX + 1, currentY - 1) else Pair(currentX - 1, currentY + 1)
        handlePossibleCapture(rightBackMoveTo, rightBackCapturePos)
    }

    fun canMoveTo(xy: Pair<Int, Int>): Boolean =
        this.board.squares[xy] != null && this.board.getPiecesByXY()[xy] == null

    fun enemyPiece(xy: Pair<Int, Int>): Piece? {
        if (this.board.squares[xy] == null) {
            return null
        }
        val piece = this.board.getPiecesByXY()[xy]
        if (piece != null && piece.playerType != this.playerType) {
            return piece
        }
        return null
    }

    override fun equals(other: Any?): Boolean =
        other === this || (other is DraughtsPiece && other.board === this.board && this.initialCoordinate == other.initialCoordinate)

    override fun hashCode(): Int = this.initialCoordinate.hashCode()

    override fun format(draughtsFormatter: DraughtsFormatting<BoardElement, FormattableList>): FormattableList {
        return draughtsFormatter.format(this)
    }

    override fun compareTo(other: Piece) = this.initialCoordinate.position

    override fun toString(): String =
        "${this.javaClass.simpleName}(current position=$currentCoordinate, playerType=$playerType, isCaptured=$isCaptured, isCrowned=$isCrowned)"

}