package nl.jhvh.draughts.model.game

import nl.jhvh.draughts.model.DraughtsBoard
import nl.jhvh.draughts.model.base.PlayerType
import nl.jhvh.draughts.model.structure.Board

class DraughtsGame: Game, Board by DraughtsBoard() {

    private var isStartingPlayersTurn = true

    override fun playerTypeInTurn(): PlayerType =
        if (isStartingPlayersTurn) PlayerType.STARTING_PLAYER else PlayerType.SECOND_PLAYER

}