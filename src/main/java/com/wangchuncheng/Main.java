package com.wangchuncheng;

import com.wangchuncheng.controller.DataEmulator;
import com.wangchuncheng.controller.TaskExecutePool;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-config.xml");
        TaskExecutePool executorPool = (TaskExecutePool) ctx.getBean("executorPool");
        DataEmulator emulator = (DataEmulator) ctx.getBean("dataEmulator");
        executorPool.getExecutor().execute(emulator);
    }
}
