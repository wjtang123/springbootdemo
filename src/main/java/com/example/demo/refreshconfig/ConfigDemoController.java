package com.example.demo.refreshconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RefreshScope
@RestController
@RequestMapping("/config-demo")
class ConfigDemoController {

    // 从 Nacos 配置中心读取，支持热更新
    @Value("${app.max-retry:3}")
    private int maxRetry;

    @Value("${app.welcome-message:Default Message}")
    private String welcomeMessage;

    @Value("${feature.new-user-gift:false}")
    private boolean newUserGift;

    @GetMapping
    public Map<String, Object> showConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("maxRetry", maxRetry);
        config.put("welcomeMessage", welcomeMessage);
        config.put("newUserGift", newUserGift);
        config.put("tip", "修改 Nacos 中的配置，刷新此接口，值会自动更新");
        return config;
    }
    /*
     * 验证热更新：
     *   1. 启动服务，访问 /config-demo，看到初始值
     *   2. 在 Nacos 控制台修改配置并发布
     *   3. 再次访问 /config-demo，值自动变了（无需重启服务）
     *
     * @RefreshScope 的原理：
     *   Nacos 配置变更 → 触发 RefreshEvent → Spring 重新创建被 @RefreshScope 标注的 Bean
     *   注意：不要在 @RefreshScope 的 Bean 里做耗时的初始化操作
     */
}
