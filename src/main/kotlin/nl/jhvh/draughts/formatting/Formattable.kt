package nl.jhvh.draughts.formatting


/**
 * Interface to support formatting of Sudoku elements to a human readable format.
 *
 * The design patterns used is [Visitor Pattern](https://en.wikipedia.org/wiki/Visitor_pattern#Java_example).
 *  * The [format] method in this interface represents the 'accept' method of the classic Visitor Pattern.
 */
interface Formattable {

    /**
     * Output a draughts [BoardElement] (e.g. [Board], [DraughtsPiece], [Square]),
     * in a human readable or machine readable (e.g. HTML) way
     *
     * @param draughtsFormatter The [DraughtsFormatting] implementation to be accepted by the element to print.
     *  * The element to print will accept the [DraughtsFormatting] as a delegate for the print formatting
     * @return The draughts element as a formatted [FormattableList]
     */
    fun format(draughtsFormatter: DraughtsFormatting): FormattableList
}