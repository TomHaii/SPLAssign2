package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Report;

import java.util.List;

public class Reports {
    private List<Report> reports;
    private int total;

    public Reports(List<Report> reportList, int totalReports){
        this.reports = reportList;
        this.total = totalReports;
    }
}
