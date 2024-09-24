package com.chattest.controller;

import com.chattest.dto.ConditionDTO;
import com.chattest.repository.MachbaseJDBC;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Log4j2
@Controller
@RequestMapping("/condition")
@RequiredArgsConstructor
public class ConditionController {
    private final MachbaseJDBC machbaseJDBC;

    @GetMapping("/data")
    @ResponseBody
    public ConditionDTO getData() {
        log.info("condition Controller....");
    return machbaseJDBC.getAirConditionData();
    }
}
