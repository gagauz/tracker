package com.gagauz.tracker.db.model;

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "i18n_string")
public class I18nString {
    @Embeddable
    public static class Id implements Serializable {
        private static final long serialVersionUID = -1449333149260035058L;
        private Locale locale;
        private String key;

        public Id() {

        }
        public Id(Locale locale, String key) {
            this.locale = locale;
            this.key = key;
        }
        public Locale getLocale() {
            return locale;
        }
        public void setLocale(Locale locale) {
            this.locale = locale;
        }

        @Column(nullable = false)
        public String getKey() {
            return key;
        }
        public void setKey(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return locale.getLanguage() + "|" + key;
        }
    }

    private Id id;
    private String value;

    @EmbeddedId
    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    @Column(length = 1024)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
