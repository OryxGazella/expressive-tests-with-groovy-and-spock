package soy.frank.flutterby

import soy.frank.flutterby.actors.Butterfly
import soy.frank.flutterby.actors.ImmutableScene
import soy.frank.flutterby.actors.PhysicalEntity

class SceneSpec {
    def ImmutableScene.Builder builder = ImmutableScene.builder()

    static ImmutableScene aScene(@DelegatesTo(SceneSpec) Closure cl) {
        def scene = new SceneSpec()
        def code = cl.rehydrate(scene, scene, scene)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()
        scene.build()
    }

    def ImmutableScene build() {
        builder.build()
    }

    def cooldown(int cooldown) {
        builder.cooldown(cooldown)
    }

    def butterfly(@DelegatesTo(PhysicalEntitySpec) Closure closure) {
        def physicalEntitySpec = new PhysicalEntitySpec()
        def code = closure.rehydrate(physicalEntitySpec, this, this)
        physicalEntitySpec.width(Butterfly.WIDTH)
        physicalEntitySpec.height(Butterfly.HEIGHT)
        code()
        builder.butterfly(physicalEntitySpec.build())
    }

    def lasers(PhysicalEntity... entities) {
        builder.lasers(entities.toList())
    }

    def dragonflies(PhysicalEntity... entities) {
        builder.dragonflies(entities.toList())
    }

    def lasers(List<PhysicalEntity> lasers) {
        builder.lasers(lasers)
    }
}
