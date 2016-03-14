package soy.frank.flutterby

import soy.frank.flutterby.actors.*
import soy.frank.flutterby.input.ButterflyControls
import spock.lang.Specification
import spock.lang.Unroll

import static soy.frank.flutterby.PhysicalEntitySpec.aLaser
import static soy.frank.flutterby.SceneSpec.aScene

class GameLogicTest extends Specification {
    public static final float VELOCITY = 0.005f
    public static final initialX = 0.0f
    public static final initialY = 0.0f

    @Unroll
    def "A butterfly moving #direction is displaced by #displacement on the #axis axis"() {
        expect:
        GameLogic.applyLogic(aScene {
            butterfly {
                x 0.0f
                y 0.0f
            }
        }, move(Direction)).butterfly().position()."$Axis"() == Displacement

        where:
        Direction | Displacement                    | Axis
        "up"      | (initialY + VELOCITY).toFloat() | "y"
        "right"   | (initialX + VELOCITY).toFloat() | "x"
        "down"    | (initialY - VELOCITY).toFloat() | "y"
        "left"    | (initialX - VELOCITY).toFloat() | "x"
    }

    private ButterflyControls move(String direction) {
        def upperCase = direction.capitalize()
        Stub(ButterflyControls) { "move$upperCase"() >> true }
    }

    @Unroll
    def """A laser at (#X, #Y) with an initial velocity of #InitialVelocity and an acceleration of #Acceleration will
                find itself at (#X, #NewY) have its resulting velocity be #NewVelocity"""() {
        given:
        def laserWithAcceleration = aLaser {
            x X
            y Y
            velocity InitialVelocity
            acceleration Acceleration
        }

        def scene = aScene {
            butterfly {
                x 0.0f
                y 0.0f
            }
            lasers laserWithAcceleration
        }

        when:
        def resultingScene = GameLogic.applyLogic(scene, Stub(ButterflyControls))

        then:
        resultingScene.lasers() == [
                aLaser {
                    velocity NewVelocity
                    x X
                    y NewY
                    acceleration Acceleration
                }
        ]

        where:
        X    | Y    | InitialVelocity | Acceleration | NewY                                           | NewVelocity
        0f   | 0f   | Laser.VELOCITY  | 0.00025f     | (Laser.VELOCITY + Laser.ACCELERATION) as float | (Laser.VELOCITY + Laser.ACCELERATION) as float
        0f   | 0f   | 0.3f            | 0.1f         | 0.4f                                           | 0.4f
        0.1f | 1.2f | 0.3f            | 0.0f         | 1.5f                                           | 0.3f
    }

    @Unroll
    def "A butterfly should spawn lasers -> #Lasers when the fire command is issued and the cooldown is #Cooldown and the resulting cooldown should be #ResultingCooldown"() {
        given: "A butterfly with a cooldown of 0"
        def scene = aScene {
            butterfly {
                x 0.0f
                y 0.0f
            }
            cooldown Cooldown
        }

        when:
        def resultingScene = GameLogic.applyLogic(scene, Stub(ButterflyControls) { fire() >> true })

        then:
        resultingScene == aScene {
            butterfly {
                x 0f
                y 0f
            }
            lasers Lasers
            cooldown ResultingCooldown
        }

        where:
        Lasers                                                                            | Cooldown | ResultingCooldown
        [aLaser { x Butterfly.WIDTH / 2 - Laser.WIDTH / 2 as float; y Butterfly.HEIGHT }] | 0        | 8
        []                                                                                | 8        | 7
        [aLaser { x Butterfly.WIDTH / 2 - Laser.WIDTH / 2 as float; y Butterfly.HEIGHT }] | -3       | 8
        []                                                                                | 1        | 0
    }

    @Unroll
    def "A butterfly with a cooldown of #Cooldown should have a cooldown of #ResultingCooldown when the game logic is applied"() {
        given:
        def butterflyWithCooldown = aScene {
            butterfly {
                x 0f
                y 0f
            }
            cooldown Cooldown
        }

        expect:
        GameLogic.applyLogic(butterflyWithCooldown, Stub(ButterflyControls)) == aScene {
            butterfly {
                x 0f
                y 0f
            }
            cooldown ResultingCooldown
        }

        where:
        Cooldown | ResultingCooldown
        8        | 7
        -3       | 0
        1        | 0
        0        | 0
    }

    ButterflyControls fire() {
        Stub(ButterflyControls) { fire() >> true }
    }
}
