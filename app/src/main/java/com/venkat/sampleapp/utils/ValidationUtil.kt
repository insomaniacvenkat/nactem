package com.venkat.sampleapp.utils


/**
 * Validates the abbreviation.
 * Util class to add more helper methods
 * @param abbreviation The abbreviation to be validated.
 * @return A Pair indicating whether the abbreviation is valid (Boolean) and a message (String).
 */
object ValidationUtil {

    private const val SUCCESS_MESSAGE = "Success"
    private const val EMPTY_SF_MESSAGE = "Please provide a valid abbreviation."
    private const val SINGLE_CHAR_SF_MESSAGE = "Abbreviation can't be a single character."
    private const val NON_ALPHABET_SF_MESSAGE = "Abbreviation can contain only alphabets."
    const val NETWORK_ERROR_MESSAGE = "Please check Internet Connectivity."
    const val RESPONSE_ERROR_MESSAGE = "Response is null or empty."

    fun isValid(abbreviation: String): Pair<Boolean, String> {
        return when {
            abbreviation.isEmpty() -> Pair(false, EMPTY_SF_MESSAGE)
            abbreviation.length == 1 -> Pair(false, SINGLE_CHAR_SF_MESSAGE)
            !abbreviation.all { it.isLetter() } -> Pair(false, NON_ALPHABET_SF_MESSAGE)
            else -> Pair(true, SUCCESS_MESSAGE)
        }
    }
}
