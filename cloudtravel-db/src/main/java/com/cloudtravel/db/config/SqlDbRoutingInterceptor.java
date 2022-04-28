package com.cloudtravel.db.config;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLReplaceStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.cloudtravel.common.exception.CloudTravelException;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.DateFormat;
import java.util.*;
import java.util.regex.Matcher;

/**
 * 数据源路由策略
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update",
                args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
})
public class SqlDbRoutingInterceptor implements Interceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlDbRoutingInterceptor.class);

    private static final String TENANT_ID = "TENANT_ID";

    private static final List<String> PUBLIC_TABLES = Arrays.asList("B_USER" , "TB");

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Long startTime = System.currentTimeMillis();
        String sql = "";
        MappedStatement mappedStatement = null;
        Object param = null;
        try {
            Object[] args = invocation.getArgs();
            mappedStatement = (MappedStatement)invocation.getArgs()[0];
            if(args.length > 1) {
                param = args[1];
                LOGGER.info("current sql param is [{}]" , param);
            }
            BoundSql boundSql = mappedStatement.getBoundSql(param);
            String sqlId = mappedStatement.getId();
            sql = getWholeSql(mappedStatement.getConfiguration() , boundSql);
            executeDbRouting(sqlId , sql);
        }catch (Exception e) {
            throw e;
        }
        Object result = invocation.proceed();
        Long endTime = System.currentTimeMillis();
        LOGGER.info("ExecuteDbRouting use time {} ms" , endTime - startTime);
        return result;
    }

    private void executeDbRouting(String sqlId , String sql)throws Exception {
        DbType dbType = JdbcConstants.MYSQL;
        LOGGER.info("ExecuteDbRouting , sqlId = {} , sql = {}" , sqlId , sql);
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql , dbType);
        for (SQLStatement sqlStatement : sqlStatements) {
            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            sqlStatement.accept(visitor);
            String tableName = "";
            String sqlType = "";
            Map<TableStat.Name, TableStat> tableStatMap = visitor.getTables();
            for (Map.Entry<TableStat.Name, TableStat> nameTableStatEntry : tableStatMap.entrySet()) {
                tableName = nameTableStatEntry.getKey().toString().toUpperCase();
                sqlType = nameTableStatEntry.getValue().toString();
            }
            LOGGER.info("tableName = {} , sqlType = {}" , tableName , sqlType);
            if(PUBLIC_TABLES.contains(tableName)) {
                LOGGER.info("ExecuteDbRouting : table: [{}] is public table , route db to public dataBase" , tableName);
                DataSourceContext.setDataSourceType(DataSourceEnums.CLOUDTRAVEL_CONSUMER1);
            }else {
                //读取公共租户标识
                String tenantId= "";
                if (StringUtils.isNotBlank(sqlType) && sqlType.equalsIgnoreCase("insert")) {
                    List<SQLExpr> sqlExprList = null;
                    if(sqlStatement instanceof SQLInsertStatement) {
                        SQLInsertStatement insertStatement = (SQLInsertStatement) sqlStatement;
                        sqlExprList = insertStatement.getValuesList().get(0).getValues();
                    }else if(sqlStatement instanceof SQLReplaceStatement){
                        SQLReplaceStatement replaceStatement = (SQLReplaceStatement)sqlStatement;
                        sqlExprList = replaceStatement.getValuesList().get(0).getValues();
                    }else {
                        throw new CloudTravelException("ExecuteDbRouting error : Unsupported SqlType [{}]" , sqlType);
                    }
                    int i = 0;
                    for (TableStat.Column column : visitor.getColumns()) {
                        if(TENANT_ID.equalsIgnoreCase(getColumnName(column)) ) {
                            SQLIntegerExpr sqlIntegerExpr = (SQLIntegerExpr)sqlExprList.get(i);
                            tenantId = String.valueOf(sqlIntegerExpr.getValue());
                            break;
                        }
                        i ++;
                    }
                }else {
                    List<TableStat.Condition> conditions = visitor.getConditions();
                    tenantId = conditions.stream()
                            .filter(k -> TENANT_ID.equalsIgnoreCase(getColumnName(k.getColumn())))
                            .findFirst().orElseThrow(() -> new CloudTravelException("ExecuteDbRouting error : Unsupported sql operate not find param:{TENANT_ID}"))
                            .getValues().get(0).toString();
                }
                DataSourceContext.setDataSourceType(tenantId);
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }


    /**
     * 获取完整的sql语句
     * @return
     */
    private String getWholeSql(Configuration configuration , BoundSql boundSql) {
        Object paramObj = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappingList =  boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+" , " ");
        if(CollectionUtils.isNotEmpty(parameterMappingList) && paramObj != null) {
            //获取类型注册器
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if(typeHandlerRegistry.hasTypeHandler(paramObj.getClass())) {
                sql = sql.replaceFirst("\\?" , Matcher.quoteReplacement(getParamVal(paramObj)));
            }else {
                //若是个json对象,则挨个取出并在sql中填补上参数值
                MetaObject metaObject = configuration.newMetaObject(paramObj);
                for (ParameterMapping parameterMapping : parameterMappingList) {
                    String propertyName = parameterMapping.getProperty();
                    if(metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?" , Matcher.quoteReplacement(getParamVal(obj)));
                    }else if(boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?" , Matcher.quoteReplacement(getParamVal(obj)));
                    }else {
                        sql = sql.replaceFirst("\\?" , "NULL");
                    }
                }
            }
        }
        return sql;
    }

    private String getParamVal(Object obj) {
        String value = null;
        if(obj instanceof String) {
            value = "'" + obj.toString() + "'";
        }else if (obj instanceof Date) {
            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT , DateFormat.DATE_FIELD , Locale.CHINA);
            value = "'" + dateFormat.format(obj) + "'";
        }else {
            if(obj != null) {
                value = obj.toString();
            }else {
                value = "NULL";
            }
        }
        return value;
    }

    private String getColumnName(TableStat.Column column) {
        String columnName = "";
        if(null != column) {
            columnName = column.getFullName();
            if(StringUtils.isBlank(columnName)) {
                columnName = column.getName();
            }
            if(StringUtils.isNotBlank(columnName)) {
                int index = columnName.lastIndexOf(".");
                if(index > 0 && (columnName.length() - index -1) >= 0) {
                    columnName = columnName.substring(index + 1);
                }
            }
        }
        String regexp = "\'";
        columnName = columnName.replaceAll(regexp , "");
        return columnName;
    }
}
