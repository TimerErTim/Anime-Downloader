package eu.timerertim.downlomatic.console

import org.apache.commons.cli.Option

/**
 * This enum stores every possible argument passable via CLI on the server program.
 */
enum class ServerArgument(override val option: Option, override val isHidden: Boolean = false) : Argument {
    HELP(Option(null, "help", false, "shows this")),
    VERBOSE(Option("verbose", "v", false, "enables all levels of log messages"), true);

    constructor(option: Option, isHidden: Boolean, setup: Option.() -> Unit) : this(option, isHidden) {
        setup(this.option)
    }

    constructor(option: Option, setup: Option.() -> Unit) : this(option, false, setup)
}