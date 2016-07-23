package soy.frank.flutterby

import soy.frank.flutterby.actors.ImmutablePhysicalEntity
import soy.frank.flutterby.actors.ImmutableVector2D

class ButterflyBuilder {

    @Delegate
    ImmutablePhysicalEntity.Builder builder = ImmutablePhysicalEntity.builder()

    @Delegate
    ImmutableVector2D.Builder position = ImmutableVector2D.builder()

    def build() {
        builder.position(position.build()).build()
    }
}
