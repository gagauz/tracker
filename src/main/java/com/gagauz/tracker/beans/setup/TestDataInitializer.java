package com.gagauz.tracker.beans.setup;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.gagauz.tracker.utils.AppProperties;

@Service
public class TestDataInitializer {

    @Autowired
    private DataBaseScenario[] scenarios;

    @Autowired
    protected PlatformTransactionManager transactionManager;

    @Transactional
    public void execute() {
        for (DataBaseScenario scenario : scenarios) {
            scenario.run();
        }
    }

    @PostConstruct
    void executeScenarios() {
        if (AppProperties.FILL_TEST_DATA.getBoolean()) {
            TransactionTemplate tmpl = new TransactionTemplate(transactionManager);
            tmpl.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    execute();
                }
            });
        }
    }
}
