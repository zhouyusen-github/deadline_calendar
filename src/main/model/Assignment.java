package model;


import org.json.JSONObject;
import persistence.Writable;

import java.text.SimpleDateFormat;
import java.util.Date;

// Represents an Assignment having an id, deadline(in timestamp), content and isDoneTag.
public class Assignment implements Comparable<Assignment>, Writable {
    private int id; // assignment id
    private static int count = 0; // tracks id of next assignment created
    private int deadline; //  timestamp by second
    private String content; // assignment content
    private int isDoneTag; // record is the assignment done

    /*
     * REQUIRES: an integer deadline and a String content
     * EFFECTS: assignment id is a positive integer not assigned to any other assignment;
     *          deadline on assignment is set to deadline;
     *          content on assignment is set to content;
     *          isDoneTag on assignment is set to 0;
     */
    public Assignment(int deadline, String content) {
        this.id = count;
        count++;
        this.deadline = deadline;
        this.content = content;
        this.isDoneTag = 0;
    }

    public Assignment(int id, int deadline, String content, int isDoneTag) {
        this.id = id;
        if (count <= id) {
            count = id + 1;
        }
        this.deadline = deadline;
        this.content = content;
        this.isDoneTag = isDoneTag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsDoneTag() {
        return isDoneTag;
    }

    public void setIsDoneTag(int isDoneTag) {
        this.isDoneTag = isDoneTag;
    }

    /*
     * REQUIRES: an assignment
     * EFFECTS: return the compare result of this assignment and the input assignment,
     *          which is the difference of their deadline;
     */
    @Override
    public int compareTo(Assignment o) {
        return this.getDeadline() - o.getDeadline();
    }

    /*
     * EFFECTS: returns a string representation of assignment
     */
    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date((long)deadline * 1000);
        String deadlineString = simpleDateFormat.format(date);
        return "Assignment{"
                + "id=" + id
                + ", deadline=" + deadlineString
                + ", content='" + content + '\''
                + ", isDoneTag=" + isDoneTag
                + '}';
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("deadline", deadline);
        json.put("content", content);
        json.put("isDoneTag", isDoneTag);
        return json;
    }
}
