package com.luxshare.demo.quartz.listener;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.listeners.TriggerListenerSupport;

@Slf4j
@NoArgsConstructor
public class MyTriggerListener extends TriggerListenerSupport {
    @Override
    public String getName() {
        return "myTriggerListener !";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        super.triggerFired(trigger, context);
        log.info("Trigger is fired!!!");
    }
}
