package parsing

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class CommandParserFreeSpec :
    FreeSpec({

        "CommandParser" - {

            "posting" - {
                "parses user -> message" {
                    CommandParser().parse("Alice -> Hello") shouldBe Command.Post("Alice", "Hello")
                }

                "trims spaces around user and message" {
                    CommandParser().parse("  Alice   ->   Hi  ") shouldBe Command.Post("Alice", "Hi")
                }

                "rejects blank user" {
                    CommandParser().parse(" -> Hi") shouldBe null
                }

                "rejects blank message" {
                    CommandParser().parse("Alice ->   ") shouldBe null
                }

                "keeps arrows inside message (only splits by first ' -> ')" {
                    CommandParser().parse("Alice -> a -> b") shouldBe Command.Post("Alice", "a -> b")
                }
            }

            "reading" - {
                "parses user only" {
                    CommandParser().parse("Alice") shouldBe Command.Read("Alice")
                }

                "trims user name" {
                    CommandParser().parse("   Alice   ") shouldBe Command.Read("Alice")
                }
            }

            "following" - {
                "parses follows command" {
                    CommandParser().parse("Charlie follows Bob") shouldBe Command.Follow("Charlie", "Bob")
                }

                "trims both sides" {
                    CommandParser().parse("  Charlie   follows   Bob  ") shouldBe Command.Follow("Charlie", "Bob")
                }

                "rejects missing follower" {
                    CommandParser().parse(" follows Bob") shouldBe null
                }

                "rejects missing followee" {
                    CommandParser().parse("Charlie follows ") shouldBe null
                }
            }

            "wall" - {
                "parses wall command" {
                    CommandParser().parse("Charlie wall") shouldBe Command.Wall("Charlie")
                }

                "trims user" {
                    CommandParser().parse("   Charlie   wall  ") shouldBe Command.Wall("Charlie")
                }

                "rejects empty user" {
                    CommandParser().parse(" wall") shouldBe null
                }
            }

            "invalid input" - {
                "returns null for blank line" {
                    CommandParser().parse("") shouldBe null
                    CommandParser().parse("   ") shouldBe null
                }
            }
        }
    })
