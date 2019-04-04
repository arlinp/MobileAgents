# Project 4 - Mobile Agents
### Info
Program that demonstrates a simulation system for a wireless sensor network application.  In this application we simulate a sensor network to be used in the detection and monitoring of forest fires.
Each sensor node of the network has a fire condition state and is connected to neighbor sensors. Sensors can only communicate to connected neighbors and any mobile agent occupying the sensor. Only 
one mobile agent can occupy a sensor at a time. Mobile agents are used to traverse the sensor network. Agents randomly move along the network checking node states.  If the node state indicates 
that the current node is near or connected to a node that is on fire, the agent copies itself to the non-near fire neighbors and monitors the nodes near the fire node.  If the fire spreads to the 
near fire node containing the original agent, the agent is killed off and the new near-fire agents clone themselves to neighboring non-near fire nodes.  Simulation continues until the fire kills 
all mobile agents or until the fire reaches the base station / root sensor node.

### Instructions
Program reads an input text file that describes a sensor network and stores network as a planar graph.  Each line of the configuration file gives information of either a node, edge, base station
location, or fire location.

### Completed
Reads in a configuration file and displays a GUI containing the indicated graph.  Nodes are connected with lines and node color indicates the node state.  Agent and Node threads are initialized and 
started.  Initial configuration contains a single agent that walks the graph searching for a node on fire.  If a fire found a message is printed to standard out.

### Todo
-Finish message queue.
-Fix walking of agents so they stop when they have already explored the graph entirely at first.
-Fix configuration file reading to accept node location values larger than a single digit.
-Add update functionality to GUI to display updates as the fire spreads.