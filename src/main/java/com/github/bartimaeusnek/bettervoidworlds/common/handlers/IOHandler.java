package com.github.bartimaeusnek.bettervoidworlds.common.handlers;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class IOHandler {
    public final LinkedHashMap<Integer, Integer> idDid = new LinkedHashMap<>();
    final Configuration C;

    public IOHandler(String path) {
        C = new Configuration(new File(path));
        int[] id = C.get("SYSTEM", "ID", new int[0]).getIntList(), did = C.get("SYSTEM", "DID", new int[0]).getIntList();

        for (int i = 0; i < id.length; i++) {
            idDid.put(id[i], did[i]);
        }

    }

    public void addToAndSave(int id, int did) {
        idDid.put(id, did);
        save();
    }

    public void save() {
        ArrayList<Integer> ID = new ArrayList<>();
        ArrayList<Integer> DID = new ArrayList<>();
        for (Integer i : idDid.keySet()) {
            ID.add(i);
            DID.add(idDid.get(i));
        }
        int[] ida = new int[ID.size()];
        int[] idb = new int[ID.size()];
        for (int i = 0; i < ID.size(); i++) {
            ida[i] = ID.get(i);
        }
        for (int i = 0; i < ID.size(); i++) {
            idb[i] = DID.get(i);
        }
        C.get("SYSTEM", "ID", new int[0]).set(ida);
        C.get("SYSTEM", "DID", new int[0]).set(idb);
        if (C.hasChanged())
            C.save();
    }


}
