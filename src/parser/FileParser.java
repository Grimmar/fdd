package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import model.CloseModelInterface;
import model.CloseModel;
import model.Line;

public class FileParser {

    private static final String LINE_VALIDATOR_REGEX = "^[0-9]+(\\|[a-zA-Z]+[a-zA-Z0-9]*)+";

    public static CloseModelInterface parse(File f) throws FileNotFoundException, IOException {
        if (!f.canRead()) {
            throw new IllegalStateException();
        }
        CloseModelInterface model = new CloseModel();
        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(f)));
        String s;
        while ((s = br.readLine()) != null) {
            if (s.matches(LINE_VALIDATOR_REGEX)) {
                String split[] = s.split("\\|");
                Line l = new Line();
                l.setIdentifier(split[0]);
                for (int i = 1; i < split.length; i++) {
                    l.addItem(split[i]);
                }
                model.add(l);
            }

        }

        br.close();
        return model;
    }
}
