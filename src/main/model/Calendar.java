package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Represents a Calendar having a list of assignments, a name.
public class Calendar implements Writable {
    public List<Assignment> assignmentList;
    public String name;

    /*
     * REQUIRES: a String calendarName
     * EFFECTS: name on assignment is set to calendarName;
     *          assignmentList is initialized as a list of assignment;
     */
    public Calendar(String calendarName) {
        this.assignmentList = new ArrayList<>();
        this.name = calendarName;
    }

    public String getName() {
        return name;
    }

    public void setName(String calendarName) {
        this.name = calendarName;
    }

    /*
     * REQUIRES: an assignment
     * MODIFIES: this
     * EFFECTS: add an assignment to assignmentList
     */
    public void addAssignment(Assignment assignment) {
        this.assignmentList.add(assignment);
    }

    /*
     * REQUIRES: a timestamp startTime and a timestamp endTime of the time period want to search assignment
     * EFFECTS: returns assignments in the time period.
     */
    public List<Assignment> searchAssignmentByTime(int startTime, int endTime) {
        List<Assignment> returnAssignments = new ArrayList<>();
        for (Assignment i: this.assignmentList) {
            if (i.getDeadline() > startTime && i.getDeadline() <= endTime) {
                returnAssignments.add(i);
            }
        }
        Collections.sort(returnAssignments);
        return returnAssignments;
    }

    /*
     * REQUIRES: a timestamp startTime and a timestamp endTime of the time period want to search assignment
     * EFFECTS: returns assignments which is not done in the time period.
     */
    public List<Assignment> searchNotDoneAssignmentByTime(int startTime, int endTime) {
        List<Assignment> returnAssignments = new ArrayList<>();
        List<Assignment> searchAssignmentList = searchAssignmentByTime(startTime, endTime);
        for (Assignment i: searchAssignmentList) {
            if (i.getIsDoneTag() == 0) {
                returnAssignments.add(i);
            }
        }
        return returnAssignments;
    }

    /*
     * REQUIRES: an int id
     * EFFECTS: returns an assignment own this id.
     */
    public Assignment searchAssignmentById(int id) {
        // according id to find the assignment
        for (Assignment i: this.assignmentList) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }

    /*
     * REQUIRES: an int id, an int newDeadline, a string newContent
     * MODIFIES: this
     * EFFECTS: change the deadline and the content of the assignment own this id.
     */
    public boolean changeAssignment(int id, int newDeadline, String newContent) {
        Assignment assignment = searchAssignmentById(id);
        if (assignment != null) {
            assignment.setContent(newContent);
            assignment.setDeadline(newDeadline);
            return true;
        } else {
            return false;
        }

    }

    /*
     * REQUIRES: an int id
     * MODIFIES: this
     * EFFECTS: delete the assignment own this id.
     */
    public boolean deleteAssignment(int id) {
        Assignment assignment = searchAssignmentById(id);
        if (assignment == null) {
            return false;
        } else {
            this.assignmentList.remove(assignment);
            return true;
        }
    }

    /*
     * REQUIRES: an int id
     * MODIFIES: this
     * EFFECTS: change the isDoneTag of the assignment own this id to 0;
     */
    public boolean finishAssignment(int id) {
        Assignment assignment = searchAssignmentById(id);
        if (assignment == null) {
            return false;
        } else {
            assignment.setIsDoneTag(1);
            return true;
        }

    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("assignments", assignmentsToJson());
        return json;
    }

    // EFFECTS: returns things in this calendar as a JSON array
    private JSONArray assignmentsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Assignment assign : assignmentList) {
            jsonArray.put(assign.toJson());
        }

        return jsonArray;
    }
}
