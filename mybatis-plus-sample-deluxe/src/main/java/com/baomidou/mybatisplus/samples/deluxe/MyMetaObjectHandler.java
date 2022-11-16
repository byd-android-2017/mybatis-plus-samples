package com.baomidou.mybatisplus.samples.deluxe;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * 填充器
 *
 * @author nieqiurong 2018-08-10 22:59:23.
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        // 避免使用metaObject.setValue()
        this.strictInsertFill(metaObject, "createTime",
            () -> new Timestamp(System.currentTimeMillis()), Timestamp.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("nothing to fill ....");
    }
}
