package com.baomidou.mybatisplus.samples.crud;

import static org.assertj.core.api.Assertions.assertThat;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.samples.crud.entity.User;
import com.baomidou.mybatisplus.samples.crud.entity.User2;
import com.baomidou.mybatisplus.samples.crud.mapper.User2Mapper;
import com.baomidou.mybatisplus.samples.crud.mapper.UserMapper;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>
 * 内置 CRUD 演示
 * </p>
 *
 * @author hubin
 * @since 2018-08-11
 */
@SpringBootTest
class CrudTest {

    @Resource
    private UserMapper mapper;
    @Resource
    private User2Mapper user2Mapper;

    @Test
    void aInsert() {
        User user = new User();
        user.setName("小羊");
        user.setAge(3);
        user.setEmail("abc@mp.com");
        assertThat(mapper.insert(user))
            .isPositive();
        // 成功直接拿回写的 ID
        assertThat(user.getId()).isNotNull();
    }


    @Test
    void bDelete() {
        assertThat(mapper.deleteById(3L))
            .isPositive();
        assertThat(mapper.delete(new QueryWrapper<User>()
            .lambda().eq(User::getName, "Sandy")))
            .isPositive();
    }


    @Test
    void cUpdate() {
        assertThat(mapper.updateById(new User()
            .setId(1L)
            .setEmail("ab@c.c")))
            .isPositive();
        assertThat(
            mapper.update(
                new User().setName("mp"),
                Wrappers.<User>lambdaUpdate()
                    .set(User::getAge, 3)
                    .eq(User::getId, 2L)
            )
        ).isPositive();

        User user = mapper.selectById(2);
        assertThat(user)
            .extracting(User::getAge, User::getName)
                .containsExactly(3, "mp");

        mapper.update(
            null,
            Wrappers.<User>lambdaUpdate()
                .set(User::getEmail, null)
                .eq(User::getId, 2L)
        );
        assertThat(mapper.selectById(1L).getEmail())
            .isEqualTo("ab@c.c");

        user = mapper.selectById(2);
        assertThat(user.getEmail()).isNull();
        assertThat(user.getName()).isEqualTo("mp");

        mapper.update(
            new User().setEmail("miemie@baomidou.com"),
            new QueryWrapper<User>()
                .lambda().eq(User::getId, 2)
        );
        user = mapper.selectById(2);
        assertThat(user.getEmail()).isEqualTo("miemie@baomidou.com");

        mapper.update(
            new User().setEmail("miemie2@baomidou.com"),
            Wrappers.<User>lambdaUpdate()
                .set(User::getAge, null)
                .eq(User::getId, 2)
        );
        user = mapper.selectById(2);
        assertThat(user)
            .extracting(User::getAge, User::getEmail)
            .containsExactly(null, "miemie2@baomidou.com");
    }


    @Test
    void dSelect() {
        mapper.insert(
            new User().setId(10086L)
                .setName("miemie")
                .setEmail("miemie@baomidou.com")
                .setAge(3));
        assertThat(mapper.selectById(10086L).getEmail()).isEqualTo("miemie@baomidou.com");
        User user = mapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getId, 10086));
        assertThat(user.getName()).isEqualTo("miemie");
        assertThat(user.getAge()).isEqualTo(3);

        mapper.selectList(Wrappers.<User>lambdaQuery().select(User::getId))
            .forEach(x -> {
                assertThat(x.getId()).isNotNull();
                assertThat(x.getEmail()).isNull();
                assertThat(x.getName()).isNull();
                assertThat(x.getAge()).isNull();
            });
        mapper.selectList(new QueryWrapper<User>().select("id", "name"))
            .forEach(x -> {
                assertThat(x.getId()).isNotNull();
                assertThat(x.getEmail()).isNull();
                assertThat(x.getName()).isNotNull();
                assertThat(x.getAge()).isNull();
            });
    }

    @Test
    void orderBy() {
        List<User> users = mapper.selectList(Wrappers.<User>query().orderByAsc("age"));
        assertThat(users).isNotEmpty();
        //多字段排序
        List<User> users2 = mapper.selectList(Wrappers.<User>query().orderByAsc("age", "name"));
        assertThat(users2).isNotEmpty();
        //先按age升序排列，age相同再按name降序排列
        List<User> users3 = mapper.selectList(
            Wrappers.<User>query().orderByAsc("age").orderByDesc("name"));
        assertThat(users3).isNotEmpty();
    }

    @Test
    public void selectMaps() {
        List<Map<String, Object>> mapList = mapper.selectMaps(
            Wrappers.<User>query().orderByAsc("age"));
        assertThat(mapList).isNotEmpty();
        assertThat(mapList.get(0)).isNotEmpty();
        System.out.println(mapList.get(0));
    }

    @Test
    public void selectMapsPage() {
        IPage<Map<String, Object>> page = mapper.selectMapsPage(new Page<>(1, 5),
            Wrappers.<User>query().orderByAsc("age"));
        assertThat(page).isNotNull();
        assertThat(page.getRecords()).isNotEmpty();
        assertThat(page.getRecords().get(0)).isNotEmpty();
        System.out.println(page.getRecords().get(0));
    }

    @Test
    public void orderByLambda() {
        List<User> users = mapper.selectList(Wrappers.<User>lambdaQuery().orderByAsc(User::getAge));
        assertThat(users).isNotEmpty();
        //多字段排序
        List<User> users2 = mapper.selectList(
            Wrappers.<User>lambdaQuery().orderByAsc(User::getAge, User::getName));
        assertThat(users2).isNotEmpty();
        //先按age升序排列，age相同再按name降序排列
        List<User> users3 = mapper.selectList(
            Wrappers.<User>lambdaQuery().orderByAsc(User::getAge).orderByDesc(User::getName));
        assertThat(users3).isNotEmpty();
    }

    @Test
    public void testSelectMaxId() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("max(id) as id");
        User user = mapper.selectOne(wrapper);
        System.out.println("maxId=" + user.getId());
        List<User> users = mapper.selectList(Wrappers.<User>lambdaQuery().orderByDesc(User::getId));
        Assertions.assertEquals(user.getId().longValue(), users.get(0).getId().longValue());
    }

    @Test
    public void testGroup() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("age, count(*)")
            .groupBy("age");
        List<Map<String, Object>> maplist = mapper.selectMaps(wrapper);
        for (Map<String, Object> mp : maplist) {
            System.out.println(mp);
        }
        /**
         * lambdaQueryWrapper groupBy orderBy
         */
        LambdaQueryWrapper<User> lambdaQueryWrapper = new QueryWrapper<User>().lambda()
            .select(User::getAge)
            .groupBy(User::getAge)
            .orderByAsc(User::getAge);
        for (User user : mapper.selectList(lambdaQueryWrapper)) {
            System.out.println(user);
        }
    }

    @Test
    public void testTableFieldExistFalse() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("age, count(age) as count")
            .groupBy("age");
        List<User> list = mapper.selectList(wrapper);
        list.forEach(System.out::println);
        list.forEach(x -> {
            Assertions.assertNull(x.getId());
            Assertions.assertNotNull(x.getAge());
            Assertions.assertNotNull(x.getCount());
        });
        mapper.insert(
            new User().setId(10088L)
                .setName("miemie")
                .setEmail("miemie@baomidou.com")
                .setAge(3));
        User miemie = mapper.selectById(10088L);
        Assertions.assertNotNull(miemie);

    }

    @Test
    public void testSqlCondition() {
        Assertions.assertEquals(user2Mapper.selectList(Wrappers.<User2>query()
            .setEntity(new User2().setName("n"))).size(), 2);
        Assertions.assertEquals(
            user2Mapper.selectList(Wrappers.<User2>query().like("name", "J")).size(), 2);
        Assertions.assertEquals(user2Mapper.selectList(Wrappers.<User2>query().gt("age", 18)
            .setEntity(new User2().setName("J"))).size(), 1);
    }
}
