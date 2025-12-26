# Hide and Seek The Challenge

## Description

Hide and Seek The Challenge is a Java-based game developed using the MVVM (Model–View–ViewModel) architecture.
The game tells the story of a main character who must survive alien attacks by hiding and avoiding incoming bullets. Uniquely, the player starts the game with zero bullets. Bullets are only gained when the player successfully avoids alien shots (missed bullets).

The collected bullets can be used to shoot aliens. Each defeated alien increases the player's score, while every shot fired decreases the remaining bullets. Game results such as score, missed alien bullets, and remaining bullets are stored in a database and accumulated across multiple play sessions.

This game was developed as part of the Future Assignment (TMD) for the DPBO course, featuring a Java Swing user interface and animations implemented using a Timer.

---

## Game Concept

- Player starts with zero bullets
- Player gains bullets when alien bullets miss
- Shooting consumes player bullets
- Defeating aliens increases score
- Game ends when the player is hit
- Obstacles are randomly generated each game

---

## Application Architecture (MVVM)
### 1. Model
Handles data and database access:
- `Database`
- `Benefit`
- `BenefitRepository`
- `Obstacle`

### 2. ViewModel
Handles business logic and gameplay:
- `MenuViewModel`
- `GameViewModel`

### 3. View
Handles GUI:
- `MenuView` 
- `GameView` 
- `Main` 

---

## How to Compile and Run
### Prerequisites
Before running the game, make sure you have the following installed:
- Java Development Kit (JDK): JDK 11 or newer is recommended.
- MySQL Server Used to store game results (score, missed bullets, remaining bullets).
- MySQL Connector/J The JDBC driver (mysql-connector-j-9.5.0.jar) is located in the lib directory.

1. Database Setup
   - Start your MySQL server.
   - Create a database named tmd_dpbo.
   - Create a table tbenefit with the following columns:
     - username
     - skor
     - peluru_meleset
     - peluru_sisa
   - If needed, adjust database credentials in src/model/Database.java
    
2. Compilation
   Open Command Prompt / Terminal in the root directory of the project.
   ### On Windows
   ```
   javac -d bin -cp "lib/mysql-connector-j-9.5.0.jar" src/Main.java src/model/*.java src/view/*.java src/viewmodel/*.java
   ```

   Copy game assets (backgrounds, sprites, etc.):

   ```
   xcopy src\assets bin\assets /E /I /Y
   ```

3. Running the Game
   After successful compilation, run the game using:

   ### On Windows
   ```
   java -cp "bin;lib/mysql-connector-j-9.5.0.jar" Main
   ```

---
   
## Controls
1. Arrow Up (↑) : Move character upward
2. Arrow Down (↓) : Move character downward
3. Arrow Left (←) : Move character to the left
4. Arrow Right (→) : Move character to the right
5. Z : Shoot bullet
6. Spacebar : Stop the current game and return to the main menu

---

## Notes

- Built with Java Swing
- Uses Timer for animation loop
- Desktop-based
- Emphasizes OOP and MVVM architecture

---

## Credits
Asset credits are listed in the CREDITS.txt file.

