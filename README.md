### Objective

Create 1-round Pokemon Battle using CSV data, Java and JavaScript.

![Illumise](https://img.pokemondb.net/sprites/home/normal/illumise.png) **vs** ![Charmeleon](https://img.pokemondb.net/sprites/home/normal/charmeleon.png)

### Brief

Professor Oak is learning the basics of Pokemon, he wants to know which pokemon are more effective against other pokemon. He isn't concerned about what abilities that they might have. 

### Tasks

- Implement assignment using:
    - Languages: **Java** & **Javascript**
    - Frameworks: **Spring** & **VueJS**
- Create a Pokemon class (or classes) that include all fields outlined in `src/main/resources/data/pokemon.csv`
- Parse the .csv file and instantiate an object for each row
- The Pokemon share similar attributes, but there are 4 types, each with different behavior of the _effectiveness modifier_ for calculating damage as follows:
    - Fire is: 2X more effective vs grass, 0.5 effective vs water, neutral against electric
    - Water is: 2X more effective vs fire, 0.5 effective vs electric, neutral against grass
    - Grass is: 2X more effective vs electric, 0.5 effective vs fire, neutral against water
    - Electric is: 2X more effective vs water, 0.5 effective vs grass, neutral against fire
- Create one API endpoint `/attack` which:
  - Accept 2 pokemon as input parameters
  - Let each pokemon continuously attack the other one at a time, until one of the two pokemon run out of hit points.
  - The pokemon with the highest _Speed_ will always attack first. If two pokemon have the same speed, a random one goes first
  - A pokemon who has run out of hit points is considered the loser.
  - The attack damage can be calculated as follows `50 x (attack of attacking pokemon / defense of defending pokemon) * effectiveness modifier` (You're welcome to use a different formula to calculate attack damage based on the pokemon.csv) 
  - Return which of the 2 pokemon is the winner and its remaining HitPoints (HP) after damage has been calculated
- Modify the HTML & JavaScript in `src/main/resources/public` to:
  - Allow for two pokemon to be entered or chosen by Professor Oak
  - Use these two pokemon as input for the `/attack` endpoint
  - Use the response to show at least an image of the winning pokemon (There is an example loading an image from a sprite website)
- Add unit tests for all of your conditions

### Notes
- We've included a gradle wrapper, so you can (should) run gradle commands as `./gradlew` instead of just `gradle` in the terminal
- We've also included Spring Boot, which makes it simple to start up your local web server and add new endpoints
- Your endpoint should just be backed up by an in memory copy of your data, so you should just need to load up the data once when the application initializes
- Before making any updates to the repo, you should be able to run `./gradlew bootRun` to get the Spring app running on your local machine. Once you see a message in the logs that says something like "Started SpringPokemonApplication...", then it should be up
    - You can also verify it's running by going to http://localhost:8080 in your browser, and you should see "_ wins"

### Evaluation Criteria

- Data parsing and representation
- Code organization and simplicity
- Creativity and usability
- Show your work through commit history
- Correctness: does the correct pokemon win in a certain scenario?
- Maintainability: is it written in a clean, maintainable way?
- Testing: is the system adequately tested? (Should be able to run unit tests by running `./gradlew test`)

### CodeSubmit

Please organize, design, test and document your code as if it were going into production - then push your changes to the main branch. After you have pushed your code, you may submit the assignment on the assignment page.

All the best and happy coding,

The Impact Team
