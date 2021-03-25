package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
public class CalendarTest {
    @Test
    void testConstructor() {
        Calendar testCalendar = new Calendar("study");
        assertEquals("study", testCalendar.getName());
    }

    @Test
    void testSearchAssignmentByTime() {
        Calendar testCalendar = new Calendar("study");
        int now = (int)(System.currentTimeMillis() / 1000);
        Assignment oneDayAgo = new Assignment(now - 1 * 24 * 60 * 60,"oneDayAgo");
        Assignment oneDayLater = new Assignment(now + 1 * 24 * 60 * 60,"oneDayLater");
        Assignment oneDayLater2 = new Assignment(now + 1 * 24 * 60 * 60,"oneDayLater");
        oneDayLater2.setIsDoneTag(1);
        Assignment eightDayLater = new Assignment(now + 8 * 24 * 60 * 60,"eightDayLater");
        Assignment fortyDayLater = new Assignment(now + 40 * 24 * 60 * 60,"fortyDayLater");
        testCalendar.addAssignment(oneDayAgo);
        testCalendar.addAssignment(oneDayLater);
        testCalendar.addAssignment(oneDayLater2);
        testCalendar.addAssignment(eightDayLater);
        testCalendar.addAssignment(fortyDayLater);
        assertEquals(oneDayAgo, testCalendar.searchNotDoneAssignmentByTime(now-86401, now).get(0));
        assertEquals(1, testCalendar.searchNotDoneAssignmentByTime(now-86401, now).size());
        assertEquals(oneDayLater, testCalendar.searchNotDoneAssignmentByTime(now, now + 86400).get(0));
        assertEquals(1, testCalendar.searchNotDoneAssignmentByTime(now, now + 86400).size());
        assertEquals(eightDayLater, testCalendar.searchNotDoneAssignmentByTime(now, now + 864000).get(1));
        assertEquals(2, testCalendar.searchNotDoneAssignmentByTime(now, now + 864000).size());
        assertEquals(fortyDayLater, testCalendar.searchNotDoneAssignmentByTime(now, now + 8640000).get(2));
        assertEquals(3, testCalendar.searchNotDoneAssignmentByTime(now, now + 8640000).size());
        testCalendar.finishAssignment(0);
    }

    @Test
    void testSearchAssignmentById() {
        Calendar testCalendar = new Calendar("study");
        Assignment test3 = new Assignment(1602432015,"testSearchAssignmentById");
        Assignment test4 = new Assignment(1602432015,"testSearchAssignmentById");
        assertEquals(null, testCalendar.searchAssignmentById(0));
        testCalendar.addAssignment(test3);
        testCalendar.addAssignment(test4);
        assertEquals(test3, testCalendar.searchAssignmentById(test3.getId()));
        assertEquals(test4, testCalendar.searchAssignmentById(test4.getId()));
        assertEquals(null, testCalendar.searchAssignmentById(100));
    }

    @Test
    void testChangeAssignment() {
        Calendar testCalendar = new Calendar("study");
        Assignment test = new Assignment(1602432015,"beforeChange");
        testCalendar.addAssignment(test);
        assertEquals(true, testCalendar.changeAssignment(test.getId(),1700000000,"t"));
        assertEquals(false, testCalendar.changeAssignment(100,1700000000,"t"));
    }

    @Test
    void testDeleteAssignment() {
        Calendar testCalendar = new Calendar("study");
        Assignment test = new Assignment(1602432015,"testDeleteAssignment");
        testCalendar.addAssignment(test);
        assertEquals(true,testCalendar.deleteAssignment(test.getId()));
        assertEquals(false,testCalendar.deleteAssignment(100));
    }

    @Test
    void testFinishAssignment() {
        Calendar testCalendar = new Calendar("study");
        Assignment test = new Assignment(1602432015,"Finish");
        testCalendar.addAssignment(test);
        assertEquals(true, testCalendar.finishAssignment(test.getId()));
        assertEquals(false, testCalendar.finishAssignment(100));
    }

    @Test
    void testSetName() {
        Calendar testCalendar = new Calendar("study");
        testCalendar.setName("test2");
        assertEquals("test2", testCalendar.getName());
    }

    @Test
    void testToJson() {
        Calendar testCalendar2 = new Calendar("study2");
        Assignment testAssignment1 = new Assignment(100,1602433015, "test", 0);
        Assignment testAssignment2 = new Assignment(200,1602433015, "test", 0);
        ArrayList<Assignment> assignmentList = new ArrayList<>();
        testCalendar2.addAssignment(testAssignment1);
        testCalendar2.addAssignment(testAssignment2);
        assignmentList.add(testAssignment1);
        assignmentList.add(testAssignment2);
        JSONObject json = new JSONObject();
        json.put("name", testCalendar2.getName());
        JSONArray jsonArray = new JSONArray();
        for (Assignment assign : assignmentList) {
            jsonArray.put(assign.toJson());
        }
        json.put("assignments", jsonArray);
        assertEquals(json.toString(), testCalendar2.toJson().toString());
    }


}
