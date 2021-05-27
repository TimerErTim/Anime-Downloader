package eu.timerertim.downlomatic.console

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options

/**
 * Holds parsed [Argument]s to quickly check for console parameters.
 */
class ParsedArguments private constructor(private val commandLine: CommandLine, shownArguments: Options) {
    val missingArgumentMessage = commandLine.argList.getOrNull(0)?.takeIf { it.startsWith("missing-required: ") }
        ?.removePrefix("missing-required: ")
    val size = commandLine.options.size
    val sizeHidden = commandLine.options.toMutableList().apply { removeAll(shownArguments.options) }.size
    val sizeShown = commandLine.options.count { shownArguments.options.contains(it) }

    /**
     * Checks for all required [Argument]s.
     *
     * Returns false if one or more required Arguments are missing.
     */
    fun hasRequiredArguments() = missingArgumentMessage == null

    /**
     * Returns true if the [arg] is present.
     */
    fun hasArgument(arg: Argument) = commandLine.options.find { it == arg.option } != null

    /**
     * Returns the value for [arg] or null if it has none.
     */
    operator fun get(arg: Argument) = commandLine.options.find { it == arg.option }?.value

    /**
     * Returns all set values for [arg] or an empty array if it has no values.
     */
    fun getValues(arg: Argument): Array<String> = commandLine.options.find { it == arg.option }?.values ?: emptyArray()

    companion object {
        @JvmSynthetic
        fun Console._build(line: CommandLine, shownArguments: Options) = ParsedArguments(line, shownArguments)
    }
}