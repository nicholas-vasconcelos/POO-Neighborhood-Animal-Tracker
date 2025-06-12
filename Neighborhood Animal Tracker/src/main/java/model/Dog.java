package model;

import enums.Color;
import enums.Size;
import enums.TailLength;

public class Dog extends Animal{

    private String breed;
    private TailLength tailLength;
    private boolean chasesCars;
    private boolean likesBeingPet;
    private boolean isVaccinated;
    private boolean isCastrated;


    public Dog(int id, String nickName, Color color, Size size, String breed,
               TailLength tailLength, boolean chasesCars, boolean likesBeingPet,
               boolean isVaccinated, boolean isCastrated) {
        super(id, nickName, color, size);
        this.breed = breed;
        this.tailLength = tailLength;
        this.chasesCars = chasesCars;
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

    public TailLength getTailLength() {
        return tailLength;
    }

    public void setTailLength(TailLength tailLength) {
        this.tailLength = tailLength;
    }

    public boolean isChasesCars() {
        return chasesCars;
    }

    public void setChasesCars(boolean chasesCars) {
        this.chasesCars = chasesCars;
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
