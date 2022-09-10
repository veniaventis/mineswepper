package com.javarush.games.snake;

import com.javarush.engine.cell.*;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.List;

public class Snake {
    private List<GameObject> snakeParts = new ArrayList<>();
    private static final String HEAD_SIGN = "\uD83D\uDC7E";
    private static final String BODY_SIGN = "\u26AB";

    public void draw(Game game){
        for(GameObject gameObject: snakeParts) {
            if(gameObject == snakeParts.get(0)) {//проверить условия в случаю если тест не пройдет
                game.setCellValue(gameObject.x, gameObject.y, HEAD_SIGN);
            }else{game.setCellValue(gameObject.x, gameObject.y, BODY_SIGN);
            }
        }
    }


    public Snake(int x, int y){
        for(int i = 0; i<3; i++){
            snakeParts.add(new GameObject(x+i,y));
        }
    }
}
