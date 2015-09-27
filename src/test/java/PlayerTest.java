import game.Player;
import org.junit.Ignore;
import org.junit.Test;
import game.Player.*;

import java.util.*;

import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void testTurnToString() {
        assertEquals("1 2", new Turn(1, 2, 0).toString());
        assertEquals("1 2\rBACK 1", new Turn(1, 2, 1).toString());
        assertEquals("1 2\rBACK 1\rmsg", new Turn(1, 2, 1, "msg").toString());
        assertEquals("1 2\rmsg", new Turn(1, 2, 0, "msg").toString());
    }

    @Test
    public void testMoveClockWise() {
        Player.moveClockWise(0, 2, 0, 2, 0, 0);
        assertEquals(0, Player.moveI);
        assertEquals(1, Player.moveH);
        Player.moveClockWise(0, 2, 0, 2, 0, 1);
        assertEquals(0, Player.moveI);
        assertEquals(2, Player.moveH);
        Player.moveClockWise(0, 2, 0, 2, 0, 2);
        assertEquals(1, Player.moveI);
        assertEquals(2, Player.moveH);
        Player.moveClockWise(0, 2, 0, 2, 1, 2);
        assertEquals(2, Player.moveI);
        assertEquals(2, Player.moveH);
        Player.moveClockWise(0, 2, 0, 2, 2, 2);
        assertEquals(2, Player.moveI);
        assertEquals(1, Player.moveH);
        Player.moveClockWise(0, 2, 0, 2, 2, 1);
        assertEquals(2, Player.moveI);
        assertEquals(0, Player.moveH);
        Player.moveClockWise(0, 2, 0, 2, 2, 0);
        assertEquals(1, Player.moveI);
        assertEquals(0, Player.moveH);
        Player.moveClockWise(0, 2, 0, 2, 1, 0);
        assertEquals(0, Player.moveI);
        assertEquals(0, Player.moveH);
    }

    @Test
    public void testMoveClockUnwise() {
        Player.moveClockUnwise(0, 2, 0, 2, 0, 0);
        assertEquals(1, Player.moveI);
        assertEquals(0, Player.moveH);
        Player.moveClockUnwise(0, 2, 0, 2, 1, 0);
        assertEquals(2, Player.moveI);
        assertEquals(0, Player.moveH);
        Player.moveClockUnwise(0, 2, 0, 2, 2, 0);
        assertEquals(2, Player.moveI);
        assertEquals(1, Player.moveH);
        Player.moveClockUnwise(0, 2, 0, 2, 2, 1);
        assertEquals(2, Player.moveI);
        assertEquals(2, Player.moveH);
        Player.moveClockUnwise(0, 2, 0, 2, 2, 2);
        assertEquals(1, Player.moveI);
        assertEquals(2, Player.moveH);
        Player.moveClockUnwise(0, 2, 0, 2, 1, 2);
        assertEquals(0, Player.moveI);
        assertEquals(2, Player.moveH);
        Player.moveClockUnwise(0, 2, 0, 2, 0, 2);
        assertEquals(0, Player.moveI);
        assertEquals(1, Player.moveH);
        Player.moveClockUnwise(0, 2, 0, 2, 0, 1);
        assertEquals(0, Player.moveI);
        assertEquals(0, Player.moveH);
    }

    @Test
    public void testDistanceToPoint() {
        assertEquals(0, Player.distanceToPoint(0, 0, 0, 0));
        assertEquals(1, Player.distanceToPoint(0, 0, 0, 1));
        assertEquals(1, Player.distanceToPoint(0, 0, 1, 0));
        assertEquals(2, Player.distanceToPoint(0, 0, 1, 1));
    }

    @Test
    public void testDistanceToRec() {
        assertEquals(0, Player.distanceToRec(0, 2, 0, 2, 0, 0));
        assertEquals(0, Player.distanceToRec(0, 2, 0, 2, 0, 1));
        assertEquals(0, Player.distanceToRec(0, 2, 0, 2, 2, 2));
        assertEquals(1, Player.distanceToRec(0, 2, 0, 2, 1, 1));
        assertEquals(1, Player.distanceToRec(0, 2, 0, 2, 3, 2));
        assertEquals(1, Player.distanceToRec(0, 2, 0, 2, 2, 3));
        assertEquals(2, Player.distanceToRec(0, 2, 0, 2, 3, 3));
        assertEquals(3, Player.distanceToRec(0, 2, 0, 2, 3, 4));
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
        assertEquals(7, Player.workToComplete(gameState, 0, 2, 0, 2));
        assertEquals(false, Player.stepDesc.toRec);
        assertTrue((Player.stepDesc.toI == 2 && Player.stepDesc.toH == 3) ||
                (Player.stepDesc.toI == 3 && Player.stepDesc.toH == 2));
        assertEquals(2, Player.stepDesc.pointI);
        assertEquals(0, Player.stepDesc.pointH);

        grid[2][2] = -1;
        grid[2][1] = -1;
        grid[1][2] = -1;
        grid[0][2] = -1;
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertEquals(9, Player.workToComplete(gameState, 0, 2, 0, 2));
        assertEquals(true, Player.stepDesc.toRec);
        assertTrue((Player.stepDesc.toI == 2 && Player.stepDesc.toH == 3) ||
                (Player.stepDesc.toI == 3 && Player.stepDesc.toH == 2));

        grid[0][0] = 0;
        grid[2][2] = 0;
        grid[2][1] = 0;
        grid[1][2] = 0;
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertEquals(8, Player.workToComplete(gameState, 0, 2, 0, 2));

        grid[0][0] = 0;
        grid[2][2] = 0;
        grid[2][1] = 0;
        grid[1][2] = 0;
        grid[0][2] = 0;
        myPlayerState = new PlayerState(2, 2, 0);
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertEquals(5, Player.workToComplete(gameState, 0, 2, 0, 2));
        assertEquals(false, Player.stepDesc.toRec);
        assertEquals(2, Player.stepDesc.toI);
        assertEquals(1, Player.stepDesc.toH);
        assertEquals(2, Player.stepDesc.pointI);
        assertEquals(0, Player.stepDesc.pointH);

        grid[0][0] = -1;
        grid[2][2] = 0;
        grid[2][1] = 0;
        grid[1][2] = 0;
        grid[0][2] = 0;
        grid[0][1] = 0;
        myPlayerState = new PlayerState(0, 1, 0);
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertEquals(3, Player.workToComplete(gameState, 0, 2, 0, 2));
        assertEquals(false, Player.stepDesc.toRec);
        assertEquals(0, Player.stepDesc.toI);
        assertEquals(0, Player.stepDesc.toH);

        grid[0][0] = -1;
        grid[2][2] = 0;
        grid[2][1] = 0;
        grid[1][2] = 0;
        grid[0][2] = 0;
        grid[0][1] = -1;
        grid[2][0] = 0;
        myPlayerState = new PlayerState(2, 0, 0);
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertEquals(3, Player.workToComplete(gameState, 0, 2, 0, 2));
        assertEquals(false, Player.stepDesc.toRec);
        assertEquals(1, Player.stepDesc.toI);
        assertEquals(0, Player.stepDesc.toH);

        grid = emptyGrid();
        grid[2][2] = 0;
        grid[2][3] = 0;
        myPlayerState = new PlayerState(2, 2, 0);
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertEquals(2, Player.workToComplete(gameState, 1, 2, 2, 3));

        grid = emptyGrid();
        grid[2][2] = 0;
        grid[2][3] = 0;
        grid[3][3] = 0;
        myPlayerState = new PlayerState(2, 2, 0);
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertEquals(3, Player.workToComplete(gameState, 2, 3, 3, 4));
    }

    @Test
    public void testComputePoints() {
        PlayerState myPlayerState = new PlayerState(3, 3, 0);
        int[][] grid = emptyGrid();
        grid[2][2] = 0;
        grid[2][1] = 0;
        grid[1][2] = 0;
        GameState gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        Player.stepDesc.toRec = false;
        Player.stepDesc.pointI = 2;
        Player.stepDesc.pointH = 0;
        // 9 possible
        assertEquals(7, Player.computePoints(gameState, Player.stepDesc, 0, 2, 0, 2));

        myPlayerState = new PlayerState(2, 2, 0);
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertEquals(6, Player.computePoints(gameState, Player.stepDesc, 0, 2, 0, 2));

        grid[2][2] = -1;
        grid[2][1] = -1;
        grid[1][2] = -1;
        Player.stepDesc.toRec = true;
        myPlayerState = new PlayerState(3, 3, 0);
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertEquals(10, Player.computePoints(gameState, Player.stepDesc, 0, 2, 0, 2));
    }

    @Test
    public void testMoveTowardPoint() {
        assertEquals(false, Player.moveTowardPoint(0, 0, 0, 0));
        assertEquals(true, Player.moveTowardPoint(0, 0, 1, 0));
        assertEquals(1, Player.moveI);
        assertEquals(0, Player.moveH);
        assertEquals(true, Player.moveTowardPoint(0, 0, 0, 1));
        assertEquals(0, Player.moveI);
        assertEquals(1, Player.moveH);
        assertEquals(true, Player.moveTowardPoint(1, 1, 0, 1));
        assertEquals(0, Player.moveI);
        assertEquals(1, Player.moveH);
        assertEquals(true, Player.moveTowardPoint(1, 1, 1, 0));
        assertEquals(1, Player.moveI);
        assertEquals(0, Player.moveH);
    }

    @Test
    public void testMoveTowardRec() {
        assertEquals(false, Player.moveTowardRec(0, 2, 0, 2, 0, 0));
        assertEquals(false, Player.moveTowardRec(0, 2, 0, 2, 0, 1));
        assertEquals(false, Player.moveTowardRec(0, 2, 0, 2, 2, 2));
        assertEquals(false, Player.moveTowardRec(0, 2, 0, 2, 1, 0));
        assertEquals(true, Player.moveTowardRec(0, 2, 0, 2, 2, 3));
        assertEquals(2, Player.moveI);
        assertEquals(2, Player.moveH);
        assertEquals(true, Player.moveTowardRec(0, 2, 0, 2, 3, 2));
        assertEquals(2, Player.moveI);
        assertEquals(2, Player.moveH);
        assertEquals(true, Player.moveTowardRec(0, 6, 0, 6, 2, 1));
        assertEquals(2, Player.moveI);
        assertEquals(0, Player.moveH);
        assertEquals(true, Player.moveTowardRec(0, 6, 0, 6, 1, 4));
        assertEquals(0, Player.moveI);
        assertEquals(4, Player.moveH);
    }

    @Test
    public void testComputeHazzard() {
        PlayerState myPlayerState = new PlayerState(3, 3, 0);
        int[][] grid = emptyGrid();
        GameState gameState = new GameState(0,
                1,
                Arrays.asList(myPlayerState, new PlayerState(4, 4, 0), new PlayerState(1, 1, 0)),
                grid);
        int[] z = Player.computeHazzard(gameState, 0, 2, 0, 2);
        assertEquals(4, z[0]);
        assertEquals(1, z[1]);
    }

    @Test
    public void testIsValidRectangle() {
        PlayerState myPlayerState = new PlayerState(3, 3, 0);
        int[][] grid = emptyGrid();
        GameState gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertTrue(Player.isValidRectangle(gameState, 0, 2, 0, 2));
        grid[0][0] = 1;
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertFalse(Player.isValidRectangle(gameState, 0, 2, 0, 2));
        grid[0][0] = 0;
        grid[0][1] = 0;
        grid[1][0] = 0;
        grid[1][1] = 0;
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertFalse(Player.isValidRectangle(gameState, 0, 1, 0, 1));
        grid = emptyGrid();
        grid[1][1] = 1;
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertFalse(Player.isValidRectangle(gameState, 0, 2, 0, 2));
        grid = emptyGrid();
        for (int i = 18; i <= 19; i++) {
            for (int h = 21; h <= 25; h++) {
                grid[i][h] = 0;
            }
        }
        gameState = new GameState(0, 1, Collections.singletonList(myPlayerState), grid);
        assertFalse(Player.isValidRectangle(gameState, 18, 19, 21, 25));
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
            Turn turn = Player.makeTurn(gameState);
            long ed = System.currentTimeMillis();
            System.out.println((ed - st) + "ms " + turn.toString());
            myPlayerState = new PlayerState(turn.y, turn.x, 0);
            grid[turn.y][turn.x] = 0;
        }
    }

    @Test
    public void testFenwick() {
        int[][] grid = emptyGrid();
        grid[0][0] = 0;
        grid[2][2] = 0;
        grid[4][4] = 0;
        grid[5][8] = 0;
        grid[19][34] = 0;
        Fenwick f = new Fenwick(grid);
        assertEquals(2, f.countMy(0, 2, 0, 2));
        assertEquals(1, f.countMy(0, 0, 0, 0));
        assertEquals(5, f.countMy(0, 19, 0, 34));
        assertEquals(2, f.countMy(3, 8, 3, 8));
        assertEquals(0, f.countMy(1, 1, 1, 1));
        assertEquals(7, f.countEmpty(0, 2, 0, 2));
        assertEquals(1, f.countEmpty(1, 1, 1, 1));
        assertEquals(700 - 5, f.countEmpty(0, 19, 0, 34));
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
        double val1 = Player.computeValue(gameState, 0, 1, 0, 1);
        double val2 = Player.computeValue(gameState, 2, 3, 1, 2);
        double maxVal1 = Player.maxValueFor(gameState, 0, 1, 0, 1);
        assertTrue(maxVal1 > val2);
        assertTrue(val1 > val2);
    }

    @Ignore
    @Test
    public void testCrash() {
        int Np = 18;
        double profiles[][] = {
                {0.5, 1.0, 0.0},
                {0.1, 1.0, 0.0},
                {1.0, 1.0, 0.0},
                {0.5, 1.5, 0.0},
                {0.1, 1.5, 0.0},
                {1.0, 1.5, 0.0},
                {0.5, 1.0, 0.3},
                {0.1, 1.0, 0.3},
                {1.0, 1.0, 0.3},
                {0.5, 1.5, 0.3},
                {0.1, 1.5, 0.3},
                {1.0, 1.5, 0.3},
                {0.5, 1.0, 0.6},
                {0.1, 1.0, 0.6},
                {1.0, 1.0, 0.6},
                {0.5, 1.5, 0.6},
                {0.1, 1.5, 0.6},
                {1.0, 1.5, 0.6}
        };
        int[][] scores = new int[Np][4];

        Random rand = new Random();
        while(true) {
            System.out.println("NEW GAME");
            int[] profs = new int[4];
            PlayerState[] states = new PlayerState[4];
            int[][] grid = emptyGrid();
            for (int i = 0; i < 4; i++) {
                profs[i] = Math.abs(rand.nextInt()) % Np;
                states[i] = new PlayerState(Math.abs(rand.nextInt()) % Player.N, Math.abs(rand.nextInt()) % Player.M, 0);
                System.out.println("Player : " + states[i].i + " " + states[i].h);
                grid[states[i].i][states[i].h] = i;
            }
            for (int i = 0; i < 200 * 4; i++) {
                long st = System.currentTimeMillis();
                GameState gameState = new GameState(0, 1, Arrays.asList(states), grid);
//                System.out.println("Current profile " + profs[0]);
                Player.k[2][0] = profiles[profs[0]][0];
                Player.k[2][1] = profiles[profs[0]][1];
                Player.k[2][2] = profiles[profs[0]][2];
                Turn turn = Player.makeTurn(gameState);
                long ed = System.currentTimeMillis();

//                System.out.println("Player goes from (" + states[0].i + ", " + states[0].h + ") to (" + turn.y + ", " +
//                        turn.x + ") in " + (ed - st) + "ms");
                handle(grid);
                states[0] = new PlayerState(turn.y, turn.x, 0);
                grid[turn.y][turn.x] = 0;
                grid = shiftColors(grid, 4);

                PlayerState cur = states[3];
                int pr = profs[3];
                states[3] = states[0];
                profs[3] = profs[0];
                int pos = 3;
                while (pos != 0) {
                    pos--;
                    PlayerState z = states[pos];
                    int zp = profs[pos];
                    states[pos] = cur;
                    profs[pos] = pr;
                    cur = z;
                    pr = zp;
                }
            }
            int pos[] = winners(grid);
            System.out.println(pos[0] + " " + pos[1] + " " + pos[2] + " " + pos[3]);
            System.out.println(profs[0] + " " + profs[1] + " " + profs[2] + " " + profs[3]);
            for (int i = 0; i < 4; i++) {
                int ind = pos[i];
                scores[profs[ind]][i]++;
            }
            for (int i = 0; i < Player.N; i++) {
                for (int h = 0; h < Player.M; h++) {
                    if (grid[i][h] == -1) {
                        System.out.print('.');
                    } else {
                        System.out.print(grid[i][h]);
                    }
                }
                System.out.println();
            }
            for (int i = 0; i < Np; i++) {
                System.out.println("Profile x=" + profiles[i][0] + ", y=" + profiles[i][1] + "z=" + profiles[i][2] +
                        ", current scores " + scores[i][0] + ", " + scores[i][1] + ", " + scores[i][2] + ", " +
                        scores[i][3]);
            }
        }
    }

    private int[] winners(int[][] grid) {
        int[] res = new int[4];
        for (int i = 0; i < Player.N; i++) {
            for (int h = 0; h < Player.M; h++) {
                if (grid[i][h] != Player.EMPTY) {
                    res[grid[i][h]]++;
                }
            }
        }
        int[] pos = new int[4];
        for (int i = 0; i < 4; i++) {
            pos[i] = 0;
            for (int h = 0; h < 4; h++) {
                if (i != h && res[h] > res[i]) {
                    pos[i]++;
                }
            }
        }
        return pos;
    }

    boolean[][] c = new boolean[Player.N][Player.M];
    int[][] b = new int[1000][2];
    private int[][] handle(int[][] grid) {
        for (boolean[] row : c) {
            Arrays.fill(row, false);
        }
        Set<Integer> dye = new HashSet<Integer>();
        for (int i = 0; i < Player.N; i++) {
            for (int h = 0; h < Player.M; h++) {
                if (c[i][h] || grid[i][h] != Player.EMPTY) {
                    continue;
                }
                dye.clear();
                b[0][0] = i;
                b[0][1] = h;
                c[i][h] = true;
                int kn = 1;
                int t = 0;
                boolean foundBorder = false;
                while (t < kn) {
                    int t1 = b[t][0];
                    int t2 = b[t][1];
                    for (int j1 = -1; j1 <= 1; j1++) {
                        for (int j2 = -1; j2 <= 1; j2++) {
                            if (!(j1 == 0 ^ j2 == 0)) {
                                continue;
                            }
                            int to1 = t1 + j1;
                            int to2 = t2 + j2;
                            if (to1 >= 0 && to1 < Player.N && to2 >= 0 && to2 < Player.M && !c[to1][to2]) {
                                if (grid[to1][to2] != Player.EMPTY) {
                                    dye.add(grid[to1][to2]);
                                } else {
                                    if (to1 == 0 || to2 == 0 || to1 == Player.N - 1 || to2 == Player.M - 1) {
                                        foundBorder = true;
                                    } else {
                                        c[to1][to2] = true;
                                        b[kn][0] = to1;
                                        b[kn][1] = to2;
                                        kn++;
                                    }
                                }
                            }
                        }
                    }
                    t++;
                }
                if (!foundBorder && dye.size() == 1) {
                    int color = dye.iterator().next();
                    for (int j = 0; j < kn; j++) {
                        grid[b[j][0]][b[j][1]] = color;
                    }
                }
            }
        }
        return grid;
    }

    private int[][] shiftColors(int[][] grid, int nPlayers) {
        for (int i = 0; i < Player.N; i++) {
            for (int h = 0; h < Player.M; h++) {
                if (grid[i][h] == 0) {
                    grid[i][h] = nPlayers - 1;
                } else if (grid[i][h] != Player.EMPTY) {
                    grid[i][h] = grid[i][h] - 1;
                }
            }
        }
        return grid;
    }

    private int[][] emptyGrid() {
        int[][] grid = new int[Player.N][Player.M];
        for (int i = 0; i < Player.N; i++) {
            for (int h = 0; h < Player.M; h++) {
                grid[i][h] = -1;
            }
        }
        return grid;
    }
}