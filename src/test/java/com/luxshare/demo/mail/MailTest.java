package com.luxshare.demo.mail;

import com.luxshare.demo.DemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;

/**
 * 邮件测试
 *
 * @author lion hua
 * @since 2019-11-29
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
public class MailTest {

    @Autowired
    private EmailTest emailTest;

    @Test
    public void test() throws MessagingException {
        emailTest.sendSimpleMail();
    }
}
