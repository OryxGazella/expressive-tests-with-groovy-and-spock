package soy.frank.flutterby

import soy.frank.flutterby.actors.Butterfly
import soy.frank.flutterby.actors.ImmutableScene
import soy.frank.flutterby.actors.Laser
import soy.frank.flutterby.actors.PhysicalEntity
import soy.frank.flutterby.input.ButterflyControls
import spock.lang.Specification

class GameLogicTest extends Specification {

    public static final float velocity = 0.005f
    public static final initialX = 0.0f
    public static final initialY = 0.0f
    public static
    final initialScene = ImmutableScene.builder().butterfly(PhysicalEntity.createButterflyAt(initialX, initialY)).build() as ImmutableScene

    def "Butterfly moves to the right"() {
        given:
        def movingRight = Stub(ButterflyControls) {
            moveRight() >> true
        }

        when:
        def result = GameLogic.applyLogic(initialScene, movingRight)

        then:
        result.butterfly().position().x() == (initialX + velocity).toFloat()
    }

    def "Butterfly moves to the left"() {
        given:
        def movingLeft = Stub(ButterflyControls) {
            moveLeft() >> true
        }

        when:
        def result = GameLogic.applyLogic(initialScene, movingLeft)

        then:
        result.butterfly().position().x() == (initialX - velocity).toFloat()
    }

    def "Butterfly moves up"() {
        given:
        def movingUp = Stub(ButterflyControls) {
            moveUp() >> true
        }

        when:
        def xy = GameLogic.applyLogic(initialScene, movingUp)

        then:
        xy.butterfly().position().y() == (initialY + velocity).toFloat()
    }

    def "Butterfly moves down"() {
        given:
        def movingDown = Stub(ButterflyControls) {
            moveDown() >> true
        }

        when:
        def xy = GameLogic.applyLogic(initialScene, movingDown)

        then:
        xy.butterfly().position().y() == (initialY - velocity).toFloat()
    }

    def "Butterfly spawns a laser in the center of its head when the fire control is sent"() {
        given:
        def firing = Stub(ButterflyControls) {
            fire() >> true
        }

        when:
        def resultingScene = GameLogic.applyLogic(initialScene, firing)

        then:
        resultingScene ==
                initialScene
                        .withLasers(PhysicalEntity.createLaserAt(Butterfly.WIDTH / 2 - Laser.WIDTH / 2 as float, Butterfly.HEIGHT))
    }
}
