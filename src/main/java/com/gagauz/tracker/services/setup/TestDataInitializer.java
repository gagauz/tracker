package com.gagauz.tracker.services.setup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xl0e.testdata.DataBaseScenario;

@Service
public class TestDataInitializer {

    @Autowired
    private DataBaseScenario[] scenarios;

    @Transactional
    public void execute() {
        for (DataBaseScenario scenario : scenarios) {
            scenario.run();
        }
    }
}
