## 🔖 1.3.0 (2023-02-12)


### ✨ Features

- feat(groovy): 移除enableGroovy3扩展属性  [@henry-hub](https://github.com/henry-hub) ([#370](https://github.com/ihub-pub/plugins/pull/370))
- feat(groovy): 移除compileGroovyAllModules扩展属性  [@henry-hub](https://github.com/henry-hub) ([#368](https://github.com/ihub-pub/plugins/pull/368))
- revert(bom): 还原公共平台组件配置  [@henry-hub](https://github.com/henry-hub) ([#366](https://github.com/ihub-pub/plugins/pull/366))
- test: 补充测试用例  [@henry-hub](https://github.com/henry-hub) ([#364](https://github.com/ihub-pub/plugins/pull/364))
- refactor: 基础模块名称调整  [@henry-hub](https://github.com/henry-hub) ([#363](https://github.com/ihub-pub/plugins/pull/363))
- refactor: 各插件重构为独立组件  [@henry-hub](https://github.com/henry-hub) ([#356](https://github.com/ihub-pub/plugins/pull/356))
- feat(spring): 独立spring插件  [@henry-hub](https://github.com/henry-hub) ([#352](https://github.com/ihub-pub/plugins/pull/352))

### 🐛 Bug Fixes

- fix(bom): bom组件项目不使用ihub-bom插件  [@henry-hub](https://github.com/henry-hub) ([#367](https://github.com/ihub-pub/plugins/pull/367))

### ⬆️ Dependency Updates

- gradle: bump pub.ihub.lib:ihub-libs from 1.0.17 to 1.1.0  [@dependabot](https://github.com/dependabot) ([#369](https://github.com/ihub-pub/plugins/pull/369))
- gradle: bump pub.ihub.plugin.ihub-settings from 1.2.11 to 1.2.12  [@dependabot](https://github.com/dependabot) ([#342](https://github.com/ihub-pub/plugins/pull/342))
- gradle: bump com.github.ben-manes:gradle-versions-plugin from 0.44.0 to 0.45.0  [@dependabot](https://github.com/dependabot) ([#365](https://github.com/ihub-pub/plugins/pull/365))
- gradle: bump spring-boot-gradle-plugin from 3.0.1 to 3.0.2  [@dependabot](https://github.com/dependabot) ([#362](https://github.com/ihub-pub/plugins/pull/362))
- gradle: bump spring-boot-gradle-plugin from 3.0.0 to 3.0.1 in /ihub-spring  [@dependabot](https://github.com/dependabot) ([#354](https://github.com/ihub-pub/plugins/pull/354))
- gradle: bump native-gradle-plugin from 0.9.18 to 0.9.19 in /ihub-spring  [@dependabot](https://github.com/dependabot) ([#353](https://github.com/ihub-pub/plugins/pull/353))
- gradle: bump lombok-plugin from 6.6 to 6.6.1 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#350](https://github.com/ihub-pub/plugins/pull/350))
- gradle: bump lombok-plugin from 6.5.1 to 6.6 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#345](https://github.com/ihub-pub/plugins/pull/345))
- gradle: bump spring-boot-gradle-plugin from 2.7.5 to 3.0.0 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#338](https://github.com/ihub-pub/plugins/pull/338))

### 🧰 Maintenance

- test(verification): 修正测试用例  [@henry-hub](https://github.com/henry-hub) ([#371](https://github.com/ihub-pub/plugins/pull/371))
- ci(gradle): 修正ihub-test模块配置  [@henry-hub](https://github.com/henry-hub) ([#355](https://github.com/ihub-pub/plugins/pull/355))


---

## 🔖 1.2.12 (2022-11-28)


### ✨ Features

- feat(java): iHubJava的log依赖中添加jcl-over-slf4j  [@henry-hub](https://github.com/henry-hub) ([#341](https://github.com/ihub-pub/plugins/pull/341))
- feat(groovy): 调整groovy版本开关，默认groovy4，可以通过enableGroovy3切换至groovy3  [@henry-hub](https://github.com/henry-hub) ([#332](https://github.com/ihub-pub/plugins/pull/332))

### ⬆️ Dependency Updates

- build(gradle): 升级gradle至7.6  [@henry-hub](https://github.com/henry-hub) ([#340](https://github.com/ihub-pub/plugins/pull/340))
- gradle: bump spring-boot-gradle-plugin from 2.7.5 to 2.7.6 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#337](https://github.com/ihub-pub/plugins/pull/337))
- gradle: bump ihub-core from 1.0.16 to 1.0.17 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#339](https://github.com/ihub-pub/plugins/pull/339))
- gradle: bump springdoc-openapi-gradle-plugin from 1.4.0 to 1.5.0 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#334](https://github.com/ihub-pub/plugins/pull/334))
- gradle: bump gradle-versions-plugin from 0.43.0 to 0.44.0 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#333](https://github.com/ihub-pub/plugins/pull/333))
- gradle: bump pub.ihub.plugin.ihub-settings from 1.2.10 to 1.2.11  [@dependabot](https://github.com/dependabot) ([#331](https://github.com/ihub-pub/plugins/pull/331))


---

## 🔖 1.2.11 (2022-10-28)


### ✨ Features

- feat: 启用阿里云代理仓库开关支持系统属性和环境变量属性配置  [@henry-hub](https://github.com/henry-hub) ([#329](https://github.com/ihub-pub/plugins/pull/329))

### 🐛 Bug Fixes

- fix(spock): 修复spock测试缺失spring测试组件依赖的问题  [@henry-hub](https://github.com/henry-hub) ([#328](https://github.com/ihub-pub/plugins/pull/328))
- test(version): 修复版本号推断测试用例  [@henry-hub](https://github.com/henry-hub) ([#327](https://github.com/ihub-pub/plugins/pull/327))

### ⬆️ Dependency Updates

- gradle: bump ihub-core from 1.0.14 to 1.0.15 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#330](https://github.com/ihub-pub/plugins/pull/330))
- gradle: bump spring-boot-gradle-plugin from 2.7.4 to 2.7.5 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#326](https://github.com/ihub-pub/plugins/pull/326))
- gradle: bump dependency-management-plugin from 1.0.14.RELEASE to 1.1.0 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#325](https://github.com/ihub-pub/plugins/pull/325))
- gradle: bump gradle-versions-plugin from 0.42.0 to 0.43.0 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#324](https://github.com/ihub-pub/plugins/pull/324))
- gradle: bump ihub-core from 1.0.12 to 1.0.14 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#323](https://github.com/ihub-pub/plugins/pull/323))
- gradle: bump spring-boot-gradle-plugin from 2.7.3 to 2.7.4 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#322](https://github.com/ihub-pub/plugins/pull/322))
- gradle: bump dependency-management-plugin from 1.0.13.RELEASE to 1.0.14.RELEASE in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#321](https://github.com/ihub-pub/plugins/pull/321))
- gradle: bump lombok-plugin from 6.5.0.3 to 6.5.1 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#316](https://github.com/ihub-pub/plugins/pull/316))
- build(gradle): 升级gradle至7.5.1  [@henry-hub](https://github.com/henry-hub) ([#311](https://github.com/ihub-pub/plugins/pull/311))
- gradle: bump pub.ihub.plugin.ihub-settings from 1.2.9 to 1.2.10  [@dependabot](https://github.com/dependabot) ([#310](https://github.com/ihub-pub/plugins/pull/310))


---

## 🔖 1.2.10 (2022-08-27)


### ✨ Features

- feat(encoding): ihub-java插件添加Java编译编码配置，默认UTF-8  [@henry-hub](https://github.com/henry-hub) ([#305](https://github.com/ihub-pub/plugins/pull/305))
- feat(java): 调整组件能力依赖方式  [@henry-hub](https://github.com/henry-hub) ([#304](https://github.com/ihub-pub/plugins/pull/304))
- feat(groovy4): 配置groovy4时对应的spock组件版本  [@henry-hub](https://github.com/henry-hub) ([#301](https://github.com/ihub-pub/plugins/pull/301))
- feat(groovy): 添加“是否启用Groovy 4”的开关  [@zhanghuabin](https://github.com/zhanghuabin) ([#299](https://github.com/ihub-pub/plugins/pull/299))

### 🐛 Bug Fixes

- fix(java): 修正组件多个能力配置失败的问题  [@henry-hub](https://github.com/henry-hub) ([#302](https://github.com/ihub-pub/plugins/pull/302))

### ⬆️ Dependency Updates

- gradle: bump ihub-core from 1.0.11 to 1.0.12 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#306](https://github.com/ihub-pub/plugins/pull/306))
- gradle: bump spring-boot-gradle-plugin from 2.7.2 to 2.7.3 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#303](https://github.com/ihub-pub/plugins/pull/303))
- gradle: bump springdoc-openapi-gradle-plugin from 1.3.4 to 1.4.0 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#298](https://github.com/ihub-pub/plugins/pull/298))
- gradle: bump dependency-management-plugin from 1.0.12.RELEASE to 1.0.13.RELEASE in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#300](https://github.com/ihub-pub/plugins/pull/300))
- feat(version): 升级PMD、Codenarc、Jacoco版本  [@henry-hub](https://github.com/henry-hub) ([#307](https://github.com/ihub-pub/plugins/pull/307))
- gradle: bump pub.ihub.plugin.ihub-settings from 1.2.8 to 1.2.9  [@dependabot](https://github.com/dependabot) ([#297](https://github.com/ihub-pub/plugins/pull/297))

### 🧰 Maintenance

- fix(java): Groovy增量编译与Java注释处理器不能同时使用  [@henry-hub](https://github.com/henry-hub) ([#309](https://github.com/ihub-pub/plugins/pull/309))
- fix(test): 修正测试用例  [@henry-hub](https://github.com/henry-hub) ([#308](https://github.com/ihub-pub/plugins/pull/308))
- ci(github): 调整工作流组件版本  [@henry-hub](https://github.com/henry-hub) ([#296](https://github.com/ihub-pub/plugins/pull/296))


---

## 🔖 1.2.9 (2022-07-24)


### ✨ Features

- feat(bom): 支持配置组件功能以及组件需要能力  [@henry-hub](https://github.com/henry-hub) ([#294](https://github.com/ihub-pub/plugins/pull/294))
- feat(repo): 支持阿里云代理仓库启用参数  [@henry-hub](https://github.com/henry-hub) ([#289](https://github.com/ihub-pub/plugins/pull/289))

### ⬆️ Dependency Updates

- gradle: bump ihub-core from 1.0.10 to 1.0.11 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#295](https://github.com/ihub-pub/plugins/pull/295))
- gradle: bump spring-boot-gradle-plugin from 2.6.7 to 2.7.2 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#293](https://github.com/ihub-pub/plugins/pull/293))
- build(gradle): 升级plugin-publish、spring-aot组件版本  [@henry-hub](https://github.com/henry-hub) ([#292](https://github.com/ihub-pub/plugins/pull/292))
- gradle: bump lombok-plugin from 6.4.3 to 6.5.0.3 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#287](https://github.com/ihub-pub/plugins/pull/287))
- gradle: bump byte-buddy-gradle-plugin from 1.12.10 to 1.12.12 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#291](https://github.com/ihub-pub/plugins/pull/291))
- gradle: bump dependency-management-plugin from 1.0.11.RELEASE to 1.0.12.RELEASE in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#290](https://github.com/ihub-pub/plugins/pull/290))
- gradle: bump jmolecules-bytebuddy from 0.9.0 to 1.6.0 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#283](https://github.com/ihub-pub/plugins/pull/283))
- build(gradle): 升级gradle至7.5  [@henry-hub](https://github.com/henry-hub) ([#288](https://github.com/ihub-pub/plugins/pull/288))
- build(gradle): 升级spring-aot组件版本  [@henry-hub](https://github.com/henry-hub) ([#274](https://github.com/ihub-pub/plugins/pull/274))
- gradle: bump pub.ihub.plugin.ihub-settings from 1.2.6 to 1.2.8  [@dependabot](https://github.com/dependabot) ([#272](https://github.com/ihub-pub/plugins/pull/272))

### 🧰 Maintenance

- ci(github): 发布release前需要完整构建，避免再次发版翻车  [@henry-hub](https://github.com/henry-hub) ([#273](https://github.com/ihub-pub/plugins/pull/273))


---

## 🔖 1.2.8 (2022-05-10)


### 🐛 Bug Fixes

- fix(gradle): 插件仓库配置添加中央仓库  [@henry-hub](https://github.com/henry-hub) ([#271](https://github.com/ihub-pub/plugins/pull/271))


---

## 🔖 1.2.7 (2022-05-10)


### ✨ Features

- feat(version): 支持通过最新的tag推断下一个版本号  [@henry-hub](https://github.com/henry-hub) ([#267](https://github.com/ihub-pub/plugins/pull/267))

### ⬆️ Dependency Updates

- gradle: bump ihub-core from 1.0.9 to 1.0.10 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#269](https://github.com/ihub-pub/plugins/pull/269))
- gradle: bump byte-buddy-gradle-plugin from 1.12.9 to 1.12.10 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#268](https://github.com/ihub-pub/plugins/pull/268))
- gradle: bump spring-boot-gradle-plugin from 2.6.6 to 2.6.7 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#266](https://github.com/ihub-pub/plugins/pull/266))
- gradle: bump lombok-plugin from 6.4.2 to 6.4.3 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#265](https://github.com/ihub-pub/plugins/pull/265))
- gradle: bump pub.ihub.plugin.ihub-settings from 1.2.5 to 1.2.6  [@dependabot](https://github.com/dependabot) ([#262](https://github.com/ihub-pub/plugins/pull/262))


---

## 🔖 1.2.6 (2022-04-10)


### ⬆️ Dependency Updates

- build(gradle): 升级plugin-publish、spring-aot组件版本  [@henry-hub](https://github.com/henry-hub) ([#261](https://github.com/ihub-pub/plugins/pull/261))
- gradle: bump ihub-core from 1.0.8 to 1.0.9 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#260](https://github.com/ihub-pub/plugins/pull/260))
- build(gradle): 升级gradle至7.4.2  [@henry-hub](https://github.com/henry-hub) ([#259](https://github.com/ihub-pub/plugins/pull/259))
- gradle: bump springdoc-openapi-gradle-plugin from 1.3.3 to 1.3.4 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#252](https://github.com/ihub-pub/plugins/pull/252))
- gradle: bump jmolecules-bytebuddy from 0.8.0 to 0.9.0 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#254](https://github.com/ihub-pub/plugins/pull/254))
- gradle: bump byte-buddy-gradle-plugin from 1.12.8 to 1.12.9 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#257](https://github.com/ihub-pub/plugins/pull/257))
- gradle: bump lombok-plugin from 6.4.1 to 6.4.2 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#256](https://github.com/ihub-pub/plugins/pull/256))
- gradle: bump spring-boot-gradle-plugin from 2.6.5 to 2.6.6 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#253](https://github.com/ihub-pub/plugins/pull/253))
- gradle: Bump pub.ihub.plugin.ihub-settings from 1.2.4 to 1.2.5  [@dependabot](https://github.com/dependabot) ([#251](https://github.com/ihub-pub/plugins/pull/251))


---

## 🔖 1.2.5 (2022-03-26)


### ✨ Features

- ✨ ihub-git-hooks支持配置commit-msg模板  [@henry-hub](https://github.com/henry-hub) ([#248](https://github.com/ihub-pub/plugins/pull/248))

### 📝 Documentation

- 📝 文档调整  [@henry-hub](https://github.com/henry-hub) ([#249](https://github.com/ihub-pub/plugins/pull/249))

### ⬆️ Dependency Updates

- ⬆️ gradle: Bump spring-boot-gradle-plugin from 2.6.4 to 2.6.5 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#250](https://github.com/ihub-pub/plugins/pull/250))
- ⬆️ gradle: Bump pub.ihub.plugin.ihub-settings from 1.2.3 to 1.2.4  [@dependabot](https://github.com/dependabot) ([#245](https://github.com/ihub-pub/plugins/pull/245))

### 🧰 Maintenance

- ✏️ 移除阿里云Gradle插件代理仓库  [@henry-hub](https://github.com/henry-hub) ([#246](https://github.com/ihub-pub/plugins/pull/246))


---

## 🔖 1.2.4 (2022-03-13)


### ✨ Features

- ✨ 集成test-report-aggregation聚合测试报告  [@henry-hub](https://github.com/henry-hub) ([#243](https://github.com/ihub-pub/plugins/pull/243))

### 🐛 Bug Fixes

- 🐛 升级gradle至7.4.1，修复升级7.4导致的插件仓库配置失效的问题  [@henry-hub](https://github.com/henry-hub) ([#242](https://github.com/ihub-pub/plugins/pull/242))

### ⬆️ Dependency Updates

- ⬆️ gradle: Bump pub.ihub.plugin.ihub-settings from 1.2.2 to 1.2.3  [@dependabot](https://github.com/dependabot) ([#240](https://github.com/ihub-pub/plugins/pull/240))

### 🧰 Maintenance

- ♻️ 重构ihub-bom插件  [@henry-hub](https://github.com/henry-hub) ([#244](https://github.com/ihub-pub/plugins/pull/244))
- ✏️ 主项目默认添加ihub-git-hooks插件  [@henry-hub](https://github.com/henry-hub) ([#241](https://github.com/ihub-pub/plugins/pull/241))


---

## 🔖 1.2.3 (2022-03-06)


### ⬆️ Dependency Updates

- ⬆️ 升级gradle至7.4  [@henry-hub](https://github.com/henry-hub) ([#238](https://github.com/ihub-pub/plugins/pull/238))
- ⬆️ gradle: Bump ihub-core from 1.0.7 to 1.0.8 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#237](https://github.com/ihub-pub/plugins/pull/237))
- ⬆️ gradle: Bump spring-boot-gradle-plugin from 2.6.3 to 2.6.4 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#236](https://github.com/ihub-pub/plugins/pull/236))
- ⬆️ gradle: Bump byte-buddy-gradle-plugin from 1.12.7 to 1.12.8 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#230](https://github.com/ihub-pub/plugins/pull/230))
- ⬆️ gradle: Bump lombok-plugin from 6.3.0 to 6.4.1 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#234](https://github.com/ihub-pub/plugins/pull/234))
- ⬆️ gradle: Bump gradle-versions-plugin from 0.41.0 to 0.42.0 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#226](https://github.com/ihub-pub/plugins/pull/226))
- ⬆️ gradle: Bump pub.ihub.plugin.ihub-settings from 1.2.1 to 1.2.2  [@dependabot](https://github.com/dependabot) ([#225](https://github.com/ihub-pub/plugins/pull/225))


---

## 🔖 1.2.2 (2022-01-29)


### ✨ Features

- ✨ 添加ihub-doc插件  [@henry-hub](https://github.com/henry-hub) ([#215](https://github.com/ihub-pub/plugins/pull/215))
- 🎨 Java运行时本地系统属性扩展优化  [@henry-hub](https://github.com/henry-hub) ([#217](https://github.com/ihub-pub/plugins/pull/217))
- ✏️ iHubGitHooks插件支持自定义hooks目录  [@henry-hub](https://github.com/henry-hub) ([#216](https://github.com/ihub-pub/plugins/pull/216))
- ✨ 集成JMoleculesPlugin  [@henry-hub](https://github.com/henry-hub) ([#209](https://github.com/ihub-pub/plugins/pull/209))

### 🐛 Bug Fixes

- 🐛 修正无配文件项目替换组件版本报错，新年快乐🎆  [@henry-hub](https://github.com/henry-hub) ([#212](https://github.com/ihub-pub/plugins/pull/212))

### ⬆️ Dependency Updates

- ⬆️ gradle: Bump spring-boot-gradle-plugin from 2.6.2 to 2.6.3 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#220](https://github.com/ihub-pub/plugins/pull/220))
- ⬆️ gradle: Bump ihub-core from 1.0.6 to 1.0.7 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#222](https://github.com/ihub-pub/plugins/pull/222))
- ⬆️ gradle: Bump jmolecules-bytebuddy from 0.7.0 to 0.8.0 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#219](https://github.com/ihub-pub/plugins/pull/219))
- ⬆️ gradle: Bump byte-buddy-gradle-plugin from 1.12.6 to 1.12.7 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#218](https://github.com/ihub-pub/plugins/pull/218))
- ⬆️ gradle: Bump gradle-versions-plugin from 0.40.0 to 0.41.0 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#214](https://github.com/ihub-pub/plugins/pull/214))
- ⬆️ gradle: Bump gradle-versions-plugin from 0.39.0 to 0.40.0 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#211](https://github.com/ihub-pub/plugins/pull/211))
- ⬆️ gradle: Bump pub.ihub.plugin.ihub-settings from 1.2.0 to 1.2.1  [@dependabot](https://github.com/dependabot) ([#207](https://github.com/ihub-pub/plugins/pull/207))


---

## 🔖 1.2.1 (2021-12-24)


### ✨ Features

- ✨ ihub-settings支持自动配置项目bom组件  [@henry-hub](https://github.com/henry-hub) ([#204](https://github.com/ihub-pub/plugins/pull/204))

### ⬆️ Dependency Updates

- ⬆️ 升级spring-aot插件版本至0.11.1  [@henry-hub](https://github.com/henry-hub) ([#205](https://github.com/ihub-pub/plugins/pull/205))
- ⬆️ gradle: Bump spring-boot-gradle-plugin from 2.6.1 to 2.6.2 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#202](https://github.com/ihub-pub/plugins/pull/202))
- ⬆️ gradle: Bump spring-boot-buildpack-platform from 2.6.1 to 2.6.2 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#203](https://github.com/ihub-pub/plugins/pull/203))
- ⬆️ gradle: Bump pub.ihub.plugin.ihub-settings from 1.1.11 to 1.2.0  [@dependabot](https://github.com/dependabot) ([#201](https://github.com/ihub-pub/plugins/pull/201))

### 🧰 Maintenance

- 🎨 默认bom组件改用ihub-libs  [@henry-hub](https://github.com/henry-hub) ([#206](https://github.com/ihub-pub/plugins/pull/206))


---

## 🔖 1.2.0 (2021-11-30)


### ⬆️ Dependency Updates

- ⬆️ 升级ihub-bom至1.0.4  [@henry-hub](https://github.com/henry-hub) ([#200](https://github.com/ihub-pub/plugins/pull/200))
- ⬆️ gradle: Bump spring-boot-buildpack-platform from 2.6.0 to 2.6.1 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#198](https://github.com/ihub-pub/plugins/pull/198))
- ⬆️ gradle: Bump spring-boot-gradle-plugin from 2.6.0 to 2.6.1 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#199](https://github.com/ihub-pub/plugins/pull/199))
- ⬆️ gradle: Bump pub.ihub.plugin.ihub-settings from 1.1.10 to 1.1.11  [@dependabot](https://github.com/dependabot) ([#195](https://github.com/ihub-pub/plugins/pull/195))

### 🧰 Maintenance

- ✏️ 按需使用io.spring.dependency-management插件  [@henry-hub](https://github.com/henry-hub) ([#197](https://github.com/ihub-pub/plugins/pull/197))
- ✏️ 禁用ihub插件默认依赖  [@henry-hub](https://github.com/henry-hub) ([#196](https://github.com/ihub-pub/plugins/pull/196))


---

## 🔖 1.1.11 (2021-11-28)


### ✨ Features

- ➖ 移除jaxb默认依赖，可以配置“iHubJava.defaultDependencies=jaxb”手动依赖  [@henry-hub](https://github.com/henry-hub) ([#194](https://github.com/ihub-pub/plugins/pull/194))
- ✨ ihub-java插件集成MapStruct依赖配置  [@henry-hub](https://github.com/henry-hub) ([#190](https://github.com/ihub-pub/plugins/pull/190))

### 🐛 Bug Fixes

- 🐛 bom插件还原spring-dependency-management管理组件版本  [@henry-hub](https://github.com/henry-hub) ([#188](https://github.com/ihub-pub/plugins/pull/188))

### ⬆️ Dependency Updates

- ⬆️ gradle: Bump spring-boot-buildpack-platform from 2.5.7 to 2.6.0 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#191](https://github.com/ihub-pub/plugins/pull/191))
- ⬆️ gradle: Bump spring-boot-gradle-plugin from 2.5.7 to 2.6.0 in /ihub-plugins  [@dependabot](https://github.com/dependabot) ([#192](https://github.com/ihub-pub/plugins/pull/192))

### 🧰 Maintenance

- ✏️ ihub-java插件配置整理  [@henry-hub](https://github.com/henry-hub) ([#193](https://github.com/ihub-pub/plugins/pull/193))


---

## 🔖 1.1.10 (2021-11-12)

### ✨ Features

- ✏️ 添加gradle版本检查 [@henry-hub](https://github.com/henry-hub) ([#185](https://github.com/ihub-pub/plugins/pull/185))
- ✏️ bom组件支持配置强制组件版本 [@henry-hub](https://github.com/henry-hub) ([#183](https://github.com/ihub-pub/plugins/pull/183))

### 🐛 Bug Fixes

- ✏️ 修正阿里云代理仓库不同步导致的构建失败问题 [@henry-hub](https://github.com/henry-hub) ([#182](https://github.com/ihub-pub/plugins/pull/182))

### 📝 Documentation

- 📝 移动CHANGELOG至docs [@henry-hub](https://github.com/henry-hub) ([#181](https://github.com/ihub-pub/plugins/pull/181))
- 📝 添加CHANGELOG [@henry-hub](https://github.com/henry-hub) ([#180](https://github.com/ihub-pub/plugins/pull/180))

### ⬆️ Dependency Updates

- ⬆️ 升级freefair相关gradle插件版本 [@henry-hub](https://github.com/henry-hub) ([#186](https://github.com/ihub-pub/plugins/pull/186))
- ⬆️ 升级gradle至7.3 [@henry-hub](https://github.com/henry-hub) ([#184](https://github.com/ihub-pub/plugins/pull/184))
- ⬆️ 插件版本升至1.1.9 [@henry-hub](https://github.com/henry-hub) ([#179](https://github.com/ihub-pub/plugins/pull/179))

---

## 🔖 1.1.9 (2021-10-30)

- ✏️ bom插件配置调整 [@henry-hub](https://github.com/henry-hub) ([#174](https://github.com/ihub-pub/plugins/pull/174))
- ✏️ 移除bom配置数据库相关默认排除项；log默认依赖添加扩展开关 [@henry-hub](https://github.com/henry-hub) ([#172](https://github.com/ihub-pub/plugins/pull/172))

### ✨ Features

- ✨ bom插件替换spring-dependency-management，改用platform [@henry-hub](https://github.com/henry-hub) ([#173](https://github.com/ihub-pub/plugins/pull/173))
- ✏️ 解决java-platform插件与ihub-bom冲突问题 [@henry-hub](https://github.com/henry-hub) ([#169](https://github.com/ihub-pub/plugins/pull/169))

### 📝 Documentation

- 📝 文档组件示例使用动态版本 [@henry-hub](https://github.com/henry-hub) ([#175](https://github.com/ihub-pub/plugins/pull/175))

### ⬆️ Dependency Updates

- ⬆️ 升级组件版本 [@henry-hub](https://github.com/henry-hub) ([#178](https://github.com/ihub-pub/plugins/pull/178)) [@dependabot](https://github.com/dependabot) ([#171](https://github.com/ihub-pub/plugins/pull/171))  [@dependabot](https://github.com/dependabot) ([#170](https://github.com/ihub-pub/plugins/pull/170))
- ⬆️ 插件版本升至1.1.8 [@henry-hub](https://github.com/henry-hub) ([#168](https://github.com/ihub-pub/plugins/pull/168))

### 🧰 Maintenance

- 👷 调整工作流组件 [@henry-hub](https://github.com/henry-hub) ([#177](https://github.com/ihub-pub/plugins/pull/177))
- 👷 移除关闭里程碑时触发release草稿工作流 [@henry-hub](https://github.com/henry-hub) ([#176](https://github.com/ihub-pub/plugins/pull/176))

---

## 🔖 1.1.8 (2021-10-19)

### 🐛 Bug Fixes

- ✏️ setting私有仓库支持不安全协议 [@henry-hub](https://github.com/henry-hub) ([#165](https://github.com/ihub-pub/plugins/pull/165))

### 📝 Documentation

- 📝 完善使用文档 [@henry-hub](https://github.com/henry-hub) ([#166](https://github.com/ihub-pub/plugins/pull/166))

### ⬆️ Dependency Updates

- ⬆️ 升级ihub-bom组件 [@henry-hub](https://github.com/henry-hub) ([#167](https://github.com/ihub-pub/plugins/pull/167))

---

## 🔖 1.1.7 (2021-10-17)

### ✨ Features

- ✏️ 优化代码检查插件配置 [@henry-hub](https://github.com/henry-hub) ([#163](https://github.com/ihub-pub/plugins/pull/163))
- ✨ 配置插件项目配置支持忽略子项目目录 [@henry-hub](https://github.com/henry-hub) ([#162](https://github.com/ihub-pub/plugins/pull/162))
- ✨ 插件仓库支持配置私有仓库、自定义仓库 [@henry-hub](https://github.com/henry-hub) ([#161](https://github.com/ihub-pub/plugins/pull/161))
- ✏️ 删除lombok链式访问默认配置 [@henry-hub](https://github.com/henry-hub) ([#160](https://github.com/ihub-pub/plugins/pull/160))
- ✨ 组件pom开发者信息自动配置为github仓库贡献者 [@henry-hub](https://github.com/henry-hub) ([#159](https://github.com/ihub-pub/plugins/pull/159))

---

## 🔖 1.1.6 (2021-10-06)

### ✨ Features

- ✨ 自动替换组件升级版本 [@henry-hub](https://github.com/henry-hub) ([#157](https://github.com/ihub-pub/plugins/pull/157))
- ✨ 自定义manes.versions配置 [@henry-hub](https://github.com/henry-hub) ([#156](https://github.com/ihub-pub/plugins/pull/156))
- ✨ 自定义com.github.ben-manes.versions插件控制台打印格式 [@henry-hub](https://github.com/henry-hub) ([#155](https://github.com/ihub-pub/plugins/pull/155))

---

## 🔖 1.1.5 (2021-09-29)

### ✨ Features

- ✨ 添加lombok.config配置功能 [@henry-hub](https://github.com/henry-hub) ([#144](https://github.com/ihub-pub/plugins/pull/144))
- ✏️ 修正控制台配置打印对齐 [@henry-hub](https://github.com/henry-hub) ([#142](https://github.com/ihub-pub/plugins/pull/142))

### ⬆️ Dependency Updates

- ⬆️ 升级组件版本 [@henry-hub](https://github.com/henry-hub) ([#145](https://github.com/ihub-pub/plugins/pull/145))
- ⬆️ 插件版本升至1.1.4 [@henry-hub](https://github.com/henry-hub) ([#141](https://github.com/ihub-pub/plugins/pull/141))

---

## 🔖 1.1.4 (2021-09-19)

- 📝 控制台打印插件信息 [@henry-hub](https://github.com/henry-hub) ([#139](https://github.com/ihub-pub/plugins/pull/139))
- ✅ 补充测试用例 [@henry-hub](https://github.com/henry-hub) ([#138](https://github.com/ihub-pub/plugins/pull/138))

### ✨ Features

- ✨ 设置插件支持三级项目直接添加为主项目的子项目 [@henry-hub](https://github.com/henry-hub) ([#140](https://github.com/ihub-pub/plugins/pull/140))
- ♻️ 重构配置打印 [@henry-hub](https://github.com/henry-hub) ([#137](https://github.com/ihub-pub/plugins/pull/137))

### 📝 Documentation

- 📝 提取文档版本脚本至公共组件 [@henry-hub](https://github.com/henry-hub) ([#136](https://github.com/ihub-pub/plugins/pull/136))

### ⬆️ Dependency Updates

- ⬆️ 插件版本升至1.1.3 [@henry-hub](https://github.com/henry-hub) ([#135](https://github.com/ihub-pub/plugins/pull/135))

---

## 🔖 1.1.3 (2021-09-12)

### ✨ Features

- 🚚 bom插件组件版本配置迁移至ihub-bom组件 [@henry-hub](https://github.com/henry-hub) ([#133](https://github.com/ihub-pub/plugins/pull/133))

### 🐛 Bug Fixes

- 🐛 env和system属性读取存在问题 #132 [@henry-hub](https://github.com/henry-hub) ([#134](https://github.com/ihub-pub/plugins/pull/134))

### ⬆️ Dependency Updates

- ⬆️ 插件版本升至1.1.2 [@henry-hub](https://github.com/henry-hub) ([#130](https://github.com/ihub-pub/plugins/pull/130))

---

## 🔖 1.1.2 (2021-09-01)

- 👷 工作流调整 [@henry-hub](https://github.com/henry-hub) ([#127](https://github.com/ihub-pub/plugins/pull/127))
- 👷 添加git-hooks [@henry-hub](https://github.com/henry-hub) ([#124](https://github.com/ihub-pub/plugins/pull/124))

### ✨ Features

- ✏️ 添加引入GithubPom插件条件判断 [@henry-hub](https://github.com/henry-hub) ([#129](https://github.com/ihub-pub/plugins/pull/129))

### ⬆️ Dependency Updates

- ⬆️ 插件版本升至1.1.1 [@henry-hub](https://github.com/henry-hub) ([#118](https://github.com/ihub-pub/plugins/pull/118))
- ⬆️ hutool版本升级至5.7.11 [@henry-hub](https://github.com/henry-hub) ([#128](https://github.com/ihub-pub/plugins/pull/128))
- ⬆️ spring-boot-admin版本升级至2.5.1 [@henry-hub](https://github.com/henry-hub) ([#125](https://github.com/ihub-pub/plugins/pull/125))
- ⬆️ 升级gradle至7.2 [@henry-hub](https://github.com/henry-hub) ([#119](https://github.com/ihub-pub/plugins/pull/119))

---

## 🔖 1.1.1 (2021-08-21)

- 👷 工作流调整 [@henry-hub](https://github.com/henry-hub) ([#109](https://github.com/ihub-pub/plugins/pull/109))

### ✨ Features

- ✨ Publish插件引入io.freefair.github.pom [@henry-hub](https://github.com/henry-hub) ([#116](https://github.com/ihub-pub/plugins/pull/116))
- ✨ 添加ihub-git-hooks插件 [@henry-hub](https://github.com/henry-hub) ([#107](https://github.com/ihub-pub/plugins/pull/107))
- ✏️ Github配置任务调整不再生成Issue、PR模板 [@henry-hub](https://github.com/henry-hub) ([#100](https://github.com/ihub-pub/plugins/pull/100))
- 👷 拆分build工作流 [@henry-hub](https://github.com/henry-hub) ([#98](https://github.com/ihub-pub/plugins/pull/98))

### 🐛 Bug Fixes

- 🐛 修复groovy版本不能指定BUG [@henry-hub](https://github.com/henry-hub) ([#110](https://github.com/ihub-pub/plugins/pull/110))
- 🐛 修复CodeNarc编码问题 [@henry-hub](https://github.com/henry-hub) ([#108](https://github.com/ihub-pub/plugins/pull/108))

### 📝 Documentation

- 📝 完善文档 [@henry-hub](https://github.com/henry-hub) ([#114](https://github.com/ihub-pub/plugins/pull/114))
- 📝 支持多版本文档 [@henry-hub](https://github.com/henry-hub) ([#111](https://github.com/ihub-pub/plugins/pull/111))

### ⬆️ Dependency Updates

- ⬆️ 升级组件版本 [@henry-hub](https://github.com/henry-hub) ([#117](https://github.com/ihub-pub/plugins/pull/117))
- ⬆️ 升级bom组件版本 [@henry-hub](https://github.com/henry-hub) ([#106](https://github.com/ihub-pub/plugins/pull/106))
- Bump pl.droidsonroids.jacoco.testkit from 1.0.8 to 1.0.9 [@dependabot](https://github.com/dependabot) ([#103](https://github.com/ihub-pub/plugins/pull/103))

---

## 🔖 1.1.0 (2021-08-08)

- 💥 添加ihub-generate插件，用于生成GitHub配置 [@henry-hub](https://github.com/henry-hub) ([#94](https://github.com/ihub-pub/plugins/pull/94))
- 👷 工作流调整 [@henry-hub](https://github.com/henry-hub) ([#91](https://github.com/ihub-pub/plugins/pull/91))
