package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.*;
import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

import java.util.LinkedList;


/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) throws FileNotFoundException {
        JsonObject object = (JsonObject) new JsonParser().parse(new FileReader(args[0]));
        JsonArray inv = object.get("inventory").getAsJsonArray();
        JsonArray squ = object.get("squad").getAsJsonArray();
        JsonObject services = object.get("services").getAsJsonObject();
        Killer killer = new Killer(services.get("M").getAsInt());
        new Thread(killer).start();
        loadInventory(inv);
        loadSquad(squ);
        LinkedList<M> mList = new LinkedList<>();
        LinkedList<Moneypenny> mpList = new LinkedList<>();
        LinkedList<Intelligence> intelligenceList = new LinkedList<>();
        Q q = new Q();
        new Thread(q).start();
        createServices(services, mList, mpList, intelligenceList);
        for (Intelligence intelligence : intelligenceList) {
            new Thread(intelligence).start();
        }
        for (M m : mList) {
            new Thread(m).start();
        }
        for (Moneypenny mp : mpList) {
            new Thread(mp).start();
        }
        TimeService ts = new TimeService(services.get("time").getAsInt());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(ts).start();
        try {
            Thread.sleep(services.get("time").getAsInt() * 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Inventory.getInstance().printToFile(args[1]);
        Diary.getInstance().printToFile(args[2]);
    }


    private static void loadSquad(JsonArray squ){
        Squad squad = Squad.getInstance();
        Agent[] agents = new Agent[squ.size()];
        for(int i = 0; i < squ.size(); i++){
            JsonElement name = ((JsonObject) squ.get(i)).get("name");
            JsonElement serialNumber = ((JsonObject) squ.get(i)).get("serialNumber");
            Agent agent = new Agent();
            agent.setSerialNumber(serialNumber.getAsString());
            agent.setName(name.getAsString());
            agents[i] = agent;
        }
        squad.load(agents);
    }
    private static void loadInventory(JsonArray inv){
        Inventory inventory = Inventory.getInstance();
        String[] items = new String[inv.size()];
        for(int i = 0; i < items.length; i++) {
            items[i] = inv.get(i).getAsString();
        }
        inventory.load(items);
    }

    private static void createServices(JsonObject services, LinkedList<M> mList, LinkedList<Moneypenny> mpList, LinkedList<Intelligence> intelligenceList) {
        int mNumber = services.get("M").getAsInt();
        int mpNumber = services.get("Moneypenny").getAsInt();
        JsonArray allMissions = services.get("intelligence").getAsJsonArray();
        int iNumber = allMissions.size();
        for (int i = 0; i< iNumber; i++){
            JsonObject missions = allMissions.get(i).getAsJsonObject();
            JsonArray missionArray = missions.get("missions").getAsJsonArray();
            Intelligence intelligence = new Intelligence(i, missionArray);
            intelligenceList.add(intelligence);

        }
        for (int i = 1; i <= mNumber; i++){
            M m = new M(i);
            mList.add(m);
        }
        for (int i = 1; i <= mpNumber; i++){
            Moneypenny mp = new Moneypenny(i);
            mpList.add(mp);
        }

    }
}
