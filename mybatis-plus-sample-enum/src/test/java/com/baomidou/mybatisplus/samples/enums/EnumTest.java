package com.baomidou.mybatisplus.samples.enums;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.samples.enums.entity.User;
import com.baomidou.mybatisplus.samples.enums.enums.*;
import com.baomidou.mybatisplus.samples.enums.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>
 * 内置 Enums 演示
 * </p>
 *
 * @author hubin
 * @since 2018-08-11
 */
@SpringBootTest
@Slf4j
class EnumTest {
    @Resource
    private UserMapper mapper;

    @Test
    void selectXML() {
        User user = mapper.selectLinkById(1L);
        log.info(user.toString());
        assertThat(user).isNotNull();
    }

    @Test
    void insert() {
        User user = new User()
            .setName("K神")
            .setAge(AgeEnum.ONE)
            .setGrade(GradeEnum.HIGH)
            .setGender(GenderEnum.MALE)
            .setStrEnum(StrEnum.ONE)
            .setEmail("abc@mp.com");
        Assertions.assertTrue(mapper.insert(user) > 0);

        // 成功直接拿回写的 ID
        log.info("\n插入成功 ID 为：{}", user.getId());

        List<User> list = mapper.selectList(null);
        for (User u : list) {
            System.out.println(u);
            assertThat(u.getAge()).isNotNull();
            if (u.getId().equals(user.getId())) {
                assertThat(u.getGender()).isNotNull();
                assertThat(u.getGrade()).isNotNull();
                assertThat(u.getStrEnum()).isNotNull();
            }
        }
    }

    @Test
    void delete() {
        Assertions.assertTrue(mapper.delete(new QueryWrapper<User>()
                .lambda().eq(User::getAge, AgeEnum.TWO)) > 0);
    }

    @Test
    void update() {
        Assertions.assertTrue(mapper.update(new User().setAge(AgeEnum.TWO),
                new QueryWrapper<User>().eq("age", AgeEnum.THREE)) > 0);
    }

    @Test
    void select() {
        User user = mapper.selectOne(new QueryWrapper<User>().lambda()
            .eq(User::getId, 2));
        Assertions.assertEquals("Jack", user.getName());
        Assertions.assertSame(AgeEnum.THREE, user.getAge());

        //#1500 github: verified ok. Not a bug
        List<User> userList = mapper.selectList(new QueryWrapper<User>().lambda().eq(User::getUserState, UserState.ACTIVE));
        //TODO 一起测试的时候完蛋，先屏蔽掉了。
//        Assertions.assertEquals(3, userList.size());
        Optional<User> userOptional = userList.stream()
                .filter(x -> x.getId() == 1)
                .findFirst();
        userOptional.ifPresent(user1 -> Assertions.assertSame(UserState.ACTIVE, user1.getUserState()));
    }
}
