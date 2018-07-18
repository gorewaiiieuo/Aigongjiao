# Aigongjiao


本app基于高德地图sdk，用于规划用户所在城市的公交/地铁出行线路。

注意：若提示高德地图key问题需要下列网站注册个人key：
https://lbs.amap.com/dev/key/app
 
 并将注册得到的key，替换到AndroidManifest.xml文件下的application标签下以下红框位置：
 
 ![Image text](https://github.com/gorewaiiieuo/Aigongjiao/blob/master/img/7.png)
 
 

界面展示：

![Image text](https://github.com/gorewaiiieuo/Aigongjiao/blob/master/img/1.png)
![Image text](https://github.com/gorewaiiieuo/Aigongjiao/blob/master/img/2.png)
![Image text](https://github.com/gorewaiiieuo/Aigongjiao/blob/master/img/3.png)
![Image text](https://github.com/gorewaiiieuo/Aigongjiao/blob/master/img/4.png)
![Image text](https://github.com/gorewaiiieuo/Aigongjiao/blob/master/img/5.png)
![Image text](https://github.com/gorewaiiieuo/Aigongjiao/blob/master/img/6.png)

其中：
 
  -Game包：提供客户等待车时消遣娱乐的两个小游戏
 
  -historyRecyclerAdapater包：实现起始点搜索历史记录的RecyclerView
 
  -Navigation包：高德地图sdk使用的相关代码
 
  -routeDisplayAdapter包：实现显示规划线路的RecyclerView
 
  -searchLineUtils包：实现到站提醒和路线号搜索该站点过站功能
 
  -searchStationUtils包：实现站点名搜索线路功能
 
  -SharedPreferencesUtils包：用于保存起始点搜索历史到本地
 
  -welcomActivity包：实现应用启动前欢迎界面
