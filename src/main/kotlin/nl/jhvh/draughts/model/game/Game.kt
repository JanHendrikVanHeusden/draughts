package nl.jhvh.draughts.model.game

import nl.jhvh.draughts.model.base.PlayerType
import nl.jhvh.draughts.model.game.move.MovementChain
import nl.jhvh.draughts.model.structure.Board
import nl.jhvh.draughts.model.structure.Piece

interface Game: Board {

    /** @return the current [PlayerType] in turn */
    fun playerTypeInTurn(): PlayerType

    /**
     * Determines all moves that are possible for the given player, regardless whether if and how many capturing.
     * Note that *not all of these moves may be allowed*: draughts rules allow only these moves with the highest capturing count.
     * @param player The [PlayerType] for which the possible moves are requested; default is the current player in turn
     * @return the list of move chains as determined, in order of [MovementChain.captureCount]; highest number of captures first.
     *         In case of multi-capture moves, the chains contain all required jumps
     * @see [allowedMoves]
     */
    fun possibleMoves(player: PlayerType = playerTypeInTurn()): List<MovementChain>

    /**
     * Determines all moves that are possible for the given player, and have the highest possible capturing count,
     * so these are allowed for international draughts
     * @param player The [PlayerType] for which the allowed moves are requested; default is the current player in turn
     * @return the list of move chains as determined; in case of multi-capture moves, the chains contain all required jumps
     */
    fun allowedMoves(player: PlayerType = playerTypeInTurn()): List<MovementChain>

    /** Carries out a move of the current player */
    fun move(movement: MovementChain)

    fun move(piece: Piece, move: List<Int>): Boolean

}