package jpastudy.jpashop.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class HomeController {
    @RequestMapping("/frag")
    public String home()
    {
        // header 연습용
        log.info("home controller");
        return "home";
    }
    @RequestMapping("/") // getmapping 해도 됨
    public String index()
    {   // 진짜 Home
        return "index";
    }
}
