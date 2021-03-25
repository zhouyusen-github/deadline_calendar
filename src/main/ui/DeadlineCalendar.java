package ui;

import model.Calendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import static ui.JPanelWithCalendar.playFailSound;

public class DeadlineCalendar extends JFrame {
    Calendar calendar;

    JPanelWithCalendar panelLoadCalendar;
    JPanelWithCalendar panelAddAssignment;
    JPanelWithCalendar panelDeleteAssignment;
    JPanelWithCalendar panelSearchAssignment;
    JPanelWithCalendar panelSaveAssignment;

    JPanel nowShowPanel;

    HashMap<String, JButton> mapJButton;

    JPanel buttonPanel;

    public DeadlineCalendar() {
        mapJButton = new HashMap();
        buttonPanel = new JPanel();
        this.panelLoadCalendar = new LoadCalendar();
        this.panelAddAssignment = new AddAssignment();
        this.panelDeleteAssignment = new DeleteAssignment();
        this.panelSearchAssignment = new SearchAssignment();
        this.panelSaveAssignment = new SaveAssignment();

        this.nowShowPanel = this.panelLoadCalendar;

        init("LoadCalendar", panelLoadCalendar);
        init("AddAssignment", panelAddAssignment);
        init("DeleteAssignment", panelDeleteAssignment);
        init("SearchAssignment", panelSearchAssignment);
        init("SaveAssignment", panelSaveAssignment);

        add(this.panelLoadCalendar);
        add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Deadline Calendar");
        setSize(800, 300);
        setLocationRelativeTo(null); // stay middle
        setDefaultCloseOperation(EXIT_ON_CLOSE);// quit whe exit
        setVisible(true);
    }

    public void init(String workName, JPanelWithCalendar workJP) {
        mapJButton.put(workName,new JButton(workName));
        buttonPanel.add(mapJButton.get(workName));
        switchPanel(mapJButton.get(workName),workJP);
    }

    public void switchPanel(JButton button, JPanelWithCalendar newShowPanel) {
        button.addActionListener(new ActionListener() { // action response
            @Override
            public void actionPerformed(ActionEvent e) {
                if (panelLoadCalendar.getCalendar() != null) {
                    calendar = panelLoadCalendar.getCalendar();
                    newShowPanel.setCalendar(calendar);
                    Container pane = button.getRootPane().getContentPane();// get JFrame Pane
                    pane.remove(nowShowPanel); //
                    pane.add(newShowPanel); // change to new Panel
                    pane.validate(); // rebuild
                    pane.repaint(); // redraw
                    nowShowPanel = newShowPanel;
                } else {
                    playFailSound();
                }
            }
        });

    }

    public static void main(String[] args) {
        new DeadlineCalendar();
    }
}