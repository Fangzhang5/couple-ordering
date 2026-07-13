package com.guowei.ordering.controller;

import com.guowei.ordering.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class HealthController {

    @GetMapping("/health")
    public Result<Map<String, Object>> health(){
        return Result.success(Map.of("status","up"));
    }

}
