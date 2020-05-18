package com.example.coookbab;

import java.io.Serializable;

public class HowToSore implements Serializable {
    private String ingredientname;
    private String tip;
    //private String ingredientnum;

    public HowToSore(){
    }

    public String getIngredientname() {
        return ingredientname;
    }

    public void setIngredientname(String ingredientname) {
        this.ingredientname = ingredientname;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    /*public String getIngredientnum() {
        return ingredientnum;
    }

    public void setIngredientnum(String ingredientnum) {
        this.ingredientnum = ingredientnum;
    }*/

    public HowToSore(String ingredientname, String tip) {
        this.ingredientname = ingredientname;
        this.tip = tip;
        //this.ingredientnum = ingredientnum;
    }
}
