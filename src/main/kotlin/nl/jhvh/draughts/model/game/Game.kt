package nl.jhvh.draughts.model.game

import nl.jhvh.draughts.model.base.PlayerType

interface Game {

    fun playerTypeInTurn(): PlayerType

}