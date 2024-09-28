Full-stack Application: Backend built with Spring Boot, using Gradle for dependency management and MongoDB for persistence.

Frontend: Retro-looking, dynamic, and beautifully designed with Vue.js and vanilla CSS for a polished, interactive user experience. Includes animations for Pokémon and icons, as well.

Features: Simulates battles between over 680 Pokémon (Gen 1-5), incorporating type matchups, stats, RNG, normal/special attack and defense mechanics. Simulation retrieves the moveset for each individual Pokémon using moves.json and MongoDB.

Modern Development: Leverages modern libraries like Lombok for clean code and includes comprehensive unit tests for robust code coverage.

Scalable: Future expansion planned to include additional Pokémon generations and types.

Home page:
![Screenshot 2024-09-28 163655](https://github.com/user-attachments/assets/075839bc-f47c-487f-a251-b7888c38ab5e)

What user sees after inputting the 2 Pokémon they wish to duke it out:
![image](https://github.com/user-attachments/assets/9daa4b61-46a1-47d0-9063-430c5116e411)

Added an option for randomized battles, which pits 2 random Pokémon from the MongoDB:
![Screenshot 2024-09-28 163133](https://github.com/user-attachments/assets/64cd72f6-2cd2-4926-91e8-ef7c5521b1a4)

Still a work-in-progress, as I plan to deploy this application. I also plan to include a library of all the supported Pokémon, where users can see their stats, primary type, secondary type (if exists), game of origin, and a description of the Pokémon. 
