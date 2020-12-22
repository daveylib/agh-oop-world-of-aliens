# The Amazing World of Aliens

### Necessary Maven libraries

- **Jackson-Databind:2.7.3** from com.fasterxml.jackson.core
- **JUnit:4.13.1** from junit
- **JUnit5.4**

### Purpose of the project

The Amazing World of Aliens is an assignment for object-oriented programming course at University Of Science and Technology in Kraków and also my own interpretation of evolution simulator. The game represents an environment of some distant planet in the universe with living aliens and growing mushrooms. There is also some kind of a jungle in the center of a map where mushrooms grow faster.

![](https://raw.githubusercontent.com/daveylib/agh-oop-world-of-aliens/master/screenshots/simulation.png)

### Technical details of the project

The application was written in Java with use of JavaFX for graphical user interface and advanced collections such as ObservableList and ObservableSet. Futhermore, most of changing properties of aliens are defined as observable. It allowed me to add necessary listeners to track changes when they occur.

Of course to maintain security and adequate encapsulation all properties (including lists and sets) are created as read-only properties. To be precise, these wrappers creates two properties that are synchronized. One property is read-only and is public. The other property is readable and writable - it is used only internally.

### Functioning of the simulation

The simulation goes on forever (unless you stop it) and each day there are several unchanging events happening:
- dead aliens are removed from the map, but alive ones rotate and move forward
- aliens eat mushrooms and propagate if they meet
- new mushrooms show up on the map

Default duration of a day in the simulation is set to 1 second, but you can modify it at any moment with a slider. You can also stop and resume the simulation by clicking the button next to the slider.

### Anatomy of the alien

Besides the obvious things such as energy and age, each alien has own genotype which has information about the preferred rotation. For example, alien with genotype **[0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 4, 4, 4, 4, 4, 4, 5, 5, 6, 6, 7, 7, 7, 7]** prefers no rotation, because gene number 0 appears most often. Next is a gene number 2 which means right rotation and reverse rotation with gene number 4. More specifically, rotation is calculated as gene times 45°.

Alien is showed on the map with proper icon. It is chosen on the basis of alien's energy and the fraction of initial energy. Color of the icon starts from green and fades to red.

### Propagation of aliens

If two aliens meet on the same field on a map and both have more than 50% of initial energy, they propagate and new child is born near them. In this process quarter of each parent is lost, but the child's initial energy is just sum of these values.

The child receives a crossed genotype from its parents. Genotype of the first parent is divided into three, not necessarily of the same length, parts. In the same places genotype of the second parent also is divided. Crossed genotype is composed of left and right part from the first parent and middle part of the second parent. We can assume that the first parent is the strongest one or just a random one in case of the same energy.

If there are more than 3 aliens on the same field only two strongest propagate, of course in condition that they have more than 50% of initial energy.

### Consuming mushrooms

If alien stands on a field with mushroom it consumes the plant and gains some points of the energy - the value is specified in settings. In case of more than one alien standing on a field with mushroom the energy boost is divided equally between these aliens.

### Settings

![](https://raw.githubusercontent.com/daveylib/agh-oop-world-of-aliens/master/screenshots/settings.png)

### Stats of the simulation

Next to the visualization of the map you can find stats with following information:
- day of the simulation
- quantity of alive aliens on the map
- quantity of mushrooms on the map
- dominant genotype of all living aliens
- average energy of alive aliens
- average lifetime of dead aliens
- average children quantity of alive aliens

The information changes every day and presents the current situation.

### Generating stats of the simulation

![](https://raw.githubusercontent.com/daveylib/agh-oop-world-of-aliens/master/screenshots/stats.png)

Below the map you can find a button titled **Generate stats from the simulation**. When you click it a new window is displayed. There you can specify a number of days since the beginning of the simulation and path with a name of the file to which stats will be saved.

The file is saved as JSON and contains average values from each day of the simulation.

### Following aliens

If you click twice on a field on the map you will see a list of all aliens that are standing there and automatically you start following them. For every alien you see its current age, energy, position, genotype, quantity of children and quantity of all descendants. In addition you see also a visualization with proper icon. These values change each day of the simulation.

In every moment you can stop following the alien by clicking **X** next to it.

### Showing aliens with dominant genotype

![](https://raw.githubusercontent.com/daveylib/agh-oop-world-of-aliens/master/screenshots/dominant_genotype.png)

Below the map you can see also a button titled **Show aliens with dominant genotype**. By clicking it you display a new window with a list of all aliens that have dominant genotype.

### Simulation with two maps

![](https://raw.githubusercontent.com/daveylib/agh-oop-world-of-aliens/master/screenshots/simulation_two_maps.png)

In the main scene of the game you can turn on option of showing two maps simultaneously. In fact they are two separate and different simulations.
