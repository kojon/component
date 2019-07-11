package cn.net.chestnut.zkcurator.controller;

import cn.net.chestnut.zkcurator.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @Author tarzan
 * @Date 2019/7/11 10:36 AM
 **/
@Controller
@Slf4j
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @RequestMapping("/get")
    @ResponseBody
    public String get(HttpServletResponse response) {
        String message=ticketService.start();
        if (message!=null) {
            return message;
        } else {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
        return "fail";
    }

    @RequestMapping("/reset")
    @ResponseBody
    public String reset() {
        ticketService.reset();
        return "success";
    }
}
