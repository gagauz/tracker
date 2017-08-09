package com.gagauz.tracker.beans.scenarios;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.tapestry5.web.config.Global;
import org.springframework.beans.factory.annotation.Autowired;

import com.gagauz.tracker.beans.dao.I18nStringDao;
import com.gagauz.tracker.beans.setup.DataBaseScenario;
import com.gagauz.tracker.db.model.I18nString;
import com.gagauz.tracker.db.model.I18nString.Id;

public class ScI18n extends DataBaseScenario {

    @Autowired
    private I18nStringDao i18nStringDao;

    @Override
    protected void execute() {
        importLocale(Locale.ENGLISH, "app.properties");
        importLocale(new Locale("ru"), "app_ru.properties");
    }

    private void importLocale(Locale locale, String filename) {
        try {
            Reader reader = new FileReader(
                    new File(Global.getServletContex().getRealPath("/WEB-INF/" + filename)));
            Properties p = new Properties();
            p.load(reader);
            List<I18nString> result = new ArrayList<>(p.size());
            p.entrySet().forEach(e -> {
                I18nString i18 = new I18nString();
                i18.setId(new Id(locale, e.getKey().toString()));
                if (null != e.getValue()) {
                    i18.setValue(e.getValue().toString());
                }
                result.add(i18);
            });
            this.i18nStringDao.saveAll(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
