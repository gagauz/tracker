package com.gagauz.tracker.web.services;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry5.ioc.MessageFormatter;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.internal.util.MessageFormatterImpl;
import org.apache.tapestry5.web.config.Global;

import com.gagauz.tracker.beans.dao.I18nStringDao;

public class MessageServiceOverride implements Messages {

    private static final String UNDEFINED_MESSAGE = "[[ Undefined key: ";

    private Messages originalMessages;
    private I18nStringDao i18nStringDao;

    private Map<Locale, Map<String, String>> cache = new HashMap<>();
    private Map<Locale, Map<String, MessageFormatter>> formatterCache = new HashMap<>();

    public MessageServiceOverride(Messages messages, I18nStringDao dao) {
        this.originalMessages = messages;
        this.i18nStringDao = dao;
    }

    private Map<Locale, Map<String, String>> getCache() {
        if (this.cache.isEmpty()) {
            final Map<Locale, Map<String, String>> cache0 = new HashMap<>();
            this.i18nStringDao.findAll().forEach(e -> {
                Map<String, String> map = cache0.get(e.getLocale());
                if (null == map) {
                    map = new HashMap<>();
                    cache0.put(e.getLocale(), map);
                }
                map.put(e.getKey(), e.getValue());
            });
            this.cache = Collections.unmodifiableMap(cache0);
            this.formatterCache = new HashMap<>();
        }
        return this.cache;
    }

    private Map<String, String> getLocaleMap() {
        Map<String, String> map = getCache().get(Global.getLocale());
        return null == map ? Collections.emptyMap() : map;
    }

    private Map<String, MessageFormatter> getLocaleFormatterMap() {
        Map<String, MessageFormatter> map = this.formatterCache.get(Global.getLocale());
        return null == map ? Collections.emptyMap() : map;
    }

    @Override
    public Set<String> getKeys() {
        return getLocaleMap().keySet();
    }

    @Override
    public boolean contains(String key) {
        return getLocaleMap().containsKey(key);
    }

    @Override
    public String get(String key) {
        String value = getLocaleMap().get(key);
        if (null == value) {
            this.originalMessages.get(key);
        }
        return value;
    }

    @Override
    public MessageFormatter getFormatter(String key) {
        MessageFormatter result = getLocaleFormatterMap().get(key);

        if (result == null) {
            String format = getLocaleMap().get(key);
            if (null == format) {
                return this.originalMessages.getFormatter(key);
            }
            result = buildMessageFormatter(format);
            Map<String, MessageFormatter> map = this.formatterCache.get(Global.getLocale());
            if (null == map) {
                map = new HashMap<>();
                this.formatterCache.put(Global.getLocale(), map);
            }
            map.put(key, result);
        }

        return result;
    }

    private MessageFormatter buildMessageFormatter(String format) {
        return new MessageFormatterImpl(format, Global.getLocale());
    }

    @Override
    public String format(String key, Object... args) {
        return getFormatter(key).format(args);
    }

}
