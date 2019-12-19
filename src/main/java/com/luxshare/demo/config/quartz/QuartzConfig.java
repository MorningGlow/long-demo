package com.luxshare.demo.config.quartz;

import com.luxshare.demo.job.MyJob;
import com.luxshare.demo.quartz.listener.MyTriggerListener;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * quartz config
 * 1.Seconds （秒）
 * 2.Minutes（分）
 * 3.Hours（小时）
 * 4.Day-of-Month  （天）
 * 5.Month（月）
 * 6.Day-of-Week （周）
 * 7.Year（年）可选
 * 字段名              允许的值                    允许的特殊字符
 * 秒                     0-59                           , - * /
 * 分                     0-59                           , - * /
 * 小时                  0-23                           , - * /
 * 日                     1-31                            , - * ? / L W C
 * 月                     1-12 or JAN-DEC        , - * /
 * 周几                  1-7 or SUN-SAT         , - * ? / L C #
 * 年(可选字段)     empty                         1970-2099 , - * /
 * <p>
 * * ：代表所有可能的值。因此，“*”在Month中表示每个月，在Day-of-Month中表示每天，在Hours表示每小时
 * - ：表示指定范围。
 * , ：表示列出枚举值。例如：在Minutes子表达式中，“5,20”表示在5分钟和20分钟触发。
 * / ：被用于指定增量。例如：在Minutes子表达式中，“0/15”表示从0分钟开始，每15分钟执行一次。"3/20"表示从第三分钟开始，每20分钟执行一次。和"3,23,43"（表示第3，23，43分钟触发）的含义一样。
 * ? ：用在Day-of-Month和Day-of-Week中，指“没有具体的值”。当两个子表达式其中一个被指定了值以后，为了避免冲突，需要将另外一个的值设为“?”。例如：想在每月20日触发调度，不管20号是星期几，只能用如下写法：0 0 0 20 * ?，其中最后以为只能用“?”，而不能用“*”。
 * <p>
 * 0 * * * * ? 每1分钟触发一次
 * 0 0 * * * ? 每天每1小时触发一次
 * 0 0 10 * * ? 每天10点触发一次
 * 0 * 14 * * ? 在每天下午2点到下午2:59期间的每1分钟触发
 * 0 30 9 1 * ? 每月1号上午9点半
 * 0 15 10 15 * ? 每月15日上午10:15触发
 *
 * @author lion hua
 * @since 2019-12-02
 */
@Configuration
public class QuartzConfig {

    public static final String TEST_GROUP = "testGroup";

    @Bean
    public JobDetail jobDetail() {
        JobDetail detail = JobBuilder.newJob(MyJob.class)
                .withIdentity("myJob", TEST_GROUP)
                .storeDurably()
                .build();
        return detail;
    }

    @Bean
    public Trigger trigger() {
        return TriggerBuilder.newTrigger()
//                .startNow()
                .withIdentity("myTrigger", TEST_GROUP)
                .forJob(jobDetail())
                // 从第五分钟开始,五分钟执行一次
                .withSchedule(CronScheduleBuilder.cronSchedule("0 1/5 * * * ?"))
                .build();
    }

    /**
     * 在 spring boot 中不会生效
     *
     * @return TriggerListener
     */
    @Bean
    public TriggerListener triggerListener() {
        return new MyTriggerListener();
    }


}
