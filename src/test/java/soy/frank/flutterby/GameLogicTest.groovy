package soy.frank.flutterby

import soy.frank.flutterby.actors.*
import soy.frank.flutterby.input.ButterflyControls
import spock.lang.Specification
import spock.lang.Unroll

import static soy.frank.flutterby.DSL.*
import static spock.util.matcher.HamcrestMatchers.closeTo
import static spock.util.matcher.HamcrestSupport.expect

class GameLogicTest extends Specification {
    public static final float VELOCITY = 0.005f
    public static final initialX = 0.0f
    public static final initialY = 0.0f

    GameLogic gameLogic

    def setup() {
        gameLogic = new GameLogic({ 0 })
    }

    @Unroll
    def "A butterfly moving #Direction is displaced by #Displacement on the #Axis axis"() {
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
                acceleration 0f
                velocity 0f
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
            explosions() == [anExplosion {
                position Vector2D.of(0f, 0f)
            }]
        }
    }

    def "A butterfly that collides with a dragonfly causes the dragonfly to explode and the butterfly loses a life"() {
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
            lives 5
        }

        expect:
        with(gameLogic.applyLogic(scene, Stub(ButterflyControls))) {
            dragonflies() == []
            explosions() == [anExplosion {
                position Vector2D.of(0f, 0f)
            }]
            lives() == 4
        }
    }

    @Unroll
    def "Dragonflies move on the X axis by the sin(τ/120 * #Phase) * 0.1 = #Displacement"() {
        given:
        def scene = aScene {
            butterfly {
                x 3 * DragonFly.WIDTH + 0.3f as float
                y 0f
            }
            dragonflies aDragonfly {
                x 0f
                y 0f
                phase Phase
            }
        }

        expect:
        with(gameLogic.applyLogic(scene, Stub(ButterflyControls)).dragonflies()[0]) {
            expect position().x(), closeTo(Displacement as float, 0.00001)
            phase() == ResultingPhase
        }

        where:
        Phase << [0, 1, 2, 3, 60, 61, 62, 63, 120]
        ResultingPhase << [1, 2, 3, 4, 61, 62, 63, 64, 0]
        Displacement << [0, 0.00523359562, 0.01045284632, 0.0156434465, 0, -0.00523359562, -0.01045284632, -0.0156434465, 0]
    }

    def "Keeps lasers and dragonflies that don't collide"() {
        given:
        def scene = aScene {
            butterfly {
                x 3 * DragonFly.WIDTH as float
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
            dragonflies().size() == 1
        }
    }

    @Unroll
    def """A dragonfly spawns when the dragonfly cooldown reaches 0 and a new random dragonfly cooldown
            of #Cooldown (#Seconds seconds) is set when the random number generator returns #RandomNumber"""() {
        given:
        def gameLogic = new GameLogic({ RandomNumber })
        def scene = aScene {
            butterfly {
                x 3 * DragonFly.WIDTH as float
                y 0f
            }
            dragonflyCooldown 0
        }

        when:
        def resultingScene = gameLogic.applyLogic(scene, Stub(ButterflyControls))

        then:
        with(resultingScene) {
            dragonflyCooldown() == Cooldown
            dragonflies().size() == 1
        }

        where:
        RandomNumber | Cooldown  | Seconds
        0            | 120       | 2.0
        121          | 241       | (241 / 60).toFloat().round(2)
        179          | 120 + 179 | (299 / 60).toFloat().round(2)
        180          | 120       | (120 / 60).toFloat().round(2)
        184          | 124       | (124 / 60).toFloat().round(2)
    }

    @Unroll
    def "A dragonfly spawns at #YPosition on the y axis when the random number generator returns #RandomNumber"() {
        given:
        def gameLogic = new GameLogic({ RandomNumber })
        def scene = aScene {
            butterfly {
                x 3 * DragonFly.WIDTH as float
                y 0f
            }
            dragonflyCooldown 0
        }

        when:
        def resultingScene = gameLogic.applyLogic(scene, Stub(ButterflyControls))

        then:
        with(resultingScene.dragonflies()[0]) {
            position().y() == YPosition
        }

        where:
        RandomNumber | YPosition
        0            | -2 * DragonFly.HEIGHT as float
        1            | -1 * DragonFly.HEIGHT as float
        2            | 0f
        3            | 1 * DragonFly.HEIGHT as float
        4            | 2 * DragonFly.HEIGHT as float
        5            | 3 * DragonFly.HEIGHT as float
        6            | 4 * DragonFly.HEIGHT as float
        7            | 5 * DragonFly.HEIGHT as float
        8            | 6 * DragonFly.HEIGHT as float
        9            | 7 * DragonFly.HEIGHT as float
        10           | 8 * DragonFly.HEIGHT as float
        11           | 9 * DragonFly.HEIGHT as float
        12           | 10 * DragonFly.HEIGHT as float
        13           | 11 * DragonFly.HEIGHT as float
    }

    ButterflyControls fire() {
        Stub(ButterflyControls) { fire() >> true }
    }
}
