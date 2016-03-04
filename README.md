# IDE Integration #

## IntelliJ ##
In IntelliJ, do at least one Maven Compile from the IDE to get the generated code to be
marked as generated source root.

## Eclipse/STS ###
You will need to do the following one-time setup (preferably before importing the project):

1. Install the latest snapshot of the groovy support from this [update site](http://dist.springsource.org/snapshot/GRECLIPSE/e4.5/) select everything.
2. Install `m2e-apt` from the marketplace and configure it (once and for all) with the instructions [here](https://immutables.github.io/apt.html#eclipse)
3. Ignore the problem with GMavenPlus, there is no eclipse connector for it.
4. Right click on your project and mark it as a Groovy project `Configure -> Convert to Groovy Project`.