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
