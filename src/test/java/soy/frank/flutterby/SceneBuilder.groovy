package soy.frank.flutterby

import soy.frank.flutterby.actors.ImmutableScene

class SceneBuilder {

    @Delegate
    ImmutableScene.Builder sceneBuilder = ImmutableScene.builder()

    def butterfly(@DelegatesTo(ButterflyBuilder) Closure closure) {
        def builder = new ButterflyBuilder()
        closure.delegate = builder
        closure.run()
        sceneBuilder.butterfly(builder.build())
        this
    }
}
