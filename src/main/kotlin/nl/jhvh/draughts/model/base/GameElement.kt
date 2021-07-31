package nl.jhvh.draughts.model.base

import nl.jhvh.draughts.model.game.Game
import nl.jhvh.draughts.model.structure.Board

interface GameElement {
    val board: Board
    val game: Game
}