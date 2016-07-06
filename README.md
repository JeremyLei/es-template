# Matrix-Job

Matrix-Job 是格瓦拉数据中心任务模板

方便对任务代码进行集中管理开发

## matrix-job任务常用组件

* jdbc连接模板
* Kafka生产、消费模板
* es索引管理模板
* hbase操作模板
* 任务监控
* dubbo服务提供与消费

## 任务开发流程

* 设置任务参数
在配置文件schedule-job.properties中添加参数：
    * 应用名:`test.app.name=Test`
    * 应用端口:`test.app.poot=20881`
	* 应用的任务分组:`test.job.group.name=TestGroup`

	* 任务名:`test.job1.name=TestJob`
	* 任务类:`test.job1.class=com.gewara.job.TestJob`

* 自定义任务类
	* 实现`Job`接口
	* 在`execute`方法中，若有异常导致任务无法正常执行，请`return`退出当前执行函数
	* `JobServer`添加`case`语句

* 本地测试说明：
	* 定时任务执行一次，使用：`QuartzManager.startSimpleJob(jobName, jobClass, startTime)`
	* 定时任务周期执行，使用：`QuartzManager.startJob(jobName, jobClass, cronExpression)`

## 打包发布
* maven打包
* 将tar.gz文件上传要运行的服务器
* 启动：`/bin/run.sh 任务名称`;停止：`/bin/stop.sh 任务名称`
* 访问数据中心：[gewa-dn5.gewara.cn:8088/gewa-datacenter/index.xhtml](gewa-dn5.gewara.cn:8088/gewa-datacenter/index.xhtml)
	* 添加任务
	* 启动任务


## 实现的任务列表


|  应用名   | 任务分组         | dubbo服务端口  |                     任务列表                        |
| ----------| ---------------- |----------------|-----------------------------------------------------|
| Hankeye   | HankeyeJobGroup  | 20881          | `HankeyeJob`,`HankeyeSellJob`,`HankeyeDeleteJob`    |
| Gmatrix   | GmatrixJobGroup  | 20883          | `GmatrixPvuvComputeJob`,`GmatrixPvuvInsertMysqlJob` |
| Spider    | PiaoFangJobGroup | 20884          | `PiaoFangJob`                                       |

