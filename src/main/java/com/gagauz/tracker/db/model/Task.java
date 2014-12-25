package com.gagauz.tracker.db.model;

import com.gagauz.tracker.db.base.CollectionType;
import com.gagauz.tracker.db.base.CommitOwner;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "task")
@TypeDefs({
        @TypeDef(name = "listOf.Attachment",
                typeClass = CollectionType.class,
                parameters = @Parameter(name = CollectionType.ELEMENT_CLASS, value = "com.gagauz.tracker.db.model.Attachment")
        )
})
public class Task extends CommitOwner {

    @Embeddable
    public static class TaskId implements Serializable {
        private static final long serialVersionUID = 4441697145474451670L;

        private int taskHeaderId;
        private int versionId;

        public TaskId(int taskHeaderId, int versionId) {
            this.setTaskHeaderId(taskHeaderId);
            this.setVersionId(versionId);
        }

        protected TaskId() {
        }

        public int getTaskHeaderId() {
            return taskHeaderId;
        }

        public void setTaskHeaderId(int taskHeaderId) {
            this.taskHeaderId = taskHeaderId;
        }

        public int getVersionId() {
            return versionId;
        }

        public void setVersionId(int versionId) {
            this.versionId = versionId;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj instanceof TaskId) {
                return taskHeaderId == ((TaskId) obj).getTaskHeaderId() && versionId == ((TaskId) obj).getVersionId();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return taskHeaderId * (taskHeaderId + versionId);
        }

    }

    private TaskId id = new TaskId();
    private TaskHeader taskHeader;
    private Version version;
    private User creator;
    private User owner;
    private Date created = new Date();
    private Date updated = new Date();
    private List<SubTask> subTasks;
    private List<Bug> bugs;
    private String name;
    private String description;

    private List<Attachment> attachments;

    @EmbeddedId
    public TaskId getId() {
        return id;
    }

    public void setId(TaskId id) {
        this.id = id;
    }

    @MapsId("taskHeaderId")
    @JoinColumn(nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public TaskHeader getTaskHeader() {
        return taskHeader;
    }

    public void setTaskHeader(TaskHeader taskHeader) {
        this.taskHeader = taskHeader;
    }

    @MapsId("versionId")
    @JoinColumn(nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public Version getVersion() {
        return version;
    }

    public void setVersion(Version project) {
        this.version = project;
    }

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(columnDefinition = "text")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "task")
    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "task")
    public List<Bug> getBugs() {
        return bugs;
    }

    public void setBugs(List<Bug> bugs) {
        this.bugs = bugs;
    }

    @Column(columnDefinition = "TEXT")
    @Type(type = "listOf.Attachment")
    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

}
