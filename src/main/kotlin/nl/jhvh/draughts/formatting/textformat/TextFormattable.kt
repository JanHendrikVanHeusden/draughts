package nl.jhvh.draughts.formatting.textformat

import nl.jhvh.draughts.formatting.DraughtsFormatting
import nl.jhvh.draughts.model.base.BoardElement


/**
 * Interface to support formatting of Draughts elements ([BoardElement]s) to a human or machine readable format.
 *
 * The design patterns used is [Visitor Pattern](https://en.wikipedia.org/wiki/Visitor_pattern#Java_example).
 *  * The [format] method in this interface represents the 'accept' method of the classic Visitor Pattern.
 * @param E The [BoardElement] type to format
 * @param T The target type to which the [BoardElement] is to be formatted
 */
interface TextFormattable<E: BoardElement, T: FormattableList> {

    /**
     * Output a draughts [BoardElement] (e.g. [Board], [DraughtsPiece], [Square]),
     * in a human readable or machine readable (e.g. HTML, json) way
     *
     * @param draughtsFormatter The [DraughtsFormatting] implementation to be accepted by the element to print.
     *  * The element to print will accept the [DraughtsFormatting] as a delegate for the print formatting
     * @return The draughts element as formatted to [T]
     */
    fun format(draughtsFormatter: DraughtsFormatting<E, T>): T
}