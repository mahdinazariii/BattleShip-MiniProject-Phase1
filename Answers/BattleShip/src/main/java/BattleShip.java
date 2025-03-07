import java.util.Random;
import java.util.Scanner;

/**
  The BattleShip class manages the gameplay of the Battleship game between two players.
  It includes methods to manage grids, turns, and check the game status.
 */
public class BattleShip {
    static char EMPTY = '~';
    static char SHIP = 'S';
    static char HIT = 'X';
    static char MISS = 'O';

    // Grid size for the game
    static final int GRID_SIZE = 10;

    // Player 1's main grid containing their ships
    static char[][] player1Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's main grid containing their ships
    static char[][] player2Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 1's tracking grid to show their hits and misses
    static char[][] player1TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's tracking grid to show their hits and misses
    static char[][] player2TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Scanner object for user input
    static Scanner scanner = new Scanner(System.in);

    /**
      The main method that runs the game loop.
      It initializes the grids for both players, places ships randomly, and manages turns.
      The game continues until one player's ships are completely sunk.
     */
    public static void main(String[] args) {
        // Initialize grids for both players
        initializeGrid(player1Grid);
        initializeGrid(player2Grid);
        initializeGrid(player1TrackingGrid);
        initializeGrid(player2TrackingGrid);


        // Place ships randomly on each player's grid
        placeShips(player1Grid);
        placeShips(player2Grid);
       //printGrid(player1Grid);
        //printGrid(player2Grid);

        // Variable to track whose turn it is
        boolean player1Turn = true;

        // Main game loop, runs until one player's ships are all sunk
        while (!isGameOver()) {
            if (player1Turn) {
                System.out.println("Player 1's turn:");
                printGrid(player1TrackingGrid);
                playerTurn(player2Grid, player1TrackingGrid);
            } else {
                System.out.println("Player 2's turn:");
                printGrid(player2TrackingGrid);
                playerTurn(player1Grid, player2TrackingGrid);
            }
            player1Turn = !player1Turn;
        }

        System.out.println("Game Over!");
    }

    /**
      Initializes the grid by filling it with water ('~').

      @param grid The grid to initialize.
     */
    static void initializeGrid(char[][] grid) {
        for (int i =0 ; i < GRID_SIZE ; ++i ){
            for (int j =0 ; j < GRID_SIZE ; ++j ){
                grid[i][j] = EMPTY;
            }
        }
    }

    /**
      Places ships randomly on the given grid.
      This method is called for both players to place their ships on their respective grids.

      @param grid The grid where ships need to be placed.
     */
    static void placeShips(char[][] grid) {
        int [] sizeShips = {5,4,3,2};
        Random random = new Random();
        for (int size : sizeShips) {
            boolean placed = false;
            while (!placed){
                boolean horizontal = random.nextBoolean();
                int x = random.nextInt(GRID_SIZE);
                int y = random.nextInt(GRID_SIZE);
                if(canPlaceShip(grid , x ,y ,size,horizontal )){
                    for (int i =0 ; i < size ; ++i ){
                        if(horizontal){
                            grid[x][y+i]=SHIP;
                        }else {
                            grid[x+i][y]=SHIP;
                        }
                    }
                    placed = true;
                }
            }

        }
    }

    /**
      Checks if a ship can be placed at the specified location on the grid.
      This includes checking the size of the ship, its direction (horizontal or vertical),
      and if there's enough space to place it.

      @param grid The grid where the ship is to be placed.
      @param row The starting row for the ship.
      @param col The starting column for the ship.
      @param size The size of the ship.
      @param horizontal The direction of the ship (horizontal or vertical).
      @return true if the ship can be placed at the specified location, false otherwise.
     */
    static boolean canPlaceShip(char[][] grid, int row, int col, int size, boolean horizontal) {

        if (horizontal) {
            if (col + size > GRID_SIZE)
                return false;
          for (int i = 0; i < size ; ++i ){
              if (grid[row][col+i] != EMPTY)
                  return false;
          }
        }else {
            if (row + size > GRID_SIZE)
                return false;
            for (int i = 0; i < size ; ++i ){
                if (grid[row+i][col] != EMPTY)
                    return false;
            }
        }
        return true;
    }

    /**
      Manages a player's turn, allowing them to attack the opponent's grid
      and updates their tracking grid with hits or misses.

      @param opponentGrid The opponent's grid to attack.
      @param trackingGrid The player's tracking grid to update.
     */
    static void playerTurn(char[][] opponentGrid, char[][] trackingGrid) {
        System.out.println("Enter the target like ( B6 ) ! ");
        String input = scanner.nextLine().toUpperCase();
        if (!isValidInput(input)){
            System.out.println("Invalid input!");
            return;
        }
        int row = input.charAt(1) - '0';
        int col = input.charAt(0)-  'A';

        if(trackingGrid[row][col]==MISS || trackingGrid[row][col]==HIT){
            System.out.println("Already attacked !");
        }
        if (opponentGrid[row][col] == SHIP){
            System.out.println("Hit !");
            opponentGrid[row][col] = HIT;
            trackingGrid[row][col] = HIT;

        }else if (opponentGrid[row][col] == EMPTY){
            System.out.println("Miss !");
            opponentGrid[row][col] = MISS;
            trackingGrid[row][col] = MISS;
        }

    }

    /**
      Checks if the game is over by verifying if all ships are sunk.

      @return true if the game is over (all ships are sunk), false otherwise.
     */
    static boolean isGameOver() {
        boolean player1ships = allShipsSunk(player1Grid);
        boolean player2ships = allShipsSunk(player2Grid);

        return player1ships || player2ships;
    }

    /**
      Checks if all ships have been destroyed on a given grid.

      @param grid The grid to check for destroyed ships.
      @return true if all ships are sunk, false otherwise.
     */
    static boolean allShipsSunk(char[][] grid) {
        for (int i =0 ; i < GRID_SIZE ; ++i ){
            for (int j =0 ; j < GRID_SIZE ; ++j ){
                if (grid[i][j]==SHIP)
                    return false;
            }

        }
        return true;
    }

    /**
      Validates if the user input is in the correct format (e.g., A5).

      @param input The input string to validate.
      @return true if the input is in the correct format, falsae otherwise.
     */
    static boolean isValidInput(String input) {
        //todo
        if (input.length()>2){
            return false;
        }

        if (input.charAt(0) < 'A' ||input.charAt(0) > 'J') {
            return false;
        }
        int row = Integer.parseInt(input.substring(1));
        if (row <0 || row > GRID_SIZE ) {
            return false;
        }
        return true;
    }

    /**
      Prints the current state of the player's tracking grid.
      This method displays the grid, showing hits, misses, and untried locations.

      @param grid The tracking grid to print.
     */
    static void printGrid(char[][] grid) {
        char [] alpahbet = {'A','B','C','D','E','F','G','H','I','J'};
        System.out.print("  ");
        for (int i =0 ; i < GRID_SIZE ; ++i ){
            System.out.print( "  " + alpahbet[i] + "");


            }
        System.out.println();
        for (int i =0 ; i < GRID_SIZE ; ++i ){
            System.out.print(i + "  " );
            for (int j =0 ; j < GRID_SIZE ; ++j ){
                System.out.print( " " + grid[i][j] + " ");

            }
            System.out.println();
        }
        System.out.println("------------------------------------------------------------------------------------------------------");
    }
}
