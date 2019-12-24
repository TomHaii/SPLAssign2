package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.List;

public class AgentsAvailableEvent implements Event<String>{
    private List<String> agentSerialNumbers;
    private Report report;
    private String sender;
    private Future<String> future;
    private int time;

    public AgentsAvailableEvent(List<String> agentsSerialNumbers, Report report, String sender, int time, Future<String> future){
        this.agentSerialNumbers = agentsSerialNumbers;
        this.report = report;
        this.sender = sender;
        this.time = time;
        this.future = future;
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

    public Future<String> getFuture() {
        return future;
    }

    public int getTime() {
        return time;
    }
}
