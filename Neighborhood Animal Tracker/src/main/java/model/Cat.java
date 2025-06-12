package model;

import enums.Color;
import enums.Size;

public class Cat extends Animal{

    private String breed;
    private boolean scratchesPeople;
    private boolean likesBeingPet;
    private boolean isVaccinated;
    private boolean isCastrated;

    public Cat(int id, String nickName, Color color, Size size, String breed,
               boolean scratchesPeople, boolean likesBeingPet, boolean isVaccinated, boolean isCastrated) {
        super(id, nickName, color, size);
        this.breed = breed;
        this.scratchesPeople = scratchesPeople;
        this.likesBeingPet = likesBeingPet;
        this.isVaccinated = isVaccinated;
        this.isCastrated = isCastrated;
    }


    // Getter and Setters
    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public boolean isScratchesPeople() {
        return scratchesPeople;
    }

    public void setScratchesPeople(boolean scratchesPeople) {
        this.scratchesPeople = scratchesPeople;
    }

    public boolean isLikesBeingPet() {
        return likesBeingPet;
    }

    public void setLikesBeingPet(boolean likesBeingPet) {
        this.likesBeingPet = likesBeingPet;
    }

    public boolean isVaccinated() {
        return isVaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        isVaccinated = vaccinated;
    }

    public boolean isCastrated() {
        return isCastrated;
    }

    public void setCastrated(boolean castrated) {
        isCastrated = castrated;
    }

}
