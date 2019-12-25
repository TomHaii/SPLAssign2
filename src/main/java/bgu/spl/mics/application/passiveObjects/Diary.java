package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.application.Reports;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary {
	private static class DiarySingletonHolder {
		private static Diary instance = new Diary();
	}
	private List<Report> reports;
	private AtomicInteger totalMissions = new AtomicInteger(0);

	private Diary(){
		reports = new LinkedList<Report>();
	}
	/**
	 * Retrieves the single instance of this class.
	 */
	public static Diary getInstance() {
		return DiarySingletonHolder.instance;
	}

	public List<Report> getReports() {
		return reports;
	}

	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public void addReport(Report reportToAdd){
		if(reportToAdd != null) {
			synchronized (this) {
				reports.add(reportToAdd);
			}
		}
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename){
		try {
			Reports repos = new Reports(getReports(), getTotal());
			Writer writer = new FileWriter(filename);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(repos, writer);
			writer.flush();
			writer.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the total number of received missions (executed / aborted) be all the M-instances.
	 * @return the total number of received missions (executed / aborted) be all the M-instances.
	 */
	public int getTotal(){
		return totalMissions.get();
	}
	/**
	 * Increments the total number of received missions by 1
	 */
	public void incrementTotal(){
		totalMissions.compareAndSet(totalMissions.get(), totalMissions.get()+1);
	}
}
