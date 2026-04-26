package com.example.demo.sentinelservice;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProductService {

    // @SentinelResource：标注资源名，配置限流/熔断规则
    // value：资源名（在 Sentinel 控制台配置规则时用这个名字）
    // blockHandler：被限流/熔断时调用的方法
    // fallback：业务异常时的降级方法（fallback 和 blockHandler 区别：
    //           blockHandler 处理 Sentinel 控制的 BlockException
    //           fallback 处理业务代码抛出的 Throwable）
    @SentinelResource(value = "getProduct",
            blockHandler = "getProductBlocked",
            fallback = "getProductFallback")
    public Map<String, Object> getProduct(Integer id) {
        // 模拟偶发的慢请求
        if (id % 5 == 0) {
            try { Thread.sleep(2000); } catch (InterruptedException e) {}
        }
        // 模拟偶发的异常
        if (id % 7 == 0) {
            throw new RuntimeException("商品服务偶发异常");
        }

        Map<String, Object> product = new HashMap<>();
        product.put("id", id);
        product.put("name", "商品" + id);
        product.put("price", 99.9 * id);
        return product;
    }

    // blockHandler：被 Sentinel 限流或熔断时调用
    // 必须和原方法同一个类，参数列表最后多一个 BlockException
    public Map<String, Object> getProductBlocked(Integer id, BlockException ex) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", "系统繁忙，请稍后再试");
        result.put("blocked", true);
        result.put("reason", ex.getClass().getSimpleName()); // FlowException/DegradeException
        return result;
    }

    // fallback：业务异常（非 BlockException）时调用
    public Map<String, Object> getProductFallback(Integer id, Throwable t) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", "商品信息暂时不可用");
        result.put("fallback", true);
        result.put("error", t.getMessage());
        return result;
    }
}
