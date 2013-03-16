package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import model.CloseModel;
import model.CloseModelInterface;
import model.Line;

public class FileParser {

    public static CloseModelInterface parse(File f) {
        if (!f.canRead()) {
            throw new IllegalStateException();
        }
        CloseModelInterface model = new CloseModel();
        try {
            InputStream ips = new FileInputStream(f);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String ligne = null;
            while ((ligne = br.readLine()) != null) {
                String split[] = ligne.split("\\|");
                Line r = new Line();
                r.setIdentifier(split[0]);
                for (int i = 1; i < split.length; i++) {
                    r.addItem(split[i]);
                }
                model.add(r);
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return model;
    }
}
