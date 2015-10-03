package game;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import game.PlayerMajor.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlayerMajorTest {

    static int N = PlayerMajor.N;
    static int M = PlayerMajor.M;

    @Before
    public void setup() {
        PlayerMajor.gameHistory = new GameHistory(3);
    }

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
        grid[2][3] = 1;
        grid[3][1] = 1;
        grid[3][2] = 1;
        grid[3][3] = 1;
        history.updateHistory(gameState);
        assertEquals(PlayerHistory.CRAZY_MODE_DNG, history.history[0].calcDanger(), 1.e-9);
    }

    int round = 0;
    private GameState createGameStateForOponentPosition(int[][] grid, int i, int h) {
        return new GameState(round++, 1, Arrays.asList(new PlayerState(0, 0, 0), new PlayerState(i, h, 0)), grid);
    }

    @Test
    public void testBsf() throws Exception {
        Random rnd = new Random();
        // Significant is upper left corner
        String map =
                "00000000000011100000000000000000000" +
                        "0..........011100011110000000000000" +
                        "0..00000000011100001110000000000000" +
                        "0..01111111111100001100000000000000" +
                        "11111111111111100000000000000000000" +
                        "11111111111111101111100000000000000" +
                        "11111111111111101000000000000000000" +
                        "11111111111111101000000000000000000" +
                        "11111111111111101000000000000000000" +
                        "11111111111111101000000000000000000" +
                        "11111111111111100000000000000000000" +
                        "11111111111111111100000000000000000" +
                        "11111111111111111111110000000000000" +
                        "111111111111111111...0............0" +
                        "111111111111111111...0............0" +
                        "1111111111111111110...............0" +
                        "1111111111111111110...............0" +
                        "1111111111111111110...............0" +
                        "1111111111111111110...............0" +
                        "11111111111111111100000000000000000";
        int myInx = nexPositiveInt(rnd);
        int mark = nexPositiveInt(rnd);
        PlayerMajor.MARK = mark;
        assertEquals(14, bfsOnMap(map, 3, -1, 1, 1, myInx, true));
        assertTrue(PlayerMajor.need[1] == myInx);
        assertTrue(PlayerMajor.need[2] == myInx);
        assertTrue(PlayerMajor.sgNeedN == 0);
        assertTrue(PlayerMajor.c[1][1] == mark);
        assertTrue(PlayerMajor.c[3][1] != mark);
        assertTrue(PlayerMajor.c[3][2] != mark);
        assertTrue(PlayerMajor.c[1][10] == mark);
        assertEquals(2, PlayerMajor.islandHazz[myInx][0]);

        map =
                "00000000000011100000000000000000000" +
                        "0..........011100011110000000000000" +
                        "0.......000011100001110000000000000" +
                        "00........1111100001100000000000000" +
                        "11110...011111100000000000000000000" +
                        "11110...011111101111100000000000000" +
                        "11110000011111101000000000000000000" +
                        "11111111111111101000000000000000000" +
                        "11111111111111101000000000000000000" +
                        "11111111111111101000000000000000000" +
                        "11111111111111100000000000000000000" +
                        "11111111111111111100000000000000000" +
                        "11111111111111111111110000000000000" +
                        "111111111111111111...0............0" +
                        "111111111111111111...0............0" +
                        "1111111111111111110...............0" +
                        "1111111111111111110...............0" +
                        "1111111111111111110...............0" +
                        "1111111111111111110...............0" +
                        "11111111111111111100000000000000000";
        myInx = nexPositiveInt(rnd);
        mark = nexPositiveInt(rnd);
        PlayerMajor.MARK = mark;
        assertEquals(24, bfsOnMap(map, 3, -1, 1, 1, myInx, true));
        assertTrue(PlayerMajor.need[1] != myInx);
        assertTrue(PlayerMajor.need[2] == myInx);
        assertTrue(PlayerMajor.need[3] == myInx);
        assertTrue(PlayerMajor.need[4] == myInx);
        assertTrue(PlayerMajor.need[5] == myInx);
        assertTrue(PlayerMajor.need[6] == myInx);
        assertTrue(PlayerMajor.need[7] == myInx);
        assertTrue(PlayerMajor.need[8] != myInx);
        assertTrue(PlayerMajor.need[9] != myInx);
        assertTrue(PlayerMajor.sgNeedN == 1);
        assertTrue(PlayerMajor.sgNeed[0][0] == myInx);
        assertTrue(PlayerMajor.sgNeed[0][1] == 8);
        assertTrue(PlayerMajor.c[3][8] != mark);
        assertTrue(PlayerMajor.c[3][7] != mark);
        assertTrue(PlayerMajor.c[2][7] == mark);
        assertTrue(PlayerMajor.c[1][10] == mark);
        assertTrue(PlayerMajor.c[1][10] == mark);
        assertTrue(PlayerMajor.c[4][5] != mark);
        assertEquals(0, PlayerMajor.conN);
        assertEquals(2, PlayerMajor.islandHazz[myInx][0]);
        myInx++;
        assertEquals(11, bfsOnMap(map, 3, -1, 4, 5, myInx, false));
        assertTrue(PlayerMajor.need[1] != myInx);
        assertTrue(PlayerMajor.need[2] == myInx - 1);
        assertTrue(PlayerMajor.need[3] == myInx - 1);
        assertTrue(PlayerMajor.need[4] == myInx - 1);
        assertTrue(PlayerMajor.need[5] == 0);
        assertTrue(PlayerMajor.need[6] == 0);
        assertTrue(PlayerMajor.need[7] == 0);
        assertTrue(PlayerMajor.need[8] != myInx);
        assertTrue(PlayerMajor.need[9] != myInx);
        assertEquals(3, PlayerMajor.sgNeedN);
        assertTrue(PlayerMajor.sgNeed[0][0] == myInx - 1);
        assertTrue(PlayerMajor.sgNeed[0][1] == 8);
        assertTrue(PlayerMajor.sgNeed[1][0] == myInx);
        assertTrue(PlayerMajor.sgNeed[1][1] == 4);
        assertTrue(PlayerMajor.sgNeed[2][0] == myInx);
        assertTrue(PlayerMajor.sgNeed[2][1] == 8);
        assertTrue(PlayerMajor.c[4][5] == mark);
        assertTrue(PlayerMajor.c[5][6] == mark);
        assertEquals(3, PlayerMajor.conN);
        assertEquals(myInx, PlayerMajor.con[0][0]);
        assertEquals(myInx - 1, PlayerMajor.con[0][1]);
        assertEquals(7, PlayerMajor.islandHazz[myInx][0]);

        map =
                "00000000000011100000000000000000000" +
                        "0..........011100011110000000000000" +
                        "0.000000000011100001110000000000000" +
                        "00........1111100001100000000000000" +
                        "11000...011111100000000000000000000" +
                        "11110...011111101111100000000000000" +
                        "11110000011111101000000000000000000" +
                        "11111111111111101000000000000000000" +
                        "11111111111111101000000000000000000" +
                        "11111111111111101000000000000000000" +
                        "11111111111111100000000000000000000" +
                        "11111111111111111100000000000000000" +
                        "11111111111111111111110000000000000" +
                        "111111111111111111...0............0" +
                        "111111111111111111...0............0" +
                        "1111111111111111110...............0" +
                        "1111111111111111110...............0" +
                        "1111111111111111110...............0" +
                        "1111111111111111110...............0" +
                        "11111111111111111100000000000000000";

        myInx = nexPositiveInt(rnd);
        mark = nexPositiveInt(rnd);
        PlayerMajor.MARK = mark;
        assertEquals(0, bfsOnMap(map, -1, 4, 1, 1, myInx, true));
        assertTrue(PlayerMajor.need[1] == 0);
        assertTrue(PlayerMajor.need[3] == 0);
        assertEquals(0, PlayerMajor.sgNeedN);
        assertTrue(PlayerMajor.c[3][2] == mark);
        assertTrue(PlayerMajor.c[3][3] == mark);
        assertEquals(2, PlayerMajor.islandHazz[myInx][0]);

        myInx = nexPositiveInt(rnd);
        mark = nexPositiveInt(rnd);
        PlayerMajor.MARK = mark;
        assertEquals(4, bfsOnMap(map, -1, 2, 1, 1, myInx, true));
        assertEquals(9, bfsOnMap(map, -1, 2, 1, 3, myInx + 1, false));
        assertTrue(PlayerMajor.need[1] == 0);
        assertTrue(PlayerMajor.need[3] == 0);
        assertEquals(1, PlayerMajor.sgNeedN);
        assertTrue(PlayerMajor.sgNeed[0][0] == myInx);
        assertTrue(PlayerMajor.sgNeed[0][1] == 3);
        assertTrue(PlayerMajor.c[1][5] == mark);
        assertEquals(1, PlayerMajor.conN);
        assertEquals(myInx + 1, PlayerMajor.con[0][0]);
        assertEquals(myInx, PlayerMajor.con[0][1]);
        assertEquals(2, PlayerMajor.islandHazz[myInx][0]);
        assertEquals(3, PlayerMajor.islandHazz[myInx + 1][0]);

        map =
                "00000.00000011100000000000000000000" +
                        "00000..0000011100011110000000000000" +
                        "00000..000000011100001110000000000000" +
                        "0000....001111100001100000000000000" +
                        "11000...011111100000000000000000000" +
                        "11110...011111101111100000000000000" +
                        "11110000011111101000000000000000000" +
                        "11111111111111101000000000000000000" +
                        "11111111111111101000000000000000000" +
                        "11111111111111101000000000000000000" +
                        "11111111111111100000000000000000000" +
                        "11111111111111111100000000000000000" +
                        "11111111111111111111110000000000000" +
                        "111111111111111111...0............0" +
                        "111111111111111111...0............0" +
                        "1111111111111111110...............0" +
                        "1111111111111111110...............0" +
                        "1111111111111111110...............0" +
                        "1111111111111111110...............0" +
                        "11111111111111111100000000000000000";

        myInx = nexPositiveInt(rnd);
        mark = nexPositiveInt(rnd);
        PlayerMajor.MARK = mark;
        assertEquals(0, bfsOnMap(map, 2, -1, 0, 5, myInx, true));
        assertTrue(PlayerMajor.need[5] == 0);
        assertTrue(PlayerMajor.need[6] == 0);
        assertEquals(0, PlayerMajor.sgNeedN);
        assertTrue(PlayerMajor.c[1][5] == mark);
        assertEquals(0, PlayerMajor.conN);
    }

    @Test
    public void testRelatedIslands() {
        cleanIslandsGrapth();
        setIslandGraphEdge(1, 2, 5);
        setIslandGraphEdge(1, 3, 5);
        assertEquals(2, PlayerMajor.relatedIslands(1, 5, 0));
        assertEquals(10, PlayerMajor.islandCons);
        assertEquals(1, PlayerMajor.islandGroup[0]);
        assertEquals(2, PlayerMajor.islandGroup[1]);
        assertEquals(3, PlayerMajor.islandGroup[2]);

        cleanIslandsGrapth();
        setIslandGraphEdge(1, 2, 1);
        setIslandGraphEdge(2, 3, 5);
        setIslandGraphEdge(2, 4, 5);
        setIslandGraphEdge(4, 5, 5);
        assertEquals(4, PlayerMajor.relatedIslands(1, 6, 0));
        assertEquals(16, PlayerMajor.islandCons);
        assertEquals(1, PlayerMajor.islandGroup[0]);
        assertEquals(2, PlayerMajor.islandGroup[1]);
        assertEquals(3, PlayerMajor.islandGroup[2]);
        assertEquals(4, PlayerMajor.islandGroup[3]);
        assertEquals(5, PlayerMajor.islandGroup[4]);
    }

    @Test
    public void testIslandsForLine() {
        String map = "00000000000011100000000000000000000" +
                "0..........011100011110000000000000" +
                "0..00000000011100001110000000000000" +
                "0..01111111111100001100000000000000" +
                "11111111111111100000000000000000000" +
                "11111111111111101111100000000000000" +
                "11111111111111101000000000000000000" +
                "11111111111111101000000000000000000" +
                "11111111111111101000000000000000000" +
                "11111111111111101000000000000000000" +
                "11111111111111100000000000000000000" +
                "11111111111111111100000000000000000" +
                "11111111111111111111110000000000000" +
                "111111111111111111...0............0" +
                "111111111111111111...0............0" +
                "1111111111111111110...............0" +
                "1111111111111111110...............0" +
                "1111111111111111110...............0" +
                "1111111111111111110...............0" +
                "11111111111111111100000000000000000";
        int x = 5;
        int y = 14;
        int[] z = new int[]{2};
        assertEquals(PlayerMajor.valueFor(fakeGameStateWithOponents(1), x, y, z), testIslandsForLine(map, 3, -1), 1.e-9);
        map = "00000000000011100000000000000000000" +
                "0..........011100011110000000000000" +
                "0.......000011100001110000000000000" +
                "00........1111100001100000000000000" +
                "11110...011111100000000000000000000" +
                "11110...011111101111100000000000000" +
                "11110000011111101000000000000000000" +
                "11111111111111101000000000000000000" +
                "11111111111111101000000000000000000" +
                "11111111111111101000000000000000000" +
                "11111111111111100000000000000000000" +
                "11111111111111111100000000000000000" +
                "11111111111111111111110000000000000" +
                "111111111111111111...0............0" +
                "111111111111111111...0............0" +
                "1111111111111111110...............0" +
                "1111111111111111110...............0" +
                "1111111111111111110...............0" +
                "1111111111111111110...............0" +
                "11111111111111111100000000000000000";
        x = 11;
        y = 30;
        z = new int[]{2};
        assertEquals(PlayerMajor.valueFor(fakeGameStateWithOponents(1), x, y, z),
                testIslandsForLine(map, 3, -1),
                1.e-9);

        map = "0..0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.00" +
                "11111111111111111111111111111111111" +
                "00.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.00" +
                "11111111111111111111111111111111111" +
                "0..0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.00" +
                "11111111111111111111111111111111111" +
                "0..0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.00" +
                "11111111111111111111111111111111111" +
                "0..0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.00" +
                "11111111111111111111111111111111111" +
                "0..0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.00" +
                "11111111111111111111111111111111111" +
                "0..0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.00" +
                "11111111111111111111111111111111111" +
                "0..0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.00" +
                "11111111111111111111111111111111111" +
                "0..0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.00" +
                "11111111111111111111111111111111111" +
                "0..0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.00" +
                "11111111111111111111111111111111111";
        assertEquals(Double.MIN_VALUE, testIslandsForLine(map, 1, -1), 1.e-9);
    }

    @Test
    public void testRunForestRun() {
        int[][] grid = fullGrid();
        grid[0][0] = PlayerMajor.EMPTY;
        GameState gameState = new GameState(1, 1, Arrays.asList(new PlayerState(19, 34, 0)), grid);
        assertEquals(true, PlayerMajor.runForestRun(Double.MIN_VALUE, gameState));
        assertEquals(18, PlayerMajor.moveI);
        assertEquals(34, PlayerMajor.moveH);
        assertEquals(false, PlayerMajor.runForestRun(0.5, gameState));

        grid = fullGrid();
        grid[0][34] = PlayerMajor.EMPTY;
        grid[19][0] = PlayerMajor.EMPTY;
        gameState = new GameState(1, 1, Arrays.asList(new PlayerState(19, 34, 0)), grid);
        assertEquals(true, PlayerMajor.runForestRun(Double.MIN_VALUE, gameState));
        assertEquals(18, PlayerMajor.moveI);
        assertEquals(34, PlayerMajor.moveH);

        grid = fullGrid();
        gameState = new GameState(1, 1, Arrays.asList(new PlayerState(19, 34, 0)), grid);
        assertEquals(false, PlayerMajor.runForestRun(Double.MIN_VALUE, gameState));
    }

    private PlayerMajor.GameState fakeGameStateWithOponents(int ops) {
        return new PlayerMajor.GameState(1, 1, null, null);
    }

    private double testIslandsForLine(String map, int lineI, int lineH) {
        PlayerMajor.PlayerState myPlayerState = new PlayerMajor.PlayerState(0, 0, 0);
        int[][] grid = stringToGrid(map);
        PlayerMajor.GameState gameState =
                new PlayerMajor.GameState(0, 1, Arrays.asList(myPlayerState, myPlayerState), grid);
        return PlayerMajor.islandsForLine(gameState, lineI, lineH);
    }

    private void setIslandGraphEdge(int a, int b, int value) {
        PlayerMajor.islandsGraph[a][b] = value;
        PlayerMajor.islandsGraph[b][a] = value;
    }

    private void cleanIslandsGrapth() {
        for (int[] row : PlayerMajor.islandsGraph) {
            Arrays.fill(row, 0);
        }
        Arrays.fill(PlayerMajor.visitedIsland, false);
        PlayerMajor.islandCons = 0;
    }

    private int bfsOnMap(String map, int lineI, int lineH, int startI, int startH, int myIndx, boolean refresh) {
        PlayerMajor.PlayerState myPlayerState = new PlayerMajor.PlayerState(0, 0, 0);
        int[][] grid = stringToGrid(map);
        PlayerMajor.GameState gameState =
                new PlayerMajor.GameState(0, 1, Arrays.asList(myPlayerState, myPlayerState), grid);
        if (refresh) {
            Arrays.fill(PlayerMajor.need, 0);
            for (int[] row : PlayerMajor.c) {
                Arrays.fill(row, 0);
            }
            for (int[] row : PlayerMajor.islandHazz) {
                Arrays.fill(row, Integer.MAX_VALUE);
            }
            PlayerMajor.sgNeedN = 0;
            PlayerMajor.conN = 0;
        }
        return PlayerMajor.bsf(gameState, lineI, lineH, startI, startH, myIndx);
    }

    private int[][] stringToGrid(String map) {
        int[][] grid = new int[N][M];
        int hh = 0;
        for (int i = 0; i < N; i++) {
            for (int h = 0; h < M; h++) {
                if (map.charAt(hh) == '.') {
                    grid[i][h] = PlayerMajor.EMPTY;
                } else {
                    grid[i][h] = Integer.valueOf(Character.toString(map.charAt(hh)));
                }
                hh++;
            }
        }
        return grid;
    }

    private int nexPositiveInt(Random rnd) {
        return Math.abs(rnd.nextInt(15)) + 1;
    }

    // OLD TESTS

    @Test
    public void testTurnToString() {
        assertEquals("1 2", new Turn(1, 2, 0).toString());
        assertEquals("1 2\rBACK 1", new Turn(1, 2, 1).toString());
        assertEquals("1 2\rBACK 1\rmsg", new Turn(1, 2, 1, "msg").toString());
        assertEquals("1 2\rmsg", new Turn(1, 2, 0, "msg").toString());
    }

    @Test
    public void testMoveClockWise() {
        PlayerMajor.moveClockWise(0, 2, 0, 2, 0, 0);
        assertEquals(0, PlayerMajor.moveI);
        assertEquals(1, PlayerMajor.moveH);
        PlayerMajor.moveClockWise(0, 2, 0, 2, 0, 1);
        assertEquals(0, PlayerMajor.moveI);
        assertEquals(2, PlayerMajor.moveH);
        PlayerMajor.moveClockWise(0, 2, 0, 2, 0, 2);
        assertEquals(1, PlayerMajor.moveI);
        assertEquals(2, PlayerMajor.moveH);
        PlayerMajor.moveClockWise(0, 2, 0, 2, 1, 2);
        assertEquals(2, PlayerMajor.moveI);
        assertEquals(2, PlayerMajor.moveH);
        PlayerMajor.moveClockWise(0, 2, 0, 2, 2, 2);
        assertEquals(2, PlayerMajor.moveI);
        assertEquals(1, PlayerMajor.moveH);
        PlayerMajor.moveClockWise(0, 2, 0, 2, 2, 1);
        assertEquals(2, PlayerMajor.moveI);
        assertEquals(0, PlayerMajor.moveH);
        PlayerMajor.moveClockWise(0, 2, 0, 2, 2, 0);
        assertEquals(1, PlayerMajor.moveI);
        assertEquals(0, PlayerMajor.moveH);
        PlayerMajor.moveClockWise(0, 2, 0, 2, 1, 0);
        assertEquals(0, PlayerMajor.moveI);
        assertEquals(0, PlayerMajor.moveH);
    }

    @Test
    public void testMoveClockUnwise() {
        PlayerMajor.moveClockUnwise(0, 2, 0, 2, 0, 0);
        assertEquals(1, PlayerMajor.moveI);
        assertEquals(0, PlayerMajor.moveH);
        PlayerMajor.moveClockUnwise(0, 2, 0, 2, 1, 0);
        assertEquals(2, PlayerMajor.moveI);
        assertEquals(0, PlayerMajor.moveH);
        PlayerMajor.moveClockUnwise(0, 2, 0, 2, 2, 0);
        assertEquals(2, PlayerMajor.moveI);
        assertEquals(1, PlayerMajor.moveH);
        PlayerMajor.moveClockUnwise(0, 2, 0, 2, 2, 1);
        assertEquals(2, PlayerMajor.moveI);
        assertEquals(2, PlayerMajor.moveH);
        PlayerMajor.moveClockUnwise(0, 2, 0, 2, 2, 2);
        assertEquals(1, PlayerMajor.moveI);
        assertEquals(2, PlayerMajor.moveH);
        PlayerMajor.moveClockUnwise(0, 2, 0, 2, 1, 2);
        assertEquals(0, PlayerMajor.moveI);
        assertEquals(2, PlayerMajor.moveH);
        PlayerMajor.moveClockUnwise(0, 2, 0, 2, 0, 2);
        assertEquals(0, PlayerMajor.moveI);
        assertEquals(1, PlayerMajor.moveH);
        PlayerMajor.moveClockUnwise(0, 2, 0, 2, 0, 1);
        assertEquals(0, PlayerMajor.moveI);
        assertEquals(0, PlayerMajor.moveH);
    }

    @Test
    public void testDistanceToPoint() {
        assertEquals(0, PlayerMajor.distanceToPoint(0, 0, 0, 0));
        assertEquals(1, PlayerMajor.distanceToPoint(0, 0, 0, 1));
        assertEquals(1, PlayerMajor.distanceToPoint(0, 0, 1, 0));
        assertEquals(2, PlayerMajor.distanceToPoint(0, 0, 1, 1));
    }

    @Test
    public void testDistanceToRec() {
        assertEquals(0, PlayerMajor.distanceToRec(0, 2, 0, 2, 0, 0));
        assertEquals(0, PlayerMajor.distanceToRec(0, 2, 0, 2, 0, 1));
        assertEquals(0, PlayerMajor.distanceToRec(0, 2, 0, 2, 2, 2));
        assertEquals(1, PlayerMajor.distanceToRec(0, 2, 0, 2, 1, 1));
        assertEquals(1, PlayerMajor.distanceToRec(0, 2, 0, 2, 3, 2));
        assertEquals(1, PlayerMajor.distanceToRec(0, 2, 0, 2, 2, 3));
        assertEquals(2, PlayerMajor.distanceToRec(0, 2, 0, 2, 3, 3));
        assertEquals(3, PlayerMajor.distanceToRec(0, 2, 0, 2, 3, 4));
    }

    @Test
    public void testWorkToComplete() {
        PlayerState myPlayerState = new PlayerState(3, 3, 0);
        int[][] grid = emptyGrid();
        grid[2][2] = 0;
        grid[2][1] = 0;
        grid[1][2] = 0;
        grid[0][2] = 0;
        GameState gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertEquals(7, PlayerMajor.workToComplete(gameState, 0, 2, 0, 2));
        assertEquals(false, PlayerMajor.stepDesc.toRec);
        assertTrue((PlayerMajor.stepDesc.toI == 2 && PlayerMajor.stepDesc.toH == 3) ||
                (PlayerMajor.stepDesc.toI == 3 && PlayerMajor.stepDesc.toH == 2));
        assertEquals(2, PlayerMajor.stepDesc.pointI);
        assertEquals(0, PlayerMajor.stepDesc.pointH);

        grid[2][2] = -1;
        grid[2][1] = -1;
        grid[1][2] = -1;
        grid[0][2] = -1;
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertEquals(9, PlayerMajor.workToComplete(gameState, 0, 2, 0, 2));
        assertEquals(true, PlayerMajor.stepDesc.toRec);
        assertTrue((PlayerMajor.stepDesc.toI == 2 && PlayerMajor.stepDesc.toH == 3) ||
                (PlayerMajor.stepDesc.toI == 3 && PlayerMajor.stepDesc.toH == 2));

        grid[0][0] = 0;
        grid[2][2] = 0;
        grid[2][1] = 0;
        grid[1][2] = 0;
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertEquals(8, PlayerMajor.workToComplete(gameState, 0, 2, 0, 2));

        grid[0][0] = 0;
        grid[2][2] = 0;
        grid[2][1] = 0;
        grid[1][2] = 0;
        grid[0][2] = 0;
        myPlayerState = new PlayerState(2, 2, 0);
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertEquals(5, PlayerMajor.workToComplete(gameState, 0, 2, 0, 2));
        assertEquals(false, PlayerMajor.stepDesc.toRec);
        assertEquals(2, PlayerMajor.stepDesc.toI);
        assertEquals(1, PlayerMajor.stepDesc.toH);
        assertEquals(2, PlayerMajor.stepDesc.pointI);
        assertEquals(0, PlayerMajor.stepDesc.pointH);

        grid[0][0] = -1;
        grid[2][2] = 0;
        grid[2][1] = 0;
        grid[1][2] = 0;
        grid[0][2] = 0;
        grid[0][1] = 0;
        myPlayerState = new PlayerState(0, 1, 0);
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertEquals(3, PlayerMajor.workToComplete(gameState, 0, 2, 0, 2));
        assertEquals(false, PlayerMajor.stepDesc.toRec);
        assertEquals(0, PlayerMajor.stepDesc.toI);
        assertEquals(0, PlayerMajor.stepDesc.toH);

        grid[0][0] = -1;
        grid[2][2] = 0;
        grid[2][1] = 0;
        grid[1][2] = 0;
        grid[0][2] = 0;
        grid[0][1] = -1;
        grid[2][0] = 0;
        myPlayerState = new PlayerState(2, 0, 0);
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertEquals(3, PlayerMajor.workToComplete(gameState, 0, 2, 0, 2));
        assertEquals(false, PlayerMajor.stepDesc.toRec);
        assertEquals(1, PlayerMajor.stepDesc.toI);
        assertEquals(0, PlayerMajor.stepDesc.toH);

        grid = emptyGrid();
        grid[2][2] = 0;
        grid[2][3] = 0;
        myPlayerState = new PlayerState(2, 2, 0);
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertEquals(2, PlayerMajor.workToComplete(gameState, 1, 2, 2, 3));

        grid = emptyGrid();
        grid[2][2] = 0;
        grid[2][3] = 0;
        grid[3][3] = 0;
        myPlayerState = new PlayerState(2, 2, 0);
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertEquals(3, PlayerMajor.workToComplete(gameState, 2, 3, 3, 4));
    }

    @Test
    public void testComputePoints() {
        PlayerState myPlayerState = new PlayerState(3, 3, 0);
        int[][] grid = emptyGrid();
        grid[2][2] = 0;
        grid[2][1] = 0;
        grid[1][2] = 0;
        GameState gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        PlayerMajor.stepDesc.toRec = false;
        PlayerMajor.stepDesc.pointI = 2;
        PlayerMajor.stepDesc.pointH = 0;
        initCs(0, 2, 0, 2, grid);
        // 9 possible
        assertEquals(7, PlayerMajor.computePoints(gameState, PlayerMajor.stepDesc, 0, 2, 0, 2));

        myPlayerState = new PlayerState(2, 2, 0);
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        initCs(0, 2, 0, 2, grid);
        assertEquals(6, PlayerMajor.computePoints(gameState, PlayerMajor.stepDesc, 0, 2, 0, 2));

        grid[2][2] = -1;
        grid[2][1] = -1;
        grid[1][2] = -1;
        PlayerMajor.stepDesc.toRec = true;
        myPlayerState = new PlayerState(3, 3, 0);
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        initCs(0, 2, 0, 2, grid);
        assertEquals(10, PlayerMajor.computePoints(gameState, PlayerMajor.stepDesc, 0, 2, 0, 2));
    }

    @Test
    public void testMoveTowardPoint() {
        assertEquals(false, PlayerMajor.moveTowardPoint(0, 0, 0, 0));
        assertEquals(true, PlayerMajor.moveTowardPoint(0, 0, 1, 0));
        assertEquals(1, PlayerMajor.moveI);
        assertEquals(0, PlayerMajor.moveH);
        assertEquals(true, PlayerMajor.moveTowardPoint(0, 0, 0, 1));
        assertEquals(0, PlayerMajor.moveI);
        assertEquals(1, PlayerMajor.moveH);
        assertEquals(true, PlayerMajor.moveTowardPoint(1, 1, 0, 1));
        assertEquals(0, PlayerMajor.moveI);
        assertEquals(1, PlayerMajor.moveH);
        assertEquals(true, PlayerMajor.moveTowardPoint(1, 1, 1, 0));
        assertEquals(1, PlayerMajor.moveI);
        assertEquals(0, PlayerMajor.moveH);
    }

    @Test
    public void testMoveTowardRec() {
        assertEquals(false, PlayerMajor.moveTowardRec(0, 2, 0, 2, 0, 0));
        assertEquals(false, PlayerMajor.moveTowardRec(0, 2, 0, 2, 0, 1));
        assertEquals(false, PlayerMajor.moveTowardRec(0, 2, 0, 2, 2, 2));
        assertEquals(false, PlayerMajor.moveTowardRec(0, 2, 0, 2, 1, 0));
        assertEquals(true, PlayerMajor.moveTowardRec(0, 2, 0, 2, 2, 3));
        assertEquals(2, PlayerMajor.moveI);
        assertEquals(2, PlayerMajor.moveH);
        assertEquals(true, PlayerMajor.moveTowardRec(0, 2, 0, 2, 3, 2));
        assertEquals(2, PlayerMajor.moveI);
        assertEquals(2, PlayerMajor.moveH);
        assertEquals(true, PlayerMajor.moveTowardRec(0, 6, 0, 6, 2, 1));
        assertEquals(2, PlayerMajor.moveI);
        assertEquals(0, PlayerMajor.moveH);
        assertEquals(true, PlayerMajor.moveTowardRec(0, 6, 0, 6, 1, 4));
        assertEquals(0, PlayerMajor.moveI);
        assertEquals(4, PlayerMajor.moveH);
    }

    @Test
    public void testComputeHazzard() {
        PlayerState myPlayerState = new PlayerState(3, 3, 0);
        int[][] grid = emptyGrid();
        GameState gameState = new GameState(0,
                1,
                Arrays.asList(myPlayerState, new PlayerState(4, 4, 0), new PlayerState(1, 1, 0)),
                grid);
        int[] z = PlayerMajor.computeHazzard(gameState, 0, 2, 0, 2);
        assertEquals(4, z[0]);
        assertEquals(1, z[1]);
    }

    @Test
    public void testIsValidRectangle() {
        PlayerState myPlayerState = new PlayerState(3, 3, 0);
        int[][] grid = emptyGrid();
        GameState gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        initCs(0, 2, 0, 2, grid);
        assertTrue(PlayerMajor.isValidRectangle(gameState, 0, 2, 0, 2));
        grid[0][0] = 1;
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        initCs(0, 2, 0, 2, grid);
        assertFalse(PlayerMajor.isValidRectangle(gameState, 0, 2, 0, 2));
        grid[0][0] = 0;
        grid[0][1] = 0;
        grid[1][0] = 0;
        grid[1][1] = 0;
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        initCs(0, 1, 0, 1, grid);
        assertFalse(PlayerMajor.isValidRectangle(gameState, 0, 1, 0, 1));
        grid = emptyGrid();
        grid[1][1] = 1;
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        initCs(0, 2, 0, 2, grid);
        assertFalse(PlayerMajor.isValidRectangle(gameState, 0, 2, 0, 2));
        grid = emptyGrid();
        for (int i = 18; i <= 19; i++) {
            for (int h = 21; h <= 25; h++) {
                grid[i][h] = 0;
            }
        }
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        initCs(18, 19, 21, 25, grid);
        assertFalse(PlayerMajor.isValidRectangle(gameState, 18, 19, 21, 25));
    }

    @Ignore
    @Test
    public void testMakeTurn() {
        PlayerState myPlayerState = new PlayerState(15, 5, 0);
        int[][] grid = emptyGrid();
        for (int i = 0; i < 350; i++) {
            long st = System.currentTimeMillis();
            GameState gameState = new GameState(0,
                    1,
                    Arrays.asList(myPlayerState,
                            new PlayerState(5, 15, 0),
                            new PlayerState(15, 15, 0),
                            new PlayerState(5, 5, 0)),
                    grid);
            Turn turn = PlayerMajor.makeTurn(gameState);
            long ed = System.currentTimeMillis();
            System.out.println((ed - st) + "ms " + turn.toString());
            myPlayerState = new PlayerState(turn.y, turn.x, 0);
            grid[turn.y][turn.x] = 0;
        }
    }

    @Test
    public void testComputeValue() {
        int[][] grid = emptyGrid();
        grid[0][0] = 0;
        grid[1][0] = 0;
        grid[1][1] = 0;
        grid[2][1] = 0;
        grid[2][2] = 0;
        grid[3][2] = 0;
        PlayerState myPlayerState = new PlayerState(1, 1, 0);
        GameState gameState = new GameState(0, 1, Arrays.asList(myPlayerState, new PlayerState(5, 5, 0)), grid);
        initCs(0, 1, 0, 1, grid);
        double val1 = PlayerMajor.computeValue(gameState, 0, 1, 0, 1);
        initCs(2, 3, 1, 2, grid);
        double val2 = PlayerMajor.computeValue(gameState, 2, 3, 1, 2);
        double maxVal1 = PlayerMajor.maxValueFor(gameState, 0, 1, 0, 1);
        assertTrue(maxVal1 > val2);
        assertTrue(val1 > val2);
    }

    private int[][] emptyGrid() {
        int[][] grid = new int[PlayerMajor.N][PlayerMajor.M];
        for (int i = 0; i < PlayerMajor.N; i++) {
            for (int h = 0; h < PlayerMajor.M; h++) {
                grid[i][h] = -1;
            }
        }
        return grid;
    }

    private int[][] fullGrid() {
        int[][] grid = new int[PlayerMajor.N][PlayerMajor.M];
        for (int i = 0; i < PlayerMajor.N; i++) {
            for (int h = 0; h < PlayerMajor.M; h++) {
                grid[i][h] = 0;
            }
        }
        return grid;
    }

    private void initCs(int i1, int i2, int h1, int h2, int[][] grid) {
        int cempty = 0;
        int cmy = 0;
        for (int i = i1; i <= i2; i++) {
            for (int h = h1; h <= h2; h++) {
                cempty += grid[i][h] == PlayerMajor.EMPTY ? 1 : 0;
                cmy += grid[i][h] == PlayerMajor.MY ? 1 : 0;
            }
        }
        PlayerMajor.CEMPTY = cempty;
        PlayerMajor.CMY = cmy;
    }
}