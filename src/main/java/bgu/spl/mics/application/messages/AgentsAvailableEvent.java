package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.List;

public class AgentsAvailableEvent implements Event<String>{
    private List<String> agentSerialNumbers;
    private Report report;
    private String sender;

    public AgentsAvailableEvent(List<String> agentsSerialNumbers, Report report, String sender){
        this.agentSerialNumbers = agentsSerialNumbers;
        this.report = report;
        this.sender = sender;
    }

    public List<String> getAgentSerialNumbers(){
        return agentSerialNumbers;
    }


    public Report getReport() {
        return report;
    }

    public String getSender() {
        return sender;
    }
}
