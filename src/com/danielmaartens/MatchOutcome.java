package com.danielmaartens;

public class MatchOutcome {

    private String team;
    private Integer outcome;

    public void setTeam(String team) {
        this.team = team;
    }

    public void setOutcome(Integer outcome) {
        this.outcome = outcome;
    }

    public String getTeam() {
        return this.team;
    }

    public Integer getOutcome() {
        return this.outcome;
    }
}
