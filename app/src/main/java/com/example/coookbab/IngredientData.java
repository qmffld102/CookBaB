package com.example.coookbab;

import java.io.Serializable;

public class IngredientData implements Serializable {
    private String food;
    private int ingredientid;
    private int life;
    private int num;

    public IngredientData() {
    }
    public IngredientData(String food, int ingredientid, int life, int num) {
        this.food = food;
        this.ingredientid = ingredientid;
        this.life = life;
        this.num = num;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public int getIngredientid() {
        return ingredientid;
    }

    public void setIngredientid(int ingredientid) {
        this.ingredientid = ingredientid;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
