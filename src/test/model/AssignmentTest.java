package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class AssignmentTest {
    private Assignment testAssignment;
    private Assignment testAssignment2;

    @BeforeEach
    void runBefore() {
        testAssignment = new Assignment(1602433015, "CPSC121 quiz6a");
        testAssignment2 = new Assignment(1602434015, "CPSC121 quiz6a");
    }

    @Test
    void testConstructor() {
        assertEquals(1602433015, testAssignment.getDeadline());
        assertEquals("CPSC121 quiz6a", testAssignment.getContent());
        assertEquals(0, testAssignment.getIsDoneTag());
    }

    @Test
    void testCompareTo() {
        assertEquals(-1000, testAssignment.compareTo(testAssignment2));
    }

    @Test
    void testToString() {
        int id = testAssignment.getId();
        assertEquals("Assignment{id="+id+", deadline=2020-10-12 00:16:55, content='CPSC121 quiz6a', isDoneTag=0}",
                testAssignment.toString());
    }

    @Test
    void testSetIsDoneTag() {
        testAssignment.setIsDoneTag(1);
        assertEquals(1, testAssignment.getIsDoneTag());
    }

    @Test
    void testSetContent() {
        testAssignment.setContent("CPSC210");
        assertEquals("CPSC210", testAssignment.getContent());
    }

    @Test
    void testSetDeadline() {
        testAssignment.setDeadline(1702433015);
        assertEquals(1702433015, testAssignment.getDeadline());
    }

    @Test
    void testSetId() {
        testAssignment = new Assignment(1602433015, "CPSC121 quiz6a");
        testAssignment.setId(100);
        assertEquals(100, testAssignment.getId());
    }

    @Test
    void testToJson() {
        testAssignment = new Assignment(100,1602433015, "test", 0);
        JSONObject json = new JSONObject();
        json.put("id", testAssignment.getId());
        json.put("deadline", testAssignment.getDeadline());
        json.put("content", testAssignment.getContent());
        json.put("isDoneTag", testAssignment.getIsDoneTag());
        assertEquals(json.toString(), testAssignment.toJson().toString());
    }
}