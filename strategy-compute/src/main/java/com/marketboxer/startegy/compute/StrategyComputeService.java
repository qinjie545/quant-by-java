package com.marketboxer.startegy.compute;

import com.marketboxer.startegy.compute.computor.StrategyCompute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StrategyComputeService {

    @Autowired
    StrategyComputorFactory strategyComputorFactory;

    /**
     * 计算所有的策略
     * 1.查询出来所有的生效的策略编号
     * 2.通过编号找个每个策略的执行器
     * 3.执行各自的计算器，算出来的结果，通过调用trade-execute保存到预执行队列存储中
     * @return 返回成功完成计算的策略的策略ID
     */
    public List<String> computeAllStrategy() {
        //TODO 当前为了简化处理，只通过Hardcode执行当前的策略
        final List<String> successComputeIds = new ArrayList<>(10);
        StrategyCompute strategyCompute = strategyComputorFactory.getComputorOf("Strategy001_MA");
        boolean success = strategyCompute.compute();
        if (success) {
            successComputeIds.add(strategyCompute.getStrategyId());
        }
        return successComputeIds;
    }
}
