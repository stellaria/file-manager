# C-cloud
私人定制网络云盘，为你的个人数据保驾护航。
## 快速开始
<a href="39.105.143.29">项目地址</a>

> 由于未知原因，验证码模块炸了,现阶段暂不提供注册功能.可使用提供的帐号来测试
> 帐号|密码
> :---:|:---:
> xpwanghy@hotmail.com|jack990729
> 99730729@qq.com|jack990729
> 第一个是超级管理员，后一个是普通用户

## 技术使用
+ [spring boot](https://spring.io/projects/spring-boot/) 后端框架
+ [maven](http://maven.apache.org/) 解决繁琐的项目依赖问题
+ [vue](https://cn.vuejs.org/) 前端框架
+ [vue router](https://router.vuejs.org/zh/) 前端路由组件
+ [vuex](https://vuex.vuejs.org/zh/) 组件状态管理
+ [vue-cli3](https://cli.vuejs.org/zh/) 前端脚手架快速搭建项目
+ [element-ui](https://element.eleme.cn/#/zh-CN) 网页ui，快速搭建全靠它

## 环境要求
+ jdk1.8
+ node.js 12.3.1 LTS
+ npm6.9.2

## 部署
使用docker快速部署
> 本来是想按常规，使用tomcat+nginx来处理请求，后面转念一想，第一是可以加分，第二docker更方便，因此转而使用docker。
> 
> 结果在部署的过程中，踩了不少坑。包括但不限于：mysql数据库服务没有设置初始密码、在没找到--link选项之前使用手动查找容器的ip，结果重启后因ip不正确而让tomcat容器无法访问mysql容器、mysql容器的数据持久化等。折腾了一个下午，花费的时间比正常搭建环境还要久。
### 后端服务核心
将项目整个pull到本地后，cd到file-core目录，运行`mvn install`来构建war包
### 前端页面
cd到file-front目录运行`npm install` 再运行`npm run build`构建前端页面 
