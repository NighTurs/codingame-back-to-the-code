#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <cmath>

using namespace std;

const int N = 20;
const int M = 35;
const int EMPTY = -1;
const int MY = 0;

struct StepDesc {
    int toI;
    int toH;
    bool toRec;
    int pointI;
    int pointH;
};

struct Turn {
    int x, y;
    int goBack;
    string message;

    void print() {
        cout << x << " " << y << endl;
    }
};

struct PlayerState {
    int i, h;
    int backInTimeLeft;
};

struct Fenwick {
    int empty[N][M];
    int myPlayer[N][M];

    int countEmpty(int i1, int i2, int h1, int h2) {
        return count(i1, i2, h1, h2, empty);
    }

    int countMy(int i1, int i2, int h1, int h2) {
        return count(i1, i2, h1, h2, myPlayer);
    }

    void initMy(int grid[N][M]) {
        fill(&myPlayer[0][0], &myPlayer[0][0] + sizeof(myPlayer) / sizeof(myPlayer[0][0]), 0 );
        for (int i = 0; i < N; i++) {
            for (int h = 0; h < M; h++) {
                if (grid[i][h] == MY) {
                    inc(i, h, 1, myPlayer);
                }
            }
        }
    }

    void initEmpty(int grid[N][M]) {
        fill(&empty[0][0], &empty[0][0] + sizeof(empty) / sizeof(empty[0][0]), 0 );
        for (int i = 0; i < N; i++) {
            for (int h = 0; h < M; h++) {
                if (grid[i][h] == EMPTY) {
                    inc(i, h, 1, empty);
                }
            }
        }
    }

    int count(int i1, int i2, int h1, int h2, int t[N][M]) {
        return count(i2, h2, t) - count(i1 - 1, h2, t) - count(i2, h1 - 1, t) + count(i1 - 1, h1 - 1, t);
    }

    int count(int ii, int hh, int t[N][M]) {
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

    void inc(int ii, int hh, int delta, int t[N][M]) {
        for (int i = ii; i < N; i = (i | (i + 1)))
            for (int j = hh; j < M; j = (j | (j + 1)))
                t[i][j] += delta;
    }
};

struct GameState {
    Fenwick fenwick;
    int gameRound;
    int opponentCount;
    vector<PlayerState> players;
    int grid[N][M];
};

Turn makeTurn(GameState & gameState);
double maxValueFor(GameState & gameState, int i1, int i2, int h1, int h2);
double computeValue(GameState & gameState, int i1, int i2, int h1, int h2);
double valueFor(GameState & gameState, int x, int y, int z[]);
void computeHazzard(GameState & gameState, int i1, int i2, int h1, int h2);
int computePoints(GameState & gameState, StepDesc & stepDesc, int i1, int i2, int h1, int h2);
int workToComplete(GameState & gameState, int i1, int i2, int h1, int h2);
int distanceToRec(int i1, int i2, int h1, int h2, int curI, int curH);
int distanceToPoint(int curI, int curH, int toI, int toH);
bool moveTowardRec(int i1, int i2, int h1, int h2, int curI, int curH);
bool moveTowardPoint(int curI, int curH, int toI, int toH);
void moveClockWise(int i1, int i2, int h1, int h2, int curI, int curH);
void moveClockUnwise(int i1, int i2, int h1, int h2, int curI, int curH);
void moveDown(int x, int y);
void moveUp(int x, int y);
void moveLeft(int x, int y);
void moveRight(int x, int y);
bool isValidRectangle(GameState & gameState, int i1, int i2, int h1, int h2);

double k[][3] = {{0.6, 1.0, 0.35}, {0.6, 1.0, 0.2}, {0.8, 1.0, 0.15}};
int hazz[4];
StepDesc stepDesc;
// return of move methods
int moveI = 0;
int moveH = 0;

int main() {
    int opponentCount; // Opponent count
    cin >> opponentCount; cin.ignore();

    // game loop
    while (1) {
        int gameRound;
        cin >> gameRound; cin.ignore();
        PlayerState state;
        GameState gameState;
        gameState.opponentCount = opponentCount;
        cin >> state.h >> state.i >> state.backInTimeLeft; cin.ignore();
        gameState.players.push_back(state);
        for (int i = 0; i < opponentCount; i++) {
            cin >> state.h >> state.i >> state.backInTimeLeft; cin.ignore();
            gameState.players.push_back(state);
        }
        string str;
        for (int i = 0; i < N; i++) {
            cin >> str; cin.ignore();
            for (int h = 0; h < M; h++) {
                if (str[h] == '.') {
                    gameState.grid[i][h] = EMPTY;
                } else {
                    gameState.grid[i][h] = str[h] - '0';
                }
            }
        }

        gameState.fenwick.initMy(gameState.grid);
        gameState.fenwick.initEmpty(gameState.grid);
        Turn turn = makeTurn(gameState);
        turn.print();
    }
}

Turn makeTurn(GameState & gameState) {
    double maxValue = -1000000;
    StepDesc bestStep;
    bestStep.toI = 0;
    bestStep.toH = 0;
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
    Turn turn;
    turn.x = bestStep.toH;
    turn.y = bestStep.toI;
    return turn;
}

double maxValueFor(GameState & gameState, int i1, int i2, int h1, int h2) {
    int dist = distanceToRec(i1, i2, h1, h2, gameState.players[0].i, gameState.players[0].h);
    int x = dist;
    int y = (i2 - i1 + 1) * (h2 - h1 + 1) + (dist == 0 ? 0 : (dist - 1));
    computeHazzard(gameState, i1, i2, h1, h2);
    return valueFor(gameState, x, y, hazz);
}

double computeValue(GameState & gameState, int i1, int i2, int h1, int h2) {
    if (!isValidRectangle(gameState, i1, i2, h1, h2)) {
        return -1;
    }
    int x = workToComplete(gameState, i1, i2, h1, h2);
    int y = computePoints(gameState, stepDesc, i1, i2, h1, h2);
    computeHazzard(gameState, i1, i2, h1, h2);
    return valueFor(gameState, x, y, hazz);
}

double valueFor(GameState & gameState, int x, int y, int z[]) {
    double k1 = k[gameState.opponentCount - 1][0];
    double k2 = k[gameState.opponentCount - 1][1];
    double k3 = k[gameState.opponentCount - 1][2];
    double value = pow(1.0 / (x == 0 ? 0.5 : x), k1) * pow(y, k2);
    for (int i = 0; i < gameState.opponentCount; i++) {
        value *= pow((z[i] == 0 ? 0.5 : z[i]), k3);
    }
    return value;
}

void computeHazzard(GameState & gameState, int i1, int i2, int h1, int h2) {
    for (int i = 1; i < gameState.players.size(); i++) {
        PlayerState * state = &(gameState.players[i]);
        int oppI = state->i;
        int oppH = state->h;
        hazz[i - 1] = distanceToRec(i1, i2, h1, h2, oppI, oppH);
    }
}

int computePoints(GameState & gameState, StepDesc & stepDesc, int i1, int i2, int h1, int h2) {
    int points = gameState.fenwick.countEmpty(i1, i2, h1, h2);
    int curI = gameState.players[0].i;
    int curH = gameState.players[0].h;
    bool once = false;
    if (stepDesc.toRec) {
        while (moveTowardRec(i1, i2, h1, h2, curI, curH)) {
            once = true;
            curI = moveI;
            curH = moveH;
            if (gameState.grid[curI][curH] == EMPTY) {
                points++;
            }
        }
        if (gameState.grid[curI][curH] == EMPTY && once) {
            points--;
        }
    } else {
        while (moveTowardPoint(curI, curH, stepDesc.pointI, stepDesc.pointH)) {
            once = true;
            curI = moveI;
            curH = moveH;
            if (gameState.grid[curI][curH] == EMPTY) {
                points++;
            }
        }
        if (gameState.grid[curI][curH] == EMPTY && once) {
            points--;
        }
    }
    return points;
}

int workToComplete(GameState & gameState, int i1, int i2, int h1, int h2) {
    int curI = i1;
    int curH = h1;
    while (gameState.grid[curI][curH] == MY) {
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
        if (gameState.grid[curI][curH] == MY) {
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

    int myCurI = gameState.players[0].i;
    int myCurH = gameState.players[0].h;
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
                min(distanceToPoint(myCurI, myCurH, bestStartI, bestStartH),
                        distanceToPoint(myCurI, myCurH, bestEndI, bestEndH)) - 1;
    }
}

int distanceToRec(int i1, int i2, int h1, int h2, int curI, int curH) {
    int mmin = (int)1.e9;

    if (curI >= i1 && curI <= i2) {
        mmin = min(abs(curH - h1), abs(curH - h2));
    }

    int add = min(abs(curI - i1), abs(curI - i2));

    if (curH >= h1 && curH <= h2) {
        mmin = min(mmin, add);
    } else {
        mmin = min(mmin, add + min(abs(curH - h1), abs(curH - h2)));
    }
    return mmin;
}

int distanceToPoint(int curI, int curH, int toI, int toH) {
    return abs(curI - toI) + abs(curH - toH);
}

bool moveTowardRec(int i1, int i2, int h1, int h2, int curI, int curH) {
    int toH = abs(h1 - curH) > abs(h2 - curH) ? h2 : h1;
    int toI = abs(i1 - curI) > abs(i2 - curI) ? i2 : i1;

    int mmin = (int)1.e9;
    bool result = false;
    if (curI >= i1 && curI <= i2) {
        mmin = min(abs(curH - h1), abs(curH - h2));
        result = moveTowardPoint(curI, curH, curI, toH);
    }

    if (curH >= h1 && curH <= h2) {
        int cost = min(abs(curI - i1), abs(curI - i2));
        if (mmin > cost) {
            result = moveTowardPoint(curI, curH, toI, curH);
        }
    }

    if (!((curI >= i1 && curI <= i2) || (curH >= h1 && curH <= h2))) {
        result = moveTowardPoint(curI, curH, toI, toH);
    }
    return result;
}

bool moveTowardPoint(int curI, int curH, int toI, int toH) {
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

void moveClockWise(int i1, int i2, int h1, int h2, int curI, int curH) {
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

void moveClockUnwise(int i1, int i2, int h1, int h2, int curI, int curH) {
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

void moveDown(int x, int y) {
    moveI = x + 1;
    moveH = y;
}

void moveUp(int x, int y) {
    moveI = x - 1;
    moveH = y;
}

void moveLeft(int x, int y) {
    moveI = x;
    moveH = y - 1;
}

void moveRight(int x, int y) {
    moveI = x;
    moveH = y + 1;
}

// O(N * M)
bool isValidRectangle(GameState & gameState, int i1, int i2, int h1, int h2) {
    int countEmpty = gameState.fenwick.countEmpty(i1, i2, h1, h2);
    int countMy = gameState.fenwick.countMy(i1, i2, h1, h2);

    if ((i2 - i1 + 1) * (h2 - h1 + 1) > countEmpty + countMy || countEmpty == 0) {
        return false;
    }
    return true;
}