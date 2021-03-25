package persistence;

import model.Calendar;
import model.Assignment;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads Calendar from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads Calendar from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Calendar read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseCalendar(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses Calendar from JSON object and returns it
    private Calendar parseCalendar(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Calendar calendar = new Calendar(name);
        addAssignments(calendar, jsonObject);
        return calendar;
    }

    // MODIFIES: calendar
    // EFFECTS: parses assignments from JSON object and adds them to Calendar
    private void addAssignments(Calendar calendar, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("assignments");
        for (Object json : jsonArray) {
            JSONObject nextAssignment = (JSONObject) json;
            addAssignment(calendar, nextAssignment);
        }
    }

    // MODIFIES: calendar
    // EFFECTS: parses assignment from JSON object and adds it to Calendar
    private void addAssignment(Calendar calendar, JSONObject jsonObject) {
        int id = jsonObject.getInt("id");
        int deadline = jsonObject.getInt("deadline");
        String content = jsonObject.getString("content");
        int isDoneTag = jsonObject.getInt("isDoneTag");
        Assignment assignment = new Assignment(id, deadline, content, isDoneTag);
        calendar.addAssignment(assignment);
    }
}
