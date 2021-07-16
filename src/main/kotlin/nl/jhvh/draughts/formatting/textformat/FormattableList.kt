package nl.jhvh.draughts.formatting.textformat

val lineSeparator: String = System.lineSeparator()


/** [List]<[String]> with [toString] overridden for of formatting board elements */
class FormattableList(collection: List<String> = emptyList()):
    List<String> by ArrayList<String>(collection) {

    /**
     * Produces desired results when formatting a draughts board or board elements
     * @return The content of each line, separated by [lineSeparator] = [System.lineSeparator]
     */
    override fun toString(): String {
        return this.joinToString(separator = lineSeparator)
    }

    /** [equals] based [List.equals] */
    override fun equals(other: Any?): Boolean {
        return this.toList() == other
    }

    /** [hashCode] based [List.hashCode] */
    override fun hashCode(): Int {
        return this.toList().hashCode()
    }
}
