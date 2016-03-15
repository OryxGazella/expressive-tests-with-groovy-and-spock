package soy.frank.flutterby

import soy.frank.flutterby.actors.ImmutablePhysicalEntity
import soy.frank.flutterby.actors.ImmutableVector2D
import soy.frank.flutterby.actors.Laser
import soy.frank.flutterby.actors.PhysicalEntity

class PhysicalEntitySpec {

    def builder = ImmutablePhysicalEntity.builder()
    def positionBuilder = ImmutableVector2D.builder()

    def x (float xPos) {
        positionBuilder.x(xPos)
    }

    def y(float yPos) {
        positionBuilder.y(yPos)
    }

    static PhysicalEntity aPhysicalEntity(@DelegatesTo(PhysicalEntitySpec) Closure closure) {
        def physicalEntitySpec = new PhysicalEntitySpec()
        def code = closure.rehydrate(physicalEntitySpec, this, this)
        code.run()
        physicalEntitySpec.build()

    }


    static ImmutablePhysicalEntity aLaser(@DelegatesTo(PhysicalEntitySpec) Closure closure) {
        def laserSpec = new PhysicalEntitySpec()
        laserSpec.laser()
        def code = closure.rehydrate(laserSpec, this, this)
        code.run()
        laserSpec.build()
    }

    private void laser() {
        velocity(Laser.VELOCITY)
        acceleration(Laser.ACCELERATION)
        width(Laser.WIDTH)
        height(Laser.HEIGHT)
    }

    def acceleration(float a) {
        builder.acceleration(a)
    }

    ImmutablePhysicalEntity build() {
        builder.position(positionBuilder.build())
        builder.build()
    }

    def width(float w) {
        builder.width(w)
    }

    def height(float h) {
        builder.height(h)
    }

    def velocity(float v) {
        builder.velocity(v)
    }
}
