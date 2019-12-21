package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Agent;

public class JsonParse {

    private String[] inventory;
    private Services services;
    private Agent[] agents;


    public String[] getInventory() {
        return inventory;
    }

    public Services getServices() {
        return services;
    }

    public Agent[] getAgents() {
        return agents;
    }
}
