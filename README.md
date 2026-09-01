# MarketBoxer

基于 Java、Spring Boot、MySQL 和富途 OpenAPI 的量化交易实验项目。

> **发布状态：尚未完成开源发布准备。** 主分支已重建为不含旧历史的单一根提交，但部分源码带有原公司的版权声明，公开发布前仍必须确认代码权属。历史中暴露过的交易口令必须轮换。

## 前置条件

- JDK 8 或更高版本
- MySQL
- Python 和 Jupyter（仅运行 `strategy/` 中的分析 Notebook 时需要）
- 富途 OpenD 与官方 Java SDK 6.2.2707

## 安装富途 SDK

富途 API Code 采用非商业许可证，禁止本项目直接再分发。请阅读并接受[富途官方许可](https://github.com/FutunnOpen/java-futu-api/blob/main/License.txt)，自行从富途官方 SDK 包取得兼容 6.2.2707 API 的 JAR 后安装到本地 Maven 仓库：

```bash
./mvnw install:install-file \
  -Dfile=/path/to/futu-api-6.2.2707.jar \
  -DgroupId=com.futunn.openapi \
  -DartifactId=futu-api \
  -Dversion=6.2.2707 \
  -Dpackaging=jar \
  -DgeneratePom=true
```

本项目不授予任何富途 API Code 或行情数据的再分发权利。

## 运行配置

MySQL 配置使用 Spring Boot 标准配置项，可通过环境变量设置：

```bash
export SPRING_DATASOURCE_URL='jdbc:mysql://localhost:3306/stock'
export SPRING_DATASOURCE_USERNAME='marketboxer'
export SPRING_DATASOURCE_PASSWORD='replace-with-local-password'
```

调用富途交易接口前设置：

```bash
export FUTU_USER_ID='replace-with-user-id'
export FUTU_TRADE_ACCOUNT_ID='replace-with-account-id'
export FUTU_UNLOCK_TRADE_PASSWORD_MD5='replace-with-password-md5'
export FUTU_OPEND_HOST='127.0.0.1'
export FUTU_OPEND_PORT='11111'
export FUTU_RSA_KEY_FILE='/path/to/private-key.pem'
```

不要将真实值、私钥或本地配置提交到 Git。测试数据库可使用 `TEST_DB_URL`、`TEST_DB_USERNAME` 和 `TEST_DB_PASSWORD` 覆盖默认配置。

## 初始化和启动

1. 安装并启动[富途 OpenD](https://openapi.futunn.com/futu-api-doc/)。
2. 执行 `data-access/init.sql` 初始化 MySQL 表结构。
3. 运行测试：`./mvnw test`。
4. 启动应用：`./mvnw -pl container -am spring-boot:run`。

## 下载数据

- 服务端口默认为 `9966`。
- K 线接口：`/kline/get/{code}/{start}/{end}`。
- 股票元数据接口：`/stock_profile/{market_code}`。
- 可用市场包括 `CNSH_S`、`CNSH_F`、`CNSZ_S`、`CNSZ_F`、`HK_S`、`HK_F`、`US_S`、`US_F`、`SG_S`、`SG_F`、`JP_S`。

请自行确认行情数据来源的许可，不要提交从第三方接口下载的完整数据集。

## 策略分析

macOS/Linux 使用 `scripts/start_jupyter.sh`，Windows 使用 `scripts/start_jupyter.bat`。脚本会通过相对路径打开 `strategy/`，无需填写个人目录。

Notebook 不包含运行输出或行情数据。运行前请自行准备具有合法使用权的数据。

## 许可证

主项目计划采用 Apache License 2.0。该许可证不覆盖富途 API Code、第三方行情数据或其他独立许可的第三方材料。
