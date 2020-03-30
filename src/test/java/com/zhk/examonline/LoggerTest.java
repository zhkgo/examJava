package com.zhk.examonline;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LoggerTest {

    @Test
    public void test1(){
        String name="zhk";
        String password="123456";
        System.out.println(Calendar.DAY_OF_WEEK);
        log.info("name: {} ,password :{}",name, password);
        log.debug("debug...");
        log.info("info...");
        log.error("error...");
    }
}
