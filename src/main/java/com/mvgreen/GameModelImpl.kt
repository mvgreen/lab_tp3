package main.java.com.mvgreen

import main.java.com.mvgreen.GameModel.Companion.REGISTRATION_CLOSED

class GameModelImpl : GameModel {

    companion object {
        const val PLAYERS_TOTAL = 4
        const val ITERATIONS = 2
    }

    private var playersRegistered = 0
    private var currentPlayer = 0
    private var currentLine = 0
    private val lines = Array(PLAYERS_TOTAL * ITERATIONS) { "" }

    override fun addPlayer(): Int {
        synchronized(this) {
            return if (playersRegistered >= PLAYERS_TOTAL) {
                REGISTRATION_CLOSED
            } else {
                playersRegistered++
            }
        }
    }

    override fun getStatusForPlayer(playerId: Int): GameStatus? {
        synchronized(this) {
            if (playersRegistered != PLAYERS_TOTAL) {
                return null
            }

            val completePoem = if (currentLine == lines.size) lines.joinToString("\n") else ""
            val prevLine = if (currentPlayer != playerId || currentLine == 0) "" else lines[currentLine - 1]

            return GameStatus(currentPlayer = currentPlayer, previousLine = prevLine, completePoem = completePoem)
        }
    }

    override fun submitPlayerAction(playerAction: PlayerAction): Boolean {
        synchronized(this) {
            if (playerAction.playerId != currentPlayer) {
                return false
            }

            lines[currentLine] = playerAction.line
            currentLine++
            if (currentLine == lines.size) {
                currentPlayer = -1
            } else {
                currentPlayer++
                if (currentPlayer >= PLAYERS_TOTAL) {
                    currentPlayer = 0
                }
            }
            return true
        }
    }

}