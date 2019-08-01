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
                , "jdbc:mysql://localhost:3306/box?characterEncoding=utf8"
                , "com.kuose.box.admin.survery"
                , "root"
                , "123456");
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
                .setAuthor("fangyajun")
                .setMapperName("%sMapper")
                .setXmlName("%sMapper")
                .setServiceName("%sService")
                .setServiceImplName("%sServiceImpl")
                .setControllerName("%sController");

        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig()
                .setDbType(DbType.MYSQL)
                .setDriverName("com.mysql.jdbc.Driver")
                .setUrl(mysqlUrl)
                .setUsername(userName)
                .setPassword(pwd);

        // 策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        // 生成的表
        strategyConfig.setInclude("box_survey","box_survey_question_options","box_survey_qusetion","box_survey_user_answer");

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
