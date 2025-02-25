package com.baomidou.mybatisplus.samples.typehandler;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.baomidou.mybatisplus.samples.typehandler.entity.Wallet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import lombok.SneakyThrows;

/**
 * 自定义复杂类型处理器<br/>
 * 不要问我为什么要重写 parse 因为顶层父类是无法获取到准确的待转换复杂返回类型数据
 */
public class WalletListTypeHandler extends JacksonTypeHandler {

    public WalletListTypeHandler(Class<?> type) {
        super(type);
    }

    @SneakyThrows(JsonProcessingException.class)
    @Override
    protected Object parse(String json) {
            return getObjectMapper().readValue(json, new TypeReference<List<Wallet>>() {});
    }
}
