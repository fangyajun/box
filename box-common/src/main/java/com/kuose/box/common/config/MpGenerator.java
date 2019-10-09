package com.kuose.box.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class MpGenerator {

    public static void main(String[] args) {
        new MpGenerator().generator("D:\\MpGenerator"
                , "jdbc:mysql://192.168.5.176:3306/box?characterEncoding=utf8"
//                , "com.kuose.box.admin.user"
                , "com.kuose.box.db.survery"
                , "root"
                , "123456");

//        new MpGenerator().generator("D:\\MpGenerator"
//                , "jdbc:sqlserver://192.168.5.108:1433;DatabaseName=SCM_PRODUCT"
//                , "com.kuose.source.goods"
//                , "wx"
//                , "WXuser&2019");
    }

    public void generator(String outPutPath, String mysqlUrl, String parentPackage, String userName, String pwd) {
        // 全局配置
        GlobalConfig gc = new GlobalConfig()
                .setOutputDir(outPutPath)
                .setFileOverride(true)
                .setActiveRecord(true)
                .setEnableCache(false)
                .setBaseResultMap(true)
                .setBaseColumnList(true)
                .setAuthor("魔舞清华")
                .setMapperName("%sMapper")
                .setXmlName("%sMapper")
                .setServiceName("%sService")
                .setServiceImplName("%sServiceImpl")
                .setControllerName("%sController");

        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig()
//                .setDbType(DbType.SQL_SERVER)
                .setDbType(DbType.MYSQL)
                .setDriverName("com.mysql.jdbc.Driver")
//                .setDriverName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
                .setUrl(mysqlUrl)
                .setUsername(userName)
                .setPassword(pwd);

        // 策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        // 生成的表
        strategyConfig.setInclude("box_survey_question_view_type");
//        strategyConfig.setInclude("box_order_goods_comment", "box_order_comment");

        new AutoGenerator().setDataSource(dataSourceConfig)
                .setGlobalConfig(gc)
                .setStrategy(strategyConfig)
                .setPackageInfo(new PackageConfig()
                        .setParent(parentPackage)
                        .setEntity("entity")
                        .setMapper("dao")
                        .setXml("mapper")
                        .setService("service")
                        .setServiceImpl("service.impl")
                        .setController("controller")
                ).execute();
    }
}
