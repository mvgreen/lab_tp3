package main.java.com.mvgreen

interface GameModel {

    companion object {
        const val REGISTRATION_CLOSED = -1
    }

    /* Регистрирует нового игрока, возвращает его ID или REGISTRATION_CLOSED, если регистрация закрыта */
    fun addPlayer(): Int

    /* Возвращает состояние игры, которое может наблюдать указанный игрок */
    fun getStatusForPlayer(playerId: Int): GameStatus?

    /* Обновляет состояние игры */
    fun submitPlayerAction(playerAction: PlayerAction): Boolean

}