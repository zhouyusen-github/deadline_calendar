package persistence;

import model.Assignment;
import model.Calendar;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonWriterTest {
    @Test
    void testConstructor() throws IOException {
        JsonWriter jsonWriter = new JsonWriter("./data/test2.json");
        Calendar calendar = new Calendar("test");
        Assignment assignment = new Assignment(0,1320981071,"test",0);
        calendar.addAssignment(assignment);
        jsonWriter.open();
        jsonWriter.write(calendar);
        jsonWriter.close();
        JsonReader jsonReader = new JsonReader("./data/test2.json");
        assertEquals("test",jsonReader.read().getName());
        List<Assignment> assignmentArrayList = jsonReader.read().searchAssignmentByTime(1,2000000000);
        assertEquals(1,assignmentArrayList.size());
        Assignment assignment2 = assignmentArrayList.get(0);
        assertEquals(0, assignment2.getId());
        assertEquals(1320981071, assignment2.getDeadline());
        assertEquals("test", assignment2.getContent());
        assertEquals(0, assignment2.getIsDoneTag());
    }
}
