package org.context.app;


import org.context.implementation.ConTextEnglish;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A java swing application to use the ConTex algorithm.
 * 
 * @author Oscar Ferrandez-Escamez
 * Department of Biomedical Informatics, University of Utah, 2011
 */
public class RunApp extends JFrame implements ActionListener
{
	
 
  private JButton Button = new JButton("ConTextEnglish");
  private JButton Button2 = new JButton("Clear");
 
  private JTextField concepts = new JTextField(50);
  private JTextArea TextA = new JTextArea(13,50);
  private JPanel bottomPanel = new JPanel();


  private JPanel holdAll = new JPanel();
 
  /*
   * A Listener to clear the text fields. 
   */
  private class ClearListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			TextA.setText("");
			concepts.setText("");

		}

	}
  


  private RunApp()
  {
    bottomPanel.setLayout(new FlowLayout());
    bottomPanel.add(Button);bottomPanel.add(Button2);
    
    JLabel a = new JLabel();
    a.setText("CONTEXT ALGORITHM IMPLEMENTATION");
    JPanel upper = new JPanel();
    upper.setLayout(new FlowLayout());
    upper.add(a);
 
    JLabel conc = new JLabel();
    conc.setText("Write target concept(s) (separated by commas):");
    
    JLabel se = new JLabel();
    se.setText("Write the sentence(s) (one sentence per line):");
    
    concepts.setText("Fever, headache, diabetes");
    
    JPanel mid = new JPanel();
    FlowLayout lmd = new FlowLayout();
    lmd.setAlignment(FlowLayout.LEFT);
    mid.setLayout(lmd);
    mid.add(conc);mid.add(concepts);mid.add(se);
    
    TextA.setText("Patient denies fever, but complains of headache.\nFamily history of diabetes.");
    TextA.setLineWrap(true);
    TextA.setWrapStyleWord(true);
    
    JScrollPane scroll = new JScrollPane(TextA,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    mid.add(scroll);
    
    
    holdAll.setLayout(new BorderLayout());
    holdAll.add(bottomPanel, BorderLayout.SOUTH);
    holdAll.add(upper, BorderLayout.NORTH);
    holdAll.add(mid, BorderLayout.CENTER);
    holdAll.setBorder(new LineBorder(Color.white,10));
 
    this.setTitle ("ConTextEnglish Algorithm Demo");
    
    getContentPane().add(holdAll, BorderLayout.CENTER);

 
    Button.addActionListener(this);

    Button2.addActionListener(new ClearListener());
    
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
  }
 

  public static void main(String[] args)
  {
    RunApp myApplication = new RunApp();
 
    // Specify where will it appear on the screen:
    myApplication.setLocation(100, 100);
    myApplication.setSize(660, 400);
    myApplication.setResizable(false);
 
    // Show it!
    myApplication.setVisible(true);
  }
 
  /**
   * Each non abstract class that implements the ActionListener
   * must have this method.
   * 
   * @param e the action event.
   */
  public void actionPerformed(ActionEvent e)
  {

	 if (e.getSource() == Button) //this button will call the ConTextEnglish algorithm
    {
    	String[] sentences = TextA.getText().split("\n");
    	System.out.println(sentences.toString());
    	String[] mp = concepts.getText().split(", *");
    	
    	String context_output = "";
    	
    	//for each concept
    	//identify the concept within each sentence and analyze its context
        for (String aMp : mp)
            for (String sentence : sentences) {
                ConTextEnglish myApplication = new ConTextEnglish();
                java.util.List<String> res = null;

                try {
                    res = myApplication.applyContext(aMp, sentence);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                if (res != null) {
                    context_output += "ConTextEnglish for '" + res.get(0) + "':\n" +
                            "Sentence: '" + res.get(1) + "'\n" +
                            "Negation: '" + res.get(2) + "'\n" +
                            "Temporality: '" + res.get(3) + "'\n" +
                            "Experiencer: '" + res.get(4) + "'\n\n";
                }
            }
    	
    	System.out.println(context_output);
    	if(context_output.isEmpty()) context_output = "No concept(s) found in the sentence(s).";
    	// new window with the results
    	SmallFrame res = new SmallFrame(context_output,mp);
    }
  }
}
