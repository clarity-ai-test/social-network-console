package app

import application.SocialNetworkService
import domain.repo.InMemoryFollowRepository
import domain.repo.InMemoryPostRepository
import domain.time.HumanTimeFormatter
import parsing.CommandParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.time.Clock

fun main() {
    val clock = Clock.systemUTC()

    val service =
        SocialNetworkService(
            InMemoryPostRepository(),
            InMemoryFollowRepository(),
            clock,
        )

    val app =
        ConsoleApp(
            CommandParser(),
            service,
            HumanTimeFormatter(clock),
            BufferedReader(InputStreamReader(System.`in`)),
            PrintWriter(System.out, true),
        )

    app.run()
}
