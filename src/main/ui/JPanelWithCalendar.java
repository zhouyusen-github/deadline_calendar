package ui;

import model.Calendar;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;

public class JPanelWithCalendar extends JPanel {
    protected Calendar calendar;
    protected static final String JSON_STORE = "./data/%s.json";
    protected JsonReader jsonReader;
    protected JsonWriter jsonWriter;

    public static void playSuccessSound() {
        playSound("success");
    }

    public static void playFailSound() {
        playSound("fail");
    }

    public static void playSound(String name) {
        try {
            String location = "./data/" + name + ".wav";
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(location).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}
