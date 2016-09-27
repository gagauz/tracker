package com.gagauz.tracker.beans.scenarios;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gagauz.tracker.beans.dao.I18nStringDao;
import com.gagauz.tracker.beans.setup.DataBaseScenario;

@Service
public class ScI18n extends DataBaseScenario {

    @Autowired
    private I18nStringDao i18nStringDao;
    @Override
    protected void execute() {
        InputStream is = getClass().getResourceAsStream("/app.properties");
        Properties p = new Properties();
        try {
            p.load(is);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
