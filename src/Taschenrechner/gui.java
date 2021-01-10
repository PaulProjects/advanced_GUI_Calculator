package Taschenrechner;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class gui implements ActionListener {

    //Konstruktor
    public gui() {
    }

    //Methode zum Überschreiben
    public void actionPerformed(ActionEvent ae) {
    }

    public static void main(String[] args) {
        //Taschenrechner.Rechner als Objekt anlegen
        Rechner prechner = new Rechner();

        //Erzeugung des Panels auf dem der Taschenrechner.Rechner und die Liste sitzen werden
        JFrame jmain = new JFrame("Pauls Taschenrechner.Rechner");
        Container c = jmain.getContentPane();
        //Fenster kann nicht kleiner/größer gemacht werden
        //Löst Probleme mit Resizing etc.
        jmain.setResizable(false);
        //Dark Design

        //Erzeugung des Taschenrechner.Rechner Fensters
        JPanel p1 = new JPanel();
        p1.setLayout(new BorderLayout());
        //Hintergrundfarbe ändern
        p1.setBackground(Color.WHITE);

        //Erzeugung des Antwort Fensters
        JLabel loesung = new JLabel("Lösung:");
        //Erlaubt das Ändern der Hintergrundsfarbe [Quelle:https://stackoverflow.com/questions/2380314/how-do-i-set-a-jlabels-background-color]
        loesung.setOpaque(true);
        //Hintergrundfarbe ändern
        loesung.setBackground(Color.WHITE);

        //Erzeugung der Listenansicht [Quelle:https://www.educba.com/jlist-in-java/]
        DefaultListModel<String> model = new DefaultListModel<>();
        final JList<String> rechnungen = new JList<>(model);
        rechnungen.setLayoutOrientation(JList.VERTICAL);
        //Hintergrundfarbe
        rechnungen.setBackground(Color.WHITE);

        //Die Liste scrollbar machen
        JScrollPane scrollPaneRechnungen = new JScrollPane();
        scrollPaneRechnungen.setViewportView(rechnungen);
        scrollPaneRechnungen.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneRechnungen.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        //Label welches den Titel der Liste trägt
        JLabel label = new JLabel("Letzte Rechnungen:");

        //Textfeld welches den Userinput zeigt [Quelle:https://www.geeksforgeeks.org/java-swing-jtextfield/]
        final JTextField textFeld = new JTextField(1);
        //Setzt den Cursor automatisch in das Textfeld, der User muss es nicht mehr auswählen [Quelle:https://stackoverflow.com/questions/18908902/set-cursor-on-a-jtextfield]
        textFeld.requestFocusInWindow();

        //Falls die Eingabe groß wird eine Scroll Funktion [Quelle:https://www.javatpoint.com/java-jscrollpane]
        JScrollPane scrollPaneTextFeld = new JScrollPane(textFeld);
        scrollPaneTextFeld.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneTextFeld.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPaneTextFeld.setViewportView(textFeld);

        //Text Art und größe
        Font myFontSize = textFeld.getFont().deriveFont(Font.BOLD, 50f);
        textFeld.setFont(myFontSize);

        //Menüleiste anlegen [Quelle:https://www.javatpoint.com/java-jmenuitem-and-jmenu]
        JMenuItem i1, i2, i3, i4;

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Edit");
        i1 = new JMenuItem("Copy");
        i2 = new JMenuItem("Paste");
        i3 = new JMenuItem("Cut");
        i4 = new JMenuItem("Clear List");
        menu.add(i1);
        menu.add(i2);
        menu.add(i3);
        menu.add(i4);
        menuBar.add(menu);

        //Copy Knopf
        i1.addActionListener(actionEvent -> {
            //Setzt den Inhalt des Textfeldes in den Zwischenspeicher [Quelle.https://stackoverflow.com/questions/11596368/set-clipboard-contents]
            StringSelection textFeldContent = new StringSelection(textFeld.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(textFeldContent, textFeldContent);
        });

        //Paste Knopf
        i2.addActionListener(actionEvent -> {
            //Setzt den Inhalt des Clipboard hinter den Cursor [Quelle:https://stackoverflow.com/questions/6631933/how-would-i-make-a-paste-from-java-using-the-system-clipboard]
            try {
                String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                String global = textFeld.getText();
                int j = textFeld.getCaretPosition();
                global = global.substring(0, j) + data + global.substring(j);
                //Cursor hinter das Eingefügt setzen
                textFeld.setText(global);
                textFeld.requestFocusInWindow();
                textFeld.setCaretPosition(j + data.length());
            } catch (UnsupportedFlavorException | IOException e) {
                e.printStackTrace();
            }
        });

        //Cut
        i3.addActionListener(actionEvent -> {
            try {
                StringSelection textFeldContent = new StringSelection(textFeld.getText());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(textFeldContent, textFeldContent);
                textFeld.setText(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //Clear List
        i4.addActionListener(actionEvent -> {
            DefaultListModel<String> listModel = (DefaultListModel<String>) rechnungen.getModel();
            listModel.removeAllElements();
        });

				/*
				Modus wechsler [Quelle:https://stackoverflow.com/a/30702197]
				 */
        JMenu modemenu = new JMenu("Modus");
        JCheckBoxMenuItem basicmenuitem = new JCheckBoxMenuItem("Basic");
        basicmenuitem.setState(true);
        JCheckBoxMenuItem erweitertmenuitem = new JCheckBoxMenuItem("Erweitert");

        modemenu.add(basicmenuitem);
        modemenu.add(erweitertmenuitem);
        menuBar.add(modemenu);

        //Splitpane 4 hier schon benötigt daher schon hier instanziert
        JSplitPane splitpane4 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitpane4.setDividerSize(0);

        //Actionlistener für den Modus Wechsler
        basicmenuitem.addActionListener(actionEvent -> {
            if (!erweitertmenuitem.getState()) {
                basicmenuitem.setState(true);
            }
            erweitertmenuitem.setState(false);
            jmain.setSize(850, 850);
            splitpane4.setDividerLocation(900);
        });

        erweitertmenuitem.addActionListener(actionEvent -> {
            if (!basicmenuitem.getState()) {
                erweitertmenuitem.setState(true);
            }
            basicmenuitem.setState(false);
            //40 mehr pro Reihe
            jmain.setSize(1270, 850);
            //-100 pro Reihe
            splitpane4.setDividerLocation(jmain.getSize().width - 300);
        });

				/*
				Gibt Fenstergröße aus
				 */
				/*
				jmain.addComponentListener(new ComponentListener() {
					@Override
					public void componentResized(ComponentEvent componentEvent) {
						Rectangle r = jmain.getBounds();
						System.out.println(r.height);
						System.out.println(r.width);
					}

					@Override
					public void componentMoved(ComponentEvent componentEvent) {
					}

					@Override
					public void componentShown(ComponentEvent componentEvent) {
					}

					@Override
					public void componentHidden(ComponentEvent componentEvent) {
					}
				});
				*/
				/*
					Knöpfe
				 */
        		/*
				Modus wechsler [Quelle:https://stackoverflow.com/a/30702197]
				 */

        //Menü um zwischen Dark und Light mode umzuschalten
        /*JMenu lookmenu = new JMenu("Aussehen");
        JCheckBoxMenuItem whitemi = new JCheckBoxMenuItem("Hell");
        whitemi.setState(true);
        JCheckBoxMenuItem darkmi = new JCheckBoxMenuItem("Dunkel");

        lookmenu.add(whitemi);
        lookmenu.add(darkmi);
        menuBar.add(lookmenu);

        whitemi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!darkmi.getState()) {
                    whitemi.setState(true);
                }
                darkmi.setState(false);
                //textfeld [Quelle:https://stackoverflow.com/a/34218295]
                textFeld.setForeground(Color.BLACK);
                textFeld.setBackground(Color.WHITE);
                //Liste
                rechnungen.setBackground(Color.WHITE);
                rechnungen.setForeground(Color.BLACK);
                //Label über Liste
                label.setBackground(Color.WHITE);
                label.setForeground(Color.BLACK);
            }
        });

        darkmi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!whitemi.getState()) {
                    darkmi.setState(true);
                }
                whitemi.setState(false);

                textFeld.setForeground(Color.WHITE);
                textFeld.setBackground(Color.BLACK);

                rechnungen.setBackground(Color.BLACK);
                rechnungen.setForeground(Color.WHITE);

                label.setBackground(Color.BLACK);
                label.setForeground(Color.WHITE);
            }
        });*/

        //Gridbag [Quelle:https://riptutorial.com/de/swing/example/10132/wie-funktioniert-gridbaglayout-]
        p1.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

				/*
				Knöpfe
				Layout:
				AC (    ) .   ESC
				7  8    9 *   /
				4  5    6 +   -
				1  2    3 wz  ^
				  0     C   =
			 */
        //AC
        Icon n0ic = new ImageIcon("ressource/icons/AC.png");
        final JButton n0 = new JButton(n0ic);
        //Gridbag layout setzen [Quelle:https://www.tutorialspoint.com/swing/swing_gridbaglayout.htm]
        gbc.gridx = 0;
        gbc.gridy = 0;
        p1.add(n0, gbc);
        //ActionListener [Quelle:https://www.javatpoint.com/java-actionlistener]
        n0.addActionListener(e -> {
            textFeld.setBackground(Color.white);
            textFeld.setText(null);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(0);
        });

        // (
        Icon n1ic = new ImageIcon("ressource/icons/(.png");
        final JButton n1 = new JButton(n1ic);
        gbc.gridx = 1;
        gbc.gridy = 0;
        p1.add(n1, gbc);
        n1.addActionListener(e -> {
            //Speichert das Rechensymbol in num1
            String num1 = "(";
            //Speichert den Text in der Rechenbox in global
            String global = textFeld.getText();
            //Speichert den Standort des Cursors [Quelle:https://stackoverflow.com/questions/11018837/finding-the-cursor-text-position-in-jtextfield]
            int j = textFeld.getCaretPosition();
            //Fügt das Zeichen an der Stelle ein [Quelle:https://www.baeldung.com/java-add-character-to-string]
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            //setzt das textFeld wieder in den Fokus
            textFeld.requestFocusInWindow();
            //Setzt den Cursor hinter da gesetzte Element [Quelle:https://www.rgagnon.com/javadetails/java-0332.html]
            textFeld.setCaretPosition(j + 1);
        });

        // )
        Icon n2ic = new ImageIcon("ressource/icons/).png");
        final JButton n2 = new JButton(n2ic);
        gbc.gridx = 2;
        gbc.gridy = 0;
        p1.add(n2, gbc);
        n2.addActionListener(e -> {
            String num1 = ")";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 1);
        });

        // .
        Icon n3ic = new ImageIcon("ressource/icons/..png");
        final JButton n3 = new JButton(n3ic);
        gbc.gridx = 3;
        gbc.gridy = 0;
        p1.add(n3, gbc);
        n3.addActionListener(e -> {
            String num1 = ".";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 1);
        });

        // esc
        Icon n4ic = new ImageIcon("ressource/icons/esc.png");
        final JButton n4 = new JButton(n4ic);
        gbc.gridx = 4;
        gbc.gridy = 0;
        p1.add(n4, gbc);
        n4.addActionListener(e -> System.exit(2));

        // 7
        Icon n5ic = new ImageIcon("ressource/icons/7.png");
        final JButton n5 = new JButton(n5ic);
        gbc.gridx = 0;
        gbc.gridy = 1;
        p1.add(n5, gbc);
        n5.addActionListener(e -> {
            String num1 = "7";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 1);
        });

        //8
        Icon n6ic = new ImageIcon("ressource/icons/8.png");
        final JButton n6 = new JButton(n6ic);
        gbc.gridx = 1;
        gbc.gridy = 1;
        p1.add(n6, gbc);
        n6.addActionListener(e -> {
            String num1 = "8";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 1);
        });

        //9
        Icon n7ic = new ImageIcon("ressource/icons/9.png");
        final JButton n7 = new JButton(n7ic);
        gbc.gridx = 2;
        gbc.gridy = 1;
        p1.add(n7, gbc);
        n7.addActionListener(e -> {
            String num1 = "9";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 1);
        });

        //*
        Icon n8ic = new ImageIcon("ressource/icons/*.png");
        final JButton n8 = new JButton(n8ic);
        gbc.gridx = 3;
        gbc.gridy = 1;
        p1.add(n8, gbc);
        n8.addActionListener(e -> {
            String num1 = "*";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 1);
        });

        // /
        Icon n9ic = new ImageIcon("ressource/icons/Division.png");
        final JButton n9 = new JButton(n9ic);
        gbc.gridx = 4;
        gbc.gridy = 1;
        p1.add(n9, gbc);
        n9.addActionListener(e -> {
            String num1 = "/";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 1);
        });

        //4
        Icon n10ic = new ImageIcon("ressource/icons/4.png");
        final JButton n10 = new JButton(n10ic);
        gbc.gridx = 0;
        gbc.gridy = 2;
        p1.add(n10, gbc);
        n10.addActionListener(e -> {
            String num1 = "4";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 1);
        });

        //5
        Icon n11ic = new ImageIcon("ressource/icons/5.png");
        final JButton n11 = new JButton(n11ic);
        gbc.gridx = 1;
        gbc.gridy = 2;
        p1.add(n11, gbc);
        n11.addActionListener(e -> {
            String num1 = "5";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 1);
        });

        //6
        Icon n12ic = new ImageIcon("ressource/icons/6.png");
        final JButton n12 = new JButton(n12ic);
        gbc.gridx = 2;
        gbc.gridy = 2;
        p1.add(n12, gbc);
        n12.addActionListener(e -> {
            String num1 = "6";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 1);
        });

        //+
        Icon n13ic = new ImageIcon("ressource/icons/+.png");
        final JButton n13 = new JButton(n13ic);
        gbc.gridx = 3;
        gbc.gridy = 2;
        p1.add(n13, gbc);
        n13.addActionListener(e -> {
            String num1 = "+";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 1);
        });

        //-
        Icon n14ic = new ImageIcon("ressource/icons/-.png");
        final JButton n14 = new JButton(n14ic);
        gbc.gridx = 4;
        gbc.gridy = 2;
        p1.add(n14, gbc);
        n14.addActionListener(e -> {
            String num1 = "-";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 1);
        });

        //1
        Icon n15ic = new ImageIcon("ressource/icons/1.png");
        final JButton n15 = new JButton(n15ic);
        gbc.gridx = 0;
        gbc.gridy = 3;
        p1.add(n15, gbc);
        n15.addActionListener(e -> {
            String num1 = "1";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 1);
        });

        //2
        Icon n16ic = new ImageIcon("ressource/icons/2.png");
        final JButton n16 = new JButton(n16ic);
        gbc.gridx = 1;
        gbc.gridy = 3;
        p1.add(n16, gbc);
        n16.addActionListener(e -> {
            String num1 = "2";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 1);
        });

        //3
        Icon n17ic = new ImageIcon("ressource/icons/3.png");
        final JButton n17 = new JButton(n17ic);
        gbc.gridx = 2;
        gbc.gridy = 3;
        p1.add(n17, gbc);
        n17.addActionListener(e -> {
            String num1 = "3";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 1);
        });

        // √
        Icon n18ic = new ImageIcon("ressource/icons/√.png");
        final JButton n18 = new JButton(n18ic);
        gbc.gridx = 3;
        gbc.gridy = 3;
        p1.add(n18, gbc);
        n18.addActionListener(e -> {
            //Pop up Window zum Lösen von Rechnung
            String wurzelterm = JOptionPane.showInputDialog(jmain, "Wurzel ziehen von?", null);
            try {
                //Taschenrechner.Rechner aufrufen
                Double s = prechner.rechnerstarten(wurzelterm);
                s = Math.sqrt(s);
                //Wenn es keine Nachkommastellen gibt werden das Komma und die 0 entfernt
                String sstr = String.valueOf(String.valueOf(s));
                if (sstr.endsWith(".0")) {
                    //Löscht die letzten 2 Zeichen[Quelle:https://stackoverflow.com/questions/30708036/delete-the-last-two-characters-of-the-string]
                    sstr = sstr.substring(0, sstr.length() - 2);
                }
                //Ergebnis der Liste hinzufügen
                model.addElement("√" + wurzelterm + "=" + sstr);
                //Ergebnis in textFeld setzen
                int j = textFeld.getCaretPosition();
                String global = textFeld.getText();
                global = global.substring(0, j) + sstr + global.substring(j);
                textFeld.setText(global);
                textFeld.requestFocusInWindow();
                textFeld.setCaretPosition(j + sstr.length());
            } catch (Exception d) {
                textFeld.setText("[ERROR] " + d.getMessage());
                textFeld.setBackground(Color.red);
            }

        });

        // ^
        Icon n19ic = new ImageIcon("ressource/icons/^.png");
        final JButton n19 = new JButton(n19ic);
        gbc.gridx = 4;
        gbc.gridy = 3;
        p1.add(n19, gbc);
        n19.addActionListener(e -> {
            String num1 = "^";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 1);
        });

        //0
        Icon n20ic = new ImageIcon("ressource/icons/0.png");
        final JButton n20 = new JButton(n20ic);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        p1.add(n20, gbc);
        n20.addActionListener(e -> {
            String num1 = "0";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 1);
        });

        //21 von 20 überlagert

        //C
        Icon n22ic = new ImageIcon("ressource/icons/C.png");
        final JButton n22 = new JButton(n22ic);
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        p1.add(n22, gbc);
        n22.addActionListener(e -> {
            int j = textFeld.getCaretPosition();
            textFeld.setBackground(Color.white);
            try {
                String str = textFeld.getText();
                //[Quelle:https://www.techiedelight.com/replace-character-specific-index-java-string/]
                str = str.substring(0, j - 1) + str.substring(j - 1 + 1);
                textFeld.setText(str);
                textFeld.requestFocusInWindow();
                textFeld.setCaretPosition(j - 1);
            } catch (Exception e2) {
                textFeld.setBackground(Color.RED);
                textFeld.setText("[Error] Nicht Möglich");
            }
        });

        //=
        Icon n23ic = new ImageIcon("ressource/icons/=.png");
        final JButton n23 = new JButton(n23ic);
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        p1.add(n23, gbc);
        n23.addActionListener(e -> {
            try {
                //Taschenrechner.Rechner aufrufen
                String s = String.valueOf(prechner.rechnerstarten(textFeld.getText()));
                //Wenn es keine Nachkommastellen gibt werden das Komma und die 0 entfernt
                //[Quelle:https://www.javatpoint.com/java-string-endswith]
                if (s.endsWith(".0")) {
                    //Löscht die letzten 2 Zeichen[Quelle:https://stackoverflow.com/questions/30708036/delete-the-last-two-characters-of-the-string]
                    s = s.substring(0, s.length() - 2);
                }
                //Ergebnis der Liste hinzufügen
                model.addElement(textFeld.getText() + "=" + s);
                //Ergebnis in textFeld setzen
                textFeld.setText(s);
            } catch (Exception d) {
                //theoretisch unerreichbar
                textFeld.setText(d.getMessage());
                textFeld.setBackground(Color.red);
            }
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(textFeld.getDocument().getLength());
        });
        //n24 wird überlagert von 23

        /*
        Erweiterter Modus Knöpfe
		 */
        //Erzeugung des Erweiterten Fensters


        JPanel p2 = new JPanel();
        p2.setLayout(new BorderLayout());
        //Hintergrundfarbe ändern
        p2.setBackground(Color.WHITE);

        p2.setLayout(new GridBagLayout());
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        //runden vor das Komma
        Icon n25ic = new ImageIcon("ressource/icons/rnd.png");
        final JButton n25 = new JButton(n25ic);
        gbc.gridx = 0;
        gbc.gridy = 0;
        p2.add(n25, gbc);
        n25.addActionListener(e -> {
            try {
                double global = Double.parseDouble(textFeld.getText());
                textFeld.setText(String.valueOf(Math.round(global)));
            } catch (Exception ex) {
                textFeld.setText("[ERROR] Nur einzelne Zahlen runden");
                textFeld.setBackground(Color.red);
            }
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(textFeld.getDocument().getLength());
        });

        //sin
        Icon n26ic = new ImageIcon("ressource/icons/sin.png");
        final JButton n26 = new JButton(n26ic);
        gbc.gridx = 0;
        gbc.gridy = 1;
        p2.add(n26, gbc);
        n26.addActionListener(e -> {
            String num1 = "sin()";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 4);
        });

        //cos
        Icon n27ic = new ImageIcon("ressource/icons/cos.png");
        final JButton n27 = new JButton(n27ic);
        gbc.gridx = 0;
        gbc.gridy = 2;
        p2.add(n27, gbc);
        n27.addActionListener(e -> {
            String num1 = "cos()";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 4);
        });

        //tan
        Icon n28ic = new ImageIcon("ressource/icons/tan.png");
        final JButton n28 = new JButton(n28ic);
        gbc.gridx = 0;
        gbc.gridy = 3;
        p2.add(n28, gbc);
        n28.addActionListener(e -> {
            String num1 = "tan()";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 4);
        });

        // Pfeil Links
        Icon n29ic = new ImageIcon("ressource/icons/arrowleft.png");
        final JButton n29 = new JButton(n29ic);
        gbc.gridx = 0;
        gbc.gridy = 4;
        p2.add(n29, gbc);
        n29.addActionListener(e -> {
            int j = textFeld.getCaretPosition();
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j - 1);
        });

        //rnd 2
        //rundet auf die 2. Nachkommastelle [Quelle:https://java.soeinding.de/content.php/Runden_Auf_Kommastellen]
        Icon n30ic = new ImageIcon("ressource/icons/rnd,x.png");
        final JButton n30 = new JButton(n30ic);
        gbc.gridx = 1;
        gbc.gridy = 0;
        p2.add(n30, gbc);
        n30.addActionListener(e -> {
            try {
                double global = Double.parseDouble(textFeld.getText());
                double value = Math.round(100.0 * global) / 100.0;
                textFeld.setText(String.valueOf(value));
            } catch (Exception ex) {
                textFeld.setText("[ERROR] Nur einzelne Zahlen runden");
                textFeld.setBackground(Color.red);
            }
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(textFeld.getDocument().getLength());
        });

        //sinh
        Icon n31ic = new ImageIcon("ressource/icons/sinh.png");
        final JButton n31 = new JButton(n31ic);
        gbc.gridx = 1;
        gbc.gridy = 1;
        p2.add(n31, gbc);
        n31.addActionListener(e -> {
            String num1 = "sinh()";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 5);
        });

        //cosh
        Icon n32ic = new ImageIcon("ressource/icons/cosh.png");
        final JButton n32 = new JButton(n32ic);
        gbc.gridx = 1;
        gbc.gridy = 2;
        p2.add(n32, gbc);
        n32.addActionListener(e -> {
            String num1 = "cosh()";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 5);
        });

        //tanh
        Icon n33ic = new ImageIcon("ressource/icons/tanh.png");
        final JButton n33 = new JButton(n33ic);
        gbc.gridx = 1;
        gbc.gridy = 3;
        p2.add(n33, gbc);
        n33.addActionListener(e -> {
            String num1 = "tanh()";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 5);
        });

        //Pfeil rechts
        Icon n34ic = new ImageIcon("ressource/icons/arrowright.png");
        final JButton n34 = new JButton(n34ic);
        gbc.gridx = 1;
        gbc.gridy = 4;
        p2.add(n34, gbc);
        n34.addActionListener(e -> {
            int j = textFeld.getCaretPosition();
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 1);
        });

        // Konstanten
        Icon n35ic = new ImageIcon("ressource/icons/Konstanten.png");
        // Icon X
        Icon n35ic2 = new ImageIcon("ressource/icons/x.png");
        final JToggleButton n35 = new JToggleButton(n35ic);
        n35.setSelectedIcon(n35ic2);
        gbc.gridx = 2;
        gbc.gridy = 0;
        p2.add(n35, gbc);

        //
        Icon n36ic = new ImageIcon("ressource/icons/empty.png");
        final JButton n36 = new JButton(n36ic);
        gbc.gridx = 2;
        gbc.gridy = 1;
        p2.add(n36, gbc);
        n36.addActionListener(e -> {
        });

        //
        Icon n37ic = new ImageIcon("ressource/icons/empty.png");
        final JButton n37 = new JButton(n37ic);
        gbc.gridx = 2;
        gbc.gridy = 2;
        p2.add(n37, gbc);
        n37.addActionListener(e -> {
        });

        //
        Icon n38ic = new ImageIcon("ressource/icons/empty.png");
        final JButton n38 = new JButton(n38ic);
        gbc.gridx = 2;
        gbc.gridy = 3;
        p2.add(n38, gbc);
        n38.addActionListener(e -> {
        });

        //
        Icon n39ic = new ImageIcon("ressource/icons/empty.png");
        final JButton n39 = new JButton(n39ic);
        gbc.gridx = 2;
        gbc.gridy = 4;
        p2.add(n39, gbc);
        n39.addActionListener(e -> {
        });

        /*
            Konstanten
         */

        //e
        Icon n40ic = new ImageIcon("ressource/icons/e.png");
        final JButton n40 = new JButton(n40ic);
        n40.setToolTipText("Eulersche Zahl");
        n40.addActionListener(e -> {
            String num1 = "e";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 1);
        });

        //φ
        Icon n41ic = new ImageIcon("ressource/icons/φ.png");
        final JButton n41 = new JButton(n41ic);
        n41.setToolTipText("Goldener Schnitt");
        n41.addActionListener(e -> {
            String num1 = "φ";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 1);
        });

        //π
        Icon n42ic = new ImageIcon("ressource/icons/π.png");
        final JButton n42 = new JButton(n42ic);
        n42.setToolTipText("pi");
        n42.addActionListener(e -> {
            String num1 = "π";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 1);
        });

        //θ
        Icon n43ic = new ImageIcon("ressource/icons/θ.png");
        final JButton n43 = new JButton(n43ic);
        n43.setToolTipText("Mills-Konstante");
        n43.addActionListener(e -> {
            String num1 = "θ";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 1);
        });

        //γ
        Icon n44ic = new ImageIcon("ressource/icons/γ.png");
        final JButton n44 = new JButton(n44ic);
        n44.setToolTipText("Euler-Masch-Konstante");
        n44.addActionListener(e -> {
            String num1 = "γ";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 1);
        });

        //λ
        Icon n45ic = new ImageIcon("ressource/icons/λ.png");
        final JButton n45 = new JButton(n45ic);
        n45.setToolTipText("Golomb-Dickman-Konstante");
        n45.addActionListener(e -> {
            String num1 = "λ";
            String global = textFeld.getText();
            int j = textFeld.getCaretPosition();
            global = global.substring(0, j) + num1 + global.substring(j);
            textFeld.setText(global);
            textFeld.requestFocusInWindow();
            textFeld.setCaretPosition(j + 1);
        });

        /*
           Gibt dem Konstanten Knopf seine Fukntion die Buttons zu tauschen
         */
        n35.addActionListener(e -> {
            if(n35.isSelected()) {
                //Ersetzt die Knöpfe [Quelle:https://stackoverflow.com/a/11097167]
                gbc.gridx = 1;
                gbc.gridy = 1;
                p2.remove(n31);
                p2.add(n40, gbc);
                gbc.gridy = 2;
                p2.remove(n32);
                p2.add(n41, gbc);
                gbc.gridy = 3;
                p2.remove(n33);
                p2.add(n42, gbc);

                gbc.gridx = 2;
                gbc.gridy = 1;
                p2.remove(n36);
                p2.add(n43, gbc);
                gbc.gridy = 2;
                p2.remove(n37);
                p2.add(n44, gbc);
                gbc.gridy = 3;
                p2.remove(n38);
                p2.add(n45, gbc);

                p2.revalidate();
                p2.repaint();
            }
            else if(!n35.isSelected()){
                gbc.gridx = 1;
                gbc.gridy = 1;
                p2.remove(n40);
                p2.add(n31, gbc);
                gbc.gridy = 2;
                p2.remove(n41);
                p2.add(n32, gbc);
                gbc.gridy = 3;
                p2.remove(n42);
                p2.add(n33, gbc);

                gbc.gridx = 2;
                gbc.gridy = 1;
                p2.remove(n43);
                p2.add(n36, gbc);
                gbc.gridy = 2;
                p2.remove(n44);
                p2.add(n37, gbc);
                gbc.gridy = 3;
                p2.remove(n45);
                p2.add(n38, gbc);

                //Konstanten Knopf
                n35.setIcon(n35ic);

                p2.revalidate();
                p2.repaint();
            }
        });

        /*
				Methode welche wenn auf ein Eintrag in der Liste ergebnisse geklickt wird, das Ergebnis
				dieser Rechnung in das Rechenfeld einfügt.
		*/
        //Listen clickevent [Quelle:https://docs.oracle.com/javase/tutorial/uiswing/events/listselectionlistener.html]
        rechnungen.addListSelectionListener(listSelectionEvent -> {
            //Das Feld welches angeklickt wird wird in str gespeichert [Quelle:https://stackoverflow.com/questions/28852864/get-the-selected-values-from-jlist-in-java]
            String str = String.valueOf(rechnungen.getSelectedValue());
            //Fall der Benutzer ein leeres Feld anklickt
            str = str.replaceAll("null", "");
            //Nur das Ergebnis wird benötigt [Quelle:https://stackoverflow.com/questions/16741274/java-extract-characters-after-a-specific-character]
            str = str.substring(str.lastIndexOf("=") + 1);
            //Das Ergebnis wird in das TextFeld eingefügt
            textFeld.setText(textFeld.getText() + str);
            //Auswahl wird rückgängig gemacht [Quelle:https://www.tutorialspoint.com/how-can-we-clear-all-selections-in-java-swing-jlist]
            rechnungen.clearSelection();
        });

        //Live Updater des lösungsfeld [Quelle:https://stackoverflow.com/questions/3953208/value-change-listener-to-jtextfield]
        textFeld.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                rechnen();
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                rechnen();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                rechnen();
            }

            public void rechnen() {
                try {
                    //Aktiviert den "=" Knopf da das Lösen möglich ist [Quelle:https://stackoverflow.com/questions/1625855/how-to-disable-javax-swing-jbutton-in-java]
                    n23.setEnabled(true);
                    loesung.setBackground(Color.WHITE);
                    //Taschenrechner.Rechner aufrufen
                    String s = String.valueOf(prechner.rechnerstarten(textFeld.getText()));
                    //Wenn es keine Nachkommastellen gibt werden das Komma und die 0 entfernt
                    //[Quelle:https://www.javatpoint.com/java-string-endswith]
                    if (s.endsWith(".0")) {
                        //Löscht die letzten 2 Zeichen[Quelle:https://stackoverflow.com/questions/30708036/delete-the-last-two-characters-of-the-string]
                        s = s.substring(0, s.length() - 2);
                    }
                    //Ergebnis in textFeld setzen
                    loesung.setText("Lösung:" + s);
                } catch (Exception d) {
                    //Deaktiviert den "=" Knopf da die Rechnung nicht möglich ist
                    n23.setEnabled(false);
                    String str = d.getMessage();
                    if (d.getMessage().contains("String index out of range:"))
                        str = "Fehlende Zahl";
                    loesung.setText(str);
                    loesung.setBackground(Color.red);
                }
                //Deaktiviert die Runden Funktion wenn mehr als nur ein int in der Rechenbox steht
                //Schaut ob überhaupt ein Komma vorkommt [Quelle:https://www.javatpoint.com/java-string-contains]
                if (textFeld.getText().contains(".")) {
                    try {
                        //Abwandlung von [Quelle:https://stackoverflow.com/a/5439547]
                        Double.parseDouble(textFeld.getText());
                        n25.setEnabled(true);
                        n30.setEnabled(true);
                    } catch (Exception e) {
                        n25.setEnabled(false);
                        n30.setEnabled(false);
                    }
                } else {
                    n25.setEnabled(false);
                    n30.setEnabled(false);
                }

            }
        });

        //Rand entfernen [Quelle:https://stackoverflow.com/questions/8367500/how-to-hide-background-of-jbutton-which-containt-icon-image]
        n0.setBorderPainted(false);
        n1.setBorderPainted(false);
        n2.setBorderPainted(false);
        n3.setBorderPainted(false);
        n4.setBorderPainted(false);
        n5.setBorderPainted(false);
        n6.setBorderPainted(false);
        n7.setBorderPainted(false);
        n8.setBorderPainted(false);
        n9.setBorderPainted(false);
        n10.setBorderPainted(false);
        n11.setBorderPainted(false);
        n12.setBorderPainted(false);
        n13.setBorderPainted(false);
        n14.setBorderPainted(false);
        n15.setBorderPainted(false);
        n16.setBorderPainted(false);
        n17.setBorderPainted(false);
        n18.setBorderPainted(false);
        n19.setBorderPainted(false);
        n20.setBorderPainted(false);
        //n21
        n22.setBorderPainted(false);
        n23.setBorderPainted(false);
        //n24
        n25.setBorderPainted(false);
        n26.setBorderPainted(false);
        n27.setBorderPainted(false);
        n28.setBorderPainted(false);
        n29.setBorderPainted(false);
        n30.setBorderPainted(false);
        n31.setBorderPainted(false);
        n32.setBorderPainted(false);
        n33.setBorderPainted(false);
        n34.setBorderPainted(false);
        n35.setBorderPainted(false);
        n36.setBorderPainted(false);
        n37.setBorderPainted(false);
        n38.setBorderPainted(false);
        n39.setBorderPainted(false);
        n40.setBorderPainted(false);
        n41.setBorderPainted(false);
        n42.setBorderPainted(false);
        n43.setBorderPainted(false);
        n44.setBorderPainted(false);
        n45.setBorderPainted(false);

        //Hintergrund entfernen
        n0.setContentAreaFilled(false);
        n1.setContentAreaFilled(false);
        n2.setContentAreaFilled(false);
        n3.setContentAreaFilled(false);
        n4.setContentAreaFilled(false);
        n5.setContentAreaFilled(false);
        n6.setContentAreaFilled(false);
        n7.setContentAreaFilled(false);
        n8.setContentAreaFilled(false);
        n9.setContentAreaFilled(false);
        n10.setContentAreaFilled(false);
        n11.setContentAreaFilled(false);
        n12.setContentAreaFilled(false);
        n13.setContentAreaFilled(false);
        n14.setContentAreaFilled(false);
        n15.setContentAreaFilled(false);
        n16.setContentAreaFilled(false);
        n17.setContentAreaFilled(false);
        n18.setContentAreaFilled(false);
        n19.setContentAreaFilled(false);
        n20.setContentAreaFilled(false);
        //n21
        n22.setContentAreaFilled(false);
        n23.setContentAreaFilled(false);
        //n24
        n25.setContentAreaFilled(false);
        n25.setContentAreaFilled(false);
        n26.setContentAreaFilled(false);
        n27.setContentAreaFilled(false);
        n28.setContentAreaFilled(false);
        n29.setContentAreaFilled(false);
        n30.setContentAreaFilled(false);
        n31.setContentAreaFilled(false);
        n32.setContentAreaFilled(false);
        n33.setContentAreaFilled(false);
        n34.setContentAreaFilled(false);
        n35.setContentAreaFilled(false);
        n36.setContentAreaFilled(false);
        n37.setContentAreaFilled(false);
        n38.setContentAreaFilled(false);
        n39.setContentAreaFilled(false);
        n40.setContentAreaFilled(false);
        n41.setContentAreaFilled(false);
        n42.setContentAreaFilled(false);
        n43.setContentAreaFilled(false);
        n44.setContentAreaFilled(false);
        n45.setContentAreaFilled(false);

        //Keylistener der bestimmte Sachen macht wenn eine bestimmt Taste kopiert wird
        textFeld.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                switch (keyEvent.getKeyChar()) {
                    case KeyEvent.VK_ESCAPE, KeyEvent.VK_EQUALS -> {
                        //JOptionPane [Quelle:https://www.javatpoint.com/java-joptionpane]
                        int a = JOptionPane.showConfirmDialog(jmain, "Close?");
                        if (a == JOptionPane.YES_OPTION) {
                            System.exit(3); //ESC
                        }
                    }
                    case KeyEvent.VK_ENTER -> n23.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
            }

        });
        //Splitpane 4 Normal Knöpfe und Erweitern
        //Splitpane 4 wurde bereits vorher definiert
			/*
				Basic Modus Buttons | Erweiterter Modus Buttons
			 */
        splitpane4.setLeftComponent(p1);
        splitpane4.setRightComponent(p2);

        //Splitpane 3 das Label und das Textfeld hinzufügen
        //Splitpane 3 das Label und das Textfeld hinzufügen
        JSplitPane splitpane3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			/*
				Livelösungsvorschau
				-------------------
				Rechnungen zeichen
			 */
        splitpane3.setTopComponent(loesung);
        splitpane3.setBottomComponent(label);
        //Den Teiler "entfernen"
        splitpane3.setDividerSize(0);

        //Container Textfeld hinzufügen
			/*
				Textfeld
				--------
				 Knöpfe
			 */
        c.add(scrollPaneTextFeld, BorderLayout.NORTH);

        //Splitpane 2 die Komponenten anfügen
        JSplitPane splitpane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			/*
				     splitpane3
				-------------------
				Liste der Rechnungen
			 */
        splitpane2.setBottomComponent(scrollPaneRechnungen);
        splitpane2.setTopComponent(splitpane3);
        splitpane2.setDividerSize(0);

        //Splitpane 1 hinzufügen
        //Erzeugung eines JSplitPane-Objektes mit horizontaler Trennung [https://java-tutorial.org/jsplitpane.html]
        JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			/*
				Splitpane2 | splitpane4
			 */
        splitpane.setLeftComponent(splitpane2);
        splitpane.setRightComponent(splitpane4);
        splitpane.setDividerSize(0);

        c.add(splitpane, BorderLayout.CENTER);
        //Menüleiste hinzufügen
        jmain.setJMenuBar(menuBar);

        jmain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Größe (optimal für icons und Layout)
        jmain.setSize(850, 850);
        jmain.setVisible(true);
    }
}