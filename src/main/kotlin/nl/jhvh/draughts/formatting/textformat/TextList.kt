package nl.jhvh.draughts.formatting.textformat

val lineSeparator: String = System.lineSeparator()


/** [List]<[String]> with [toString] overridden for of formatting board elements */
open class FormattableList(collection: List<String> = emptyList()):
    List<String> by ArrayList<String>(collection) {

    /**
     * Produces desired results when formatting a draughts board or board elements
     * @return The content of each line, separated by [lineSeparator] = [System.lineSeparator]
     */
    override fun toString(): String = this.joinToString(separator = lineSeparator)

    /** [equals] based [List.equals] */
    override fun equals(other: Any?): Boolean = this.toList() == other

    /** [hashCode] based [List.hashCode] */
    override fun hashCode(): Int = this.toList().hashCode()

}
