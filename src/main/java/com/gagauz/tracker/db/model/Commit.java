package com.gagauz.tracker.db.model;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "commit")
public class Commit {
    private String hash;
    private User author;
    private String comment;
    private Date date;

    @Id
    @Column(updatable = false, nullable = false)
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @JoinColumn
    @ManyToOne
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Column(nullable = false)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Column(updatable = false, nullable = false)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
