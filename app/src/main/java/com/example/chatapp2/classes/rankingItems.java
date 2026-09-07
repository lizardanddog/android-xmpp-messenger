package com.example.chatapp2.classes;

public class rankingItems {
    int ranking;
    String name;

    @Override
    public String toString() {
        return "rankingItems{" +
                "ranking=" + ranking +
                ", name='" + name + '\'' +
                '}';
    }

    public rankingItems(int ranking, String name) {
        this.ranking = ranking;
        this.name = name;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
