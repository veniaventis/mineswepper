package com.javarush.games.minesweeper;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame extends Game {
    private static final int SIDE = 9;
    private static final String MINE = "\uD83D\uDCA3";
    private static final String FLAG = "\uD83D\uDEA9";
    private GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField;
    private int countFlags;
    private int countClosedTiles = SIDE*SIDE;
    private int score;
    private boolean isGameStopped;


    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
    }
    @Override
    public void onMouseLeftClick(int x, int y ){
        if (isGameStopped){
            restart();
        }else{openTile(x, y);}
    }

    @Override
    public void onMouseRightClick(int x, int y) {markTile(x,y);}

    private void createGame() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                setCellValue(x, y,"");
                boolean isMine = getRandomNumber(10) < 1;
                if (isMine) {
                    countMinesOnField++;
                }
                gameField[y][x] = new GameObject(x, y, isMine);
                setCellColor(x, y, Color.ORANGE);

            }
        }

        countFlags = countMinesOnField;
        countMineNeighbors();
    }


    private void markTile(int x, int y){
        GameObject gameObject = gameField[y][x];
        if (gameObject.isOpen) {
        } else if((countFlags == 0) && (!gameObject.isFlag)) {
        } else if (isGameStopped) {
        } else {
            if (!gameObject.isFlag) {
                gameObject.isFlag = true;
                setCellValue(gameObject.x, gameObject.y, FLAG);
                setCellColor(gameObject.x, gameObject.y, Color.YELLOW);
                countFlags--;
            } else if (gameObject.isFlag) {
                gameObject.isFlag = false;
                setCellValue(gameObject.x, gameObject.y, "");
                setCellColor(gameObject.x, gameObject.y, Color.ORANGE);
                countFlags++;
            }
        }

    }

    private void openTile(int x, int y){
        if (gameField[y][x].isOpen || gameField[y][x].isFlag || isGameStopped  ){
            return;
        } else {
            GameObject gameObject = gameField[y][x];
            gameObject.isOpen = true;
            countClosedTiles--;
            setCellColor(x,y,Color.GREEN);
            if(countClosedTiles == countMinesOnField && !gameObject.isMine) {
                win();
                return;
            } if (gameObject.isMine) {
                setCellValueEx(gameObject.x, gameObject.y, Color.RED, MINE);
                gameOver();
            } else if (gameObject.countMineNeighbors == 0) {
                for (GameObject neighbor : getNeighbors(gameObject)) {
                    if (!neighbor.isOpen) {
                        openTile(neighbor.x, neighbor.y);
                    }
                }
                setCellValue(gameObject.x, gameObject.y, "");
                getNeighbors(gameObject);
            } else if (!gameObject.isMine) {
                score += 5;
                setScore(score);
                setCellNumber(x, y, gameObject.countMineNeighbors);
            }
        }

    }


    private void countMineNeighbors(){
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                GameObject gameObject = gameField[y][x];
                if (!gameObject.isMine){
                    for (GameObject neighbor : getNeighbors(gameObject)) {
                        if (neighbor.isMine)
                            gameObject.countMineNeighbors++;
                    }
                }
            }
        }

    }

    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> result = new ArrayList<>();
        for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
            for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
                if (y < 0 || y >= SIDE) {
                    continue;
                }
                if (x < 0 || x >= SIDE) {
                    continue;
                }
                if (gameField[y][x] == gameObject) {
                    continue;
                }
                result.add(gameField[y][x]);
            }
        }
        return result;
    }
    
    private void win(){
        isGameStopped = true;
        showMessageDialog(Color.GREEN, "You are WON!!", Color.WHITE, 40);
    }

    private void gameOver(){
        isGameStopped = true;
        showMessageDialog(Color.RED, "Game over", Color.WHITE, 50);
    }

    private void restart(){
        isGameStopped = false;
        countClosedTiles = SIDE * SIDE;
        score = 0;
        countMinesOnField = 0;
        setScore(score);
        createGame();

    }
}
