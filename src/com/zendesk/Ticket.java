package com.zendesk;

import java.util.Date;

public class Ticket {

    private String id;
    private String subject;
    private Date created_at;

    public Ticket(String id, String subject, Date created_at) {
        this.id = id;
        this.subject = subject;
        this.created_at = created_at;
    }

    /**********************
     * GETTERS and SETTERS
     **********************/

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
