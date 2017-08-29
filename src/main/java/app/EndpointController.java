package app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import app.MailService;
import lombok.extern.slf4j.Slf4j;
import model.MailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EndpointController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MailService mailService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public ModelAndView startup() {
        return new ModelAndView("/index.html");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/sendMail")
    // public MailResponse sendMail(@RequestBody HttpServletRequest request, response) {
    public boolean sendMail(@Valid @RequestBody MailRequest request) {
        log.info("here is our request {}", request);
        return mailService.sendMail(request);
    }
}