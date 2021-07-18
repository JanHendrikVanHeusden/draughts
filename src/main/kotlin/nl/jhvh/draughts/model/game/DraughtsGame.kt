package nl.jhvh.draughts.model.game

import nl.jhvh.draughts.model.DraughtsBoard
import nl.jhvh.draughts.model.base.PlayerType
import nl.jhvh.draughts.model.game.move.MovementChain
import nl.jhvh.draughts.model.structure.Board
import nl.jhvh.draughts.model.structure.Piece
import nl.jhvh.draughts.userLog

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
        return allPiecesByPlayerType[player]!!
            .filter { piece -> !piece.isCaptured }
            .map { piece -> piece.allowedMoves() }
            .flatten()
    }

    override fun move(movement: MovementChain) {
        userLog()
        val playerInTurn = playerTypeInTurn()
        val piece = movement.piece
        require(piece.playerType == playerInTurn) { "Player $playerInTurn is in turn, can not play opponent's piece! piece = $piece" }
        require(movement in piece.possibleMoves()) { "This move is not possible for this piece! You may need to capture more pieces; or square to move to may be occupied already.\n}Move you tried = \n${movement.asPositions}" }
        val allowedMovesAllPieces = this.allowedMoves()
        require(movement in allowedMovesAllPieces) { "This move is not allowed, it does not have the highest capture count! Allowed moves: ${allowedMovesAllPieces.map { it.asPositions }.joinToString("\n", "\n")}" }
        piece.move(movement)
        userLog("Move done! move =")
        userLog(movement)
        togglePlayer()
    }

    override fun move(piece: Piece, move: List<Int>) {
        require(move.size >= 2) { """No "to" position specified! intended move = $move, piece = $piece""" }
        val possibleMoves = piece.possibleMoves().toList()
        require(possibleMoves.isNotEmpty() && possibleMoves.maxOf { it.moves.isNotEmpty() }) { "This piece can not move! piece = $piece, intended move = $move" }

        // find the move-chain for the given positions
        val chain = possibleMoves.firstOrNull { chain -> chain.piece == move }
        if (chain !== null) {
            // chain found, move the piece
            move(chain)
            return
        }

        // If we are here, the move is not possible. Find best match, if any, as a suggestion for the user
        if (possibleMoves.size == 1) {
            require(false) { "The only possible move for this piece = ${possibleMoves[0].asPositions}" }
        }
        val firstTo = move[1]
        val matchingTos = possibleMoves.filter { it.moves.map { it.to.position }.contains(firstTo) }
        if (matchingTos.isNotEmpty()) {
            require(false) {
                """The intended move "$move" is not possible. Best matching suggestions for this piece are: \n""" +
                        matchingTos.map { it.asPositions }.joinToString(System.lineSeparator())
            }
        }
        require(false) { """Move "$move" is not possible for this piece! A square to move to may be occupied already.\nIntended move = \n$move""" }
    }

    private fun togglePlayer() {
        isStartingPlayersTurn = !isStartingPlayersTurn
        userLog()
        userLog { "Player ${playerTypeInTurn()} is now in turn!" }
    }

}