package com.baomidou.mybatisplus.samples.deluxe.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * <p>
 * </p>
 *
 * @author yuxiaobin
 * @date 2019/6/14
 */
public class MyInsertAll extends AbstractMethod {

    public MyInsertAll(String methodName) {
        super(methodName);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass,
        TableInfo tableInfo) {

        StringBuilder fieldSql = new StringBuilder(LEFT_BRACKET);
        fieldSql.append(tableInfo.getKeyColumn()).append(COMMA);

        StringBuilder valueSql = new StringBuilder(LEFT_BRACKET);
        valueSql.append(HASH_LEFT_BRACE).append(tableInfo.getKeyProperty()).append(RIGHT_BRACE)
            .append(COMMA);

        tableInfo.getFieldList().forEach(x -> {
            fieldSql.append(x.getColumn()).append(COMMA);
            valueSql.append(HASH_LEFT_BRACE).append(x.getProperty()).append(RIGHT_BRACE)
                .append(COMMA);
        });

        fieldSql.replace(fieldSql.length() - 1, fieldSql.length(), RIGHT_BRACKET);
        valueSql.replace(valueSql.length() - 1, valueSql.length(), RIGHT_BRACKET);

        String sql = "insert into %s %s values %s";
        SqlSource sqlSource = languageDriver.createSqlSource(configuration,
            String.format(sql, tableInfo.getTableName(), fieldSql, valueSql), modelClass);
        return this.addInsertMappedStatement(mapperClass, modelClass, methodName, sqlSource,
            new NoKeyGenerator(), null, null);
    }
}
