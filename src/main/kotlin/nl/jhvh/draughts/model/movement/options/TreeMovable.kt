package nl.jhvh.draughts.model.movement.options

interface TreeMovable {

    val options: Collection<TreeMovable>

}