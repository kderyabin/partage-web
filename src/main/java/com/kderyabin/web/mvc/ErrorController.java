package com.kderyabin.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Outputs application errors.
 */
@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    ModelAndView display(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("error");
        String errorMsg;
        int httpErrorCode = getErrorCode(request);

        switch (httpErrorCode) {
            case 400:
                errorMsg = "Bad Request";
                break;
            case 401:
                errorMsg = "Unauthorized";
                break;
            case 404:
                errorMsg = "Page not found";
                break;
            default:
                errorMsg = "Internal Server Error";
                break;
        }
        modelAndView.addObject("title", errorMsg);
        modelAndView.addObject("errorCode", httpErrorCode);
        modelAndView.addObject("errorMsg", errorMsg);
        return modelAndView;
    }

    /**
     * @param request Current request
     * @return HTTP error code
     */
    private int getErrorCode(HttpServletRequest request) {
        return (Integer) request
                .getAttribute("javax.servlet.error.status_code");
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}