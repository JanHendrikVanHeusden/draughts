package nl.jhvh.draughts.model.game

import nl.jhvh.draughts.model.DraughtsBoard
import nl.jhvh.draughts.model.DraughtsPiece
import nl.jhvh.draughts.model.base.PlayableCoordinate
import nl.jhvh.draughts.model.base.PlayerType
import nl.jhvh.draughts.model.base.boardLength
import nl.jhvh.draughts.model.base.piecesPerPlayer
import nl.jhvh.draughts.model.base.positionRange
import nl.jhvh.draughts.model.game.move.MovementChain
import nl.jhvh.draughts.model.structure.Board
import nl.jhvh.draughts.model.structure.Piece
import nl.jhvh.draughts.model.structure.Square
import nl.jhvh.draughts.rule.validate
import nl.jhvh.draughts.userInfo

internal class DraughtsGame: Game, Board by DraughtsBoard() {

    private var isStartingPlayersTurn = true

    val startingPlayerPieces: Set<Piece> = let {
        positionRange.reversed().take(piecesPerPlayer)
            .map { position -> DraughtsPiece(this, PlayableCoordinate(position), PlayerType.STARTING_PLAYER) }
            .toSet()
    }.onEach { piece -> squares[piece.currentCoordinate!!.xy]?.piece = piece }

    val secondPlayerPieces: Set<Piece> = let {
        positionRange.take(piecesPerPlayer)
            .map { position -> DraughtsPiece(this, PlayableCoordinate(position), PlayerType.SECOND_PLAYER) }
            .toSet()
    }.onEach { piece -> squares[piece.currentCoordinate!!.xy]?.piece = piece }

    override val allPieces: Set<Piece> = startingPlayerPieces + secondPlayerPieces

    override val allPiecesByPlayerType: Map<PlayerType, Set<Piece>> = mapOf(PlayerType.STARTING_PLAYER to startingPlayerPieces, PlayerType.SECOND_PLAYER to secondPlayerPieces)

    override fun getPiece(position: Int): Piece? = getPiece(PlayableCoordinate(position).xy)

    override fun getPiece(square: Square): Piece? = getPiece(square.xy)

    override fun getPiece(xy: Pair<Int, Int>): Piece? = squares[xy]?.piece

    override fun playerTypeInTurn(): PlayerType =
        if (isStartingPlayersTurn) PlayerType.STARTING_PLAYER else PlayerType.SECOND_PLAYER

    override fun possibleMoves(player: PlayerType): List<MovementChain> {
        return allPiecesByPlayerType[player]!!
            .filter { piece -> !piece.isCaptured }
            .map { piece -> piece.possibleMoves() }
            .flatten()
            .sorted()
    }

    override fun allowedMoves(player: PlayerType): List<MovementChain> {
        val allowedMovesPerPiece = allPiecesByPlayerType[player]!!
            .filter { piece -> !piece.isCaptured }
            .map { piece -> piece.allowedMoves() }
            .flatten()
        val maxCaptured = allowedMovesPerPiece.maxOf { it.captureCount }
        return allowedMovesPerPiece.filter { it.captureCount == maxCaptured }
    }

    override fun move(movement: MovementChain) {
        userInfo()
        val playerInTurn = playerTypeInTurn()
        val piece = movement.piece
        validate(piece.playerType == playerInTurn) { "${playerInTurn.color} is in turn and must not play opponent's piece! piece = ${piece.currentCoordinate!!.xy}" }
        validate(movement.asPositions in piece.possibleMoves().map { it.asPositions }) {
            "This move is not possible for this piece! You may need to capture more pieces; or square to move to may be occupied already.\nMove you tried = \n${movement.asPositions}"
        }
        val allowedMovesAllPieces = this.allowedMoves()
        validate(movement.asPositions in allowedMovesAllPieces.map { it.asPositions }) {
            "This move is not allowed, it does not have the highest capture count! Allowed moves: ${allowedMovesAllPieces.map { it.asPositions }.joinToString("\n", "\n")}"
        }
        piece.move(movement)
        userInfo("Move done! Move = ${movement.asPositions} (${movement.piece.playerType.color})")
        togglePlayer()
    }

    override fun move(piece: Piece, move: List<Int>): Boolean {
        validate(move.size >= 2) { """No "to" position specified! intended move = $move, piece = $piece""" }
        val possibleMoves = piece.possibleMoves().toList()
        validate(possibleMoves.isNotEmpty() && possibleMoves.maxOf { it.moves.isNotEmpty() }) { "This piece can not move! piece = $piece, intended move = $move" }

        // find the move-chain for the given positions
        val chain = possibleMoves.firstOrNull { chain -> chain.asPositions == move }
        if (chain != null) {
            // chain found, move the piece
            move(chain)
            return true
        }

        // If we are here, the move is not possible. Find best match, if any, as a suggestion for the user
        if (possibleMoves.size == 1) {
            validate(false) { "The only possible move for this piece = ${possibleMoves[0].asPositions}" }
        }
        val firstTo = move[1]
        val matchingTos = possibleMoves.filter { it.moves.map { it.to.position }.contains(firstTo) }
        if (matchingTos.isNotEmpty()) {
            validate(false) {
                """The intended move "$move" is not possible. Best matching suggestions for this piece are:
                    |
                """.trimMargin() +
                        matchingTos.map { it.asPositions }.joinToString(System.lineSeparator())
            }
        }
        validate(false) { """Move "$move" is not possible for this piece! A square to move to may be occupied already.
            |Intended move = $move""".trimMargin() }
        return false
    }

    private fun togglePlayer() {
        isStartingPlayersTurn = !isStartingPlayersTurn
        userInfo()
        userInfo { "${playerTypeInTurn().color} is now in turn!" }
    }

    override fun isCrowningPosition(piece: Piece): Boolean =
        !piece.isCaptured && ((piece.playerType.hasFirstTurn && piece.currentCoordinate!!.y == boardLength -1) ||
                (!piece.playerType.hasFirstTurn && piece.currentCoordinate!!.y == 0))

}