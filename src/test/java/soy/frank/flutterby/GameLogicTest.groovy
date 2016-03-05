package soy.frank.flutterby

import soy.frank.flutterby.actors.Butterfly
import soy.frank.flutterby.actors.ImmutableScene
import soy.frank.flutterby.actors.Laser
import soy.frank.flutterby.actors.PhysicalEntity
import soy.frank.flutterby.input.ButterflyControls
import spock.lang.Specification
import spock.lang.Unroll

class GameLogicTest extends Specification {

    public static final float velocity = Butterfly.VELOCITY
    def static initialScene = ImmutableScene.builder().butterfly(PhysicalEntity.createButterflyAt(0f, 0f)).build() as ImmutableScene

    @Unroll("A butterfly #moving is displaced by #deltaX on the x-axis and #deltaY on the y-axis")
    def "A butterfly moves according to the controls"() {
        expect:
        with(GameLogic.applyLogic(initialScene, move(directions)).butterfly().position()) {
            x() == deltaX
            y() == deltaY
        }

        where:
        moving                              | deltaX    | deltaY    | directions
        "standing still"                    | 0f        | 0f        | []
        "moving up"                         | 0f        | velocity  | ["up"]
        "moving diagonally right upwards"   | velocity  | velocity  | ["up", "right"]
        "moving right"                      | velocity  | 0f        | ["right"]
        "moving diagonally right downwards" | velocity  | -velocity | ["right", "down"]
        "moving down"                       | 0f        | -velocity | ["down"]
        "moving diagonally left downwards"  | -velocity | -velocity | ["left", "down"]
        "moving left"                       | -velocity | 0f        | ["left"]
        "moving diagonally left upwards"    | -velocity | velocity  | ["left", "up"]
    }

    def move(List<String> directions) {
        def controlsStub = Stub(ButterflyControls)
        directions.each {
            controlsStub."move${it.capitalize()}"() >> true
        }
        controlsStub
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
