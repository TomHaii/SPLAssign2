package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.List;

public class AgentsAvailableEvent implements Event<String>{
    private List<String> agentSerialNumbers;
    private Report report;

    public AgentsAvailableEvent(List<String> agentsSerialNumbers, Report report){
        this.agentSerialNumbers = agentsSerialNumbers;
        this.report = report;
    }

    public List<String> getAgentSerialNumbers(){
        return agentSerialNumbers;
    }


    public Report getReport() {
        return report;
    }
}
