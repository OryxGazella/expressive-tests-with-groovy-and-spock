package soy.frank.flutterby

import soy.frank.flutterby.actors.DragonFly
import soy.frank.flutterby.actors.ImmutableExplosion
import soy.frank.flutterby.actors.ImmutablePhysicalEntity
import soy.frank.flutterby.actors.ImmutableScene
import soy.frank.flutterby.actors.PhysicalEntity

class DSL {
    static ImmutableScene aScene(@DelegatesTo(SceneSpec) Closure cl) {
        def scene = new SceneSpec()
        def code = cl.rehydrate(scene, scene, scene)
        code.run()
        scene.build()
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

    static ImmutablePhysicalEntity aDragonflyLaser(@DelegatesTo(PhysicalEntitySpec) Closure closure) {
        def laserSpec = new PhysicalEntitySpec()
        laserSpec.laser()
        def code = closure.rehydrate(laserSpec, this, this)
        code.run()
        laserSpec.velocity(DragonFly.LASER_VELOCITY)
        laserSpec.acceleration(DragonFly.LASER_ACCELERATION)
        laserSpec.build()
    }

    static ImmutablePhysicalEntity aDragonfly(@DelegatesTo(PhysicalEntitySpec) Closure closure) {
        def dragonflySpec = new PhysicalEntitySpec()
        dragonflySpec.dragonfly()
        def code = closure.rehydrate(dragonflySpec, this, this)
        code.run()
        dragonflySpec.build()
    }

    static ImmutableExplosion anExplosion(@DelegatesTo(ImmutableExplosion.Builder) Closure closure) {
        def builder = ImmutableExplosion.builder()
        closure.delegate = builder
        closure.run()
        builder.build()
    }
}
