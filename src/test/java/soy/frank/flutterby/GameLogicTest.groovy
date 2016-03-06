package soy.frank.flutterby

import soy.frank.flutterby.actors.Butterfly
import soy.frank.flutterby.actors.ImmutablePhysicalEntity
import soy.frank.flutterby.actors.ImmutableScene
import soy.frank.flutterby.actors.Laser
import soy.frank.flutterby.actors.PhysicalEntity
import soy.frank.flutterby.actors.Vector2D
import soy.frank.flutterby.input.ButterflyControls
import spock.lang.Specification
import spock.lang.Unroll

class GameLogicTest extends Specification {

    public static final float velocity = 0.005f
    public static final initialX = 0.0f
    public static final initialY = 0.0f
    def
    static initialScene = ImmutableScene.builder().butterfly(PhysicalEntity.createButterflyAt(initialX, initialY)).build() as ImmutableScene

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

    @Unroll
    def """A laser at (#x, #y) with an initial velocity of #initialVelocity and an acceleration of #acceleration will
                find itself at (#x, #newY) have its resulting velocity be #newVelocity"""() {
        given:
        def laserWithAcceleration = ImmutablePhysicalEntity.copyOf(PhysicalEntity.createLaserAt(x, y))
                .withAcceleration(acceleration as float)
                .withVelocity(initialVelocity as float)

        def scene = initialScene.withLasers([laserWithAcceleration])

        when:
        def resultingScene = GameLogic.applyLogic(scene, Stub(ButterflyControls))

        then:
        resultingScene.lasers() == [ImmutablePhysicalEntity.copyOf(laserWithAcceleration)
                                            .withVelocity(newVelocity as float)
                                            .withPosition(Vector2D.of(x, newY as float))
        ]

        where:
        x    | y    | initialVelocity | acceleration       | newY                                           | newVelocity
        0f   | 0f   | Laser.VELOCITY  | Laser.ACCELERATION | (Laser.VELOCITY + Laser.ACCELERATION) as float | (Laser.VELOCITY + Laser.ACCELERATION) as float
        0f   | 0f   | 0.3f            | 0.1f               | 0.4f                                           | 0.4f
        0.1f | 1.2f | 0.3f            | 0.0f               | 1.5f                                           | 0.3f
    }

    ButterflyControls fire() {
        Stub(ButterflyControls) { fire() >> true }
    }
}
