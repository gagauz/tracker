package com.gagauz.tracker.db.model;

import com.gagauz.tracker.db.base.HashSetType;
import com.gagauz.tracker.db.base.Identifiable;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "ticket_status")
@TypeDefs({
        @TypeDef(name = "listOf.StatusId", typeClass = HashSetType.class, parameters = {
                @Parameter(name = com.gagauz.tracker.db.base.CollectionType.CLASS, value = "java.lang.Integer"),
                @Parameter(name = com.gagauz.tracker.db.base.CollectionType.SERIALIZER, value = "com.gagauz.tracker.db.utils.IntegerSerializer")
        })
})
public class TicketStatus implements Identifiable {
    private int id;
    private Project project;
    private String name;
    private String description;
    private Collection<Integer> allowedFrom = new ArrayList<>();
    private Collection<Integer> allowedTo = new ArrayList<>();

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Type(type = "listOf.StatusId")
    @Column(nullable = false)
    public Collection<Integer> getAllowedFrom() {
        return allowedFrom;
    }

    public void setAllowedFrom(Collection<Integer> allowedFrom) {
        this.allowedFrom = allowedFrom;
    }

    @Type(type = "listOf.StatusId")
    @Column(nullable = false)
    public Collection<Integer> getAllowedTo() {
        return allowedTo;
    }

    public void setAllowedTo(Collection<Integer> allowedTo) {
        this.allowedTo = allowedTo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj) {
            return false;
        }

        return obj.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        return id;
    }

}
