package kr.ganjuproject.controller;

import kr.ganjuproject.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/email/{mail}")
    public @ResponseBody String emailCode(@PathVariable String mail){
        return emailService.sendEmail(mail)+"";
    }

}
