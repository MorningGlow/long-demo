package com.luxshare.demo.mail;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * mail 实体
 *
 * @author lion hua
 * @since 2019-12-19
 */
@Getter
@Setter
@Accessors(chain = true)
public class MailEntity implements Serializable {

    /**
     * 发送人
     */
    private String from;

    /**
     * 发送给谁
     */
    private List<String> to;

    /**
     * 邮件标题
     */
    private String subject;

    /**
     * 邮件发送的主体内容
     */
    private StringBuilder sb;
}
