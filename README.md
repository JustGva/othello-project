# Othello Board Game

A Java implementation of Othello board game, featuring a text-based UI, a client-server multiplayer
mode over sockets, and two computer AI strategies.

Made in group with Jordan Sberlo.

## Features

- **Local two-player mode** via a text-based interface (`OthelloTUI`)
- **Networked multiplayer** — a `Server` that queues and matches players,
  with a custom text-based protocol for moves, login, and game-over states
- **Two computer AI strategies** — a `Naive` (random-move) player and a
  `Smart` player that prioritizes corners and edges
- **Unit tests** for the board logic, game rules, and network protocol

## Requirements

- **JDK 11** or later
- No build tool (Maven/Gradle) is used — the project is compiled and run
  directly with `javac`/`java`
- **JUnit 5** is included as a standalone jar in `lib/` for running the tests

## Building

From the project root:

```bash
javac -d out $(find src -name "*.java")
```

This compiles all source files into an `out/` folder, preserving the
`model`, `player`, `AI`, `networking`, `UI`, and `exceptions` packages.

## Running

### Local two-player game

```bash
java -cp out UI.OthelloTUI
```

Enter both player names when prompted, then take turns entering moves as a
column letter + row number (e.g. `D3`).

### Networked multiplayer

Networked play requires three separate processes: one server, and one
client per player.

**1. Start the server:**

```bash
java -cp out networking.Server
```

You'll be prompted to choose a port.

**2. Start each client** (in its own terminal):

```bash
java -cp out networking.Client
```

Each client will prompt for the server's IP address (`localhost` if
running on the same machine), the port, and a unique username. Once two
clients have queued up, the server matches them into a game.

To play from a different device on the same network, use the server
machine's local IP address instead of `localhost`. Playing across different
networks requires port forwarding or a tunneling tool and isn't set up out
of the box.


