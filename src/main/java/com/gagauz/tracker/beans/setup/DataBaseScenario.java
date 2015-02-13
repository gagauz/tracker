package com.gagauz.tracker.beans.setup;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class DataBaseScenario {

    private static final Set<Class<?>> executedScenarios = new HashSet<Class<?>>();

    protected Random rand = new Random(System.currentTimeMillis());

    protected abstract void execute();

    public final void run() {

        if (isExecuted()) {
            return;
        }

        for (DataBaseScenario scenario : getDependsOn()) {
            if (scenario.equals(this)) {
                throw new IllegalStateException("Scenario " + this.getClass() + " depends on itself!");
            }

            if (!scenario.isExecuted()) {
                scenario.run();
            }

        }

        try {
            System.out.println("* Execute scenario " + getClass().getSimpleName());
            execute();
        } finally {
            executedScenarios.add(this.getClass());
        }
    }

    protected final boolean isExecuted() {
        return executedScenarios.contains(this.getClass());
    }

    protected DataBaseScenario[] getDependsOn() {
        return new DataBaseScenario[0];
    }
}
