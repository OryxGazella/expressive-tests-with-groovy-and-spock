package soy.frank.flutterby

import spock.lang.Specification

import static soy.frank.flutterby.DSL.aPhysicalEntity

class CollisionDetectorTest extends Specification {

    def "Determines whether two entities collide"() {
        given:
        def entityOne = aPhysicalEntity {
            x 0f
            y 0f
            width 1f
            height 1f
        }

        def entityTwo = aPhysicalEntity {
            x 0f
            y 0f
            width 1f
            height 1f
        }

        expect:
        CollisionDetector.collides(entityOne, entityTwo)
    }

    def "States when two entities don't collide"() {
        given:
        def entityOne = aPhysicalEntity {
            x 0f
            y 0f
            width 0.5f
            height 0.5f
        }

        def entityTwo = aPhysicalEntity {
            x 1f
            y 1f
            width 1f
            height 1f
        }

        expect:
        !CollisionDetector.collides(entityOne, entityTwo)
    }
}
