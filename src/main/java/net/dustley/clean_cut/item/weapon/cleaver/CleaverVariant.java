package net.dustley.clean_cut.item.weapon.cleaver;

public class CleaverVariant {

    public String id;
    public String color;
    public Float predicate;

    public CleaverVariant(String cleaverId, String cleaverColor, Float predicateID) {
        id = cleaverId;
        color = cleaverColor;
        predicate = predicateID;
    }

}
