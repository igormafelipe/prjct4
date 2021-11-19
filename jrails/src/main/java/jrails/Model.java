package jrails;

import java.io.BufferedWriter;
import java.io.File; // Import the File class
import java.io.FileWriter;
import java.io.IOException; // Import the IOException class to handle errors
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import java.lang.reflect.*;
import java.nio.file.Files;
import java.security.InvalidParameterException;

public class Model {
    int id = 0;
    static int next_avail_id;

    private static File fileCreation() {
        try {
            File myObj = new File("database.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
            return myObj;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
    }

    // Can be optmized by not rewriting whole file at all times, but appending if
    // needed.
    public void save() {
        Field[] fields = this.getClass().getFields();
        File file = fileCreation();
        FileWriter fw;
        BufferedWriter bw;
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            fw = new FileWriter(file.getAbsoluteFile(), false);
            bw = new BufferedWriter(fw);
            String curr_line = "";
            // Creates line to be added based on curr object
            for (int i = 0; i < fields.length; i++) {
                curr_line += String.valueOf(fields[i].get(this));
                if (i + 1 != fields.length) {
                    curr_line += ",";
                }
            }

            // Adds new line to or rewrites specified id spot on database
            if (this.id() == 0) {
                lines.add(curr_line);
                id = lines.size();
            } else {
                lines.set(this.id - 1, curr_line);
            }

            // Rewrites database with new data
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
            bw.close();
            fw.close();
        } catch (Throwable e) {
            System.out.println("Problem writting to file. Model.java save");
            throw new InvalidParameterException();
        }
    }

    public int id() {
        return this.id;
    }

    public static <T> T find(Class<T> c, int id) {
        Field[] fields = c.getClass().getFields();
        File file = fileCreation();
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            System.out.println("here1");
            if (id >= lines.size()) {
                System.out.println("here2");
                return null;
            } else {
                System.out.println("here3");
                String objectParams = lines.get(id - 1);
                String[] splitted = objectParams.split(",");

                System.out.println("here4");
                System.out.println(c.getClass());
                Constructor<T> constr = c.getConstructor();
                System.out.println("here5");
                Object obj = constr.newInstance();
                System.out.println("here6");
                for (int i = 0; i < fields.length; i++) {
                    fields[i].set(obj, splitted[i]);
                    Field objId = c.getField("id");
                    objId.set(obj, id);
                }
                return (T) obj;
            }
        } catch (Throwable e) {
            System.out.println(e);
            throw new InvalidParameterException();
        }
    }

    public static <T> List<T> all(Class<T> c) {
        // Returns a List<element type>
        throw new UnsupportedOperationException();
    }

    public void destroy() {
        throw new UnsupportedOperationException();
    }

    public static void reset() {
        throw new UnsupportedOperationException();
    }
}
