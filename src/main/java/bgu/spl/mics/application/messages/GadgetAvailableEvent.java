package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Report;

public class GadgetAvailableEvent implements Event<String> {
    private String gadget;
    private Report report;
    private String sender;

    public GadgetAvailableEvent(String gadget, Report report, String sender){
        this.gadget = gadget;
        this.report = report;
        this.sender = sender;
    }

    public String getGadget(){
        return gadget;
    }

    public Report getReport() {
        return report;
    }

    public String getSender() {
        return sender;
    }
}
