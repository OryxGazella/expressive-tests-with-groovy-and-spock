package soy.frank.flutterby

import soy.frank.flutterby.actors.Butterfly
import soy.frank.flutterby.actors.ImmutableScene
import soy.frank.flutterby.actors.PhysicalEntity

class SceneSpec {

    @Delegate
    def ImmutableScene.Builder builder = ImmutableScene.builder()

    def butterfly(@DelegatesTo(PhysicalEntitySpec) Closure closure) {
        def physicalEntitySpec = new PhysicalEntitySpec()
        def code = closure.rehydrate(physicalEntitySpec, this, this)
        physicalEntitySpec.width(Butterfly.WIDTH)
        physicalEntitySpec.height(Butterfly.HEIGHT)
        code()
        builder.butterfly(physicalEntitySpec.build())
    }

    def dragonflies(PhysicalEntity dragonfly) {
        builder.dragonflies([dragonfly])
        this
    }

    def lasers(PhysicalEntity laser) {
        builder.lasers([laser])
        this
    }
}
