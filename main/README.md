# WorldSim V1.0.0 Manual

## Controls
The following are the list of controls for the simulation

|  Key/Input  |      Action      |
|:-----------:|:----------------:|
|      W      |     Move Up      |
|      A      |    Move Left     |
|      S      |    Move Down     |
|      D      |    Move Right    |
|    SPACE    | Pause Simulation |
|     ESC     |   Toggle menu    |
|  UP ARROW   |  Speed up time   |
| DOWN ARROW  |  Slow down time  |
|  SCROLL UP  |     Zoom in      |
| SCROLL DOWN |     Zoom out     |

### Targeting bodies

Bodies can be targeted by left-clicking near them, while a body is targeted,
the body will be centered and the movement keys will be locked.
The simulation will also show stats on the current body, 
such as temperature and mass.

To deselect a target, simply click away from the center of the screen.

### Loading and reloading new files

To load a new system, press esc to bring up the menu, by clicking on "Load file", 
a menu will pop up to select a .json file

To reload a system, press esc to bring up the menu and click on "Reload file",
the simulation will restart itself.

## How to create your own system

### Outer .json object

Inside the .json file should be an object with 2 fields, "settings" and "system".
- "settings" contains information regarding the simulation itself such as the speed
of the simulation and the camera zoom level. 
- "system" is an array of all the celestial objects in the simulation.

### Inside settings

Inside the "settings" object should be 2 fields, "time scale" and "zoom".

- "time scale" is how fast the simulation will run at the start, with 1 meaning
real time the larger the number the faster the simulation and vise versa.
- "zoom" is the camera zoom level at the start of the simulation. A value of 1 is
about 1 pixel per meter. The larger the number, the more zoomed in the camera will be

### Inside system

Inside the system array will contain all the celestial bodies you want in the simulation.
There is 3 different types to add in the array

- Star
- Terrestrial
- Gas Giant

For all celestial bodies, they will contain the following fields:

- type

  - One of the 3 types stated above. 
- name

  - A string for is the name of the body
- mass

  - The mass can be any non-negative number (42, 2.5234, 3.4e23) which are in kg,
  or it can be a string number with the units stated.
- radius

  - The radius can be any positive number (42, 2.5234, 3.4e23) which are in meters,
  or it can be a string number with the units stated
- temperature

  - The temperature can be any non-negative number and the units are in kelvin.
- color

    - The color can be a hex string with a '#' in front ("#ffffff", "#0f24b2")
- position

  - A 2d array as [x, y], they can be numbers in units of meters or strings with distance units 
  ([0, 0], [351.11, 524.43], [0.0, "39.4214 Au"]) 
- velocity

    - A 2d array as [dx, dy], only numbers in units of meters per second
- parent

  - The parent is the name of the celestial body that the current body is relative to
  if there is no parent object, the value can be null.
  Note that the parent must be defined beforehand.

#### Terrestrial Specific Fields

- albedo

  - A value from 0 to 1 of how reflective the body is, used for temperature calculations.
  Note that if there is no star in the system, the temperatures will all be 0 kelvin
- atmosphere present?

  - A boolean of whether the body has an atmosphere present or not
- greenhouse effect

  - How much heat the atmosphere will absorb, if there is no atmosphere, this value is ignored.

#### Gas Giant Specific Fields

- albedo

    - A value from 0 to 1 of how reflective the body is, used for temperature calculations.
      Note that if there is no star in the system, the temperatures will all be 0 kelvin
- ring system

  - A json object with the following fields.
  
    - inner radius, in multiples of the planet radius
    - outer radius, in multiples of the planet radius
    - color, a hex string similar to the ones stated above
    - opacity, how visible are the rings
  - if there isn't a ring system, the value can be null
  


### Units

The following are the allowed units in the .json file

- Mass units

  - "Ms" - Solar Masses (1.988435e30 kg)
  - "Mj" - Jupiter Masses (1.898e27 kg)
  - "Me" - Earth Masses (5.97e24 kg)
  - "Mm" - Moon Masses (7.3459e22 kg)
- Radius units

  - "Rs" - Solar Radii (6.957e8 meters)
  - "Rj" - Jupiter Radii (6.995e7 meters)
  - "Re" - Earth Radii (6.371e6 meters)
  - "Rm" - Moon Radii (1.7374e6 meters)
- Distance units

    - "Km" - Kilometers (1000 meters)
    - "Au" - Astronomical Units (1.496e11 meters)

## Libraries used

- [JavaFX](https://openjfx.io)
- [SLF4J](https://www.slf4j.org/index.html)
- [json-simple](https://code.google.com/archive/p/json-simple/)