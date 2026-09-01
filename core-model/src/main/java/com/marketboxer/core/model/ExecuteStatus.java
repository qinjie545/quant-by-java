package com.marketboxer.core.model;

/**
 * 交易执行状态, 推荐状态机如下
 *         DONE ------> DEAD
 *         ^
 *         |
 *         V
 * NEW -> ON -> DONE
 *        ^    |
 *        |    -> FAILED ------> DEAD
 *        |          |
 *        |<——-----新交易周期
 * @author qinjie
 * Created At : 2022/10/17 0:23
 */
public enum ExecuteStatus {
    /**
     * 新创建，未开始挂单
     */
    STATUS_NEW(0),
    /**
     * 已经挂单成功
     */
    STATUS_ON(100),
    /**
     * 当前交易已经执行
     */
    STATUS_DONE(200),
    /**
     * 当前交易被取消，会再次重试
     */
    STATUS_FAILED(300),
    /**
     * 当前交易被撤单，可以手动重新上单
     */
    STATUS_DOWN(101),
    /**
     * 彻底删除，不可再重新拉起
     */
    STATUS_DEAD(400)
    ;

    int code;

    ExecuteStatus(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
