package com.stevdza_san.game.domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.stevdza_san.game.domain.PowerUp.PowerUp
import com.stevdza_san.game.domain.PowerUp.PowerUpType
import com.stevdza_san.game.domain.database.UserStats
import com.stevdza_san.game.domain.database.UserStatsDAO
import com.stevdza_san.game.util.Platform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class Game(
    val platform: Platform,
    val gravity: Float = if (platform == Platform.Android) 0.5f else 0.15f,
    val beeRadius: Float = 40f,
    val beeJumpImpulse: Float = if (platform == Platform.Android) -8f else -6f,
    val beeMaxVelocity: Float = 25f,
    val pipeWidth: Float = 150f,
    val pipeVelocity: Float = if (platform == Platform.Android) 5f else 2.5f,
    val pipeGapSize: Float = if (platform == Platform.Android) 400f else 450f
) : KoinComponent {

    // --- ESTADOS DEL JUEGO ---
    var screenWidth by mutableStateOf(0)
    var screenHeight by mutableStateOf(0)
    var status by mutableStateOf(GameStatus.Idle)
    var beeVelocity by mutableStateOf(0f)
    var bee by mutableStateOf(Bee(x = 0f, y = 0f, radius = beeRadius))
    val pipePairs = mutableStateListOf<PipePair>()
    var currentScore by mutableStateOf(0)
    var bestScore by mutableStateOf(0)

    // --- POWER-UPS Y ESTADÍSTICAS ---
    var powerUp by mutableStateOf<PowerUp?>(null)
    var isRainbowActive by mutableStateOf(false)
    var shieldsInCurrentGame by mutableStateOf(0)
    private var lastPowerUpScore = -1

    // --- INYECCIONES ---
    private val audioPlayer: AudioPlayer by inject()
    private val userStatsDAO: UserStatsDAO by inject()
    private val gameScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    init {
        // Carga el récord histórico desde la base de datos o settings
        gameScope.launch {
            val record = withContext(Dispatchers.Default) { userStatsDAO.getBestScore() }
            bestScore = record?.score ?: 0
        }
    }

    // Actualiza las dimensiones y posiciona al personaje al inicio
    fun updateDimensions(width: Int, height: Int) {
        if (width > 0 && screenWidth == 0) {
            screenWidth = width
            screenHeight = height
            bee = Bee(x = (width / 4).toFloat(), y = (height / 2).toFloat(), radius = beeRadius)
        }
    }

    fun start() {
        if (screenWidth == 0) return
        status = GameStatus.Started
        audioPlayer.playGameSoundInLoop()
    }

    fun jump() {
        if (status != GameStatus.Started) return
        // El salto se vuelve ligeramente más fuerte con la puntuación
        val jumpMultiplier = 1.0f + (currentScore / 10) * 0.15f
        beeVelocity = beeJumpImpulse * jumpMultiplier
        audioPlayer.playJumpSound()
    }

    @OptIn(ExperimentalTime::class)
    fun gameOver() {
        if (status == GameStatus.Over) return
        status = GameStatus.Over
        audioPlayer.stopGameSound()

        val finalScore = currentScore
        val finalShields = shieldsInCurrentGame

        // Guarda la partida en la base de datos de forma asíncrona
        gameScope.launch(Dispatchers.Default) {
            try {
                userStatsDAO.insertStat(UserStats(
                    score = finalScore,
                    shieldsCollected = finalShields,
                    timestamp = Clock.System.now().toEpochMilliseconds()
                ))
                if (finalScore > bestScore) {
                    withContext(Dispatchers.Main) { bestScore = finalScore }
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
        powerUp = null
        isRainbowActive = false
    }

    fun restart() {
        bee = bee.copy(y = (screenHeight / 2).toFloat())
        beeVelocity = 0f
        pipePairs.clear()
        currentScore = 0
        lastPowerUpScore = -1
        shieldsInCurrentGame = 0
        isRainbowActive = false
        start()
    }

    fun updateGameProgress() {
        if (status != GameStatus.Started || screenWidth == 0) return

        // Aumenta la velocidad de las tuberías según la puntuación
        val difficultyMultiplier = 1.0f + (currentScore / 5) * 0.3f
        val currentSpeed = pipeVelocity * difficultyMultiplier

        val iterator = pipePairs.listIterator()
        while (iterator.hasNext()) {
            val pipePair = iterator.next()
            pipePair.x -= currentSpeed

            // Gestión de colisiones
            if (isCollision(pipePair)) {
                if (isRainbowActive) {
                    // Si tienes el escudo, rompes la tubería y pierdes el escudo
                    isRainbowActive = false
                    pipePair.scored = true
                } else {
                    gameOver()
                    return
                }
            }

            // Incrementar puntuación al pasar la tubería
            if (!pipePair.scored && bee.x > pipePair.x + (pipeWidth / 2)) {
                pipePair.scored = true
                currentScore++
            }
            if (pipePair.x + pipeWidth < 0) iterator.remove()
        }

        // Movimiento y recolección del Power-Up
        powerUp?.let {
            it.move(currentSpeed)
            val distance = kotlin.math.hypot(bee.x - it.x, bee.y - it.y)
            if (distance < beeRadius + it.radius) {
                isRainbowActive = true
                shieldsInCurrentGame++
                powerUp = null
            } else if (it.x < -100f) powerUp = null
        }

        // Aplicar gravedad al personaje
        beeVelocity = (beeVelocity + gravity).coerceIn(-beeMaxVelocity, beeMaxVelocity)
        bee = bee.copy(y = bee.y + beeVelocity)

        if (bee.y < 0 || bee.y > screenHeight) {
            gameOver()
            return
        }
        spawnPipes()
    }

    private fun spawnPipes() {
        val threshold = if (screenWidth > screenHeight) screenWidth / 1.5 else screenWidth / 3.0
        if (pipePairs.isEmpty() || pipePairs.last().x < threshold) {
            val topHeight = Random.nextFloat() * (screenHeight / 3) + 100f
            val pipeX = screenWidth.toFloat() + pipeWidth
            val pipeY = topHeight + (pipeGapSize / 2)

            pipePairs.add(PipePair(x = pipeX, y = pipeY, topHeight = topHeight, bottomHeight = screenHeight - topHeight - pipeGapSize))

            //Se intenta generar cada 3 puntos
            if (currentScore % 3 == 0 && currentScore != 0 && lastPowerUpScore != currentScore) {
                //Probabilidad de 1 entre 2 (50%)
                if (Random.nextInt(1, 3) == 1) {
                    lastPowerUpScore = currentScore
                    powerUp = PowerUp(type = PowerUpType.SHIELD, initialX = pipeX, initialY = pipeY)
                }
            }
        }
    }

    private fun isCollision(pipePair: PipePair): Boolean {
        if (pipePair.scored) return false
        val beeRight = bee.x + bee.radius
        val beeLeft = bee.x - bee.radius
        val pipeLeft = pipePair.x - (pipeWidth / 2)
        val pipeRight = pipePair.x + (pipeWidth / 2)

        if (beeRight > pipeLeft && beeLeft < pipeRight) {
            val beeTop = bee.y - bee.radius
            val beeBottom = bee.y + bee.radius
            val gapTop = pipePair.y - (pipeGapSize / 2)
            val gapBottom = pipePair.y + (pipeGapSize / 2)
            return (beeTop < gapTop || beeBottom > gapBottom)
        }
        return false
    }
}