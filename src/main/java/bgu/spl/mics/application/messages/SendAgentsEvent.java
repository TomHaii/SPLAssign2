package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;


import java.awt.*;
import java.util.List;

public class SendAgentsEvent implements Event<String> {
    private List<String> agentSerialNumbers;
    private int time;
    private String sender;

    public SendAgentsEvent(List<String> agentsSerialNumbers, int time, String sender){
        this.agentSerialNumbers = agentsSerialNumbers;
        this.time = time;
        this.sender = sender;
    }

    public List<String> getAgentSerialNumbers(){
        return agentSerialNumbers;
    }

    public int getTime() {
        return time;
    }


    public String getSender() {
        return sender;
    }
}
