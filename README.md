# What is dante?

**dante** is Distributed Testing Environment for Artificial Intelligence algorithms.

Its main goals is to provide server/client architecture that is capable of testing and evaluating different artificial 
intelligence algorithms dealing with some defined problem in the specified environment.

The problem that **dante** is testing and evaluating is an efficient control a group of agents (visualised by default 
as robots) that explore a specified simple 2D environment and try to eliminate all other groups of agents. 

**dante** also provides separate modules with well-defined roles that may be easily reused in other projects 
(e.g. network layer, simulation layer).
 

# Dear Reader, please read below and be kind  

Most of this code is 10+ years old. This was my first large project that I've built as part of my dissertation back in late 2006.
I've worked on bits of it later on (especially modularity) but at some point I've abandoned it due to other commitments.

A lot has changed in 10 years so you won't find good examples of automated tests, 
proper approaches to modularization or any modern tools and techniques that you'd like to learn or follow. 

However *this code still works* - and only depends on JDK 1.5 and Ant to be built. 
I've decided to put it on GitHub after its previous home - java.net - started closing down its projects.

Finally, despite its age I hope you might still learn a thing or two from it. 
Use it and abuse it - it's here to serve you. Enjoy! 

# Features

* Extensible testing environment
* Client/server architecture
* Simple 2D graphics engine implementation with collision detection and animations
* Module separation

# Requirements

* Ant (to build)
* Java Runtime Environment 5.0 or higher (to run)

# How to build dante?

**dante** provides Ant build script capable of building executable JAR containing dante battle client/server, 
JARs containing dante modules or dante release packages.
Before you can build any of the Ant targets listed below you have to have [Apache Ant](http://ant.apache.org/) 
downloaded and installed. Then you can type `ant <target_name>`, e.g. `ant release` to build chosen target.

To build **dante** binaries simply type:

`ant release.std`

After the task is successful all of the binaries can be found in `out/dante.v0.5.0/bin`

You can also find the full list of available ant tasks in a section below.

# How to start dante?

You need to start:
* 1 server with UI
* *at least* 2 clients with UI, with *exactly* one client creating games and second one joining them

After all clients are started you need to setup them to use desired algorithms - this step is a bit complicated,
but hopefully instructions below will make it easier.

Once all clients are configured correctly and connected to the server game session should start and it will last 
until only one group of agents remain on the map or timer expires (by default set to 150 seconds).
New game session will start automatically once current session is ended.

## Starting server

Simply start `dante.battle_server.v0.5.0.jar` from binaries (e.g. `out/dante.v0.5.0/bin`) directory by typing: 

`java -jar dante.battle_server.v0.5.0.jar`

## Starting first client

Start first client (aka *"creating games client"*) `dante.battle_client.v0.5.0.jar` from binaries directory by typing: 

`java -jar dante.battle_client.v0.5.0.jar`

Setup *creating games client*:

1. Click *Setup algorithm* button and choose lookup path - path from which all resources and classes required by algorithm you wish to set are loaded (e.g. `out/dante.v0.5.0/bin/algorithms`).
2. After choosing lookup path, second dialog window will appear. Here you can choose a Java main class of desired algorithm. **Remember that this class must be placed in one of algorithm's lookup path subdirectories.** 

   For example you can choose one of the following algorithms:
    * `bin/algorithms/net/java/dante/algorithms/flock/FlockAlgorithm.class`
    * `bin/algorithms/net/java/dante/algorithms/rl/ReinforcementLearningAlgorithm.class`
    * `bin/algorithms/net/java/dante/algorithms/examples/Algorithm.class`

3. Algorithm should be loaded and proper log entry should appear (at the bottom of the screen), e.g. `Loaded algorithm: Reinforcement Learning Algorithm [Q-learning]`
4. Click *Configure connection* button and - if desired - configure server's address and port (you don't need to change anything if you're connecting within localhost).
   Also, for this client leave option *Creating games* in *Select client behavior* selected.
5. Press *Connect* button to connect this client to the server and automatically create a game session.
   
## Starting second client

1. Start second client (aka *"joining games client"*) and load one of algorithms in the same way as for first client (points 1-3)
2. Click *Configure connection* button and - if desired - configure server's address and port (don't change anything if you're connecting with localhost).
   Also, for the second client select option *Joining games* in *Select client behavior*.
3. Press *Connect* button to connect this client to the server and automatically join a game session created by the "creating games client".
4. Both clients should connect with the server now and game should start.


# Project Modules Summary

Currently dante contains following modules:

| Name                | Description                                       |
|---------------------|---------------------------------------------------|
| AlgorithmsFramework | Defines framework for AI algorithms run by dante. |
| BattleClientServer  | Module containing client and server logic. |
| DarkNet             | Module responsible for network communication. |
| Receiver            | Simple module defining framework for message-driven applications. |
| Simulation          | Module defining framework for algorithms environments, also called simulations. |


# Full list of ant build targets

| Name              | Description                                                                                                                                                    |
|-------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| build.dbg	        | builds all dante modules and battle server and client with debugs and tests.                                                                                   |
| darknet.dbg	    | builds DarkNet module with debugs and tests.                                                                                                                   | 
| receiver.dbg	    | builds Receiver module with debugs and tests.                                                                                                                  |
| sim.dbg	        | builds Simulation module with debugs and tests.                                                                                                                |
| algorithms.dbg	| builds Algorithms Layer module with debugs and tests.                                                                                                          |
| server_client.dbg	| builds dante battle server and client with debugs and tests.                                                                                                   |
| build	            | builds all dante modules and battle server and client without debugs and tests.                                                                                |
| darknet	        | builds DarkNet module without debugs and tests.                                                                                                                |
| receiver	        | builds Receiver module without debugs and tests.                                                                                                               |
| sim	            | builds Simulation module without debugs and tests.                                                                                                             |
| algorithms	    | builds Algorithms Layer module without debugs and tests.                                                                                                       |
| server_client	    | builds dante battle server and client without debugs and tests.                                                                                                |
| jar	            | builds all dante modules as JAR files. Battle server and client are built as executable JAR files. Created JAR files contains all required external libraries. |
| darknet.jar	    | builds DarkNet module as JAR file with all required external libraries.                                                                                        |
| receiver.jar	    | builds Receiver module as JAR file.                                                                                                                            |
| sim.jar	        | builds Simulation module as JAR file.                                                                                                                          |
| algorithms.jar    | builds Algorithms Layer module as JAR file with all required external libraries.                                                                               |
| server.jar	    | builds dante battle server as executable JAR file. Created JAR file contains all required external libraries.                                                  |
| client.jar	    | builds dante battle client as executable JAR file. Created JAR file contains all required external libraries.                                                  |
| algorithms_impl   | builds all the algorithms using dante algorithms framework and copies them to release directory.                                                               |
| release	        | builds all the dante release packages (standard, source code, full).                                                                                           |
| release.std	    | builds standard dante release package containing only binaries (no source code).                                                                               |
| release.src	    | builds dante release package containing only source code (no binaries).                                                                                        |
| release.full	    | builds full dante release package containing source code and binaries.                                                                                         |

# External dependencies

Thanks to all of the authors and contributors of:
* [Apache Ant](http://ant.apache.org/)
* [Apache MINA (Multipurpose Infrastructure for Network Applications)](http://mina.apache.org/) - great network framework
* [Fat Jar Eclipse Plug-In](http://fjep.sourceforge.net/) - great Eclipse plug-in, simplifying creation of Jar files
* Free Connectionist Q-learning Java Framework (*link no longer available*) - Open Source Java library for developing simple or complicated learning systems
* [JUnit](http://junit.org/junit4) - a simple framework to write repeatable unit tests

