/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

// This code is base on Oracle JPanel example, and I do lots of change to form a user interface.

package ui;

import model.Assignment;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchAssignment extends JPanelWithCalendar
        implements ActionListener,
        FocusListener {
    JTextField startTimeField;
    JTextField endTimeField;
    boolean assignmentSet = false;
    Font regularFont;
    Font italicFont;
    JLabel assignmentDisplay;
    List<Assignment> assignmentList;
    static final int GAP = 10;

    public SearchAssignment() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        JPanel leftHalf = new JPanel() {
            //Don't allow us to stretch vertically.
            public Dimension getMaximumSize() {
                Dimension pref = getPreferredSize();
                return new Dimension(Integer.MAX_VALUE,
                        pref.height);
            }
        };
        leftHalf.setLayout(new BoxLayout(leftHalf,
                BoxLayout.PAGE_AXIS));
        leftHalf.add(createEntryFields());
        leftHalf.add(createButtons());

        add(leftHalf);
        add(createAssignmentDisplay());
    }

    protected JComponent createButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        JButton button = new JButton("Search assignment");
        button.addActionListener(this);
        panel.add(button);

        button = new JButton("Clear input");
        button.addActionListener(this);
        button.setActionCommand("clear");
        panel.add(button);

        //Match the SpringLayout's gap, subtracting 5 to make
        //up for the default gap FlowLayout provides.
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0,
                GAP - 5, GAP - 5));
        return panel;
    }

    /**
     * Called when the user clicks the button or presses
     * Enter in a text field.
     */
    public void actionPerformed(ActionEvent e) {
        if ("clear".equals(e.getActionCommand())) {
            assignmentSet = false;
            startTimeField.setText("YYYY-MM-DD-HH-mm-ss");
            endTimeField.setText("YYYY-MM-DD-HH-mm-ss"); // refresh
        } else {
            assignmentSet = true;
        }
        updateDisplays();
    }

    protected void updateDisplays() {
        assignmentDisplay.setText(formatDisplay());
    }

    protected JComponent createAssignmentDisplay() {
        JPanel panel = new JPanel(new BorderLayout());
        assignmentDisplay = new JLabel();
        assignmentDisplay.setHorizontalAlignment(JLabel.CENTER);
        regularFont = assignmentDisplay.getFont().deriveFont(Font.PLAIN,
                16.0f);
        italicFont = regularFont.deriveFont(Font.ITALIC);
        updateDisplays();

        //Lay out the panel.
        panel.setBorder(BorderFactory.createEmptyBorder(
                GAP / 2, //top
                0,     //left
                GAP / 2, //bottom
                0));   //right
        panel.add(new JSeparator(JSeparator.VERTICAL),
                BorderLayout.LINE_START);
        panel.add(assignmentDisplay,
                BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(400, 300));

        return panel;
    }

    protected String formatDisplay() {
        if (!assignmentSet) {
            return "<html><p>Please input the startTime and endTime of an assignment<p>";
        }
        String startTime = startTimeField.getText();
        String endTime = endTimeField.getText(); // get input thing
        String color = "green";
        String status;
        String mistake = "";
        String assignmentShowList = "";
        int startTimeInt = dateStringToTimeStamp(startTime);
        int endTimeInt = dateStringToTimeStamp(endTime);
        if (endTimeInt == -1 || startTimeInt == -1) {
            mistake = "Input time format is wrong<br>";
            status = "status: search fail";
            color = "red";
            playFailSound();
        } else {
            assignmentShowList = doSearchAssignment(startTimeInt, endTimeInt);
            status = "status: search successful";
            playSuccessSound();
        }
        String answer = "<html><p align=left color=" + color + ">" + mistake + status + assignmentShowList;
        return answer;
    }

    public String doSearchAssignment(int startTimeInt, int endTimeInt) {
        assignmentList = calendar.searchNotDoneAssignmentByTime(startTimeInt, endTimeInt);
        StringBuffer sb = new StringBuffer();
        for (Assignment i: assignmentList) {
            sb.append("<br>");
            sb.append(i);
        }
        return sb.toString();
    }


    private int dateStringToTimeStamp(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        try {
            Date date;
            date = simpleDateFormat.parse(dateString);
            int timeStamp = (int) ((date.getTime()) / 1000);
            return timeStamp;
        } catch (ParseException e) {
            return -1;
        }
    }

    /**
     * Called when one of the fields gets the focus so that
     * we can select the focused field.
     */
    public void focusGained(FocusEvent e) {
        Component c = e.getComponent();
        if (c instanceof JFormattedTextField) {
            selectItLater(c);
        } else if (c instanceof JTextField) {
            ((JTextField) c).selectAll();
        }
    }

    //Workaround for formatted text field focus side effects.
    protected void selectItLater(Component c) {
        if (c instanceof JFormattedTextField) {
            final JFormattedTextField ftf = (JFormattedTextField) c;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ftf.selectAll();
                }
            });
        }
    }

    //Needed for FocusListener interface.
    public void focusLost(FocusEvent e) {
    } //ignore

    protected JComponent createEntryFields() {
        JPanel panel = new JPanel(new SpringLayout());
        String[] labelStrings = {"StartTime: ", "EndTime: "};
        JLabel[] labels = new JLabel[labelStrings.length];
        JComponent[] fields = new JComponent[labelStrings.length];
        int fieldNum = 0;
        startTimeField = new JTextField("YYYY-MM-DD-HH-mm-ss",20);
        fields[fieldNum++] = startTimeField;
        endTimeField = new JTextField("YYYY-MM-DD-HH-mm-ss",20);
        fields[fieldNum++] = endTimeField;
        for (int i = 0; i < labelStrings.length; i++) {
            labels[i] = new JLabel(labelStrings[i], JLabel.TRAILING);
            labels[i].setLabelFor(fields[i]);
            panel.add(labels[i]);
            panel.add(fields[i]);
            JTextField tf = null;
            tf = (fields[i] instanceof JSpinner) ? getTextField((JSpinner) fields[i]) : (JTextField) fields[i];
            tf.addActionListener(this);
            tf.addFocusListener(this);
        }
        SpringUtilities.makeCompactGrid(panel, labelStrings.length, 2, GAP, GAP, GAP, GAP / 2);
        return panel;
    }

    public JFormattedTextField getTextField(JSpinner spinner) {
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            return ((JSpinner.DefaultEditor) editor).getTextField();
        } else {
            System.err.println("Unexpected editor type: "
                    + spinner.getEditor().getClass()
                    + " isn't a descendant of DefaultEditor");
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("search assignment");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add startTimes to the window.
        frame.add(new SearchAssignment());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }
}
