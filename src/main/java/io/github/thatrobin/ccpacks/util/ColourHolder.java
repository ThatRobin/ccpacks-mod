package io.github.thatrobin.ccpacks.util;

public class ColourHolder {

    private float red;
    private float green;
    private float blue;
    private float alpha;


    public ColourHolder(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public float getBlue() {
        return this.blue;
    }

    public float getGreen() {
        return this.green;
    }

    public float getRed() {
        return this.red;
    }
}
