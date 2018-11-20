package com.gagauz.tracker.db.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.xl0e.hibernate.model.IModel;

@Entity
@Table(name = "ticket_type_to_user_group")
public class TicketTypeToUserGroup implements IModel<TicketTypeToUserGroup.Id> {

    @Embeddable
    public static class Id implements Serializable {
        private static final long serialVersionUID = 1788098720087878267L;
        private Integer ticketTypeId;
        private Integer userGroupId;
        private TicketTransition transition;

        public Integer getTicketTypeId() {
            return ticketTypeId;
        }

        public void setTicketTypeId(Integer ticketStatusId) {
            this.ticketTypeId = ticketStatusId;
        }

        public Integer getUserGroupId() {
            return userGroupId;
        }

        public void setUserGroupId(Integer userGroupId) {
            this.userGroupId = userGroupId;
        }

        public TicketTransition getTransition() {
            return transition;
        }

        public void setTransition(TicketTransition transition) {
            this.transition = transition;
        }

        @Override
        public boolean equals(Object obj) {
            if (null == obj || this == obj || !(obj instanceof Id)) {
                return this == obj;
            }
            Id other = (Id) obj;
            return other.ticketTypeId == ticketTypeId && other.userGroupId == userGroupId;
        }

        @Override
        public String toString() {
            return transition.name() + '_' + String.valueOf(ticketTypeId) + '_' + userGroupId;
        }

        public static Id fromString(String string) {
            if (null == string) {
                return null;
            }
            Id id = new Id();
            String[] tokens = string.split("_");
            id.transition = TicketTransition.valueOf(tokens[0]);
            id.ticketTypeId = Integer.valueOf(tokens[1]);
            id.userGroupId = Integer.valueOf(tokens[2]);
            return id;
        }
    }

    private Id id = new Id();
    private TicketType ticketType;
    private UserGroup userGroup;

    @Override
    @EmbeddedId
    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticketTypeId", insertable = false, updatable = false, referencedColumnName = "id")
    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userGroupId", insertable = false, updatable = false, referencedColumnName = "id")
    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    @Transient
    public TicketTransition getTransition() {
        return null == id ? null : id.transition;
    }
}
