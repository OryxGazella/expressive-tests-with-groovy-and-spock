package soy.frank.flutterby

import soy.frank.flutterby.actors.Butterfly
import soy.frank.flutterby.actors.DragonFly
import soy.frank.flutterby.actors.Laser
import soy.frank.flutterby.input.ButterflyControls
import spock.lang.Specification
import spock.lang.Unroll

import static soy.frank.flutterby.DSL.*

class GameLogicTest extends Specification {
    public static final float VELOCITY = 0.005f
    public static final initialX = 0.0f
    public static final initialY = 0.0f

    GameLogic gameLogic

    def setup() {
        gameLogic = new GameLogic({ 0 })
    }

    @Unroll
    def "A butterfly moving #Direction is displaced by #Displacement on the #axis axis"() {
        expect:
        gameLogic.applyLogic(aScene {
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
        def resultingScene = gameLogic.applyLogic(scene, Stub(ButterflyControls))

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
        def resultingScene = gameLogic.applyLogic(scene, Stub(ButterflyControls) { fire() >> true })

        then:
        with(resultingScene) {
            lasers() ==  Lasers
            cooldown() ==  ResultingCooldown
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
        with(gameLogic.applyLogic(butterflyWithCooldown, Stub(ButterflyControls))) {
            cooldown() == ResultingCooldown
        }

        where:
        Cooldown | ResultingCooldown
        8        | 7
        -3       | 0
        1        | 0
        0        | 0
    }

    def "Removes dragonflies and lasers that collide"() {
        given:
        def scene = aScene {
            butterfly {
                x 0f
                y 0f
            }
            lasers aLaser {
                x 0f
                y 0f
            }
            dragonflies aDragonfly {
                x 0f
                y 0f
            }
        }

        when:
        def resultingScene = gameLogic.applyLogic(scene, Stub(ButterflyControls))

        then:
        with(resultingScene) {
            dragonflies() == []
            lasers() == []
        }
    }

    def "Keeps lasers and dragonflies that don't collide"() {
        given:
        def scene = aScene {
            butterfly {
                x 0f
                y 0f
            }
            dragonflies aDragonfly {
                x 0f
                y 0f
            }
            lasers aLaser {
                x 3 * DragonFly.WIDTH as float
                y 0f
                acceleration 0f
                velocity 0f
            }
        }

        when:
        def resultingScene = gameLogic.applyLogic(scene, Stub(ButterflyControls))

        then:
        with(resultingScene) {
            lasers() == [aLaser {
                x 3 * DragonFly.WIDTH as float
                y 0f
                acceleration 0f
                velocity 0f
            }]
            dragonflies() == [aDragonfly {
                x 0f
                y 0f
            }]
        }
    }

    def "A dragonfly spawns when the dragonfly cooldown reaches zero and a new random dragonfly cooldown is set"() {
        given:
        def gameLogic = new GameLogic({ 184 })
        def scene = aScene {
            butterfly {
                x 0f
                y 0f
            }
            dragonflyCooldown 0
        }

        when:
        def resultingScene = gameLogic.applyLogic(scene, Stub(ButterflyControls))

        then:
        with(resultingScene) {
            dragonflyCooldown() == 4
            dragonflies() == [aDragonfly {
                x 0f
                y 0f
            }]
        }
    }

    ButterflyControls fire() {
        Stub(ButterflyControls) { fire() >> true }
    }
}
