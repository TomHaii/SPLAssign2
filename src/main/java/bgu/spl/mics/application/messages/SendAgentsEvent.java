package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;


import java.awt.*;
import java.util.List;

public class SendAgentsEvent implements Event<Boolean> {
    private List<String> agentSerialNumbers;

    public SendAgentsEvent(List<String> agentsSerialNumbers){
        this.agentSerialNumbers = agentsSerialNumbers;
    }

    public List<String> getAgentSerialNumbers(){
        return agentSerialNumbers;
    }
}
