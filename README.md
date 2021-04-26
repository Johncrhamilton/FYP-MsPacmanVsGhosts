# What is the project about?
The project is about using computer games, more specifically Ms Pac-Man, as a testing
ground for computational intelligence learning techniques since computer games are often
noisy and unpredictable, they will provide an interesting environment for CI learning
techniques.<br/>
The project will focus on writing software to interact with defined APIs for Ms Pac-Man and
then learning high performing ghost strategies using computational intelligence techniques.

# What is the project deliverable?
The project deliverable is software that uses a genetic algorithm to teach the Ghost Team of
the “Ms. Pac-Man vs Ghosts competition” effective Flocking Strategies against varying
difficulty levels of Ms Pac-Man.<br/>
The software will seek to find satisfactory solutions by teaching both Homogeneous and
Heterogeneous agents as well as compare and contrast the solutions between the types of
agents drawing conclusions in the final year report.

# What controllers have been added in this project?
<ul>
  <li>The project proposed Flocking Strategy Ghosts (FLIGHT) for the Ms. Pacman vs Ghost framework is based on the work done by Federico Liberatore, Antonio M. Mora, Pedro A. Castillo, and Juan J. Merelo with their GALR ghosts in their article "Comparing Heterogeneous and Homogeneous Flocking Strategies for the Ghost Team in the Game of Ms. Pac-Man" https://doi.org/10.1109/TCIAIG.2015.2425795.
  </li>
  <li>The project proposed Influence Map-Based Pacman (IMP) and Ghosts (IMGO) for the Ms. Pacman vs Ghost framework is based on the work done by J. Svensson and S. J. Johansson with their Influence Map-Based Pacman and Ghosts in their article "Influence Map-based controllers for Ms. PacMan and the ghosts" https://doi.org/10.1109/CIG.2012.6374164.
  </li>
</ul>  

# Which version of the Ms. Pac-Man Versus Ghosts Competition is this project developed with?
The project is developed using the version (Ms. Pac-Man Versus Ghosts Competition) maintained by joseatovar http://joseatovar.github.io/Ms-Pacman-vs-Ghost/. In the competition's development timeline this version is after the Ms. Pac-Man Screen-Capture Competition that required participants to develop screen capture software alongside their controllers but before the Ms. Pac-Man Versus Ghost Team Competition that added Partial Observability into the mix.

# Where can I find the project controllers and underlying classes?
Flocking Strategy Ghosts (FLIGHT):
<ul>
  <li>Controller implementation, see the /pacman-vs-ghosts/src/pacman/entries/pacman/ (FLIGHT).
  </li>  
  <li>Underlying implementation classes, see the /pacman-vs-ghosts/src/pacman/strategy/flocking/.
  </li>
</ul>

Influence Map-Based Pacman (IMP) and Ghosts (IMGO):
<ul>
  <li>Controller implementation, see the /pacman-vs-ghosts/src/pacman/entries/pacman/ (IMP) and <br/> /pacman-vs-ghosts/src/pacman/entries/ghosts/ (IMGO).
  </li>  
  <li>Underlying implementation classes, see the /pacman-vs-ghosts/src/pacman/influencemap/.
  </li>
</ul>

# Setup and project execution instructions
Please do note that the application must be executed within a java IDE due to the nature of the competition being a hands-on coding challenge to make Ms. Pacman and Ghost controllers. The following set of instructions assume that Eclipse is the used java IDE.
<br/>
Setup:
<ol>
  <li>Download or clone the project and import it into Eclipse.
  </li>
  <li>In Eclipse, access the project's build path and go to the libraries tab. Edit the JRE System Library and ensure that the execution environment is set the latest JavaSE you can access. This project used JavaSE-14 (jre).
  </li>
  <li>Download Apache Commons Math 3.6.1 from http://commons.apache.org/proper/commons-math/download_math.cgi. Within libraries tab of the project's build path add the Apache commons-math3-3.6.1.jar to the project using either the "Add JARs..." or "Add External JARs..." whichever is preferable. Once done hit apply and close.
  </li>
  <li>The setup for the project is now complete and you should have no errors at this point. If you still have errors try restarting Eclipse or refreshing the project.
  </li>
</ol>

Project execution options:
<ul>
  <li>The Ms Pac-Man vs Ghosts Competition can be executed in the /pacman-vs-ghosts/src/pacman/Executor.java class. 
    <br/>The class's main function has the game's execution functions commented out so if you would like to run a single game with visuals uncomment an already setup exec.runGame method or add "exec.runGame(new PacmanControllerName(), new GhostControllerName(), visual (True or false), delay (integer));" and fill in the parameters appropriately. 
    <br/>You can find all the controllers that came with the competition framework in the /pacman-vs-ghosts/src/pacman/controllers/examples/ package.
  </li>
  <li>To run multiple games at once without visuals uncomment a exec.runExperiment() method or add "System.out.print(exec.runExperiment(new PacmanControllerName(), new GhostControllerName(), NUM_EXPERIMENT_RUNS (integer)).toString());" in the executor's main function.
  </li>
  <li>To run games with the project's FLIGHT Ghosts replace the "GhostControllerName()" with "new FlockingStrategyGhosts(FLOCKING_STRATEGIES)" in the exec.runGame. 
    <br/>The FLOCKING_STRATEGIES parameter is a collection of flocking strategies (1 strategy required for Homogeneous Ghosts and 4 strategies for Heterogeneous Ghosts) and is uncommented in the executor's main function. 
    <br/>You will need to change the Flocking Ghosts HOMOGENEOUS_GHOSTS (True or false) "mode" to false for Heterogeneous Ghosts in the /pacman-vs-ghosts/src/pacman/strategy/flocking/FSConstants.java class.
  </li>
  <li>You can also produce and optimise your own flocking strategies using the FLIGHT Ghosts Genetic Algorithm by uncommenting or adding "GeneticAlgorithm GA = new GeneticAlgorithm(exec);" and "System.out.println(GA.bestFlockingStrategy());" in the executor's main function. 
    <br/>The Genetic Algorithm's parameters are in the FSConstants.java class and the Pacman Controllers used for optimisation can be adjusted in the /pacman-vs-ghosts/src/pacman/strategy/flocking/GeneticAlgorithm.java calculateScore method.
  </li>
  <li>To run games with the project's IMP Pacman and IMGO Ghosts replace the "PacmanControllerName()" and "GhostControllerName()" with "InfluenceMapPacMan()" and "InfluenceMapGhosts()" in the exec.runGame respectively. 
    <br/>You can adjust the influence values and propagation of influences by changing constants in the /pacman-vs-ghosts/src/pacman/influencemap/IMConstants.java.
  </li>
</ul>

# Third Party Resources
<ul>
  <li> This project makes use of the Ms Pac-Man vs Ghosts Competition code written by Philipp Rohlfshagen based on earlier implementations of the game by Simon Lucas 
and David Robles as part of the competition held by the University of Essex. <br/>
    Please see copyright file: /pacman-vs-ghosts/copyright.txt.
  </li> 
  <li>This project also incorporates the Apache Commons Math 3.6.1 java library for statistical testing and mathematical distribution generation within the Eclipse IDE. http://commons.apache.org/proper/commons-math/download_math.cgi
  </li>
</ul>
