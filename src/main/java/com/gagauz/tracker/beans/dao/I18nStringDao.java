package com.gagauz.tracker.beans.dao;

import java.util.Locale;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.I18nString;
import com.gagauz.tracker.db.model.I18nString.Id;


@Service
public class I18nStringDao extends AbstractDao<I18nString.Id, I18nString> {
    @Override
    protected Function<String, Id> getIdDeserializer() {
        return s -> {
            String[] ids = s.split("\\|");
            Locale l = Locale.forLanguageTag(ids[0]);

            return new Id(l, ids[1]);
        };
    }

}
