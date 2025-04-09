package vip.gpg123.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.feign.TestFeign;


@RestController
public class TestController {

    @Autowired
    TestFeign testFeign;

    @GetMapping("/test")
    public void test() {
        System.out.println("调用message");
        testFeign.test();
    }
}