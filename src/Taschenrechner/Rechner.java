package Taschenrechner;

import java.util.ArrayList;

public class Rechner {

    //Die Verschiedenen möglichen Operatoren
    public final static int OPERATOR_PLUS = 0;
    public final static int OPERATOR_MINUS = 1;
    public final static int OPERATOR_MULTIPLY = 2;
    public final static int OPERATOR_DIVIDE = 3;
    public final static int OPERATOR_POW = 4;
    // public final static int OPERATOR_SQRT = 5;

    //Außerdem noch Sin,Cos und Tan Funktionen
    public final static int OPERATOR_SIN = 10;
    public final static int OPERATOR_COS = 11;
    public final static int OPERATOR_TAN = 12;
    //Hyperbel Funktionen
    public final static int OPERATOR_SINH = 13;
    public final static int OPERATOR_COSH = 14;
    public final static int OPERATOR_TANH = 15;
    //Mögliche Werte von Ganzen Zahlen
    public static final String DIGITS = "0123456789";
    //Und für Zahlen mit nachkommastellen
    public static final String DIGITS_CONTENTS = DIGITS + ".,";

    //Konstruktor leer da die Klasse bereits in der Main Klasse als neues Objekt angelegt wurde
    Rechner() {
    }

    //Die Methode die den Input von der input Methode aufnimmt und diesen an den rechner weitergibt
    public double rechnerstarten(String Rechnung) throws Exception {
        //Betrag Quelle zu matches [Quelle:https://www.tutorialspoint.com/java/java_string_matches.htm]
        if(Rechnung.contains("|")){
            Rechnung = Rechnung.replaceAll("\\|","");
            double db = calcKlammern(inputueberpruefen(Rechnung));
            db = Double.parseDouble(String.valueOf(db).replaceAll("-",""));
            return db;
        }
        return calcKlammern(inputueberpruefen(Rechnung));
    }

    private double calcKlammern(String str) throws Exception {
        //Klammerrechnung
        String[] parts = klammerrechnung(str);
        //Tested ob klammerrechnung null zurückgegeben hat, was heißt dass es keine zu lösenden Klammern mehr gibt
        if (parts == null) {
            return calcSingleStr(str);
        }
        //Rekursion zur Lösung der Rechnung
        parts[1] = String.valueOf(calcKlammern(parts[1]));

        if (parts[0].length() != 0) {
            boolean operatorTest = true;
            if (parts[0].length() >= 3) {
                int operator = getOperator(parts[0].substring(parts[0].length() - 3));
                if (operator != -1) {
                    operatorTest = false;
                    parts[0] = parts[0].substring(0, parts[0].length() - 3);
                    parts[1] = String.valueOf(calc2(Double.valueOf(parts[1]), operator));
                }
            }
            if (operatorTest) {
                int operator = getOperator(String.valueOf(parts[0].charAt(parts[0].length() - 1)));
                //Fügt ein Malzeichen an falls z.b. "10(8+2)" geschrieben wurde"
                if (operator == -1) parts[0] = parts[0] + "*";
                else {
                    switch (operator) {
                        case OPERATOR_POW -> {
                            parts[0] = parts[0].substring(0, parts[0].length() - 1);
                            parts[1] = String.valueOf(calc2(Double.valueOf(parts[1]), operator));
                        }
                    }
                }
            }
        }

        if (parts[2].length() != 0)
            if (getOperator(String.valueOf(parts[2].charAt(0))) == -1) parts[2] = "*" + parts[2];

        //Rekursion
        return calcKlammern(parts[0] + parts[1] + parts[2]);
    }

    //Für die Klammerregeln zuständig
    private String[] klammerrechnung(String str) throws Exception {
        //Zählvariablen für: Anzahl der offenen Klammern, Klammer Anfang und Klammer Ende
        int offeneklammern = 0, clipOpen = -1, clipClose = -1;
        //Schleife die den String von Anfang bis Ende durchgeht
        for (int i = 0; i < str.length(); i++) {
            //Speichert den aktuellen Buchstaben an der Stelle i des übergebenen Strings str in einer Variable für den folgenden Vergleich
            String aktuellerString = String.valueOf(str.charAt(i));
            //Schaut ob eine Klammer geöffnet wurde
            if (aktuellerString.equals("(")) {
                //und wenn dies die erste offene Klammer ist wird die Stelle gespeichert
                if (clipOpen == -1) {
                    clipOpen = i;
                }
                //Zählt die Anzahl der noch offenen Klammern mit
                offeneklammern++;
            }
            //Schaut ob eine Klammer geschlossen wird
            else if (aktuellerString.equals(")")) {
                //In der Mathematik dürfen nicht mehr Klammern geschlossen (oder geöffnet) sein als geöffnet (oder geschlossen)
                //also wird ein Fehler zurückgegeben
                if (offeneklammern == 0) {
                    throw new Exception("Fehlende geöffnete Klammer: " + str);
                }
                //Das Ende der Klammer wird gespeichert, diese Variable wird überschrieben wenn eine weitere geschlossene gefunden wird
                clipClose = i;
                //Eine geschlossene Klammer wurde gefunden also wird von dem Verhältnis offene/geschlossene Klammern eins abgezogen
                offeneklammern--;
                //wenn das Verhältnis 0 ist wurde eine Klammereinheit identifiziert und die Schleife abgebrochen
                if (offeneklammern == 0) {
                    break;
                }
            }
        }
        //Wenn keine Klammer vorhanden ist gibt es auch nichts zu schachteln also wird null zurückgegeben
        if (clipOpen == -1) return null;
        //Wenn nur eine Klammer geöffnet wurde aber nicht geschlossen ist die Rechnung ungültig
        if (offeneklammern != 0) throw new Exception("Fehlende geschlossene Klammer: " + str);
        //Die Inhalte der Klammer werden zurückgegeben
        return new String[]{str.substring(0, clipOpen), str.substring(clipOpen + 1, clipClose), str.substring(clipClose + 1)};
    }

    private double calcSingleStr(String str) throws Exception {
        if (str.length() == 0) {
            throw new Exception("Missing operation: " + str);
        }
        ArrayList<String> parts = new ArrayList<>();
        while (true) {
            int posEnd;
            if (String.valueOf(str.charAt(0)).equals("-")) {
                posEnd = nextNumber(str.substring(1));
                posEnd += 1;
            } else {
                posEnd = nextNumber(str);
            }
            parts.add(str.substring(0, posEnd));
            if (posEnd > str.length() - 1) break;
            parts.add(String.valueOf(str.charAt(posEnd)));
            str = str.substring(posEnd + 1);
        }
        if (parts.size() < 3) return Double.valueOf(str);
        while (true) {
            boolean pointFound = false;
            for (int i = 1; i < parts.size(); i += 2) {
                int operator = getOperator(parts.get(i));
                if (operator == -1) throw new Exception("Wrong operator: " + str);
                if (operator == OPERATOR_MULTIPLY || operator == OPERATOR_DIVIDE || operator == OPERATOR_POW) {
                    pointFound = true;
                    parts.set(i - 1, String.valueOf(calc(Double.valueOf(parts.get(i - 1)), Double.valueOf(parts.get(i + 1)), operator)));
                    parts.remove(i);
                    parts.remove(i);
                    break;
                }
            }
            if (!pointFound) break;
        }
        double retVal = Double.valueOf(parts.get(0));
        for (int i = 1; i < parts.size(); i += 2)
            retVal = calc(retVal, Double.valueOf(parts.get(i + 1)), getOperator(parts.get(i)));
        return retVal;
    }

    private int nextNumber(String str) {
        int i = 0;
        for (; i < str.length(); i++) if (!DIGITS_CONTENTS.contains(String.valueOf(str.charAt(i)))) return i;
        if (i == 0) return -1;
        return i;
    }

    private double calc(double d1, double d2, int operator) throws Exception {
        return switch (operator) {
            case OPERATOR_PLUS -> d1 + d2;
            case OPERATOR_MINUS -> d1 - d2;
            case OPERATOR_MULTIPLY -> d1 * d2;
            case OPERATOR_DIVIDE -> d1 / d2;
            case OPERATOR_POW -> Math.pow(d1, d2);
            default -> throw new Exception("Unbekanntes Rechenzeichen:" + operator);
        };
    }

    private double calc2(double d, int operator) throws Exception {
        return switch (operator) {
            case OPERATOR_SIN -> Math.sin(d);
            case OPERATOR_COS -> Math.cos(d);
            case OPERATOR_TAN -> Math.tan(d);
            case OPERATOR_SINH -> Math.sinh(d);
            case OPERATOR_COSH -> Math.cosh(d);
            case OPERATOR_TANH -> Math.tanh(d);
            default -> throw new Exception("Unbekanntes Rechenzeichen:" + operator);
        };
    }

    private int getOperator(String str) {
        if (str.length() == 3) {
            return switch (str) {
                case "sin" -> OPERATOR_SIN;
                case "cos" -> OPERATOR_COS;
                case "tan" -> OPERATOR_TAN;
                case "sih"-> OPERATOR_SINH;
                case "coh"-> OPERATOR_COSH;
                case "tah"-> OPERATOR_TANH;
                default -> -1;
            };
        }
        return switch (str) {
            case "+" -> OPERATOR_PLUS;
            case "-" -> OPERATOR_MINUS;
            case "*" -> OPERATOR_MULTIPLY;
            case "/" -> OPERATOR_DIVIDE;
            case "^" -> OPERATOR_POW;
            //case "√" -> OPERATOR_SQRT;
            default -> -1;
        };
    }

    //Entfernt Typische Tippfehler des Benutzers
    private String inputueberpruefen(String inp) {
        //Leerzeichen
        inp = inp.replaceAll("\\s", "");
        //Bring sinh, cosh und tanh auf 3 Zeichen damit es wie sin cos und tan behandelt werden kann
        inp = inp.replaceAll("sinh","sih");
        inp = inp.replaceAll("cosh","coh");
        inp = inp.replaceAll("tanh","tah");
        //Konstanten
        inp = inp.replaceAll("e","2.7182818284");
        inp = inp.replaceAll("φ","1.6180339887");
        inp = inp.replaceAll("π","3.1415926535");
        inp = inp.replaceAll("θ","1.3063778838");
        inp = inp.replaceAll("γ","0.5772156649");
        inp = inp.replaceAll("λ","0.6243299885");
        return inp;
    }
}