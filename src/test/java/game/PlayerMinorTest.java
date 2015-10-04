package game;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import game.PlayerMinor.*;

import static org.junit.Assert.*;

public class PlayerMinorTest {

    static int N = PlayerMajor.N;
    static int M = PlayerMajor.M;

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
        PlayerMinor.MARK = mark;
        assertEquals(14, bfsOnMap(map, 3, -1, 1, 1, myInx, true));
        assertTrue(PlayerMinor.need[1] == myInx);
        assertTrue(PlayerMinor.need[2] == myInx);
        assertTrue(PlayerMinor.sgNeedN == 0);
        assertTrue(PlayerMinor.c[1][1] == mark);
        assertTrue(PlayerMinor.c[3][1] != mark);
        assertTrue(PlayerMinor.c[3][2] != mark);
        assertTrue(PlayerMinor.c[1][10] == mark);
        assertEquals(2, PlayerMinor.islandHazz[myInx][0]);

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
        PlayerMinor.MARK = mark;
        assertEquals(24, bfsOnMap(map, 3, -1, 1, 1, myInx, true));
        assertTrue(PlayerMinor.need[1] != myInx);
        assertTrue(PlayerMinor.need[2] == myInx);
        assertTrue(PlayerMinor.need[3] == myInx);
        assertTrue(PlayerMinor.need[4] == myInx);
        assertTrue(PlayerMinor.need[5] == myInx);
        assertTrue(PlayerMinor.need[6] == myInx);
        assertTrue(PlayerMinor.need[7] == myInx);
        assertTrue(PlayerMinor.need[8] != myInx);
        assertTrue(PlayerMinor.need[9] != myInx);
        assertTrue(PlayerMinor.sgNeedN == 1);
        assertTrue(PlayerMinor.sgNeed[0][0] == myInx);
        assertTrue(PlayerMinor.sgNeed[0][1] == 8);
        assertTrue(PlayerMinor.c[3][8] != mark);
        assertTrue(PlayerMinor.c[3][7] != mark);
        assertTrue(PlayerMinor.c[2][7] == mark);
        assertTrue(PlayerMinor.c[1][10] == mark);
        assertTrue(PlayerMinor.c[1][10] == mark);
        assertTrue(PlayerMinor.c[4][5] != mark);
        assertEquals(0, PlayerMinor.conN);
        assertEquals(2, PlayerMinor.islandHazz[myInx][0]);
        myInx++;
        assertEquals(11, bfsOnMap(map, 3, -1, 4, 5, myInx, false));
        assertTrue(PlayerMinor.need[1] != myInx);
        assertTrue(PlayerMinor.need[2] == myInx - 1);
        assertTrue(PlayerMinor.need[3] == myInx - 1);
        assertTrue(PlayerMinor.need[4] == myInx - 1);
        assertTrue(PlayerMinor.need[5] == 0);
        assertTrue(PlayerMinor.need[6] == 0);
        assertTrue(PlayerMinor.need[7] == 0);
        assertTrue(PlayerMinor.need[8] != myInx);
        assertTrue(PlayerMinor.need[9] != myInx);
        assertEquals(3, PlayerMinor.sgNeedN);
        assertTrue(PlayerMinor.sgNeed[0][0] == myInx - 1);
        assertTrue(PlayerMinor.sgNeed[0][1] == 8);
        assertTrue(PlayerMinor.sgNeed[1][0] == myInx);
        assertTrue(PlayerMinor.sgNeed[1][1] == 4);
        assertTrue(PlayerMinor.sgNeed[2][0] == myInx);
        assertTrue(PlayerMinor.sgNeed[2][1] == 8);
        assertTrue(PlayerMinor.c[4][5] == mark);
        assertTrue(PlayerMinor.c[5][6] == mark);
        assertEquals(3, PlayerMinor.conN);
        assertEquals(myInx, PlayerMinor.con[0][0]);
        assertEquals(myInx - 1, PlayerMinor.con[0][1]);
        assertEquals(7, PlayerMinor.islandHazz[myInx][0]);

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
        PlayerMinor.MARK = mark;
        assertEquals(0, bfsOnMap(map, -1, 4, 1, 1, myInx, true));
        assertTrue(PlayerMinor.need[1] == 0);
        assertTrue(PlayerMinor.need[3] == 0);
        assertEquals(0, PlayerMinor.sgNeedN);
        assertTrue(PlayerMinor.c[3][2] == mark);
        assertTrue(PlayerMinor.c[3][3] == mark);
        assertEquals(2, PlayerMinor.islandHazz[myInx][0]);

        myInx = nexPositiveInt(rnd);
        mark = nexPositiveInt(rnd);
        PlayerMinor.MARK = mark;
        assertEquals(4, bfsOnMap(map, -1, 2, 1, 1, myInx, true));
        assertEquals(9, bfsOnMap(map, -1, 2, 1, 3, myInx + 1, false));
        assertTrue(PlayerMinor.need[1] == 0);
        assertTrue(PlayerMinor.need[3] == 0);
        assertEquals(1, PlayerMinor.sgNeedN);
        assertTrue(PlayerMinor.sgNeed[0][0] == myInx);
        assertTrue(PlayerMinor.sgNeed[0][1] == 3);
        assertTrue(PlayerMinor.c[1][5] == mark);
        assertEquals(1, PlayerMinor.conN);
        assertEquals(myInx + 1, PlayerMinor.con[0][0]);
        assertEquals(myInx, PlayerMinor.con[0][1]);
        assertEquals(2, PlayerMinor.islandHazz[myInx][0]);
        assertEquals(3, PlayerMinor.islandHazz[myInx + 1][0]);

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
        PlayerMinor.MARK = mark;
        assertEquals(0, bfsOnMap(map, 2, -1, 0, 5, myInx, true));
        assertTrue(PlayerMinor.need[5] == 0);
        assertTrue(PlayerMinor.need[6] == 0);
        assertEquals(0, PlayerMinor.sgNeedN);
        assertTrue(PlayerMinor.c[1][5] == mark);
        assertEquals(0, PlayerMinor.conN);
    }

    @Test
    public void testRelatedIslands() {
        cleanIslandsGrapth();
        setIslandGraphEdge(1, 2, 5);
        setIslandGraphEdge(1, 3, 5);
        assertEquals(2, PlayerMinor.relatedIslands(1, 5, 0));
        assertEquals(10, PlayerMinor.islandCons);
        assertEquals(1, PlayerMinor.islandGroup[0]);
        assertEquals(2, PlayerMinor.islandGroup[1]);
        assertEquals(3, PlayerMinor.islandGroup[2]);

        cleanIslandsGrapth();
        setIslandGraphEdge(1, 2, 1);
        setIslandGraphEdge(2, 3, 5);
        setIslandGraphEdge(2, 4, 5);
        setIslandGraphEdge(4, 5, 5);
        assertEquals(4, PlayerMinor.relatedIslands(1, 6, 0));
        assertEquals(16, PlayerMinor.islandCons);
        assertEquals(1, PlayerMinor.islandGroup[0]);
        assertEquals(2, PlayerMinor.islandGroup[1]);
        assertEquals(3, PlayerMinor.islandGroup[2]);
        assertEquals(4, PlayerMinor.islandGroup[3]);
        assertEquals(5, PlayerMinor.islandGroup[4]);
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
        assertEquals(PlayerMinor.valueFor(fakeGameStateWithOponents(1, map), x, y, z, fakeGameStateWithOponents(1, map).emptyCells), testIslandsForLine(map, 3, -1), 1.e-9);
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
        assertEquals(PlayerMinor.valueFor(fakeGameStateWithOponents(1, map), x, y, z, fakeGameStateWithOponents(1, map).emptyCells),
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
        grid[0][0] = PlayerMinor.EMPTY;
        GameState gameState = new GameState(1, 1, Arrays.asList(new PlayerState(19, 34, 0)), grid);
        assertEquals(true, PlayerMinor.runForestRun(Double.MIN_VALUE, gameState));
        assertEquals(18, PlayerMinor.moveI);
        assertEquals(34, PlayerMinor.moveH);
        assertEquals(false, PlayerMinor.runForestRun(0.5, gameState));

        grid = fullGrid();
        grid[0][34] = PlayerMinor.EMPTY;
        grid[19][0] = PlayerMinor.EMPTY;
        gameState = new GameState(1, 1, Arrays.asList(new PlayerState(19, 34, 0)), grid);
        assertEquals(true, PlayerMinor.runForestRun(Double.MIN_VALUE, gameState));
        assertEquals(18, PlayerMinor.moveI);
        assertEquals(34, PlayerMinor.moveH);

        grid = fullGrid();
        gameState = new GameState(1, 1, Arrays.asList(new PlayerState(19, 34, 0)), grid);
        assertEquals(false, PlayerMinor.runForestRun(Double.MIN_VALUE, gameState));
    }

    private PlayerMinor.GameState fakeGameStateWithOponents(int ops, String map) {
        return new PlayerMinor.GameState(1, 1, null, stringToGrid(map));
    }

    private double testIslandsForLine(String map, int lineI, int lineH) {
        PlayerMinor.PlayerState myPlayerState = new PlayerMinor.PlayerState(0, 0, 0);
        int[][] grid = stringToGrid(map);
        PlayerMinor.GameState gameState =
                new PlayerMinor.GameState(0, 1, Arrays.asList(myPlayerState, myPlayerState), grid);
        return PlayerMinor.islandsForLine(gameState, lineI, lineH);
    }

    private void setIslandGraphEdge(int a, int b, int value) {
        PlayerMinor.islandsGraph[a][b] = value;
        PlayerMinor.islandsGraph[b][a] = value;
    }

    private void cleanIslandsGrapth() {
        for (int[] row : PlayerMinor.islandsGraph) {
            Arrays.fill(row, 0);
        }
        Arrays.fill(PlayerMinor.visitedIsland, false);
        PlayerMinor.islandCons = 0;
    }

    private int bfsOnMap(String map, int lineI, int lineH, int startI, int startH, int myIndx, boolean refresh) {
        PlayerMinor.PlayerState myPlayerState = new PlayerMinor.PlayerState(0, 0, 0);
        int[][] grid = stringToGrid(map);
        PlayerMinor.GameState gameState =
                new PlayerMinor.GameState(0, 1, Arrays.asList(myPlayerState, myPlayerState), grid);
        if (refresh) {
            Arrays.fill(PlayerMinor.need, 0);
            for (int[] row : PlayerMinor.c) {
                Arrays.fill(row, 0);
            }
            for (int[] row : PlayerMinor.islandHazz) {
                Arrays.fill(row, Integer.MAX_VALUE);
            }
            PlayerMinor.sgNeedN = 0;
            PlayerMinor.conN = 0;
        }
        return PlayerMinor.bsf(gameState, lineI, lineH, startI, startH, myIndx);
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
        assertEquals("BACK 1", new Turn(1, 2, 1).toString());
        assertEquals("BACK 1\rmsg", new Turn(1, 2, 1, "msg").toString());
        assertEquals("1 2\rmsg", new Turn(1, 2, 0, "msg").toString());
    }

    @Test
    public void testMoveClockWise() {
        PlayerMinor.moveClockWise(0, 2, 0, 2, 0, 0);
        assertEquals(0, PlayerMinor.moveI);
        assertEquals(1, PlayerMinor.moveH);
        PlayerMinor.moveClockWise(0, 2, 0, 2, 0, 1);
        assertEquals(0, PlayerMinor.moveI);
        assertEquals(2, PlayerMinor.moveH);
        PlayerMinor.moveClockWise(0, 2, 0, 2, 0, 2);
        assertEquals(1, PlayerMinor.moveI);
        assertEquals(2, PlayerMinor.moveH);
        PlayerMinor.moveClockWise(0, 2, 0, 2, 1, 2);
        assertEquals(2, PlayerMinor.moveI);
        assertEquals(2, PlayerMinor.moveH);
        PlayerMinor.moveClockWise(0, 2, 0, 2, 2, 2);
        assertEquals(2, PlayerMinor.moveI);
        assertEquals(1, PlayerMinor.moveH);
        PlayerMinor.moveClockWise(0, 2, 0, 2, 2, 1);
        assertEquals(2, PlayerMinor.moveI);
        assertEquals(0, PlayerMinor.moveH);
        PlayerMinor.moveClockWise(0, 2, 0, 2, 2, 0);
        assertEquals(1, PlayerMinor.moveI);
        assertEquals(0, PlayerMinor.moveH);
        PlayerMinor.moveClockWise(0, 2, 0, 2, 1, 0);
        assertEquals(0, PlayerMinor.moveI);
        assertEquals(0, PlayerMinor.moveH);
    }

    @Test
    public void testMoveClockUnwise() {
        PlayerMinor.moveClockUnwise(0, 2, 0, 2, 0, 0);
        assertEquals(1, PlayerMinor.moveI);
        assertEquals(0, PlayerMinor.moveH);
        PlayerMinor.moveClockUnwise(0, 2, 0, 2, 1, 0);
        assertEquals(2, PlayerMinor.moveI);
        assertEquals(0, PlayerMinor.moveH);
        PlayerMinor.moveClockUnwise(0, 2, 0, 2, 2, 0);
        assertEquals(2, PlayerMinor.moveI);
        assertEquals(1, PlayerMinor.moveH);
        PlayerMinor.moveClockUnwise(0, 2, 0, 2, 2, 1);
        assertEquals(2, PlayerMinor.moveI);
        assertEquals(2, PlayerMinor.moveH);
        PlayerMinor.moveClockUnwise(0, 2, 0, 2, 2, 2);
        assertEquals(1, PlayerMinor.moveI);
        assertEquals(2, PlayerMinor.moveH);
        PlayerMinor.moveClockUnwise(0, 2, 0, 2, 1, 2);
        assertEquals(0, PlayerMinor.moveI);
        assertEquals(2, PlayerMinor.moveH);
        PlayerMinor.moveClockUnwise(0, 2, 0, 2, 0, 2);
        assertEquals(0, PlayerMinor.moveI);
        assertEquals(1, PlayerMinor.moveH);
        PlayerMinor.moveClockUnwise(0, 2, 0, 2, 0, 1);
        assertEquals(0, PlayerMinor.moveI);
        assertEquals(0, PlayerMinor.moveH);
    }

    @Test
    public void testDistanceToPoint() {
        assertEquals(0, PlayerMinor.distanceToPoint(0, 0, 0, 0));
        assertEquals(1, PlayerMinor.distanceToPoint(0, 0, 0, 1));
        assertEquals(1, PlayerMinor.distanceToPoint(0, 0, 1, 0));
        assertEquals(2, PlayerMinor.distanceToPoint(0, 0, 1, 1));
    }

    @Test
    public void testDistanceToRec() {
        assertEquals(0, PlayerMinor.distanceToRec(0, 2, 0, 2, 0, 0));
        assertEquals(0, PlayerMinor.distanceToRec(0, 2, 0, 2, 0, 1));
        assertEquals(0, PlayerMinor.distanceToRec(0, 2, 0, 2, 2, 2));
        assertEquals(1, PlayerMinor.distanceToRec(0, 2, 0, 2, 1, 1));
        assertEquals(1, PlayerMinor.distanceToRec(0, 2, 0, 2, 3, 2));
        assertEquals(1, PlayerMinor.distanceToRec(0, 2, 0, 2, 2, 3));
        assertEquals(2, PlayerMinor.distanceToRec(0, 2, 0, 2, 3, 3));
        assertEquals(3, PlayerMinor.distanceToRec(0, 2, 0, 2, 3, 4));
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
        assertEquals(7, PlayerMinor.workToComplete(gameState, 0, 2, 0, 2));
        assertEquals(false, PlayerMinor.stepDesc.toRec);
        assertTrue((PlayerMinor.stepDesc.toI == 2 && PlayerMinor.stepDesc.toH == 3) ||
                (PlayerMinor.stepDesc.toI == 3 && PlayerMinor.stepDesc.toH == 2));
        assertEquals(2, PlayerMinor.stepDesc.pointI);
        assertEquals(0, PlayerMinor.stepDesc.pointH);

        grid[2][2] = -1;
        grid[2][1] = -1;
        grid[1][2] = -1;
        grid[0][2] = -1;
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertEquals(9, PlayerMinor.workToComplete(gameState, 0, 2, 0, 2));
        assertEquals(true, PlayerMinor.stepDesc.toRec);
        assertTrue((PlayerMinor.stepDesc.toI == 2 && PlayerMinor.stepDesc.toH == 3) ||
                (PlayerMinor.stepDesc.toI == 3 && PlayerMinor.stepDesc.toH == 2));

        grid[0][0] = 0;
        grid[2][2] = 0;
        grid[2][1] = 0;
        grid[1][2] = 0;
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertEquals(8, PlayerMinor.workToComplete(gameState, 0, 2, 0, 2));

        grid[0][0] = 0;
        grid[2][2] = 0;
        grid[2][1] = 0;
        grid[1][2] = 0;
        grid[0][2] = 0;
        myPlayerState = new PlayerState(2, 2, 0);
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertEquals(5, PlayerMinor.workToComplete(gameState, 0, 2, 0, 2));
        assertEquals(false, PlayerMinor.stepDesc.toRec);
        assertEquals(2, PlayerMinor.stepDesc.toI);
        assertEquals(1, PlayerMinor.stepDesc.toH);
        assertEquals(2, PlayerMinor.stepDesc.pointI);
        assertEquals(0, PlayerMinor.stepDesc.pointH);

        grid[0][0] = -1;
        grid[2][2] = 0;
        grid[2][1] = 0;
        grid[1][2] = 0;
        grid[0][2] = 0;
        grid[0][1] = 0;
        myPlayerState = new PlayerState(0, 1, 0);
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertEquals(3, PlayerMinor.workToComplete(gameState, 0, 2, 0, 2));
        assertEquals(false, PlayerMinor.stepDesc.toRec);
        assertEquals(0, PlayerMinor.stepDesc.toI);
        assertEquals(0, PlayerMinor.stepDesc.toH);

        grid[0][0] = -1;
        grid[2][2] = 0;
        grid[2][1] = 0;
        grid[1][2] = 0;
        grid[0][2] = 0;
        grid[0][1] = -1;
        grid[2][0] = 0;
        myPlayerState = new PlayerState(2, 0, 0);
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertEquals(3, PlayerMinor.workToComplete(gameState, 0, 2, 0, 2));
        assertEquals(false, PlayerMinor.stepDesc.toRec);
        assertEquals(1, PlayerMinor.stepDesc.toI);
        assertEquals(0, PlayerMinor.stepDesc.toH);

        grid = emptyGrid();
        grid[2][2] = 0;
        grid[2][3] = 0;
        myPlayerState = new PlayerState(2, 2, 0);
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertEquals(2, PlayerMinor.workToComplete(gameState, 1, 2, 2, 3));

        grid = emptyGrid();
        grid[2][2] = 0;
        grid[2][3] = 0;
        grid[3][3] = 0;
        myPlayerState = new PlayerState(2, 2, 0);
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertEquals(3, PlayerMinor.workToComplete(gameState, 2, 3, 3, 4));
    }

    @Test
    public void testComputePoints() {
        PlayerState myPlayerState = new PlayerState(3, 3, 0);
        int[][] grid = emptyGrid();
        grid[2][2] = 0;
        grid[2][1] = 0;
        grid[1][2] = 0;
        GameState gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        PlayerMinor.stepDesc.toRec = false;
        PlayerMinor.stepDesc.pointI = 2;
        PlayerMinor.stepDesc.pointH = 0;
        initCs(0, 2, 0, 2, grid);
        // 9 possible
        assertEquals(7, PlayerMinor.computePoints(gameState, PlayerMinor.stepDesc, 0, 2, 0, 2));

        myPlayerState = new PlayerState(2, 2, 0);
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        initCs(0, 2, 0, 2, grid);
        assertEquals(6, PlayerMinor.computePoints(gameState, PlayerMinor.stepDesc, 0, 2, 0, 2));

        grid[2][2] = -1;
        grid[2][1] = -1;
        grid[1][2] = -1;
        PlayerMinor.stepDesc.toRec = true;
        myPlayerState = new PlayerState(3, 3, 0);
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        initCs(0, 2, 0, 2, grid);
        assertEquals(10, PlayerMinor.computePoints(gameState, PlayerMinor.stepDesc, 0, 2, 0, 2));
    }

    @Test
    public void testMoveTowardPoint() {
        assertEquals(false, PlayerMinor.moveTowardPoint(0, 0, 0, 0));
        assertEquals(true, PlayerMinor.moveTowardPoint(0, 0, 1, 0));
        assertEquals(1, PlayerMinor.moveI);
        assertEquals(0, PlayerMinor.moveH);
        assertEquals(true, PlayerMinor.moveTowardPoint(0, 0, 0, 1));
        assertEquals(0, PlayerMinor.moveI);
        assertEquals(1, PlayerMinor.moveH);
        assertEquals(true, PlayerMinor.moveTowardPoint(1, 1, 0, 1));
        assertEquals(0, PlayerMinor.moveI);
        assertEquals(1, PlayerMinor.moveH);
        assertEquals(true, PlayerMinor.moveTowardPoint(1, 1, 1, 0));
        assertEquals(1, PlayerMinor.moveI);
        assertEquals(0, PlayerMinor.moveH);
    }

    @Test
    public void testMoveTowardRec() {
        assertEquals(false, PlayerMinor.moveTowardRec(0, 2, 0, 2, 0, 0));
        assertEquals(false, PlayerMinor.moveTowardRec(0, 2, 0, 2, 0, 1));
        assertEquals(false, PlayerMinor.moveTowardRec(0, 2, 0, 2, 2, 2));
        assertEquals(false, PlayerMinor.moveTowardRec(0, 2, 0, 2, 1, 0));
        assertEquals(true, PlayerMinor.moveTowardRec(0, 2, 0, 2, 2, 3));
        assertEquals(2, PlayerMinor.moveI);
        assertEquals(2, PlayerMinor.moveH);
        assertEquals(true, PlayerMinor.moveTowardRec(0, 2, 0, 2, 3, 2));
        assertEquals(2, PlayerMinor.moveI);
        assertEquals(2, PlayerMinor.moveH);
        assertEquals(true, PlayerMinor.moveTowardRec(0, 6, 0, 6, 2, 1));
        assertEquals(2, PlayerMinor.moveI);
        assertEquals(0, PlayerMinor.moveH);
        assertEquals(true, PlayerMinor.moveTowardRec(0, 6, 0, 6, 1, 4));
        assertEquals(0, PlayerMinor.moveI);
        assertEquals(4, PlayerMinor.moveH);
    }

    @Test
    public void testComputeHazzard() {
        PlayerState myPlayerState = new PlayerState(3, 3, 0);
        int[][] grid = emptyGrid();
        GameState gameState = new GameState(0,
                1,
                Arrays.asList(myPlayerState, new PlayerState(4, 4, 0), new PlayerState(1, 1, 0)),
                grid);
        int[] z = PlayerMinor.computeHazzard(gameState, 0, 2, 0, 2);
        assertEquals(4, z[0]);
        assertEquals(1, z[1]);
    }

    @Test
    public void testIsValidRectangle() {
        PlayerState myPlayerState = new PlayerState(3, 3, 0);
        int[][] grid = emptyGrid();
        GameState gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        initCs(0, 2, 0, 2, grid);
        assertTrue(PlayerMinor.isValidRectangle(gameState, 0, 2, 0, 2));
        grid[0][0] = 1;
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        initCs(0, 2, 0, 2, grid);
        assertFalse(PlayerMinor.isValidRectangle(gameState, 0, 2, 0, 2));
        grid[0][0] = 0;
        grid[0][1] = 0;
        grid[1][0] = 0;
        grid[1][1] = 0;
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        initCs(0, 1, 0, 1, grid);
        assertFalse(PlayerMinor.isValidRectangle(gameState, 0, 1, 0, 1));
        grid = emptyGrid();
        grid[1][1] = 1;
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        initCs(0, 2, 0, 2, grid);
        assertFalse(PlayerMinor.isValidRectangle(gameState, 0, 2, 0, 2));
        grid = emptyGrid();
        for (int i = 18; i <= 19; i++) {
            for (int h = 21; h <= 25; h++) {
                grid[i][h] = 0;
            }
        }
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        initCs(18, 19, 21, 25, grid);
        assertFalse(PlayerMinor.isValidRectangle(gameState, 18, 19, 21, 25));
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
            Turn turn = PlayerMinor.makeTurn(gameState);
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
        double val1 = PlayerMinor.computeValue(gameState, 0, 1, 0, 1);
        initCs(2, 3, 1, 2, grid);
        double val2 = PlayerMinor.computeValue(gameState, 2, 3, 1, 2);
        double maxVal1 = PlayerMinor.maxValueFor(gameState, 0, 1, 0, 1);
        assertTrue(maxVal1 > val2);
        assertTrue(val1 > val2);
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

    private int[][] fullGrid() {
        int[][] grid = new int[PlayerMinor.N][PlayerMinor.M];
        for (int i = 0; i < PlayerMinor.N; i++) {
            for (int h = 0; h < PlayerMinor.M; h++) {
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
                cempty += grid[i][h] == PlayerMinor.EMPTY ? 1 : 0;
                cmy += grid[i][h] == PlayerMinor.MY ? 1 : 0;
            }
        }
        PlayerMinor.CEMPTY = cempty;
        PlayerMinor.CMY = cmy;
    }

    @Test
    public void testFutureMixin() {
        Future future = new Future();

        String map1 =
                "00000000000011100000000000000000000" +
                "0....1.....011100011110000000000000" +
                "0..00000000011100001110000000000000" +
                "00.01111111111100001100000000000000" +
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

        String map2 =
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

        GameState gameState = new GameState(10,
                2,
                Arrays.asList(new PlayerState(3, 1, 1), new PlayerState(1, 5, 0)),
                stringToGrid(map1));

        future.update(gameState);

        gameState = new GameState(9,
                2,
                Arrays.asList(new PlayerState(3, 1, 1), new PlayerState(1, 5, 0)),
                stringToGrid(map2));
        future.update(gameState);
        GameState mixed = future.futureMixin(gameState);
        assertEquals(PlayerMinor.EMPTY, mixed.grid[3][1]);
        assertEquals(1, mixed.grid[1][5]);
    }

    @Test
    public void testTriggerBack() {
        Future future = new Future();
        String fakeMap = "00000000000011100000000000000000000" +
                "0..........011100011110000000000000" +
                "0..00000000011100001110000000000000" +
                "00.01111111111100001100000000000000" +
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
        GameState gameState = new GameState(10,
                2,
                Arrays.asList(new PlayerState(3, 1, 1), new PlayerState(1, 5, 0)),
                stringToGrid(fakeMap));

        future.update(gameState);
        assertEquals(0, future.addNewExpectedAndReportBack(100.0));

        for (int i = 11; i <= 14; i++) {
            gameState = new GameState(i,
                    2,
                    Arrays.asList(new PlayerState(3, 1, 1), new PlayerState(1, 5, 0)),
                    stringToGrid(fakeMap));


            future.update(gameState);
            assertEquals(0, future.addNewExpectedAndReportBack(18.0 - i));
        }

        gameState = new GameState(15,
                2,
                Arrays.asList(new PlayerState(3, 1, 1), new PlayerState(1, 5, 0)),
                stringToGrid(fakeMap));


        future.update(gameState);
        assertEquals(25, future.addNewExpectedAndReportBack(18.0));
    }
}