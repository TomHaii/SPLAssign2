package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class ReleaseAgentsEvent implements Event<String> {
    private List<String> agentSerialNumbers;
    private String sender;

    public ReleaseAgentsEvent(List<String> agentsSerialNumbers, String sender){
        this.sender = sender;
        this.agentSerialNumbers = agentsSerialNumbers;
    }

    public List<String> getAgentSerialNumbers() {
        return agentSerialNumbers;
    }

    public String getSender() {
        return sender;
    }
}
