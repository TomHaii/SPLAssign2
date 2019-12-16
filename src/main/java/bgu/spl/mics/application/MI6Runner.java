package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import javax.xml.ws.Response;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) throws FileNotFoundException {
        // TODO Implement this
        if(args.length != 1){
            System.out.println("Invalid Arguments");
            return;
        }
        Gson gson = new Gson();
        JsonObject object = (JsonObject) new JsonParser().parse(new FileReader(args[0]));
        JsonArray inv = (JsonArray) object.get("inventory");
        JsonArray squ = (JsonArray) object.get("squad");
        JsonArray services = (JsonArray) object.get("services");
        loadInventory(inv);
        loadSquad(squ);
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
}
