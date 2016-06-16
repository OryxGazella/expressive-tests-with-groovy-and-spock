package soy.frank.flutterby

import soy.frank.flutterby.actors.DragonFly
import soy.frank.flutterby.actors.ImmutablePhysicalEntity
import soy.frank.flutterby.actors.ImmutableVector2D
import soy.frank.flutterby.actors.Laser

class PhysicalEntitySpec {

    @Delegate
    ImmutablePhysicalEntity.Builder builder = ImmutablePhysicalEntity.builder()
    def positionBuilder = ImmutableVector2D.builder()

    def x (float xPos) {
        positionBuilder.x(xPos)
    }

    def y(float yPos) {
        positionBuilder.y(yPos)
    }

    def dragonfly() {
        builder.height(DragonFly.HEIGHT)
        builder.width(DragonFly.WIDTH)
    }

    def laser() {
        builder.velocity(Laser.VELOCITY)
        builder.acceleration(Laser.ACCELERATION)
        builder.width(Laser.WIDTH)
        builder.height(Laser.HEIGHT)
    }

    def acceleration(float a) {
        builder.acceleration(a)
    }

    def build() {
        builder.position(positionBuilder.build())
        builder.build()
    }
}
