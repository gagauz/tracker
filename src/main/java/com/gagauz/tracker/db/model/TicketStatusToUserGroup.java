package com.gagauz.tracker.db.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.xl0e.hibernate.model.IModel;

@Entity
@Table(name = "ticket_status_to_user_group")
public class TicketStatusToUserGroup implements IModel<TicketStatusToUserGroup.Id> {

    @Embeddable
    public static class Id implements Serializable {
        private static final long serialVersionUID = 1788098720087878267L;
        private Integer ticketStatusId;
        private Integer userGroupId;

        public Integer getTicketStatusId() {
            return ticketStatusId;
        }

        public void setTicketStatusId(Integer ticketStatusId) {
            this.ticketStatusId = ticketStatusId;
        }

        public Integer getUserGroupId() {
            return userGroupId;
        }

        public void setUserGroupId(Integer userGroupId) {
            this.userGroupId = userGroupId;
        }

        @Override
        public boolean equals(Object obj) {
            if (null == obj || this == obj || !(obj instanceof Id)) {
                return this == obj;
            }
            Id other = (Id) obj;
            return other.ticketStatusId == ticketStatusId && other.userGroupId == userGroupId;
        }

        @Override
        public String toString() {
            return String.valueOf(ticketStatusId) + '_' + userGroupId;
        }

        public static Id fromString(String string) {
            if (null == string) {
                return null;
            }
            Id id = new Id();
            String[] tokens = string.split("_");
            id.ticketStatusId = Integer.valueOf(tokens[0]);
            id.userGroupId = Integer.valueOf(tokens[1]);
            return id;
        }
    }

    private Id id = new Id();
    private TicketStatus ticketStatus;
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
    @JoinColumn(name = "ticketStatusId", insertable = false, updatable = false, referencedColumnName = "id")
    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userGroupId", insertable = false, updatable = false, referencedColumnName = "id")
    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

}
