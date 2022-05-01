# zhua-8-bot-plugin
zhua8机器人(个人定制化机器人)执行逻辑,基于mirai-plugin实现


# 开始
首先,你需要安装 mirai:
https://github.com/mamoe/mirai

安装请参阅:
https://github.com/mamoe/mirai/blob/dev/docs/UserManual.md

本人操作系统使用的是Ubuntu
此插件通过使用mirai-core-api实现,插件使用方法为:

<b>
  
 mirai执行目录下的/plugins文件夹
  
 将打包好的插件放入文件夹中
  
 启动mirai后,mirai会通过内置的插件扫描器扫描该目录下的.jar插件
  
</b>

# 插件如何打包成.jar?
如果使用maven, 则package

如果是gradle,则jar

(说这些应该是有点多余了吧)

# 关于插件开发
具体开发流程请参阅mirai插件开发文档:
https://docs.mirai.mamoe.net/console/Plugins.html
