package persistence;

import static org.junit.jupiter.api.Assertions.*;

import model.Assignment;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;

import java.io.IOException;
import java.util.List;

public class JsonReaderTest {
    @Test
    void testConstructor() throws IOException {
        JsonReader jsonReader = new JsonReader("./data/test.json");
        assertEquals("aa",jsonReader.read().getName());
        List<Assignment> assignmentArrayList = jsonReader.read().searchAssignmentByTime(1,2000000000);
        assertEquals(1,assignmentArrayList.size());
        Assignment assignment = assignmentArrayList.get(0);
        assertEquals(0, assignment.getId());
        assertEquals(1320981071, assignment.getDeadline());
        assertEquals("test", assignment.getContent());
        assertEquals(0, assignment.getIsDoneTag());
    }
}

