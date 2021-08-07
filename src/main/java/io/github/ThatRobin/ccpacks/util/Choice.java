package io.github.ThatRobin.ccpacks.util;

import io.github.apace100.apoli.power.PowerTypeReference;

import java.util.List;

public class Choice {

    private String text;
    private Outcome outcomes;

    public Choice(String text, Outcome outcomes){
        this.text = text;
        this.outcomes = outcomes;
    }

    public Outcome getOutcomes() {
        return outcomes;
    }

    public String getText() {
        return text;
    }
}
