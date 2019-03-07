package com.xhxd.messagecenter.components;

import com.xhxd.messagecenter.common.util.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @auther: qindaorong
 * @Date: 2018/8/22 11:23
 * @Description:
 */
@Component
@Slf4j
public class ChannelCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        String str = Md5Utils.md5("xhjf_v1!#@messageServer");

        log.debug("[client_secret] is :{}",str);
    }
}
