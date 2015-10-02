package game;

import org.junit.Test;
import game.PlayerMajor.*;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class PlayerMajorTest {

    static int N = PlayerMajor.N;
    static int M = PlayerMajor.M;

    @Test
    public void testGameHistoryCalcDanger() {
        round = 0;
        int[][] grid = emptyGrid();
        GameHistory history = new GameHistory(1);
        GameState gameState = createGameStateForOponentPosition(grid, 0, 0);
        grid[0][0] = 1;
        history.updateHistory(gameState);
        assertEquals(PlayerHistory.DEFAULT_DNG, history.history[0].calcDanger(), 1.e-9);
        gameState = createGameStateForOponentPosition(grid, 0, 1);
        grid[0][1] = 1;
        history.updateHistory(gameState);
        assertEquals(PlayerHistory.DEFAULT_DNG, history.history[0].calcDanger(), 1.e-9);
        gameState = createGameStateForOponentPosition(grid, 0, 2);
        grid[0][2] = 1;
        history.updateHistory(gameState);
        assertEquals(PlayerHistory.DEFAULT_DNG, history.history[0].calcDanger(), 1.e-9);
        gameState = createGameStateForOponentPosition(grid, 0, 3);
        grid[0][3] = 1;
        history.updateHistory(gameState);
        assertEquals(PlayerHistory.DEFAULT_DNG, history.history[0].calcDanger(), 1.e-9);
        gameState = createGameStateForOponentPosition(grid, 0, 4);
        grid[0][4] = 1;
        history.updateHistory(gameState);
        assertEquals(PlayerHistory.DEFAULT_DNG, history.history[0].calcDanger(), 1.e-9);
        gameState = createGameStateForOponentPosition(grid, 1, 4);
        grid[1][4] = 1;
        history.updateHistory(gameState);
        assertEquals(PlayerHistory.DEFAULT_DNG, history.history[0].calcDanger(), 1.e-9);
        gameState = createGameStateForOponentPosition(grid, 2, 4);
        grid[2][4] = 1;
        history.updateHistory(gameState);
        assertEquals(PlayerHistory.DEFAULT_DNG, history.history[0].calcDanger(), 1.e-9);
        gameState = createGameStateForOponentPosition(grid, 3, 4);
        grid[3][4] = 1;
        history.updateHistory(gameState);
        assertEquals(PlayerHistory.DEFAULT_DNG, history.history[0].calcDanger(), 1.e-9);
        gameState = createGameStateForOponentPosition(grid, 4, 4);
        grid[4][4] = 1;
        history.updateHistory(gameState);
        assertEquals(PlayerHistory.DEFAULT_DNG, history.history[0].calcDanger(), 1.e-9);
        gameState = createGameStateForOponentPosition(grid, 4, 3);
        grid[4][3] = 1;
        history.updateHistory(gameState);
        assertEquals(1.0 - 1.0 / 7.0, history.history[0].calcDanger(), 1.e-9);
        gameState = createGameStateForOponentPosition(grid, 4, 2);
        grid[4][2] = 1;
        history.updateHistory(gameState);
        assertEquals(1.0 - 2.0 / 7.0, history.history[0].calcDanger(), 1.e-9);
        gameState = createGameStateForOponentPosition(grid, 4, 1);
        grid[4][1] = 1;
        history.updateHistory(gameState);
        assertEquals(1.0 - 3.0 / 7.0, history.history[0].calcDanger(), 1.e-9);
        gameState = createGameStateForOponentPosition(grid, 4, 0);
        grid[4][0] = 1;
        history.updateHistory(gameState);
        assertEquals(1.0 - 4.0 / 7.0, history.history[0].calcDanger(), 1.e-9);
        gameState = createGameStateForOponentPosition(grid, 3, 0);
        grid[3][0] = 1;
        history.updateHistory(gameState);
        assertEquals(1.0 - 5.0 / 7.0, history.history[0].calcDanger(), 1.e-9);
        gameState = createGameStateForOponentPosition(grid, 2, 0);
        grid[2][0] = 1;
        history.updateHistory(gameState);
        assertEquals(1.0 - 6.0 / 7.0, history.history[0].calcDanger(), 1.e-9);
        gameState = createGameStateForOponentPosition(grid, 1, 0);
        grid[1][0] = 1;
        grid[1][1] = 1;
        grid[1][2] = 1;
        grid[1][3] = 1;
        grid[2][1] = 1;
        grid[2][2] = 1;
        grid[2][3] = 1;
        grid[3][1] = 1;
        grid[3][2] = 1;
        grid[3][3] = 1;
        history.updateHistory(gameState);
        assertEquals((675.0/700), history.history[0].calcDanger(), 1.e-9);
    }

    int round = 0;
    private GameState createGameStateForOponentPosition(int[][] grid, int i, int h) {
        return new GameState(round++, 1, Arrays.asList(new PlayerState(0, 0, 0), new PlayerState(i, h, 0)), grid);
    }

    private int[][] emptyGrid() {
        int[][] grid = new int[PlayerMinor.N][PlayerMinor.M];
        for (int i = 0; i < PlayerMinor.N; i++) {
            for (int h = 0; h < PlayerMinor.M; h++) {
                grid[i][h] = -1;
            }
        }
        return grid;
    }
}