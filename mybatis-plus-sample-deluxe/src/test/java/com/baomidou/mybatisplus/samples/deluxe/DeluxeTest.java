package com.baomidou.mybatisplus.samples.deluxe;

import static org.assertj.core.api.Assertions.assertThat;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.samples.deluxe.entity.User;
import com.baomidou.mybatisplus.samples.deluxe.mapper.UserMapper;
import com.baomidou.mybatisplus.samples.deluxe.model.UserPage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author miemie
 * @since 2018-08-13
 */
@SpringBootTest
@Slf4j
class DeluxeTest {

    @Resource
    private UserMapper mapper;

    @Test
    @DisplayName("自定义 xml 分页")
    void testCustomizePage() {
        UserPage selectPage = new UserPage(1, 5)
            .setSelectInt(20);
        UserPage userPage = mapper.selectUserPage(selectPage);
        Assertions.assertSame(userPage, selectPage);

        outputPageContent(userPage);
    }

    private <T> void outputPageContent(IPage<T> page) {
        log.info("总条数 ------> {}", page.getTotal());
        log.info("当前页数 ------> {}", page.getCurrent());
        log.info("当前每页显示数 ------> {}", page.getSize());
        print(page.getRecords());
    }

    @Test
    @DisplayName("baseMapper 自带分页")
    void testInternalPage() {
        Page<User> page = new Page<>(1, 5);
        IPage<User> userIPage = mapper.selectPage(page,
            new LambdaQueryWrapper<User>().eq(User::getAge, 20));
        Assertions.assertSame(userIPage, page);
        outputPageContent(page);
    }

    @Test
    void testDelAll() {
        mapper.deleteAll();
        assertThat(mapper.selectCount(null)).isZero();
    }

    @Test
    @DisplayName("乐观锁")
    void testVersionFieldUpdate() {
        User u = new User()
            .setEmail("122@qq.com")
            .setVersion(1)
            .setDeleted(0);
        mapper.insert(u);

        u.setAge(18);
        mapper.updateById(u);
        u = mapper.selectById(u.getId());
        // version should be updated
        Assertions.assertEquals(2, u.getVersion().intValue());
    }

    @Test
    void testSelectById() {
        User user = mapper.selectById(1L);
        log.info(mapper.selectById(1L).toString());
        assertThat(user).extracting(User::getId, User::getName)
            .containsExactly(1L, "Jone");
    }

    private <T> void print(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
           return;
        }

        list.forEach(System.out::println);
    }


    @Test
    void myInsertAll() {
        long id = 1008888L;
        User u = new User()
            .setEmail("122@qq.com")
            .setVersion(1)
            .setDeleted(0)
            .setId(id);
        mapper.myInsertAll(u);

        User user = mapper.selectById(id);
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getCreateTime());
    }

    @Test
    void myInsertBatch() {
        long id = 1009991;
        List<User> batchList = new ArrayList<>(2);
        batchList.add(new User().setId(id++).setEmail("111@qq.com").setVersion(1).setDeleted(0));
        batchList.add(new User().setId(id).setEmail("112@qq.com").setVersion(1).setDeleted(0));
        mapper.mysqlInsertAllBatch(batchList);

        User user = mapper.selectById(1009991);
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getCreateTime());
    }

    @Test
    void testFindList() {
        List<User> userList = mapper.findList(new User().setName("a"));
        assertThat(userList).hasSizeGreaterThan(12);
        userList.forEach(System.out::println);
    }

    @Test
    void testCustomSqlSegment() {
        QueryWrapper<User> ew = new QueryWrapper<>();
        ew.like("u.name", "Tom");
        List<User> list = mapper.customerSqlSegment(ew);
        assertThat(list).hasSize(1);
    }
}
