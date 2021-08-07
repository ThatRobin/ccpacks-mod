package io.github.ThatRobin.ccpacks.util;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeReference;

public class Outcome {

    private String text;
    private PowerType<?> power;

    public Outcome(String text){
        this.text = text;
    }

    public Outcome(PowerType<?> power){
        this.power = power;
    }

    public PowerType<?> getPower() {
        return power;
    }

    public String getText() {
        return text;
    }
}
