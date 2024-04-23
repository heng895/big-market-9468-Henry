package com.Henry.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 抽奖奖品实体
 * @Author Henry
 * @Date 2024/4/1
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RaffleAwardEntity {

    /** 奖品ID */
    private Integer awardId;
    /** 奖品配置信息 */
    private String awardConfig;
    /** 奖品顺序号 */
    private Integer sort;
}
