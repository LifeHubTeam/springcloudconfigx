# spring cloud config with mysql and opt by pages.

## micro-config
  这是spring cloud config相关java代码
### 使用说明
####  1.数据库初始化
    使用micro-config\sql\tables.sql初始化相应数据库及表
####  2.修改相应的项目配置
    micro-config\src\main\resources\application.yml
    数据库配置、redis配置、rabbitmq配置、eureka配置
####  3.打包命令
    修改bin目录下startup.sh脚本，当前指定路径/apps/dat/config-server为打包后zip解压目录，修改相应的shutdown.sh端口
    mvn clean install
    拷贝相应zip包到服务器解压，进入bin里面执行startup.sh脚本
    
## configPages
  这是相应的操作页面
### This project is build for Vue.js 2 + vue-router + webpack2 + iView 2, just install and run.

#### modify server addrees by src/config/config.js

#### Install
```bush
// install dependencies
npm install
```
#### Run
#### Development
```bush
// For the first time, run init to create index.html
npm run init
npm run dev
```
#### Production(Build)
```bush
npm run build
```
