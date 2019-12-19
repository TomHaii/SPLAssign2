package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class ReleaseAgentsEvent implements Event<Boolean> {
    private List<String> agentSerialNumbers;

    public ReleaseAgentsEvent(List<String> agentsSerialNumbers){
        this.agentSerialNumbers = agentsSerialNumbers;
    }

    public List<String> getAgentSerialNumbers() {
        return agentSerialNumbers;
    }
}
