package com.gagauz.tracker.db.model;

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xl0e.hibernate.model.IModel;

@Entity
@Table(name = "i18n_string")
public class I18nString implements IModel<I18nString.Id> {
    @Embeddable
    public static class Id implements Serializable {
        private static final long serialVersionUID = -1449333149260035058L;
        private Locale idLocale;
        private String idKey;

        public Id() {

        }

        public Id(Locale locale, String key) {
            this.idLocale = locale;
            this.idKey = key;
        }

        public Locale getIdLocale() {
            return this.idLocale;
        }

        public void setIdLocale(Locale locale) {
            this.idLocale = locale;
        }

        @Column(nullable = false)
        public String getIdKey() {
            return this.idKey;
        }

        public void setIdKey(String key) {
            this.idKey = key;
        }

        @Override
        public String toString() {
            return this.idLocale.getLanguage() + "|" + this.idKey;
        }
    }

    private Id id;
    private String value;

    @Override
    @EmbeddedId
    public Id getId() {
        return this.id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    @Column(length = 1024)
    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @javax.persistence.Transient
    public Locale getLocale() {
        return this.id.getIdLocale();
    }

    @javax.persistence.Transient
    public String getKey() {
        return this.id.getIdKey();
    }

}
