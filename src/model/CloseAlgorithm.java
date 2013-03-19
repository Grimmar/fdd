package model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import parser.FileParser;

public class CloseAlgorithm extends ThreadedAlgorithm {

    private File file;
    private double minSupport;
    private CloseModelInterface model;

    public CloseAlgorithm() {
        file = null;
        minSupport = 0.1;
        model = null;
    }

    @Override
    protected void execute() {
        Set<Element> ffck = null;
        Set<Element> ff = null;
        List<Rule> rules = new ArrayList<Rule>();
        System.out.println("Démarrage de l'algorithme.");
        if (model == null) {
            initModel();
        }

        boolean stop = false;
        int k = 1;
        // k = 1, on récupère les générateurs de niveau 1
        ffck = initFFC();

        while (!stop) {
            // Fermeture et support
            ff = generateClosure(ffck);

            // Suppression des infréquents
            ff = removeInfrequents(ff);

            for (Element e : ff) {
                //je crée une copie de la fermeture de l'element
                Set<String> closure = new TreeSet<String>(e.getClosure().getItems());
                // pour pouvoir supprimer les items contenu dans l'element pour généré le coté droit de la regle.
                closure.removeAll(e.getItems());
                double lift = computeLift(e.getItems(), closure);
                double confidence = computeConfidence(e.getItems(), closure);

                Rule r = new Rule(e.getItems(), closure, e.getSupport(), confidence, lift);
                if (!closure.isEmpty()) {
                    rules.add(r);
                    System.out.println(r.toString());
                }
            }

            // Générer les nouveaux candidats
            k += 1;
            System.out.println("Génération des candidats de niveau " + k);
            Set<Element> candidates = generateCandidates(k, ff);

            // Tester l'arrêt pour arrêter l'algorithme
            if (candidates == null || candidates.isEmpty()) {
                stop = true;
                System.out.println("Arrêt du programme.");
            } else {
                ffck = candidates;
            }

        }
        this.stop();
    }

    private Set<Element> generateCandidates(int k, Set<Element> ffk) {

        /**
         * Phase 1 Les (k+1)-générateurs candidats sont créés en joignant les
         * k-générateurs de FFk qui possèdent les mêmes k-1 premiers items. Les
         * 3-générateurs {ABC} et {ABD} par exemple seront joints afin de créer
         * le 4-générateur candidat {ABCD}.
         */
        Set<Element> candidates = new HashSet<Element>();
        for (Element e : ffk) {
            for (Element e2 : ffk) {
                if (!e.equals(e2) && hasSameFirstItems(k, e, e2)) {
                    Element elem = new Element();
                    elem.setSupport(Math.min(e.getSupport(), e2.getSupport()));
                    elem.addItems(e.getItems());
                    elem.addItems(e2.getItems());
                    candidates.add(elem);
                }
            }
        }

        /**
         * Phase 2.
         */
        Set<Element> frequentsMinimal = new HashSet<Element>();

        Iterator<Element> it = candidates.iterator();
        while (it.hasNext()) {
            Element e = it.next();
            int counter = 0;

            Set<Set<String>> subsets = getSubsetsOfSize(k, e.getItems());
            for (Element el : ffk) {
                for (Set<String> s : subsets) {
                    if (el.getItems().containsAll(s)) {
                        counter++;
                    }
                }
            }
            if (counter == subsets.size()) {
                frequentsMinimal.add(e);
            }

        }

        /* Phase 3 */
        Set<Element> toRemove = new HashSet<Element>();
        for (Element e : frequentsMinimal) {
            for (Element ek : ffk) {
                if (ek.getClosure().equals(e)) {
                    toRemove.add(e);
                }
            }
        }
        frequentsMinimal.removeAll(toRemove);

        return frequentsMinimal;
    }

    private Set<Set<String>> getSubsetsOfSize(int k, Set<String> set) {
        Set<Set<String>> powerset = powerset(set);
        Iterator<Set<String>> it = powerset.iterator();
        while (it.hasNext()) {
            Set<String> s = it.next();
            if (s.size() != k - 1) {
                it.remove();
            }
        }
        return powerset;
    }

    private Set<Set<String>> powerset(final Set<String> set) {
        Set<Set<String>> powerset = new HashSet<Set<String>>();
        if (set.isEmpty()) {
            powerset.add(new HashSet<String>());
            return powerset;
        }
        List<String> list = new ArrayList<String>(set);
        String head = list.get(0);
        Set<String> rest = new HashSet<String>(list.subList(1, list.size()));
        for (Set<String> s : powerset(rest)) {
            Set<String> newSet = new HashSet<String>();
            newSet.add(head);
            newSet.addAll(s);
            powerset.add(newSet);
            powerset.add(s);
        }
        return powerset;
    }

    private boolean hasSameFirstItems(int k, Element e, Element e2) {
        if (k == 2) {
            return true;
        }

        int elementCommun = 0;
        for (String elem : e.getItems()) {
            if (e2.getItems().contains(elem)) {
                elementCommun++;
            }
        }
        if (elementCommun >= (k - 2)) {
            return true;
        }

        return false;
    }

    private void initModel() {
        model = FileParser.parse(file);
        if (model == null) {
            throw new IllegalArgumentException("Le fichier est invalide.");
        }
    }

    private Set<Element> initFFC() {
        Set<String> items = new TreeSet<String>();
        for (Line l : model.getNodes()) {
            for (String s : l.getItems()) {
                items.add(s);
            }
        }
        Set<Element> set = new HashSet<Element>();
        for (String s : items) {
            Element e = new Element();
            e.addItem(s);
            set.add(e);
        }
        return set;
    }

    private Set<Element> removeInfrequents(Set<Element> elems) {
        Set<Element> set = new HashSet<Element>();
        for (Element e : elems) {
            if (minSupport <= e.getSupport()) {
                set.add(e);
            }
        }
        return set;
    }

    private Set<Element> generateClosure(Set<Element> items) {
        Set<Element> set = new HashSet<Element>();
        for (Element e : items) {
            Element elem = model.generateClosures(e);
            if (elem != null || elem.getClosure() != null) {
                set.add(elem);
            }
        }
        return set;
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public void setFile(File f) {
        this.file = f;
        model = null;
    }

    @Override
    public void closeFile() {
        file = null;
    }

    public double getMinSupport() {
        return minSupport;
    }

    public void setMinSupport(double minSupport) {
        this.minSupport = minSupport;
    }

    public CloseModelInterface getModel() {
        return model;
    }

    public void setModel(CloseModelInterface d) {
        this.model = d;
    }

    public static void main(String[] args) {
        CloseAlgorithm algo = new CloseAlgorithm();
        CloseModelInterface d = new CloseModel();
        Line l = new Line();
        l.setIdentifier("1");
        l.addItem("A");
        l.addItem("C");
        l.addItem("D");
        d.add(l);
        l = new Line();
        l.setIdentifier("2");
        l.addItem("B");
        l.addItem("C");
        l.addItem("E");
        d.add(l);
        l = new Line();
        l.setIdentifier("3");
        l.addItem("A");
        l.addItem("B");
        l.addItem("C");
        l.addItem("E");
        d.add(l);
        l = new Line();
        l.setIdentifier("4");
        l.addItem("B");
        l.addItem("E");
        d.add(l);
        l = new Line();
        l.setIdentifier("5");
        l.addItem("A");
        l.addItem("B");
        l.addItem("C");
        l.addItem("E");
        d.add(l);
        l = new Line();
        l.setIdentifier("6");
        l.addItem("B");
        l.addItem("C");
        l.addItem("E");
        d.add(l);
        algo.setModel(d);
        algo.start();
    }

    private double computeLift(Set<String> left, Set<String> right) {
        double s = model.computeSupport(left) * model.computeSupport(right);
        return getRuleSupport(left, right) / s;
    }

    private double computeConfidence(Set<String> left, Set<String> right) {
        return getRuleSupport(left, right) / model.computeSupport(left);
    }

    private double getRuleSupport(Set<String> left, Set<String> right) {
        Set<String> all = new TreeSet<String>();
        all.addAll(left);
        all.addAll(right);
        double ruleSupport = model.computeSupport(all);
        all.clear();
        return ruleSupport;
    }
}
