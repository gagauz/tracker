package com.gagauz.tracker.db.model;

public interface StageTrigger {
    void triggerStages();

    void addStage(Stage stage);

    void removeStage(Stage stage);
}
