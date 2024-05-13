package com.lai.laiojbackendjudgeservice.judge;


import com.lai.laiojbackendjudgeservice.judge.rabbitmq.InitRabbitMq;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;


// todo 如需开启 Redis，须移除 exclude 中的内容
//exclude = {RedisAutoConfiguration.class}
@SpringBootApplication()
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.lai")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.lai.laiojbackendserviceclient.service"})

public class laiojBackendJudgeServiceApplication {

    public static void main(String[] args) {
        //初始化消息队列
        InitRabbitMq.doInit();
        SpringApplication.run(laiojBackendJudgeServiceApplication.class, args);
    }

}
