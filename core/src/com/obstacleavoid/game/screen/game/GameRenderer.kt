package com.obstacleavoid.game.screen.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.obstacleavoid.game.assets.AssetPaths
import com.obstacleavoid.game.config.GameConfig
import com.obstacleavoid.game.util.clearScreen
import com.obstacleavoid.game.util.debug.DebugCameraController
import com.obstacleavoid.game.util.drawGrid
import com.obstacleavoid.game.util.toInternalFile
import com.obstacleavoid.game.util.use

class GameRenderer(private val controller: GameController) : Disposable {

    // properties
    private val camera = OrthographicCamera()
    private val viewport: Viewport = FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera)
    private val uiCamera = OrthographicCamera()
    private val uiViewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, uiCamera)
    private val renderer = ShapeRenderer()
    private val batch = SpriteBatch()
    private val uiFont = BitmapFont(AssetPaths.PURSIA_FONT.toInternalFile())
    private val padding = 20f
    private val layout = GlyphLayout()
    private val debugCameraController = DebugCameraController().apply {
        setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y)
    }

    // public functions
    fun render() {
        // handle debug camera controller
        debugCameraController.handleDebugInput()
        debugCameraController.applyTo(camera)

        clearScreen()
        renderDebug()
        renderUi()

        viewport.drawGrid(renderer)
    }

    private fun renderDebug() {
        // first we have to apply the world viewport
        viewport.apply()
        renderer.projectionMatrix = camera.combined

        renderer.use {
            controller.player.drawDebug(renderer)
            controller.obstacles.forEach { it.drawDebug(renderer) }
        }
    }

    private fun renderUi() {
        // first we have to apply the UI viewport
        uiViewport.apply()
        batch.projectionMatrix = uiCamera.combined

        batch.use {

            // draw lives
            val livesText = "LIVES: ${controller.lives}"
            layout.setText(uiFont, livesText)
            uiFont.draw(batch, layout, 20f, GameConfig.HUD_HEIGHT - layout.height)

            // draw score
            val scoreText = "SCORE: ${controller.displayScore}"
            layout.setText(uiFont, scoreText)
            uiFont.draw(batch, layout, GameConfig.HUD_WIDTH - layout.width - padding,
                    GameConfig.HUD_HEIGHT - layout.height)
        }
    }

    fun resize(width: Int, height: Int) {
        // the world viewport
        viewport.update(width, height, true)

        // the UI viewport
        uiViewport.update(width, height, true)
    }

    override fun dispose() {
        renderer.dispose()
        batch.dispose()
        uiFont.dispose()
    }
}