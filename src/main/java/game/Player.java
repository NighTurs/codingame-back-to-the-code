package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class Player {

    public static final int N = 20;
    public static final int M = 35;
    public static final int EMPTY = -1;
    public static final int MY = 0;

    public static void main(String args[]) {
        InputReader in = new InputReader(System.in);
        int opponentCount = in.nextInt();

        //noinspection InfiniteLoopStatement
        while (true) {
            int gameRound = in.nextInt();
            int x = in.nextInt();
            int y = in.nextInt();
            int backInTimeLeft = in.nextInt();

            List<PlayerState> states = new ArrayList<PlayerState>();
            states.add(new PlayerState(y, x, backInTimeLeft));
            for (int i = 0; i < opponentCount; i++) {
                int xx = in.nextInt();
                int yy = in.nextInt();
                states.add(new PlayerState(yy, xx, in.nextInt()));
            }

            int[][] grid = new int[N][M];
            for (int i = 0; i < N; i++) {
                String str = in.next();
                for (int h = 0; h < M; h++) {
                    if (str.charAt(h) == '.') {
                        grid[i][h] = EMPTY;
                    } else {
                        grid[i][h] = Integer.valueOf(Character.toString(str.charAt(h)));
                    }
                }
            }

            Turn turn = makeTurn(new GameState(gameRound, opponentCount, states, grid));

            System.out.println(turn.toString());
        }
    }

    public static Turn makeTurn(GameState gameState) {
        double maxValue = Double.MIN_VALUE;
        StepDesc bestStep = new StepDesc();
        for (int i1 = 0; i1 < N; i1++) {
            for (int h1 = 0; h1 < M; h1++) {
                for (int i2 = N - 1; i2 >= i1 + 1; i2--) {
                    for (int h2 = M - 1; h2 >= h1 + 1; h2--) {
                        if (maxValueFor(gameState, i1, i2, h1, h2) > maxValue) {
                            double curValue = computeValue(gameState, i1, i2, h1, h2);
                            if (curValue > maxValue) {
                                maxValue = curValue;
                                bestStep.pointH = stepDesc.pointH;
                                bestStep.pointI = stepDesc.pointI;
                                bestStep.toH = stepDesc.toH;
                                bestStep.toI = stepDesc.toI;
                                bestStep.toRec = stepDesc.toRec;
                            }
                        }
                    }
                }
            }
        }
        return new Turn(bestStep.toH, bestStep.toI, 0);
    }



    public static double maxValueFor(GameState gameState, int i1, int i2, int h1, int h2) {
        int dist = distanceToRec(i1, i2, h1, h2, gameState.getMyPlayer().i, gameState.getMyPlayer().h);
        int x = dist;
        int y = (i2 - i1 + 1) * (h2 - h1 + 1) + (dist == 0 ? 0 : (dist - 1));
        int[] z = computeHazzard(gameState, i1, i2, h1, h2);
        return valueFor(gameState, x, y, z);
    }

    public static double computeValue(GameState gameState, int i1, int i2, int h1, int h2) {
        if (!isValidRectangle(gameState, i1, i2, h1, h2)) {
            return -1;
        }
        int x = workToComplete(gameState, i1, i2, h1, h2);
        int y = computePoints(gameState, stepDesc, i1, i2, h1, h2);
        int[] z = computeHazzard(gameState, i1, i2, h1, h2);
        return valueFor(gameState, x, y, z);
    }

    static final double k[][] = new double[][] {{0.6, 1.0, 0.35}, {0.6, 1.0, 0.2}, {0.8, 1.0, 0.15}};

    public static double valueFor(GameState gameState, int x, int y, int[] z) {
        double k1 = k[gameState.opponentCount - 1][0];
        double k2 = k[gameState.opponentCount - 1][1];
        double k3 = k[gameState.opponentCount - 1][2];
        double value = Math.pow(1.0 / (x == 0 ? 0.5 : x), k1) * Math.pow(y, k2);
        for (int i = 0; i < gameState.opponentCount; i++) {
            value *= Math.pow((z[i] == 0 ? 0.5 : z[i]), k3);
        }
        return value;
    }

    static int hazz[] = new int[5];

    public static int[] computeHazzard(GameState gameState, int i1, int i2, int h1, int h2) {
        for (int i = 1; i < gameState.players.size(); i++) {
            PlayerState state = gameState.players.get(i);
            int oppI = state.i;
            int oppH = state.h;
            hazz[i - 1] = distanceToRec(i1, i2, h1, h2, oppI, oppH);
        }
        return hazz;
    }

    public static int computePoints(GameState gameState, StepDesc stepDesc, int i1, int i2, int h1, int h2) {
        int[][] grid = gameState.grid;
        int points = gameState.fenwick.countEmpty(i1, i2, h1, h2);
        int curI = gameState.getMyPlayer().i;
        int curH = gameState.getMyPlayer().h;
        boolean once = false;
        if (stepDesc.toRec) {
            while (moveTowardRec(i1, i2, h1, h2, curI, curH)) {
                once = true;
                curI = moveI;
                curH = moveH;
                if (grid[curI][curH] == EMPTY) {
                    points++;
                }
            }
            if (grid[curI][curH] == EMPTY && once) {
                points--;
            }
        } else {
            while (moveTowardPoint(curI, curH, stepDesc.pointI, stepDesc.pointH)) {
                once = true;
                curI = moveI;
                curH = moveH;
                if (grid[curI][curH] == EMPTY) {
                    points++;
                }
            }
            if (grid[curI][curH] == EMPTY && once) {
                points--;
            }
        }
        return points;
    }

    public static StepDesc stepDesc = new StepDesc();

    public static int workToComplete(GameState gameState, int i1, int i2, int h1, int h2) {
        int[][] grid = gameState.grid;
        int curI = i1;
        int curH = h1;
        while (grid[curI][curH] == MY) {
            moveClockUnwise(i1, i2, h1, h2, curI, curH);
            curI = moveI;
            curH = moveH;
        }
        int curMyBlocks = 0;
        int curStartI = 0;
        int curStartH = 0;
        int bestStartI = 0;
        int bestStartH = 0;
        int bestEndI = 0;
        int bestEndH = 0;
        int maxMyBlocks = 0;
        int startedI = curI;
        int startedH = curH;
        do {
            if (grid[curI][curH] == MY) {
                curMyBlocks++;
            } else {
                curMyBlocks = 0;
                curStartI = curI;
                curStartH = curH;
            }
            if (curMyBlocks > maxMyBlocks) {
                maxMyBlocks = curMyBlocks;
                bestStartI = curStartI;
                bestStartH = curStartH;
                moveClockWise(i1, i2, h1, h2, curI, curH);
                bestEndI = moveI;
                bestEndH = moveH;
            }
            moveClockWise(i1, i2, h1, h2, curI, curH);
            curI = moveI;
            curH = moveH;
        } while (!(startedI == curI && startedH == curH));

        int myCurI = gameState.getMyPlayer().i;
        int myCurH = gameState.getMyPlayer().h;
        if (maxMyBlocks == 0) {
            stepDesc.toRec = true;
            moveTowardRec(i1, i2, h1, h2, myCurI, myCurH);
            stepDesc.toI = moveI;
            stepDesc.toH = moveH;
            return 2 * (i2 - i1) + 2 * (h2 - h1) + distanceToRec(i1, i2, h1, h2, myCurI, myCurH) - 1;
        } else {
            int distToStart = distanceToPoint(myCurI, myCurH, bestStartI, bestStartH);
            int distToEnd = distanceToPoint(myCurI, myCurH, bestEndI, bestEndH);
            stepDesc.toRec = false;
            if (distToStart == 0 || distToEnd == 0) {
                if (myCurI == bestStartI && myCurH == bestStartH) {
                    moveClockUnwise(i1, i2, h1, h2, myCurI, myCurH);
                    stepDesc.pointI = bestStartI;
                    stepDesc.pointH = bestStartH;
                } else {
                    moveClockWise(i1, i2, h1, h2, myCurI, myCurH);
                    stepDesc.pointI = bestEndI;
                    stepDesc.pointH = bestEndH;
                }
                stepDesc.toI = moveI;
                stepDesc.toH = moveH;
            } else if (distToStart < distToEnd) {
                stepDesc.pointI = bestStartI;
                stepDesc.pointH = bestStartH;
                moveTowardPoint(myCurI, myCurH, bestStartI, bestStartH);
                stepDesc.toI = moveI;
                stepDesc.toH = moveH;
            } else {
                stepDesc.pointI = bestEndI;
                stepDesc.pointH = bestEndH;
                moveTowardPoint(myCurI, myCurH, bestEndI, bestEndH);
                stepDesc.toI = moveI;
                stepDesc.toH = moveH;
            }
            return 2 * (i2 - i1) + 2 * (h2 - h1) - maxMyBlocks +
                    Math.min(distanceToPoint(myCurI, myCurH, bestStartI, bestStartH),
                            distanceToPoint(myCurI, myCurH, bestEndI, bestEndH)) - 1;
        }
    }

    public static int distanceToRec(int i1, int i2, int h1, int h2, int curI, int curH) {
        int min = Integer.MAX_VALUE;

        if (curI >= i1 && curI <= i2) {
            min = Math.min(Math.abs(curH - h1), Math.abs(curH - h2));
        }

        int add = Math.min(Math.abs(curI - i1), Math.abs(curI - i2));

        if (curH >= h1 && curH <= h2) {
            min = Math.min(min, add);
        } else {
            min = Math.min(min, add + Math.min(Math.abs(curH - h1), Math.abs(curH - h2)));
        }
        return min;
    }

    public static int distanceToPoint(int curI, int curH, int toI, int toH) {
        return Math.abs(curI - toI) + Math.abs(curH - toH);
    }

    // return of move methods
    public static int moveI = 0;
    public static int moveH = 0;

    public static boolean moveTowardRec(int i1, int i2, int h1, int h2, int curI, int curH) {
        int toH = Math.abs(h1 - curH) > Math.abs(h2 - curH) ? h2 : h1;
        int toI = Math.abs(i1 - curI) > Math.abs(i2 - curI) ? i2 : i1;

        int min = Integer.MAX_VALUE;
        boolean result = false;
        if (curI >= i1 && curI <= i2) {
            min = Math.min(Math.abs(curH - h1), Math.abs(curH - h2));
            result = moveTowardPoint(curI, curH, curI, toH);
        }

        if (curH >= h1 && curH <= h2) {
            int cost = Math.min(Math.abs(curI - i1), Math.abs(curI - i2));
            if (min > cost) {
                result = moveTowardPoint(curI, curH, toI, curH);
            }
        }

        if (!((curI >= i1 && curI <= i2) || (curH >= h1 && curH <= h2))) {
            result = moveTowardPoint(curI, curH, toI, toH);
        }
        return result;
    }

    public static boolean moveTowardPoint(int curI, int curH, int toI, int toH) {
        if (curI < toI) {
            moveDown(curI, curH);
            return true;
        }
        if (curI > toI) {
            moveUp(curI, curH);
            return true;
        }
        if (curH < toH) {
            moveRight(curI, curH);
            return true;
        }
        if (curH > toH) {
            moveLeft(curI, curH);
            return true;
        }
        return false;
    }

    public static void moveClockWise(int i1, int i2, int h1, int h2, int curI, int curH) {
        if (curI == i1) {
            if (curH == h2) {
                moveDown(curI, curH);
                return;
            }
            moveRight(curI, curH);
            return;
        }
        if (curI == i2) {
            if (curH == h1) {
                moveUp(curI, curH);
                return;
            }
            moveLeft(curI, curH);
            return;
        }
        if (curH == h1) {
            moveUp(curI, curH);
            return;
        }
        if (curH == h2) {
            moveDown(curI, curH);
        }
    }

    public static void moveClockUnwise(int i1, int i2, int h1, int h2, int curI, int curH) {
        if (curI == i1) {
            if (curH == h1) {
                moveDown(curI, curH);
                return;
            }
            moveLeft(curI, curH);
            return;
        }
        if (curI == i2) {
            if (curH == h2) {
                moveUp(curI, curH);
                return;
            }
            moveRight(curI, curH);
            return;
        }
        if (curH == h1) {
            moveDown(curI, curH);
            return;
        }
        if (curH == h2) {
            moveUp(curI, curH);
        }
    }

    public static void moveDown(int x, int y) {
        moveI = x + 1;
        moveH = y;
    }

    public static void moveUp(int x, int y) {
        moveI = x - 1;
        moveH = y;
    }

    public static void moveLeft(int x, int y) {
        moveI = x;
        moveH = y - 1;
    }

    public static void moveRight(int x, int y) {
        moveI = x;
        moveH = y + 1;
    }

    // O(N * M)
    public static boolean isValidRectangle(GameState gameState, int i1, int i2, int h1, int h2) {
        int countEmpty = gameState.fenwick.countEmpty(i1, i2, h1, h2);
        int countMy = gameState.fenwick.countMy(i1, i2, h1, h2);

        if ((i2 - i1 + 1) * (h2 - h1 + 1) > countEmpty + countMy || countEmpty == 0) {
            return false;
        }
        return true;
    }

    public static void debug(String msg) {
        System.err.println(msg);
    }

    public static class Fenwick {

        int[][] grid = new int[N][M];
        int[][] empty = new int[N][M];
        int[][] myPlayer = new int[N][M];

        public Fenwick(int[][] grid) {
            this.grid = grid;
            initEmpty(grid);
            initMy(grid);
        }

        public int countEmpty(int i1, int i2, int h1, int h2) {
            if ((i2 - i1 + 1) * (h2 - h1 + 1) < 8) {
                int ct = 0;
                for (int i = i1; i <= i2; ++i) {
                    for (int h = h1; h <= h2; ++h) {
                        ct += grid[i][h] == EMPTY ? 1 : 0;
                    }
                }
                return ct;
            }
            return count(i1, i2, h1, h2, empty);
        }

        public int countMy(int i1, int i2, int h1, int h2) {
            if ((i2 - i1 + 1) * (h2 - h1 + 1) < 8) {
                int ct = 0;
                for (int i = i1; i <= i2; ++i) {
                    for (int h = h1; h <= h2; ++h) {
                        ct += grid[i][h] == MY ? 1 : 0;
                    }
                }
                return ct;
            }
            return count(i1, i2, h1, h2, myPlayer);
        }

        private void initMy(int[][] grid) {
            for (int[] row: myPlayer)
                Arrays.fill(row, 0);
            for (int i = 0; i < N; i++) {
                for (int h = 0; h < M; h++) {
                    if (grid[i][h] == MY) {
                        inc(i, h, 1, myPlayer);
                    }
                }
            }
        }

        private void initEmpty(int[][] grid) {
            for (int[] row: empty)
                Arrays.fill(row, 0);
            for (int i = 0; i < N; i++) {
                for (int h = 0; h < M; h++) {
                    if (grid[i][h] == EMPTY) {
                        inc(i, h, 1, empty);
                    }
                }
            }
        }

        private int count(int i1, int i2, int h1, int h2, int[][] t) {
            return count(i2, h2, t) - count(i1 - 1, h2, t) - count(i2, h1 - 1, t) + count(i1 - 1, h1 - 1, t);
        }

        private int count(int ii, int hh, int[][] t) {
            if (ii < 0 || hh < 0) {
                return 0;
            }
            int result = 0;
            for (int i = ii; i >= 0; i = (i & (i + 1)) - 1) {
                for (int j = hh; j >= 0; j = (j & (j + 1)) - 1) {
                    result += t[i][j];
                }
            }
            return result;
        }

        private void inc(int ii, int hh, int delta, int[][] t) {
            for (int i = ii; i < N; i = (i | (i + 1)))
                for (int j = hh; j < M; j = (j | (j + 1)))
                    t[i][j] += delta;
        }
    }

    public static class StepDesc {

        public int toI;
        public int toH;
        public boolean toRec;
        public int pointI;
        public int pointH;
    }

    public static class GameState {

        final Fenwick fenwick;
        final int gameRound;
        final int opponentCount;
        final List<PlayerState> players;
        final int[][] grid;

        public GameState(int gameRound, int opponentCount, List<PlayerState> players, int[][] grid) {
            this.gameRound = gameRound;
            this.opponentCount = opponentCount;
            this.players = players;
            this.grid = grid;
            this.fenwick = new Fenwick(grid);
        }

        public PlayerState getMyPlayer() {
            return players.get(0);
        }
    }

    public static class Turn {

        public final int x, y;
        public final int goBack;
        public final String message;

        public Turn(int x, int y, int goBack) {
            this(x, y, goBack, null);
        }

        public Turn(int x, int y, int goBack, String message) {
            this.x = x;
            this.y = y;
            this.goBack = goBack;
            this.message = message;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(x).append(" ").append(y);
            if (goBack > 0) {
                sb.append("\rBACK ").append(goBack);
            }
            if (message != null) {
                sb.append("\r").append(message);
            }
            return sb.toString();
        }
    }

    public static class PlayerState {

        final int i, h;
        final int backInTimeLeft;

        public PlayerState(int i, int h, int backInTimeLeft) {
            this.i = i;
            this.h = h;
            this.backInTimeLeft = backInTimeLeft;
        }
    }

    static class InputReader {

        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }
}
