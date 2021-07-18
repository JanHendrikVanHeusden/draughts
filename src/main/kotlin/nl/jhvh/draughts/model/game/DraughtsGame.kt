package nl.jhvh.draughts.model.game

import nl.jhvh.draughts.model.DraughtsBoard
import nl.jhvh.draughts.model.base.PlayerType
import nl.jhvh.draughts.model.game.move.MovementChain
import nl.jhvh.draughts.model.structure.Board
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
        require(movement in piece.possibleMoves()) { "This move is not possible for this piece! You may need to capture more pieces; or square to move to may be occupied already.\n}Move you tried = \n$movement" }
        val allowedMovesAllPieces = this.allowedMoves()
        require(movement in allowedMovesAllPieces) { "This move is not allowed, it does not have the highest capture count! Allowed moves: $allowedMovesAllPieces" }
        piece.move(movement)
        userLog ("Move done! move =")
        userLog (movement)
        togglePlayer()
    }

    private fun togglePlayer() {
        isStartingPlayersTurn = !isStartingPlayersTurn
        userLog()
        userLog { "Player ${playerTypeInTurn()} is now in turn!" }
    }

}