package nl.jhvh.draughts.model.movement.options

interface CapturingTreeMovable: TreeMovable {

    override val options: Collection<CapturingTreeMovable>
        get() = TODO("Not yet implemented")
}