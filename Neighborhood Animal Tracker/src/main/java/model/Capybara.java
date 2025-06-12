package model;

import enums.Color;
import enums.Size;

public class Capybara extends Animal{
    private int socialGroupSize;
    private boolean eatsGrass;

    public Capybara(int id, String nickName, Color color, Size size,
                    int socialGroupSize, boolean eatsGrass) {
        super(id, nickName, Color.BROWN, size);
        this.socialGroupSize = socialGroupSize;
        this.eatsGrass = eatsGrass;
    }


    // Getters and Setters
    public int getSocialGroupSize() {
        return socialGroupSize;
    }

    public void setSocialGroupSize(int socialGroupSize) {
        this.socialGroupSize = socialGroupSize;
    }

    public boolean isEatsGrass() {
        return eatsGrass;
    }

    public void setEatsGrass(boolean eatsGrass) {
        this.eatsGrass = eatsGrass;
    }
}
