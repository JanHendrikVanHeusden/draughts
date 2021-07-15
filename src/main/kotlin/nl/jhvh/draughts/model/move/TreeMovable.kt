package nl.jhvh.draughts.model.move

interface TreeMovable {

    val destinations: Collection<TreeMovable>

}