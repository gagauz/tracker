package com.gagauz.tracker.db.model;


import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "after_action")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class AfterAction extends AbstractStageAction {

}
