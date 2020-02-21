package com.laboratorio.controller;

import com.laboratorio.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@Slf4j
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView model = new ModelAndView();

        model.setViewName("home");
        return model;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/export", method = {RequestMethod.POST, RequestMethod.GET})
    public void export(HttpServletRequest request, HttpServletResponse response
            , @RequestParam Map<String, Object> params) {
        log.info("PARAMS RECEIVED {}", params);
        reportService.exportPdfFile(response, params);
    }

}