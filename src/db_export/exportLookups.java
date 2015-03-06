package db_export;

import java.util.HashMap;

/**
 * Created by jgibson on 3/6/2015.
 */
public class exportLookups {

    public static final HashMap<String, Integer> analyst_lookup;
    static {
        analyst_lookup = new HashMap<>();
        analyst_lookup.put("KB", 2);
        analyst_lookup.put("JMG",1);
        analyst_lookup.put("JG", 1);
        analyst_lookup.put("KAB",2);
        analyst_lookup.put("KRH",3);
        analyst_lookup.put("TV", 4);
        analyst_lookup.put("EHS",5);
        analyst_lookup.put("EH",5);
        analyst_lookup.put("JW",6);
        analyst_lookup.put("MAH",7);
        analyst_lookup.put("CB",8);
        analyst_lookup.put("LLBH", 20);
    }
}
