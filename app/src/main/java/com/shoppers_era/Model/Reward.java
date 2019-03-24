package com.shoppers_era.Model;


public class Reward {

    private String reward_id;
    private String valid_from;
    private String valid_till;
    private String reward_amount;

    public Reward(String reward_id, String valid_from, String valid_till, String reward_amount) {
        this.reward_id = reward_id;
        this.valid_from = valid_from;
        this.valid_till = valid_till;
        this.reward_amount = reward_amount;
    }

    public String getReward_id() {
        return reward_id;
    }

    public String getValid_from() {
        return valid_from;
    }

    public String getValid_till() {
        return valid_till;
    }

    public String getReward_amount() {
        return reward_amount;
    }
}
