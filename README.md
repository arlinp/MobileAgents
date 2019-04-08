# Project 4 - Mobile Agents

### Requirements
Program writtin using IntelliiJ IDEA <br> Java 8 <br> Entry for program is Display.java

### Info
Program that demonstrates a simulation system for a wireless sensor network application.  In this application we simulate a sensor network to be used in the detection and monitoring of forest fires.
Each sensor node of the network has a fire condition state and is connected to neighbor sensors. Sensors can only communicate to connected neighbors and any mobile agent occupying the sensor. Only 
one mobile agent can occupy a sensor at a time. Mobile agents are used to traverse the sensor network. Agents randomly move along the network checking node states.  If the node state indicates 
that the current node is near or connected to a node that is on fire, the agent copies itself to the non-near fire neighbors and monitors the nodes near the fire node.  If the fire spreads to the 
near fire node containing the original agent, the agent is killed off and the new near-fire agents clone themselves to neighboring non-near fire nodes.  Simulation continues until the fire kills 
all mobile agents or until the fire reaches the base station / root sensor node.

### Instructions
Program reads an input text file that describes a sensor network and stores network as a planar graph.  Input file must be specified at run-time.  Each line of the configuration file gives information of either a node, edge, base station
location, or fire location.  Location of the fire and base station must be indicated at the end of the configuration file or after the node has been read and initialized. 
Simulation starts immediately with one agent at the base station.  As the agent begins randomly walking the network, the fire begins to spread until everything is on fire. 

### Todo
-Finish message queue.
-Fix configuration file reading to accept node location values larger than a single digit.

#### Workflow
A. Pedregon and J. Lusby both worked together to structure and design the project.<br>
A. Pedregon focused on thread functionality and messaging between nodes.<br>
J. Lusby focused on graph configuration and GUI functionality. <br>
Both members worked together to integrate each other's ideas / code. As well as optimization of graph objects, display, and overall functionality.<br>  