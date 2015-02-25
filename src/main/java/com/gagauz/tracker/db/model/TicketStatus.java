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
        @TypeDef(name = "listOf.StatusId",
                typeClass = HashSetType.class,
                parameters = {
                        @Parameter(name = com.gagauz.tracker.db.base.CollectionType.CLASS, value = "java.lang.Integer"),
                        @Parameter(name = com.gagauz.tracker.db.base.CollectionType.SERIALIZER, value = "com.gagauz.tracker.db.utils.IntegerSerializer")
                }
        )
})
public class TicketStatus implements Identifiable {
    private int id;
    private Project project;
    private String name;
    private String description;
    private Collection<Integer> allowedFrom = new ArrayList<Integer>();
    private Collection<Integer> allowedTo = new ArrayList<Integer>();

    @Override
    @Id
    @SequenceGenerator(name = "id_sequence", sequenceName = "status_id_seq", allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id_sequence")
    @Column(unique = true, nullable = false)
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

    //    @JoinTable(name = "jt1", joinColumns = {@JoinColumn(name = "allowedFrom1_id", referencedColumnName = "id", nullable = true)})
    //    @ManyToMany(fetch = FetchType.LAZY)
    //    public Collection<TicketStatus> getAllowedFrom() {
    //        return allowedFrom;
    //    }
    //
    //    public void setAllowedFrom(Collection<TicketStatus> allowedFrom) {
    //        this.allowedFrom = null == allowedFrom ? new ArrayList<TicketStatus>() : new ArrayList<TicketStatus>(allowedFrom);
    //    }
    //
    //    @JoinTable(name = "jt1", joinColumns = {@JoinColumn(name = "allowedTo1_id", referencedColumnName = "id", nullable = true)})
    //    @ManyToMany(fetch = FetchType.LAZY)
    //    public Collection<TicketStatus> getAllowedTo() {
    //        return allowedTo;
    //    }
    //
    //    public void setAllowedTo(Collection<TicketStatus> allowedTo) {
    //        this.allowedTo = null == allowedTo ? new ArrayList<TicketStatus>() : new ArrayList<TicketStatus>(allowedTo);
    //    }

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