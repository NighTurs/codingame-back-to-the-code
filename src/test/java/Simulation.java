import game.Player;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Simulation {

    int Np = 18;
    double profiles1[][] = {
            {1.0, 2.0, 0.0},
            {1.0, 1.7, 0.0},
            {0.9, 2.0, 0.0},
            {0.7, 1.7, 0.0},
            {0.3, 1.7, 0.0},
            {0.6, 1.0, 0.35},
            {1.0, 2.0, 0.4},
            {1.5, 4.0, 0.4},
            {0.5, 1.0, 0.0},
            {1.0, 1.0, 0.0},
            {0.5, 1.5, 0.0},
            {1.0, 1.5, 0.0},
            {0.5, 1.0, 0.3},
            {0.5, 1.5, 0.3},
            {1.0, 1.5, 0.3},
            {0.5, 1.0, 0.6},
            {0.5, 1.5, 0.6},
            {1.0, 1.5, 0.6}};
    double profiles2[][] = {{0.5, 1.0, 0.0},
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
            {1.0, 1.5, 0.6}};
    double profiles3[][] = {{0.5, 1.0, 0.0},
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
            {1.0, 1.5, 0.6}};
    int NPLAYERS = 4;
    int NROUNDS = 250;

    @Test
    public void testCrash() {
        double[][] profiles = NPLAYERS == 2 ? profiles1 : NPLAYERS == 3 ? profiles2 : profiles3;
        int[][] scores = new int[Np][NPLAYERS];
        Random rand = new Random();
        while (true) {
            System.out.println("NEW GAME");
            int[] profs = new int[NPLAYERS];
            Player.PlayerState[] states = new Player.PlayerState[NPLAYERS];
            int[][] grid = emptyGrid();
            for (int i = 0; i < NPLAYERS; i++) {
                profs[i] = Math.abs(rand.nextInt()) % Np;
                states[i] = new Player.PlayerState(Math.abs(rand.nextInt()) % Player.N,
                        Math.abs(rand.nextInt()) % Player.M,
                        0);
                System.out.println("Player : " + states[i].i + " " + states[i].h);
                grid[states[i].i][states[i].h] = i;
            }
            for (int i = 0; i < NROUNDS * NPLAYERS; i++) {
                long st = System.currentTimeMillis();
                Player.GameState gameState =
                        new Player.GameState(i / NPLAYERS + 1, NPLAYERS - 1, Arrays.asList(states), grid);
//                                System.out.println("Current profile " + profs[0]);
                Player.k[NPLAYERS - 2][0] = profiles[profs[0]][0];
                Player.k[NPLAYERS - 2][1] = profiles[profs[0]][1];
                Player.k[NPLAYERS - 2][2] = profiles[profs[0]][2];
                Player.Turn turn = Player.makeTurn(gameState);
                long ed = System.currentTimeMillis();

                //                System.out.println("Player goes from (" + states[0].i + ", " + states[0].h + ") to (" + turn.y + ", " +
                //                        turn.x + ") in " + (ed - st) + "ms");
                handle(grid);
                states[0] = new Player.PlayerState(turn.y, turn.x, 0);
                grid[turn.y][turn.x] = 0;
                grid = shiftColors(grid, NPLAYERS);

                Player.PlayerState cur = states[NPLAYERS - 1];
                int pr = profs[NPLAYERS - 1];
                states[NPLAYERS - 1] = states[0];
                profs[NPLAYERS - 1] = profs[0];
                int pos = NPLAYERS - 1;
                while (pos != 0) {
                    pos--;
                    Player.PlayerState z = states[pos];
                    int zp = profs[pos];
                    states[pos] = cur;
                    profs[pos] = pr;
                    cur = z;
                    pr = zp;
                }
            }
            int pos[] = winners(grid);
            //            System.out.println(pos[0] + " " + pos[1] + " " + pos[2] + " " + pos[3]);
            //            System.out.println(profs[0] + " " + profs[1] + " " + profs[2] + " " + profs[3]);
            for (int i = 0; i < NPLAYERS; i++) {
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
                System.out.print("Profile x=" + profiles[i][0] + ", y=" + profiles[i][1] + "z=" + profiles[i][2] +
                        ", current scores " + scores[i][0]);
                for (int h = 1; h < NPLAYERS; h++) {
                    System.out.print(", " + scores[i][h]);
                }
                System.out.println();
            }
        }
    }

    private int[] winners(int[][] grid) {
        int[] res = new int[NPLAYERS];
        for (int i = 0; i < Player.N; i++) {
            for (int h = 0; h < Player.M; h++) {
                if (grid[i][h] != Player.EMPTY) {
                    res[grid[i][h]]++;
                }
            }
        }
        int[] pos = new int[NPLAYERS];
        for (int i = 0; i < NPLAYERS; i++) {
            pos[i] = 0;
            for (int h = 0; h < NPLAYERS; h++) {
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
                                    }
                                    c[to1][to2] = true;
                                    b[kn][0] = to1;
                                    b[kn][1] = to2;
                                    kn++;
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
