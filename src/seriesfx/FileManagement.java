package seriesfx;

import java.io.*;
import java.util.ArrayList;

public class FileManagement {

    File f;

    public FileManagement(String name) {f = new File(name);}

    public void writeData(ArrayList list) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(list);
            oos.close();
        } catch (Exception ignored) {}
    }

    public ArrayList readData(ArrayList list) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            list = (ArrayList<Info>) ois.readObject();
            ois.close();
        } catch (Exception ignored) {}
        return list;
    }
}