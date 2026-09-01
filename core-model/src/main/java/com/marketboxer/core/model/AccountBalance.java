package com.marketboxer.core.model;

import lombok.Data;

/**
 * 账户现金，股票数量情况
 *
 * @author qinjie
 * Created At : 2022/10/24 22:35
 */
@Data
public class AccountBalance {

    private long secAmount;

    private long moneyAmountInCent;

    private String strategyId;

}
