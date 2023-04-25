package com.cloudtravel.consumer.controller;

import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.atomic.LongAdder;

@RestController("export")
public class ExcelExportController {

    LongAdder longAdder = new LongAdder();

    public void test() {
        longAdder.longValue();
    }
}
