package com.wangchuncheng.emulator;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

/**
 * @author chuncheng.wang@hand-china.com 2019-01-29 16:29:28
 */
@Component
public class EmulatorStartup implements ApplicationListener<ApplicationStartedEvent> {
    private DataEmulator dataEmulator;

    public EmulatorStartup(DataEmulator dataEmulator) {
        this.dataEmulator = dataEmulator;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        Executors.newSingleThreadExecutor().execute(dataEmulator);
    }
}
