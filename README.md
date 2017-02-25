# Gey Ya' Barrels Here [![](http://cf.way2muchnoise.eu/261557.svg)](https://minecraft.curseforge.com/projects/get-ya-barrels-here) [![](http://cf.way2muchnoise.eu/versions/261557.svg)](https://minecraft.curseforge.com/projects/get-ya-barrels-here)
An addon for [CraftTweaker](https://minecraft.curseforge.com/projects/crafttweaker) which provides scripts with a way to interface with the LootTable system in Minecraft. 

## Source Code
The latest source code can be found here on [GitHub](https://github.com/Epoxide-Software/GYBH). If you are using Git, you can use the following command to clone the project: `git clone git:github.com/Epoxide-Software/GYBH.git`

## Building from Source
This project can be built using the Gradle Wrapper included in the project. When the 'gradlew build' command is executed, a compiled JAR will be created in `~/build/libs`. Sources and Javadocs will also be generated in the same directory. Alternatively the latest builds of Dark-Utilities along with Sources and Javadocs can be found [here](http://maven.epoxide.org/org/epoxide/gybh/GYBH/).

## Contributing
This project is open to contributions. If you would like to report an issue, create a pull request, offer advice or provide translations for other languages, they would be more than welcome.

## Dependency Management
If you are using [Gradle](https://gradle.org) to manage your dependencies, add the following into your `build.gradle` file. Make sure to update the version from time to time.
```
repositories {

    maven { url 'http://maven.epoxide.org' }
}

dependencies {

    compile "net.darkhax.lttweaker:LootTableTweaker:1.10.2-0.0.0.9"
}
```

## Credits
* [Lclc98](https://github.com/lclc98) - Lead developer of the mod.
* [Darkhax](https://github.com/darkhax) - Bug fixes and stuff. 
