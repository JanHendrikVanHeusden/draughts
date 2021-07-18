package nl.jhvh.draughts

internal fun Int.isEven(): Boolean = this % 2 == 0
internal fun Int.isOdd(): Boolean = !this.isEven()
