package com.gagauz.tracker.beans.scenarios;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.gagauz.tapestry.web.config.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gagauz.tracker.beans.dao.I18nStringDao;
import com.gagauz.tracker.beans.setup.DataBaseScenario;
import com.gagauz.tracker.db.model.I18nString;
import com.gagauz.tracker.db.model.I18nString.Id;

@Service
public class ScI18n extends DataBaseScenario {

    @Autowired
    private I18nStringDao i18nStringDao;
    @Override
    protected void execute() {
        InputStream is = Global.servletContext.getResourceAsStream("/WEB-INF/auth.properties");
        // InputStream is =
        // getClass().getResourceAsStream("context:/WEB-INF/app.properties");
        Properties p = new Properties();

        try {
            p.load(is);
            List<I18nString> result = new ArrayList<>(p.size());
            p.entrySet().forEach(e -> {
                I18nString i18 = new I18nString();
                i18.setId(new Id(Locale.ENGLISH, e.getKey().toString()));
                if (null != e.getValue()) {
                    i18.setValue(e.getValue().toString());
                }
                result.add(i18);
            });
            i18nStringDao.save(result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
