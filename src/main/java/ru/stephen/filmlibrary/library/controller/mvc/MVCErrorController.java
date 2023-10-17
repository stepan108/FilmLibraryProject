package ru.stephen.filmlibrary.library.controller.mvc;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import static jakarta.servlet.RequestDispatcher.*;

@Controller
@Slf4j
public class MVCErrorController
        implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request,
                              Model model) {
        log.error("Ошибка! {}", request.getAttribute(ERROR_STATUS_CODE), request.getAttribute(ERROR_EXCEPTION), request.getAttribute(ERROR_REQUEST_URI));
        model.addAttribute("exception",
                "Ошибка " + request.getAttribute(ERROR_STATUS_CODE) +
                " в маппинге " + request.getAttribute(ERROR_REQUEST_URI));
        return "error";
    }
}
