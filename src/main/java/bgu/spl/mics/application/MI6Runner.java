package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
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
        if(args.length != 1){
            System.out.println("Invalid Arguments");
            return;
        }
        Gson gson = new Gson();
        JsonObject object = (JsonObject) new JsonParser().parse(new FileReader(args[0]));
        JsonArray inv = object.get("inventory").getAsJsonArray();
        JsonArray squ =  object.get("squad").getAsJsonArray();
        JsonArray services = object.get("services").getAsJsonArray();
        loadInventory(inv);
        loadSquad(squ);
        LinkedList<M> mList = new LinkedList<>();
        LinkedList<Moneypenny> mpList = new LinkedList<>();
        LinkedList<Intelligence> intelligenceList = new LinkedList<>();
        Q q = new Q();
        TimeService ts = new TimeService(services.get(3).getAsInt());
        createServices(services, mList, mpList, intelligenceList);
    }


    private static void loadSquad(JsonArray squ){
        Squad squad = Squad.getInstance();
        Agent[] agents = new Agent[squ.size()];
        for(int i = 0; i < squ.size(); i++){
            JsonElement name = ((JsonObject) squ.get(i)).get("name");
            JsonElement serialNumber = ((JsonObject) squ.get(i)).get("serialNumber");
            Agent agent = new Agent();
            agent.setSerialNumber(serialNumber.toString());
            agent.setName(name.toString());
            agents[i] = agent;
        }
        squad.load(agents);
    }
    private static void loadInventory(JsonArray inv){
        Inventory inventory = Inventory.getInstance();
        String[] items = new String[inv.size()];
        for(int i = 0; i < items.length; i++){
            items[i] = inv.get(i).toString();
        }
        inventory.load(items);
    }

    private static void createServices(JsonArray services, LinkedList<M> mList, LinkedList<Moneypenny> mpList, LinkedList<Intelligence> intelligenceList) {
        int mNumber = services.get(0).getAsInt();
        int mpNumber = services.get(1).getAsInt();
        JsonArray allMissions = services.get(2).getAsJsonArray();
        int iNumber = allMissions.size();
        for (int i = 0; i< iNumber; i++){
            JsonArray missions = allMissions.get(i).getAsJsonArray();
            Intelligence intelligence = new Intelligence(i, missions);
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
