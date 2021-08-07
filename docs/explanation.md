# 属性配置说明
插件配置属性获取目前支持4种方式：`扩展属性`（`Ext`）、`项目属性`（`Prj`）、`系统属性`（`Sys`）、`环境属性`（`Env`），
属性优先级：`Sys` > `Env` > `Prj` > `Ext`
> - `Ext`（Extension）：插件自定义扩展属性，配置于`build.gradle`文件，配置方式详见[samples](https://github.com/ihub-pub/plugins/tree/main/samples)
> - `Prj`（Project）：项目属性，配置于`gradle.properties`文件，配置格式`扩展名`.`属性名`，如`iHub.mavenLocalEnabled=true`
> - `Sys`（System）：系统属性，如命令行传递的信息等，配置格式`扩展名`.`属性名`，如`-DiHub.mavenLocalEnabled=true`
> - `Env`（Environment）：环境变量属性，配置格式全部大写，多个单词，用`_`分隔，如`MAVEN_LOCAL_ENABLED=true`