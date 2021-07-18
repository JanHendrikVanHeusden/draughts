package nl.jhvh.draughts.model.game

import nl.jhvh.draughts.model.DraughtsBoard
import nl.jhvh.draughts.model.base.PlayerType
import nl.jhvh.draughts.model.game.move.MovementChain
import nl.jhvh.draughts.model.structure.Board
import nl.jhvh.draughts.model.structure.Piece
import nl.jhvh.draughts.userInfo

class DraughtsGame: Game, Board by DraughtsBoard() {

    private var isStartingPlayersTurn = true

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
        require(piece.playerType == playerInTurn) { "${playerInTurn.color} is in turn and must not play opponent's piece! piece = ${piece.currentCoordinate!!.xy}" }
        require(movement.asPositions in piece.possibleMoves().map { it.asPositions }) {
            "This move is not possible for this piece! You may need to capture more pieces; or square to move to may be occupied already.\nMove you tried = \n${movement.asPositions}"
        }
        val allowedMovesAllPieces = this.allowedMoves()
        require(movement.asPositions in allowedMovesAllPieces.map { it.asPositions }) {
            "This move is not allowed, it does not have the highest capture count! Allowed moves: ${allowedMovesAllPieces.map { it.asPositions }.joinToString("\n", "\n")}"
        }
        piece.move(movement)
        userInfo("Move done! Move = ${movement.asPositions} (${movement.piece.playerType.color})")
        togglePlayer()
    }

    override fun move(piece: Piece, move: List<Int>): Boolean {
        require(move.size >= 2) { """No "to" position specified! intended move = $move, piece = $piece""" }
        val possibleMoves = piece.possibleMoves().toList()
        require(possibleMoves.isNotEmpty() && possibleMoves.maxOf { it.moves.isNotEmpty() }) { "This piece can not move! piece = $piece, intended move = $move" }

        // find the move-chain for the given positions
        val chain = possibleMoves.firstOrNull { chain -> chain.asPositions == move }
        if (chain != null) {
            // chain found, move the piece
            move(chain)
            return true
        }

        // If we are here, the move is not possible. Find best match, if any, as a suggestion for the user
        if (possibleMoves.size == 1) {
            require(false) { "The only possible move for this piece = ${possibleMoves[0].asPositions}" }
        }
        val firstTo = move[1]
        val matchingTos = possibleMoves.filter { it.moves.map { it.to.position }.contains(firstTo) }
        if (matchingTos.isNotEmpty()) {
            require(false) {
                """The intended move "$move" is not possible. Best matching suggestions for this piece are:
                    |
                """.trimMargin() +
                        matchingTos.map { it.asPositions }.joinToString(System.lineSeparator())
            }
        }
        require(false) { """Move "$move" is not possible for this piece! A square to move to may be occupied already.
            |Intended move = $move""".trimMargin() }
        return false
    }

    private fun togglePlayer() {
        isStartingPlayersTurn = !isStartingPlayersTurn
        userInfo()
        userInfo { "${playerTypeInTurn().color} is now in turn!" }
    }

}