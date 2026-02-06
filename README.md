# Social Network Console

A console-based social network application implemented in **Kotlin**, following clean architecture principles and fully covered by unit and integration tests.

The application allows users to post messages, follow other users, read personal timelines, and view aggregated walls, with human-readable time formatting.

---

## Features

- Post messages to a personal timeline
- Read a user timeline (most recent first)
- Follow other users
- View a wall aggregating own posts and followed users’ posts
- Human-readable time formatting (e.g. `5 minutes ago`)
- Graceful handling of invalid or malformed input
- Deterministic and fully testable design

---

## Architecture

The project follows a **Clean / Hexagonal Architecture** approach:

```
src/main/kotlin
├── domain
│   ├── model        # Core domain models (Post, UserName)
│   ├── repo         # Repository interfaces + in-memory implementations
│   └── time         # Time formatting abstraction
│
├── application
│   └── SocialNetworkService   # Application use cases
│
├── app
│   ├── ConsoleApp   # CLI adapter (input/output)
│   └── Main.kt      # Application entry point and wiring
│
├── parsing
│   └── CommandParser + Command sealed types
```

### Layer responsibilities

- **domain**  
  Pure business logic. No IO, no framework dependencies.

- **application**  
  Coordinates use cases (post, read, follow, wall) using domain abstractions.

- **app**  
  Console adapter. Responsible for parsing input and formatting output.

This separation keeps the core logic independent from the delivery mechanism and easy to extend (e.g. REST API, GUI).

---

## Time handling

Time is treated as an explicit dependency:

- `Clock` is injected for deterministic behavior
- `TimeFormatter` abstracts time presentation
- `HumanTimeFormatter` converts instants into human-readable strings

Example output:
```
Alice - Hello (5 seconds ago)
Bob - Hi (2 minutes ago)
```

---

## Running the application

### Prerequisites
- JDK 21+

### Run
```bash
./gradlew run --quiet --console=plain
```

Type commands in the console.  
Exit with `Ctrl+D`.

---

## Supported commands

```
Alice -> Hello        # Post a message
Alice                # Read Alice's timeline
Alice follows Bob    # Follow another user
Alice wall           # View aggregated wall
```

---

## Testing

Run all checks (tests, linting, static analysis):

```bash
./gradlew check
```
