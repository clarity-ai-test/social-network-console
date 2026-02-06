package app

import application.SocialNetworkService
import domain.time.TimeFormatter
import parsing.Command
import parsing.CommandParser
import java.io.BufferedReader
import java.io.PrintWriter

class ConsoleApp(
    private val parser: CommandParser,
    private val service: SocialNetworkService,
    private val timeFormatter: TimeFormatter,
    private val input: BufferedReader,
    private val output: PrintWriter,
) {
    fun run() {
        while (true) {
            val line = input.readLine() ?: break
            val command = parser.parse(line) ?: continue

            when (command) {
                is Command.Post -> service.post(command.user, command.message)

                is Command.Read ->
                    service.read(command.user).forEach {
                        output.println(
                            "${it.author.value} - " +
                                "${it.message} (${timeFormatter.formatSince(it.timestamp)})",
                        )
                    }

                is Command.Follow -> service.follow(command.user, command.other)

                is Command.Wall ->
                    service.wall(command.user).forEach {
                        output.println(
                            "${it.author.value} - " +
                                "${it.message} (${timeFormatter.formatSince(it.timestamp)})",
                        )
                    }
            }
        }
    }
}
