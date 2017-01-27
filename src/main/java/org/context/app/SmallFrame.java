package org.context.app;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.*;
import javax.swing.text.Highlighter.HighlightPainter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A java swing application to use the ConTex algorithm.
 *
 * @author Oscar Ferrandez-Escamez
 *         Department of Biomedical Informatics, University of Utah, 2011
 */
class SmallFrame extends JFrame implements ActionListener {

    private JButton Button = new JButton("close");
    private JTextArea TextA = new JTextArea();
    private JPanel bottomPanel = new JPanel();
    private JPanel holdAll = new JPanel();


    //private subclass -- highlight painter
    private Highlighter.HighlightPainter appHighlightPainter = (HighlightPainter) new AppHighlightPainter(Color.pink);

    //private subclass -- highlight painter
    private class AppHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
        AppHighlightPainter(Color color) {
            super(color);
        }
    }


    SmallFrame(String s, String[] cp) {
        TextA.setText(s);
        for (String aCp : cp) this.highlight(TextA, "'" + aCp.toLowerCase() + "'");

        TextA.setEditable(false);
        TextA.setLineWrap(true);
        TextA.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(TextA);

        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(Button);

        JLabel a = new JLabel();
        a.setText("CONTEXT ALGORITHM RESULTS");
        JPanel upper = new JPanel();
        upper.setLayout(new FlowLayout());
        upper.add(a);


        holdAll.setLayout(new BorderLayout());
        holdAll.add(upper, BorderLayout.NORTH);
        holdAll.add(bottomPanel, BorderLayout.SOUTH);
        holdAll.add(scroll, BorderLayout.CENTER);
        holdAll.setBorder(new LineBorder(Color.white, 10));

        getContentPane().add(holdAll, BorderLayout.CENTER);

        Button.addActionListener(this);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.setLocation(200, 200);
        this.setSize(500, 300);
        this.setVisible(true);
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Button) {
            this.setVisible(false);
        }
    }

    //highlight the concepts in the results frame
    private void highlight(JTextComponent textComp, String pattern) {
        //remove old
        try {
            Highlighter hilite = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength()).toLowerCase();
            int pos = 0;

            // Search for concepts and add into the highlighter
            while ((pos = text.indexOf(pattern, pos)) >= 0) {
                hilite.addHighlight(pos, pos + pattern.length(), appHighlightPainter);
                pos += pattern.length();
            }
        } catch (BadLocationException e) {
        }
    }
}
