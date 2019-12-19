package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;


import java.awt.*;
import java.util.List;

public class SendAgentsEvent implements Event<Boolean> {
    private List<String> agentSerialNumbers;
    private int time;

    public SendAgentsEvent(List<String> agentsSerialNumbers, int time){
        this.agentSerialNumbers = agentsSerialNumbers;
        this.time = time;
    }

    public List<String> getAgentSerialNumbers(){
        return agentSerialNumbers;
    }

    public int getTime() {
        return time;
    }
}
