package nl.jhvh.draughts.model

import nl.jhvh.draughts.formatting.DraughtsFormatting
import nl.jhvh.draughts.formatting.textformat.FormattableList
import nl.jhvh.draughts.model.base.BoardElement
import nl.jhvh.draughts.model.base.PlayableCoordinate
import nl.jhvh.draughts.model.base.PlayerType
import nl.jhvh.draughts.model.game.Game
import nl.jhvh.draughts.model.game.move.MovementChain
import nl.jhvh.draughts.model.game.move.options.PieceMovementOption
import nl.jhvh.draughts.model.structure.Board
import nl.jhvh.draughts.model.structure.Piece
import nl.jhvh.draughts.rule.validate
import nl.jhvh.draughts.userInfo

internal class DraughtsPiece(
    override val game: Game,
    override val initialCoordinate: PlayableCoordinate,
    override val playerType: PlayerType
) : Piece {

    override val board: Board = game.board

    override var currentCoordinate: PlayableCoordinate? = initialCoordinate
        set(value) {
            if (value == field) {
                return
            }
            check(value != null || isCaptured) { """Can set the current coordinate to null only if piece was captured, but it wasn't captured! (piece: "$field")""" }
            check(!(field == null && value != null)) { """Piece "$this" is captured already, can not be moved to $value""" }
            this.board.squares[field?.xy]?.occupyingPiece = null
            field = value
            if (value != null) {
                this.board.squares[value.xy]!!.occupyingPiece = this
            }
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
        validate(!isCaptured || chain.moves.isEmpty()) { "This piece was captured already, it cannot be moved! piece = $this" }
        // move to new position
        this.currentCoordinate = chain.moves.last().to
        // capture the enemy's pieces
        chain.moves.forEach{ it.capturing?.isCaptured = true }
        if (!isCrowned && game.isCrowningPosition(this)) {
            isCrowned = true
            userInfo("Piece on position ${currentCoordinate!!.position} (${playerType.color}) is crowned!")
        }
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
            addNonCapturingMoves(this)
            return this.toMovementChains()
        }
    }

    private val leftForward: (moveFrom: Pair<Int, Int>, length: Int) -> Pair<Int, Int> = { moveFrom, length ->
        if (this.playerType.hasFirstTurn) Pair(moveFrom.first - length, moveFrom.second + length)
        else Pair(moveFrom.first + length, moveFrom.second - length)
    }

    private val rightForward: (moveFrom: Pair<Int, Int>, length: Int) -> Pair<Int, Int> = { moveFrom, length ->
        if (this.playerType.hasFirstTurn) Pair(moveFrom.first + length, moveFrom.second + length)
        else Pair(moveFrom.first - length, moveFrom.second - length)
    }

    private val leftBackward: (moveFrom: Pair<Int, Int>, length: Int) -> Pair<Int, Int> = { moveFrom, length ->
        if (this.playerType.hasFirstTurn) Pair(moveFrom.first - length, moveFrom.second - length)
        else Pair(moveFrom.first + length, moveFrom.second + length)
    }

    private val rightBackward: (moveFrom: Pair<Int, Int>, length: Int) -> Pair<Int, Int> = { moveFrom, length ->
        if (this.playerType.hasFirstTurn) Pair(moveFrom.first + length, moveFrom.second - length)
        else Pair(moveFrom.first - length, moveFrom.second + length)
    }

    val forwardDestinations = listOf(leftForward, rightForward)
    val backwardDestinations = listOf(leftBackward, rightBackward)
    val destinations = forwardDestinations + backwardDestinations

    fun addNonCapturingMoves(parentMove: PieceMovementOption) {
        if (isCrowned) addNonCapturingCrownedMoves(parentMove)
        else addNonCapturingNonCrownedMoves(parentMove)
    }

    fun addCapturingMoves(parentMove: PieceMovementOption) {
        if (isCrowned) addCapturingCrownedMoves(parentMove)
        else addCapturingNonCrownedMoves(parentMove)
    }

    fun addNonCapturingNonCrownedMoves(parentMove: PieceMovementOption) {
        check(!isCaptured) { "Captured pieces must not be moved! Piece = $this" }
        check(!isCrowned) { "Must be crowned to make a crowned move! Piece = $this" }
        forwardDestinations.forEach { relMove ->
            addNonCapturingOption(parentMove, relMove(parentMove.coordinate.xy, 1))
        }
    }

    fun addCapturingNonCrownedMoves(parentMove: PieceMovementOption) {
        check(!isCaptured) { "Captured pieces must not be moved! Piece = $this" }
        check(!isCrowned) { """Must not be crowned to call method "addCapturingNonCrownedMoves()"! Piece = $this""" }
        destinations.forEach { relMove ->
            addCapturingOption(parentMove, destination = relMove(parentMove.coordinate.xy, 2), capturePos = relMove(parentMove.coordinate.xy, 1))
        }
    }

    fun addNonCapturingCrownedMoves(parentMove: PieceMovementOption) {
        check(!isCaptured) { "Captured pieces must not be moved! Piece = $this" }
        check(isCrowned) { "Must be crowned to make a crowned move! Piece = $this" }
        destinations.forEach { relMove ->
            var length = 1
            while (addNonCapturingOption(parentMove, relMove(parentMove.coordinate.xy, length))) {
                length++
            }
        }
    }

    fun addCapturingCrownedMoves(parentMove: PieceMovementOption) {
        check(!isCaptured) { "Captured pieces must not be moved! Piece = $this" }
        check(isCrowned) { "Must be crowned to make a crowned move! Piece = $this" }
        destinations.forEach { relMove ->
            var length = 1
            while (canMoveTo(relMove(parentMove.coordinate.xy, length))) {
                length++
            }
            val capturePos = relMove(parentMove.coordinate.xy, length)
            if (enemyPiece(capturePos) != null) {
                while (addCapturingOption(parentMove, relMove(parentMove.coordinate.xy, length+1), capturePos) != null) {
                    length++
                }
            }
        }
    }

    fun addNonCapturingOption(parentMove: PieceMovementOption, destination: Pair<Int, Int>): Boolean {
        if (canMoveTo(destination)) {
            PieceMovementOption(this, PlayableCoordinate(destination), parent = parentMove)
            return true
        }
        return false
    }

    fun addCapturingOption(parentMove: PieceMovementOption, destination: Pair<Int, Int>, capturePos: Pair<Int, Int>): Piece? {
        if (!canMoveTo(destination)) {
            return null
        }
        val enemyPiece = enemyPiece(capturePos) ?: return null
        // Avoid cycles! According to draughts rules, you can not jump the same piece twice
        // That's nice: programmers don't like endless loops either ;-)
        var parent: PieceMovementOption? = parentMove
        do {
            if (parent?.capturing == enemyPiece) {
                return null
            }
            parent = parent?.parent as PieceMovementOption?
        } while (parent != null)
        val newMove = PieceMovementOption(this, PlayableCoordinate(destination), parent = parentMove)
        newMove.capturing = enemyPiece
        // recursive call to handle any subsequent captures
        addCapturingMoves(newMove)
        return enemyPiece
    }

    fun canMoveTo(xy: Pair<Int, Int>): Boolean =
        this.board.squares[xy] != null && this.board.squares[xy]?.occupyingPiece == null

    fun enemyPiece(xy: Pair<Int, Int>): Piece? {
        if (this.board.squares[xy] == null) {
            return null
        }
        val piece = this.board.squares[xy]?.occupyingPiece
        if (piece != null && piece.playerType != this.playerType) {
            return piece // enemy piece
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