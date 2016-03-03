package soy.frank.flutterby

import soy.frank.flutterby.actors.ImmutableScene
import soy.frank.flutterby.actors.PhysicalEntity
import soy.frank.flutterby.input.ImmutableButterflyControls
import spock.lang.Specification

class GameLogicTest extends Specification {

    public static final float velocity = 0.005f
    public static final initialX = 0.0f
    public static final initialY = 0.0f
    public static final initialScene = ImmutableScene.builder().butterfly(PhysicalEntity.createButterfly(initialX, initialY)).build()

    def "Butterfly moves to the right"() {
        given:
        def selectedMovement = ImmutableButterflyControls.builder()
                .moveUp(false)
                .moveRight(true)
                .moveLeft(false)
                .moveDown(false)
                .fire(false).build()

        when:
        def result = GameLogic.applyLogic(initialScene, selectedMovement)

        then:
        result.butterfly().position().x() == (initialX + velocity).toFloat()
    }

    def "Butterfly moves to the left"() {
        given:
        def selectedMovement = ImmutableButterflyControls.builder()
                .moveUp(false)
                .moveRight(false)
                .moveLeft(true)
                .moveDown(false)
                .fire(false).build()
        when:
        def result = GameLogic.applyLogic(initialScene, selectedMovement)

        then:
        result.butterfly().position().x() == (initialX - velocity).toFloat()
    }

    def "Butterfly moves up"() {
        given:
        def selectedMovement = ImmutableButterflyControls.builder()
                .moveUp(true)
                .moveRight(false)
                .moveLeft(false)
                .moveDown(false)
                .fire(false).build()
        when:
        def xy = GameLogic.applyLogic(initialScene, selectedMovement)

        then:
        xy.butterfly().position().y() == (initialY + velocity).toFloat()
    }

    def "Butterfly moves down"() {
        given:
        def selectedMovement = ImmutableButterflyControls.builder()
                .moveUp(false)
                .moveRight(false)
                .moveLeft(false)
                .moveDown(true)
                .fire(false).build()
        when:
        def xy = GameLogic.applyLogic(initialScene, selectedMovement)

        then:
        xy.butterfly().position().y() == (initialY - velocity).toFloat()
    }

}