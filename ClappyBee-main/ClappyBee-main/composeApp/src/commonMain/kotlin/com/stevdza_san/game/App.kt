package com.stevdza_san.game

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clappybee.composeapp.generated.resources.*
import com.stevdza_san.game.domain.Game
import com.stevdza_san.game.domain.GameStatus
import com.stevdza_san.game.ui.green
import com.stevdza_san.game.util.ChewyFontFamily
import com.stevdza_san.game.util.Platform
import com.stevdza_san.game.util.getPlatform
import com.stevdza_san.sprite.component.drawSpriteView
import com.stevdza_san.sprite.domain.SpriteSheet
import com.stevdza_san.sprite.domain.SpriteSpec
import com.stevdza_san.sprite.domain.rememberSpriteState
import compose.icons.FeatherIcons
import compose.icons.feathericons.Play
import compose.icons.feathericons.RefreshCw
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val PIPE_CAP_HEIGHT = 50F

@Composable
@Preview
fun App() {
    MaterialTheme {
        val platform = remember { getPlatform() }
        val game = remember { Game(platform = platform) }

        val scaleFactor = (game.beeRadius * 3f) / 360f //Para dibujar el tucán en su tamaño real

        val spriteState = rememberSpriteState(totalFrames = 9, framesPerRow = 3)

        // El SpriteSpec debe observar screenWidth para recrearse si cambia
        val spriteSpec = remember { // Eliminamos la dependencia de screenWidth para evitar parpadeos
            SpriteSpec(
                screenWidth = 2000f, // Un valor base alto para que no recorte por ancho
                default = SpriteSheet(
                    frameWidth = 360,   // Tamaño real de tu tucan.png
                    frameHeight = 360,
                    image = Res.drawable.tucan
                )
            )
        }

        val sheetImage = spriteSpec.imageBitmap
        val animatedAngle by animateFloatAsState(
            targetValue = if (game.beeVelocity > game.beeMaxVelocity / 1.1) 30f else 0f
        )

        // Bucle del juego: Espera a que status sea Started Y screenWidth > 0
        LaunchedEffect(game.status, game.screenWidth) {
            if (game.screenWidth > 0) {
                while (game.status == GameStatus.Started) {
                    withFrameMillis { game.updateGameProgress() }
                }
                if (game.status == GameStatus.Over) spriteState.stop()
            }
        }

        LaunchedEffect(game.isRainbowActive) {
            if (game.isRainbowActive) {
                delay(5000)
                game.isRainbowActive = false
            }
        }

        val scope = rememberCoroutineScope()
        val backgroundOffsetX = remember { Animatable(0f) }
        var imageWidth by remember { mutableStateOf(0) }
        val pipeImage = imageResource(Res.drawable.pipe)
        val pipeCapImage = imageResource(Res.drawable.pipe_cap)

        LaunchedEffect(game.status) {
            while (game.status == GameStatus.Started) {
                backgroundOffsetX.animateTo(
                    targetValue = -imageWidth.toFloat(),
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = if (platform == Platform.Android || platform == Platform.iOS) 5000 else 10000,
                            easing = LinearEasing
                        ),
                        repeatMode = RepeatMode.Restart
                    )
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(Res.drawable.newbackground),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            // Fondos animados
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged { imageWidth = it.width }
                    .offset { IntOffset(x = backgroundOffsetX.value.toInt(), y = 0) },
                painter = painterResource(Res.drawable.newmoving_background),
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(x = backgroundOffsetX.value.toInt() + imageWidth, y = 0) },
                painter = painterResource(Res.drawable.newmoving_background),
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .onGloballyPositioned { coordinates ->
                        // Sincronizamos dimensiones con el objeto Game
                        game.updateDimensions(coordinates.size.width, coordinates.size.height)
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        if (game.status == GameStatus.Started) game.jump()
                    }
            ) {
                // Solo dibujar si el juego ya conoce las dimensiones
                if (game.screenWidth > 0) {
                    // 1. DIBUJAR TUBERÍAS
                    game.pipePairs.forEach { pipePair ->
                        drawImage(
                            image = pipeImage,
                            dstOffset = IntOffset((pipePair.x - game.pipeWidth / 2).toInt(), 0),
                            dstSize = IntSize(game.pipeWidth.toInt(), (pipePair.topHeight - PIPE_CAP_HEIGHT).toInt())
                        )
                        drawImage(
                            image = pipeCapImage,
                            dstOffset = IntOffset((pipePair.x - game.pipeWidth / 2).toInt(), (pipePair.topHeight - PIPE_CAP_HEIGHT).toInt()),
                            dstSize = IntSize(game.pipeWidth.toInt(), PIPE_CAP_HEIGHT.toInt())
                        )
                        drawImage(
                            image = pipeCapImage,
                            dstOffset = IntOffset((pipePair.x - game.pipeWidth / 2).toInt(), (pipePair.y + game.pipeGapSize / 2).toInt()),
                            dstSize = IntSize(game.pipeWidth.toInt(), PIPE_CAP_HEIGHT.toInt())
                        )
                        drawImage(
                            image = pipeImage,
                            dstOffset = IntOffset((pipePair.x - game.pipeWidth / 2).toInt(), (pipePair.y + game.pipeGapSize / 2 + PIPE_CAP_HEIGHT).toInt()),
                            dstSize = IntSize(game.pipeWidth.toInt(), (pipePair.bottomHeight - PIPE_CAP_HEIGHT).toInt())
                        )
                    }

                    // 2. POWER-UP
                    game.powerUp?.let { pu ->
                        drawCircle(
                            brush = androidx.compose.ui.graphics.Brush.radialGradient(
                                colors = listOf(Color.White, Color.Cyan, Color.Transparent),
                                center = Offset(pu.x, pu.y),
                                radius = pu.radius * 1.5f
                            ),
                            radius = pu.radius,
                            center = Offset(pu.x, pu.y)
                        )
                    }

                    // 3. PERSONAJE (Solo si bee tiene posición válida)
                    rotate(degrees = animatedAngle, pivot = Offset(x = game.bee.x, y = game.bee.y)) {
                        if (game.isRainbowActive) {
                            drawCircle(
                                brush = androidx.compose.ui.graphics.Brush.sweepGradient(
                                    colors = listOf(Color.Red, Color.Yellow, Color.Green, Color.Blue, Color.Red)
                                ),
                                radius = game.beeRadius + 15f,
                                center = Offset(game.bee.x, game.bee.y),
                                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 8f)
                            )
                        }

                        // Dibujamos el tucán usando el bitmap directamente
                        sheetImage?.let { bitmap ->
                            val visualSize = (game.beeRadius * 3).toInt() // Ajusta este 3 para el tamaño
                            drawImage(
                                image = bitmap,
                                dstOffset = IntOffset(
                                    x = (game.bee.x - visualSize / 2).toInt(),
                                    y = (game.bee.y - visualSize / 2).toInt()
                                ),
                                dstSize = IntSize(visualSize, visualSize) // Esto hace que se vea ENTERO y escalado
                            )
                        }
                    }
                }
            }

            // HUD
            Row(
                modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 48.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // Panel de Score Actual
                Box(
                    modifier = Modifier
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(Color(0xFF8BC34A), Color(0xFF388E3C))
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .border(3.dp, Color.White, RoundedCornerShape(16.dp))
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "${game.currentScore}",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontFamily = ChewyFontFamily(),
                        style = TextStyle(
                            shadow = Shadow(Color.Black.copy(0.4f), Offset(4f, 4f), 4f)
                        )
                    )
                }

                // Panel de Best Score
                Surface(
                    color = Color(0xFF2E7D32).copy(alpha = 0.9f),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(2.dp, Color(0xFFC5E1A5))
                ) {
                    Text(
                        text = "BEST: ${game.bestScore}",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = ChewyFontFamily()
                    )
                }

            }

            // Overlays
            if (game.status == GameStatus.Idle) {
                Box(Modifier.fillMaxSize().background(Color.Black.copy(0.5f)), Alignment.Center) {
                    Button(
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = green),
                        onClick = { game.start(); spriteState.start() }
                    ) {
                        Icon(FeatherIcons.Play, null, tint = Color.White)
                        Text(" START", fontSize = MaterialTheme.typography.titleLarge.fontSize, fontFamily = ChewyFontFamily())
                    }
                }
            }

            if (game.status == GameStatus.Over) {
                Column(Modifier.fillMaxSize().background(Color.Black.copy(0.5f)), Arrangement.Center, Alignment.CenterHorizontally) {
                    Text("Game Over!", color = Color.White, fontSize = MaterialTheme.typography.displayMedium.fontSize, fontFamily = ChewyFontFamily())
                    Text("SCORE: ${game.currentScore}", color = Color.White, fontSize = MaterialTheme.typography.titleLarge.fontSize, fontFamily = ChewyFontFamily())
                    Spacer(Modifier.height(24.dp))
                    Button(
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = green),
                        onClick = {
                            game.restart()
                            spriteState.start()
                            scope.launch { backgroundOffsetX.snapTo(0f) }
                        }
                    ) {
                        Icon(FeatherIcons.RefreshCw, null, tint = Color.White)
                        Text(" RESTART", fontSize = MaterialTheme.typography.titleLarge.fontSize, fontFamily = ChewyFontFamily())
                    }
                }
            }
        }
    }
}