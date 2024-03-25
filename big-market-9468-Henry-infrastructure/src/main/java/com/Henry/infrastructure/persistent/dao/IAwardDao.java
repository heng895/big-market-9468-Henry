package com.Henry.infrastructure.persistent.dao;

import com.Henry.infrastructure.persistent.po.Award;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description 奖品表DAO
 * @Author Henry
 * @Date 2024/3/25
 */
@Mapper
public interface IAwardDao {
    List<Award> queryAwardList();
}
