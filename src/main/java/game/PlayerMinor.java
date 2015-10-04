package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class PlayerMinor {

    public static final int N = 20;
    public static final int M = 35;
    public static final int EMPTY = -1;
    public static final int MY = 0;
    public static Future future = new Future();

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


    public static int CEMPTY;
    public static int CMY;
    public static Turn makeTurn(GameState gameState) {

        double maxValue = Double.MIN_VALUE;
        StepDesc bestStep = new StepDesc();

        long st = System.currentTimeMillis();

        int[] cempty = new int[M];
        int[] cmy = new int[M];
        CEMPTY = 0;
        CMY = 0;

        future.update(gameState);
        GameState mixedGameState = future.futureMixin(gameState);
        int[][] grid = mixedGameState.grid;

        lb:
        for (int i1 = 0; i1 < N; i1++) {
            for (int h = 0; h < M; h++) {
                cempty[h] = grid[i1][h] == EMPTY ? 1 : 0;
                cmy[h] = grid[i1][h] == MY ? 1 : 0;
            }
            for (int i2 = i1 + 1; i2 < N; i2++) {
                for (int h = 0; h < M; h++) {
                    cempty[h] += grid[i2][h] == EMPTY ? 1 : 0;
                    cmy[h] += grid[i2][h] == MY ? 1 : 0;
                }
                for (int h1 = 0; h1 < M; h1++) {
                    long ed = System.currentTimeMillis();
                    if (ed - st > 95) {
                        break lb;
                    }
                    CEMPTY = cempty[h1];
                    CMY = cmy[h1];
                    for (int h2 = h1 + 1; h2 < M; h2++) {
                        CEMPTY += cempty[h2];
                        CMY += cmy[h2];
                        if (maxValueFor(mixedGameState, i1, i2, h1, h2) > maxValue) {
                            double curValue = computeValue(mixedGameState, i1, i2, h1, h2);
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

        double val = linesMethod(mixedGameState);
        if (maxValue < val) {
            maxValue = val;
            bestStep.toI = moveI;
            bestStep.toH = moveH;
        }

        if (runForestRun(maxValue, mixedGameState)) {
            bestStep.toI = moveI;
            bestStep.toH = moveH;
        }

        int back = future.addNewExpectedAndReportBack(maxValue);
        if (back > 0 && gameState.getMyPlayer().backInTimeLeft > 0) {
            return new Turn(bestStep.toH, bestStep.toI, back);
        }

        return new Turn(bestStep.toH, bestStep.toI, 0);
    }



    public static double maxValueFor(GameState gameState, int i1, int i2, int h1, int h2) {
        int dist = distanceToRec(i1, i2, h1, h2, gameState.getMyPlayer().i, gameState.getMyPlayer().h);
        int x = dist;
        int y = CEMPTY + (dist == 0 ? 0 : (dist - 1));
        int[] z = computeHazzard(gameState, i1, i2, h1, h2);
        return valueFor(gameState, x, y, z, gameState.emptyCells);
    }

    public static double computeValue(GameState gameState, int i1, int i2, int h1, int h2) {
        if (!isValidRectangle(gameState, i1, i2, h1, h2)) {
            return -1;
        }
        int x = workToComplete(gameState, i1, i2, h1, h2);
        int y = computePoints(gameState, stepDesc, i1, i2, h1, h2);
        int[] z = computeHazzard(gameState, i1, i2, h1, h2);
        return valueFor(gameState, x, y, z, gameState.emptyCells);
    }

    public static double k[][] = new double[][] {{0.9, 1.7, 0.2}, {0.8, 1.5, 0.25}, {0.7, 1.5, 0.3}};

    public static double valueFor(GameState gameState, int x, int y, int[] z, int emptyCells) {
        double k1 = k[gameState.opponentCount - 1][0];
        double k2 = k[gameState.opponentCount - 1][1];
        double k3 = k[gameState.opponentCount - 1][2];
        double value = Math.pow(1.0 / (x == 0 ? 0.5 : x), k1) * Math.pow(y, k2);
        double minZ = Double.MAX_VALUE;
        for (int i = 0; i < gameState.opponentCount; i++) {
            minZ = Math.min(minZ, Math.pow((z[i] == 0 ? 0.5 : z[i]) * 1.0 / (x == 0 ? 0.5 : x), k3 / 10 + k3 * 9 *
                    (emptyCells * 1.0 / N / M) / 10.0));
        }
        return value * minZ;
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
        int points = CEMPTY;
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
        int countEmpty = CEMPTY;
        int countMy = CMY;
        if ((i2 - i1 + 1) * (h2 - h1 + 1) > countEmpty + countMy || countEmpty == 0) {
            return false;
        }
        return true;
    }

    public static void debug(String msg) {
        System.err.println(msg);
    }

    public static class StepDesc {

        public int toI;
        public int toH;
        public boolean toRec;
        public int pointI;
        public int pointH;
    }

    public static class GameState {

        final int gameRound;
        final int opponentCount;
        final List<PlayerState> players;
        final int[][] grid;
        final int emptyCells;

        public GameState(int gameRound, int opponentCount, List<PlayerState> players, int[][] grid) {
            this.gameRound = gameRound;
            this.opponentCount = opponentCount;
            this.players = players;
            this.grid = grid;
            int empty = 0;
            for (int i = 0; i < N; i++) {
                for (int h = 0; h < M; h++) {
                    if (grid[i][h] == EMPTY) {
                        empty++;
                    }
                }
            }
            this.emptyCells = empty;
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
            if (goBack > 0) {
                sb.append("BACK ").append(goBack);
            } else {
                sb.append(x).append(" ").append(y);
            }
            if (message != null) {
                sb.append("\r").append(message);
            }
            return sb.toString();
        }
    }

    public static class PlayerState {

        public final int i, h;
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

    static public double linesMethod(GameState gameState) {
        double maxValue = Double.MIN_VALUE;
        int moveToI = 0;
        int moveToH = 0;
        for (int lineI = -1; lineI < N; lineI++) {
            for (int lineH = -1; lineH < M; lineH++) {
                if (lineH == -1 ^ lineI == -1) {
                    double value = islandsForLine(gameState, lineI, lineH);
                    if (value > maxValue) {
                        maxValue = value;
                        moveToI = moveI;
                        moveToH = moveH;
                    }
                }
            }
        }
        moveI = moveToI;
        moveH = moveToH;
        return maxValue;
    }

    public static int[][] c = new int[N][M];
    public static int[][] b = new int[N * M + 1][2];
    public static int[][] sgNeed = new int[N * M][2];
    public static int[][] con = new int[N * M][2];
    public static int[][] islandsGraph = new int[M * N / 4][M * N / 4];
    public static int[][] islandHazz = new int[M * N / 4][3];
    public static int conN = 0;
    public static int sgNeedN = 0;
    public static int[] need = new int[M];
    public static boolean[] visitedIsland = new boolean[M * N / 4];
    public static int[] islandGroup = new int[M];
    public static int[] islandSum = new int[M * N];
    public static int MARK = 1;
    public static Set<Integer> neededPointsSet = new HashSet<Integer>();
    public static double islandsForLine(GameState gameState, int lineI, int lineH) {
        sgNeedN = 0;
        conN = 0;
        MARK++;
        Arrays.fill(needOnInd, 0);
        Arrays.fill(need, 0);
        Arrays.fill(visitedIsland, false);
        for (int[] row : islandHazz) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        int[][] grid = gameState.grid;
        int moveToI = 0;
        int moveToH = 0;
        int bsfInd = 0;
        for (int startI = 0; startI < N; startI++) {
            for (int startH = 0; startH < M; startH++) {
                if (startI != lineI && startH != lineH && c[startI][startH] != MARK && grid[startI][startH] == EMPTY) {
                    bsfInd++;
                    islandSum[bsfInd] = bsf(gameState, lineI, lineH, startI, startH, bsfInd);
                }
            }
        }
        bsfInd++;
        for (int i = 1; i < bsfInd; i++) {
            for (int h = 1; h < bsfInd; h++) {
                islandsGraph[i][h] = 0;
            }
        }
        for (int i = 0; i < conN; i++) {
            islandsGraph[con[i][0]][con[i][1]]++;
            islandsGraph[con[i][1]][con[i][0]]++;
        }

        double maxValue = Double.MIN_VALUE;
        for (int jj = 1; jj < bsfInd; jj++) {
            if (!visitedIsland[jj] && islandSum[jj] > 0) {
                Arrays.fill(hazz, Integer.MAX_VALUE);
                islandCons = 0;
                int groupN = relatedIslands(jj, bsfInd, 0) + 1;
                int sharedPoints = islandCons;
                int minDist = Integer.MAX_VALUE;
                int minDistPointI = 0;
                int minDistPointH = 0;
                int left = Integer.MAX_VALUE;
                int right = Integer.MIN_VALUE;
                neededPointsSet.clear();
                int wholeSum = 0;
                for (int i = 0; i < groupN; i++) {
                    int islInd = islandGroup[i];
                    for (int ii = 0; ii < sgNeedN; ii++) {
                        if (sgNeed[ii][0] == islInd) {
                            if (neededPointsSet.contains(sgNeed[ii][1])) {
                                sharedPoints++;
                            } else {
                                neededPointsSet.add(sgNeed[ii][1]);
                            }
                        }
                    }
                    wholeSum += islandSum[islInd];
                    for (int hh = 0; hh < gameState.opponentCount; hh++) {
                        hazz[hh] = Math.min(hazz[hh], islandHazz[islInd][hh]);
                    }
                    for (int h = 0; h < M; h++) {
                        if (need[h] == islInd) {
                            neededPointsSet.add(h);
                        }
                    }
                }
                for (Integer i : neededPointsSet) {
                    if (i > right) {
                        right = i;
                    }
                    if (i < left) {
                        left = i;
                    }
                    int pointI = 0;
                    int pointH = 0;
                    if (lineI == -1) {
                        pointI = i;
                        pointH = lineH;
                    } else {
                        pointI = lineI;
                        pointH = i;
                    }
                    int dis = distanceToPoint(gameState.getMyPlayer().i, gameState.getMyPlayer().h, pointI, pointH);
                    if (minDist > dis) {
                        minDist = dis;
                        minDistPointI = pointI;
                        minDistPointH = pointH;
                    }
                }
                int x = (minDist == 0 ? 0 : minDist - 1) + right - left + 1;
                int y = wholeSum - sharedPoints;
                double value = valueFor(gameState, x, y, hazz, gameState.emptyCells);
                if (value > maxValue) {
                    maxValue = value;
                    moveTowardPoint(gameState.getMyPlayer().i, gameState.getMyPlayer().h, minDistPointI, minDistPointH);
                    moveToI = moveI;
                    moveToH = moveH;
                }
            }
        }
        moveI = moveToI;
        moveH = moveToH;
        return maxValue;
    }

    public static int islandCons = 0;
    public static int relatedIslands(int st, int count, int groupInd) {
        islandGroup[groupInd] = st;
        visitedIsland[st] = true;
        for (int i = 1; i < count; i++) {
            if (islandsGraph[st][i] > 0 && !visitedIsland[i]) {
                islandCons += islandsGraph[st][i];
                groupInd = relatedIslands(i, count, groupInd + 1);
            }
        }
        return groupInd;
    }

    public static int[] curNeed = new int[M];
    public static int[] curSgNeed = new int[M * 2];
    public static int[] needOnInd = new int[M];
    public static int bsf(GameState gameState, int lineI, int lineH, int startI, int startH, int myIndx) {
        int[][] grid = gameState.grid;
        c[startI][startH] = MARK;

        b[0][0] = startI;
        b[0][1] = startH;
        int i = 0;
        int kn = 1;
        int curSgNeedN = 0;
        int curNeedN = 0;
        int value = 1;
        boolean struckBorderOrAlien = false;
        if (startI == 0 || startH == 0 || startI == N - 1 || startH == M - 1) {
            struckBorderOrAlien = true;
        }

        for (int jj = 0; jj < gameState.opponentCount; jj++) {
            islandHazz[myIndx][jj] =
                    distanceToPoint(gameState.players.get(jj + 1).i,
                            gameState.players.get(jj + 1).h,
                            startI,
                            startH);
        }

        while (i < kn) {
            int ci = b[i][0];
            int ch = b[i][1];
            for (int j1 = -1; j1 <= 1; j1++) {
                for (int j2 = -1; j2 <= 1; j2++) {
                    if (!(j1 == 0 && j2 == 0)) {
                        int ti = ci + j1;
                        int th = ch + j2;
                        if (ti < 0 || th < 0 || ti >= N || th >= M || c[ti][th] == MARK) {
                            continue;
                        }
                        if (grid[ti][th] != MY && grid[ti][th] != EMPTY) {
                            struckBorderOrAlien = true;
                            continue;
                        }
                        if (grid[ti][th] == MY) {
                            continue;
                        }

                        for (int jj = 0; jj < gameState.opponentCount; jj++) {
                            islandHazz[myIndx][jj] = Math.min(islandHazz[myIndx][jj],
                                    distanceToPoint(gameState.players.get(jj + 1).i,
                                            gameState.players.get(jj + 1).h,
                                            ti,
                                            th));
                        }

                        if (ti == lineI || th == lineH) {
                            int needInd = lineI == -1 ? ti : th;
                            if (ti != ci && th != ch) {
                                curSgNeed[curSgNeedN] = needInd;
                                curSgNeedN++;
                            } else {
                                curNeed[curNeedN] = needInd;
                                needOnInd[needInd] = myIndx;
                                curNeedN++;
                            }
                            continue;
                        }

                        // We will come here only if cell is empty and not on line
                        if (ti == 0 || th == 0 || ti == N - 1 || th == M - 1) {
                            struckBorderOrAlien = true;
                        }
                        value++;
                        b[kn][0] = ti;
                        b[kn][1] = th;
                        c[ti][th] = MARK;
                        kn++;
                    }
                }
            }
            i++;
        }

        if (struckBorderOrAlien) {
            return 0;
        }

        for (int h = 0; h < curNeedN; h++) {
            value++;
            if (need[curNeed[h]] == 0) {
                need[curNeed[h]] = myIndx;
            } else {
                con[conN][0] = myIndx;
                con[conN][1] = need[curNeed[h]];
                need[curNeed[h]] = 0;
                conN++;
            }
        }
        for (int h = 0; h < curSgNeedN; h++) {
            int ind = curSgNeed[h];
            if (needOnInd[ind] != myIndx) {
                value++;
                sgNeed[sgNeedN][0] = myIndx;
                sgNeed[sgNeedN][1] = ind;
                sgNeedN++;
            }
        }
        return value;
    }

    static public boolean runForestRun(double maxValue, GameState gameState) {
        int grid[][] = gameState.grid;
        if (maxValue == Double.MIN_VALUE) {
            int minDist = Integer.MAX_VALUE;
            for (int i = 0; i < N; i++) {
                for (int h = 0; h < M; h++) {
                    if (grid[i][h] == EMPTY) {
                        int dis = distanceToPoint(gameState.getMyPlayer().i, gameState.getMyPlayer().h, i, h);
                        if (dis < minDist) {
                            minDist = dis;
                            moveTowardPoint(gameState.getMyPlayer().i, gameState.getMyPlayer().h, i, h);
                        }
                    }
                }
            }
            return minDist != Integer.MAX_VALUE;
        }
        return false;
    }

    public static class Future {
        public int lastScore;
        public GameState futureState;
        public int curRound;
        public List<Double> expectedPointsByRound;
        public List<Integer> closeUpRound;

        public Future() {
            expectedPointsByRound = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                expectedPointsByRound.add(0.0);
            }
            closeUpRound = new LinkedList<>();
        }

        public GameState futureMixin(GameState gameState) {
            if (futureState == null || futureState.gameRound <= gameState.gameRound) {
                return gameState;
            }
            int[][] grid = new int[N][M];
            for (int i = 0; i < N; i++) {
                for (int h = 0; h < M; h++) {
                    grid[i][h] = gameState.grid[i][h];
                    if (grid[i][h] == EMPTY && futureState.grid[i][h] != MY) {
                        grid[i][h] = futureState.grid[i][h];
                    }
                }
            }
            return new GameState(gameState.gameRound, gameState.opponentCount, gameState.players, grid);
        }

        public void update(GameState gameState) {
            if (futureState == null || futureState.gameRound <= gameState.gameRound) {
                futureState = gameState;
            }
            curRound = gameState.gameRound;
            int score = 0;
            for (int i = 0; i < N; i++) {
                for (int h = 0; h < M; h++) {
                    if (gameState.grid[i][h] == MY) {
                        score++;
                    }
                }
            }
            if (score - lastScore >= 5) {
                closeUpRound.add(gameState.gameRound);
            }
            lastScore = score;
            // remove closeup round that were deleted
            closeUpRound.removeIf(x -> x > gameState.gameRound);
        }

        public static int WAIT_FOR = 10;
        public int addNewExpectedAndReportBack(double expected) {
            expectedPointsByRound.add(curRound, expected);
            // check for closeups in last rounds
            boolean isCloseupRecently = false;
            for (Integer i : closeUpRound) {
                if (i == curRound - WAIT_FOR + 1) {
                    isCloseupRecently = true;
                }
            }
            if (curRound > WAIT_FOR && !isCloseupRecently) {
                boolean trig = true;
                for (int i = 0; i < WAIT_FOR; i++) {
                    if (expectedPointsByRound.get(curRound - WAIT_FOR) < expectedPointsByRound.get(curRound - i) * 1.3) {
                        trig = false;
                    }
                }
                if (trig) {
                    return 25;
                }
            }
            return 0;
        }
    }
}
