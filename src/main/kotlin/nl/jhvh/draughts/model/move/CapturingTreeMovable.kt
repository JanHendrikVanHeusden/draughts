package nl.jhvh.draughts.model.move

interface CapturingTreeMovable: TreeMovable {

    override val destinations: Collection<CapturingTreeMovable>
        get() = TODO("Not yet implemented")
}