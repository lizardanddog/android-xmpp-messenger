package com.example.chatapp2.classes;

import java.util.ArrayList;

public class MixComCardRankings {
    ArrayList<communicationCard> MainArray=new ArrayList<communicationCard>();
    ArrayList<rankingItems> MainRankigs=new ArrayList<rankingItems>();

    public MixComCardRankings(ArrayList<communicationCard> mainArray, ArrayList<rankingItems> mainRankigs) {
        MainArray = mainArray;
        MainRankigs = mainRankigs;
    }
    public MixComCardRankings() {
    }

    public ArrayList<communicationCard> getMainArray() {
        return MainArray;
    }

    public void setMainArray(ArrayList<communicationCard> mainArray) {
        MainArray = mainArray;
    }

    public ArrayList<rankingItems> getMainRankigs() {
        return MainRankigs;
    }

    public void setMainRankigs(ArrayList<rankingItems> mainRankigs) {
        MainRankigs = mainRankigs;
    }
}
