package soy.frank.flutterby

import soy.frank.flutterby.actors.Butterfly
import soy.frank.flutterby.actors.ImmutableScene
import soy.frank.flutterby.actors.Laser
import soy.frank.flutterby.actors.PhysicalEntity
import soy.frank.flutterby.input.ButterflyControls
import spock.lang.Specification
import spock.lang.Unroll

class GameLogicTest extends Specification {

    public static final float velocity = 0.005f
    public static final initialX = 0.0f
    public static final initialY = 0.0f
    def static initialScene = ImmutableScene.builder().butterfly(PhysicalEntity.createButterflyAt(initialX, initialY)).build() as ImmutableScene

    @Unroll
    def "Butterfly moving #direction is displaced by #displacement on the #axis axis"() {
        expect:
        GameLogic.applyLogic(initialScene, move(direction)).butterfly().position()."$axis"() == displacement

        where:
        direction | displacement                    | axis
        "up"      | (initialY + velocity).toFloat() | "y"
        "right"   | (initialX + velocity).toFloat() | "x"
        "down"    | (initialY - velocity).toFloat() | "y"
        "left"    | (initialX - velocity).toFloat() | "x"
    }

    private ButterflyControls move(String direction) {
        def upperCase = direction.capitalize()
        Stub(ButterflyControls) { "move$upperCase"() >> true }
    }

    def "Butterfly spawns a laser in the center of its head when the fire control is sent"() {
        expect:
        GameLogic.applyLogic(initialScene, fire()) ==
                initialScene
                        .withLasers(PhysicalEntity.createLaserAt(Butterfly.WIDTH / 2 - Laser.WIDTH / 2 as float, Butterfly.HEIGHT))
    }

    ButterflyControls fire() {
        Stub(ButterflyControls) { fire() >> true }
    }
}
