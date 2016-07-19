# Expressive tests with Spock and Groovy Builders

## IDE Integration

### IntelliJ
In IntelliJ, do at least one Maven Compile from the IDE to get the generated code to be
marked as generated source root.

### Eclipse
You will need to do the following one-time setup (preferably before importing the project):

1. Install the latest snapshot of the groovy support from this
[update site](http://dist.springsource.org/snapshot/GRECLIPSE/e4.5/) for Mars or the 
[update site](http://dist.springsource.org/snapshot/GRECLIPSE/e4.6/) for Neon. Select everything.
2. Install `m2e-apt` from the marketplace and configure it (once and for all) with the instructions
[here](https://immutables.github.io/apt.html#eclipse)
3. Ignore the problem with GMavenPlus, there is no eclipse connector for it.
4. Right click on your project and mark it as a Groovy project `Configure -> Convert to Groovy Project`.
5. If you get problems with mismatched groovy versions like 
`groovy-all is loaded in version 2.4.3 and you are trying to load version 2.4.7`, remove the groovy libraries from the
build path (right click on groovy libraries -> build path -> remove from build path).

## References

### Images

Images are from [pixabay](https://pixabay.com) and are licensed under CC0 Public Domain.
Explosions are from [kenney.nl](http://kenney.nl/) and are licensed under CC0 1.0 Universal

Svg images have undergone some editing, the edited versions have been included.

* [Butterfly](https://pixabay.com/en/butterfly-animal-insect-bug-nature-152363/)
* [Dragonfly](https://pixabay.com/en/animal-dragonfly-insect-butterfly-161030/)
* [Laser](https://pixabay.com/en/eye-laser-human-body-pupil-39997/)
* [Clouds](https://pixabay.com/en/clouds-blue-white-sky-nature-33139/)
* [Explosions](http://kenney.nl/assets/smoke-particles)
