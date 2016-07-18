package soy.frank.flutterby

import soy.frank.flutterby.actors.Butterfly
import soy.frank.flutterby.actors.DragonFly
import soy.frank.flutterby.actors.Laser
import soy.frank.flutterby.actors.Vector2D
import soy.frank.flutterby.input.ButterflyControls
import soy.frank.flutterby.input.ImmutableButterflyControls
import spock.lang.Specification
import spock.lang.Unroll

import static soy.frank.flutterby.DSL.*
import static spock.util.matcher.HamcrestMatchers.closeTo
import static spock.util.matcher.HamcrestSupport.expect

class GameLogicTest extends Specification {
    public static final float VELOCITY = Butterfly.VELOCITY
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
        }, move(Direction)).butterfly.position."$Axis" == Displacement

        where:
        Direction | Displacement | Axis
        "up"      | VELOCITY     | "y"
        "right"   | VELOCITY     | "x"
        "down"    | -VELOCITY    | "y"
        "left"    | -VELOCITY    | "x"
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
        resultingScene.lasers == [
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
        def butterflyFiring = Stub(ButterflyControls) { fire() >> true }

        expect:
        with(gameLogic.applyLogic(scene, butterflyFiring)) {
            lasers ==  Lasers
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

    def "Removes dragonflies and lasers that collide, and increments the score by 50 points"() {
        given:
        def scene = aScene {
            butterfly {
                x 3 * DragonFly.WIDTH + 0.3f as float
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
            score 50
        }

        expect:
        with(gameLogic.applyLogic(scene, Stub(ButterflyControls))) {
            dragonflies.empty
            lasers.empty
            explosions == [anExplosion {
                position Vector2D.of(0f, 0f)
            }]
            score == 100
        }
    }

    def "The game stops updating when the butterfly is on less than 0 lives"() {
        given:
        def scene = aScene {
            butterfly {
                x 0f
                y 0f
            }
            lives(-1)
        }
        def firingControl = Stub(ButterflyControls) { moveRight() >> true; fire() >> true }

        when:
        def resultingScene = gameLogic.applyLogic(scene, firingControl)

        then:
        resultingScene == scene
    }

    def "The game restarts when the butterfly is on less than 0 lives and the restart control is sent"() {
        given:
        def scene = aScene {
            butterfly {
                x 0f
                y 0f
            }
            lives(-1)
        }

        when:
        def resultingScene = gameLogic.applyLogic(scene, Stub(ButterflyControls) { restart() >> true })

        then:
        resultingScene == GameLogic.INITIAL_SCENE
    }

    def "The game is no longer running when the butterfly is on less than 0 lives and the quit control is sent"() {
        given:
        def scene = aScene {
            butterfly {
                x 0f
                y 0f
            }
            lives(-1)
        }

        when:
        def resultingScene = gameLogic.applyLogic(scene, Stub(ButterflyControls) { quit() >> true })

        then:
        !resultingScene.running
    }

    def "The game is no longer running when the game is paused and the fire control is sent"() {
        given:
        def scene = aScene {
            butterfly {
                x 0f
                y 0f
            }
            lives 10
            paused true
        }

        def pauseControl = Stub(ButterflyControls) {fire() >> true}

        expect:
        with(gameLogic.applyLogic(scene, pauseControl)) {
            !running
        }
    }

    def "The game becomes paused when the quit command is sent on a running game"() {
        given:
        def scene = aScene {
            butterfly {
                x 0f
                y 0f
            }
            lives 10
        }

        def pauseControl = Stub(ButterflyControls) {quit() >> true}

        expect:
        with(gameLogic.applyLogic(scene, pauseControl)) {
            running
            paused
        }
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    def "A paused game un-pauses with any directional movement"() {
        given:
        def scene = aScene {
            butterfly {
                x 0f
                y 0f
            }
            lives 3
            paused true
        }
        when:
        def resultingScene = gameLogic.applyLogic(scene, controls(Directions))

        then:
        resultingScene.paused == Paused

        where:
        Paused  | Directions
        true    | []
        false   | ["up"]
        false   | ["up", "right"]
        false   | ["right"]
        false   | ["right", "down"]
        false   | ["down"]
        false   | ["left", "down"]
        false   | ["left"]
        false   | ["left", "up"]
    }

    def controls(List<String> directions) {
        def controlsBuilder = ImmutableButterflyControls.builder()
        controlsBuilder.fire(false)
        controlsBuilder.moveRight(false)
        controlsBuilder.moveLeft(false)
        controlsBuilder.quit(false)
        controlsBuilder.moveUp(false)
        controlsBuilder.moveDown(false)
        controlsBuilder.restart(false)
        directions.each {
            controlsBuilder."move${it.capitalize()}"(true)
        }
        controlsBuilder.build()
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
            dragonflies.empty
            explosions == [anExplosion {
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
        with(gameLogic.applyLogic(scene, Stub(ButterflyControls)).dragonflies[0]) {
            expect position.x, closeTo(Displacement as float, 0.00001)
            phase == ResultingPhase
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

        expect:
        with(gameLogic.applyLogic(scene, Stub(ButterflyControls))) {
            lasers == [aLaser {
                x 3 * DragonFly.WIDTH as float
                y 0f
                acceleration 0f
                velocity 0f
            }]
            dragonflies.size() == 1
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

        expect:
        with(gameLogic.applyLogic(scene, Stub(ButterflyControls))) {
            dragonflyCooldown() == Cooldown
            dragonflies.size() == 1
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
        with(resultingScene.dragonflies[0]) {
            position.y == YPosition
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
