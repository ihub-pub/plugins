[^Project]: `Project`：项目类型插件，配置于`build.gradle`文件，类型说明[详见](https://docs.gradle.org/current/dsl/org.gradle.api.Project.html)
[^Settings]: `Settings`：配置类型插件，配置与`settings.gradle`文件，类型说明[详见](https://docs.gradle.org/current/dsl/org.gradle.api.initialization.Settings.html)

[^Ext]: `Ext`（Extension）：插件自定义扩展属性，配置于`build.gradle`文件，配置方式[详见](explanation#属性配置说明)
[^Prj]: `Prj`（Project）：项目属性，配置于`gradle.properties`文件，配置格式`扩展名`.`属性名`[详见](explanation#属性配置说明)
[^Sys]: `Sys`（System）：系统属性，如命令行传递的信息等，配置格式`扩展名`.`属性名`[详见](explanation#属性配置说明)
[^Env]: `Env`（Environment）：环境变量属性，配置格式全部大写，多个单词，用`_`分隔[详见](explanation#属性配置说明)
