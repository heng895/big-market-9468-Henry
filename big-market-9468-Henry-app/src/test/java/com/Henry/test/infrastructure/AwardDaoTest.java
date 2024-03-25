package com.Henry.test.infrastructure;



import com.Henry.infrastructure.persistent.dao.IAwardDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Description 奖品持久化单元测试
 * @Author Henry
 * @Date 2024/3/25
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AwardDaoTest {
    @Resource
    private IAwardDao awardDao;

    @Test
    public void testQueryAwardList() {
        log.info("奖品列表: {}", awardDao.queryAwardList());
    }
}
