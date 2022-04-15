=====
框架实战
springBoot + dubbo + zookeeper + webSocket + kafka + redis + elasticSearch + apache.sharding sphere

代码同步github + gitee平台

优化程序启动内容 + dubbo接口分离 + 消费端模块socket通讯Ajax测试

1. 本地环境启动 : zookeeper -> kafka -> seata -> elasticsearch

2. 服务启动顺序 : cloudtravel-producer -> cloudtravel-kafka -> cloudtravel-websocket -> cloudtravel-shardingsphere -> cloudtravel-consumer

环境配置文档见:
https://www.cnblogs.com/kxkl123/p/15864256.html