package com.obstacleavoid.game.entity

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle

abstract class GameObjectBase {
    // properties
    var x: Float = 0f
        set(value) {
            field = value
            updateBounds()
        }
    var y: Float = 0f
        set(value) {
            field = value
            updateBounds()
        }

    abstract val bounds : Circle

    // public functions
    fun setPosition(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun drawDebug(renderer: ShapeRenderer) = renderer.circle(bounds.x, bounds.y, bounds.radius, 30)

    // private functions
    private fun updateBounds() = bounds.setPosition(x,y)
}