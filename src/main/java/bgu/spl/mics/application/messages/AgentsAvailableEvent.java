package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class AgentsAvailableEvent implements Event<String> {
    private List<String> agentSerialNumbers;

    public AgentsAvailableEvent(List<String> agentsSerialNumbers){
        this.agentSerialNumbers = agentsSerialNumbers;
    }

    public List<String> getAgentSerialNumbers(){
        return agentSerialNumbers;
    }

}
