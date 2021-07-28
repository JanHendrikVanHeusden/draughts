package nl.jhvh.draughts.rule

import mu.NamedKLogging

class ValidationException(override val message: String?, override val cause: Throwable? = null) :
    IllegalArgumentException(message, cause)

/**
 * Validates that [validationCheck] is `true`
 * @param validationCheck The check to be evaluated; should be `true` to pass validation
 * @param message The validation message associated with the failing check
 * @throws [ValidationException] thrown if the [validationCheck] fails (subclass of [ValidationException]
 */
fun validate(validationCheck: Boolean, message: String) {
    if (!validationCheck) throw ValidationException(message)
}

/**
 * Validates that [validationCheck] is `true`
 * @param validationCheck The check to be evaluated; should be `true` to pass validation
 * @param messageProvider The source of the validation message associated with the failing check;
 *                        fail-safe, if evaluation of the [messageProvider] fails, the [ValidationException] will still be thrown
 * @throws [ValidationException] thrown if the [validationCheck] fails (subclass of [ValidationException]
 */
inline fun validate(validationCheck: Boolean, messageProvider: () -> String) {
    if (!validationCheck) {
        var message: String
        try {
            message = messageProvider()
        } catch (e: Exception) {
            message = e.message?.trim() ?: "Exception occurred inside validation while evaluating the `messageProvider`"
            NamedKLogging("validate").logger.error(message, e)
        }
        throw ValidationException(message)
    }
}