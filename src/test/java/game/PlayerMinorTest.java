package game;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

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
    }

    @Test
    public void testRelatedIslands() {
        cleanIslandsGrapth();
        setIslandGraphEdge(0, 1, 5);
        setIslandGraphEdge(0, 2, 5);
        assertEquals(2, PlayerMinor.relatedIslands(0, 5, 0));
        assertEquals(10, PlayerMinor.islandCons);
        assertEquals(0, PlayerMinor.islandGroup[0]);
        assertEquals(1, PlayerMinor.islandGroup[1]);
        assertEquals(2, PlayerMinor.islandGroup[2]);

        cleanIslandsGrapth();
        setIslandGraphEdge(0, 1, 1);
        setIslandGraphEdge(1, 2, 5);
        setIslandGraphEdge(1, 3, 5);
        setIslandGraphEdge(3, 4, 5);
        assertEquals(4, PlayerMinor.relatedIslands(0, 5, 0));
        assertEquals(16, PlayerMinor.islandCons);
        assertEquals(0, PlayerMinor.islandGroup[0]);
        assertEquals(1, PlayerMinor.islandGroup[1]);
        assertEquals(2, PlayerMinor.islandGroup[2]);
        assertEquals(3, PlayerMinor.islandGroup[3]);
        assertEquals(4, PlayerMinor.islandGroup[4]);
    }

    @Test
    public void testIslandsForLine() {
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
        int x = 5;
        int y = 14;
        int[] z = new int[]{2};
        assertEquals(PlayerMinor.valueFor(fakeGameStateWithOponents(1), x, y, z), testIslandsForLine(map, 3, -1), 1.e-9);
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
        x = 11;
        y = 30;
        z = new int[]{2};
        assertEquals(PlayerMinor.valueFor(fakeGameStateWithOponents(1), x, y, z), testIslandsForLine(map, 3, -1), 1.e-9);
    }

    private PlayerMinor.GameState fakeGameStateWithOponents(int ops) {
        return new PlayerMinor.GameState(1, 1, null, null);
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
}