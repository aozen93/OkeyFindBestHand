package models;

import java.util.ArrayList;
import java.util.List;

public class PlayerHand {
    private List<Stone> stones = new ArrayList<>();
    private List<Stone> unUsedStones = new ArrayList<>();
    private List<Stone> usedPerStones = new ArrayList<>();
    private List<Stone> twinStones = new ArrayList<>();
    private int serialPerCount = 0;
    private int serialPerAndSameValueCountFor2Stones = 0;

    public PlayerHand(List<Stone> stones, List<Stone> unUsedStones, List<Stone> usedPerStones, List<Stone> twinStones) {
        this.stones = stones;
        this.unUsedStones = unUsedStones;
        this.usedPerStones = usedPerStones;
        this.twinStones = twinStones;
    }

    public PlayerHand() {
    }


    public int getSerialPerCount() {
        return serialPerCount;
    }

    public void setSerialPerCount(int serialPerCount) {
        this.serialPerCount = serialPerCount;
    }

    public int getSerialPerAndSameValueCountFor2Stones() {
        return serialPerAndSameValueCountFor2Stones;
    }

    public void setSerialPerAndSameValueCountFor2Stones(int serialPerAndSameValueCountFor2Stones) {
        this.serialPerAndSameValueCountFor2Stones = serialPerAndSameValueCountFor2Stones;
    }

    public List<Stone> getStones() {
        return stones;
    }

    public void setStones(List<Stone> stones) {
        this.stones = stones;
    }

    public List<Stone> getUnUsedStones() {
        return unUsedStones;
    }

    public void setUnUsedStones(List<Stone> unUsedStones) {
        this.unUsedStones = unUsedStones;
    }

    public List<Stone> getUsedPerStones() {
        return usedPerStones;
    }

    public void setUsedPerStones(List<Stone> usedPerStones) {
        this.usedPerStones = usedPerStones;
    }

    public List<Stone> getTwinStones() {
        return twinStones;
    }

    public void setTwinStones(List<Stone> twinStones) {
        this.twinStones = twinStones;
    }
}
