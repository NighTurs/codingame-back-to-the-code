import game.Player;
import org.junit.Ignore;
import org.junit.Test;
import game.Player.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

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