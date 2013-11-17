欢迎界面：StartActivity --> start.xml
主界面：MainActivity --> tab_main.xml
打开网络设置：startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS), 0);

一、首页：HomeActivity --> tab_home.xml
头条：HeadlinesActivty --> home_sub_banner.xml
茶座：TeahouseActivty --> home_sub_banner.xml
焦点：FocusActivity -->  
娱乐：EntertainmentActivity 
情感 ：EmotionalActivity

二、生活：LiveActivity  --> live.xml
房产：HouseActivity   -->  live_sub_banner.xml

三、论坛：ForumActivity  --> tab_forum.xml

四、商圈：DistrictActivity --> district.xml

Intent GPSIntent = new Intent();
GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
GPSIntent.setData(Uri.parse("custom:3"));
try {
    PendingIntent.getBroadcast(this, 0, GPSIntent, 0).send();
} catch (CanceledException e) {
    e.printStackTrace();
}

五、更多：MoreActivity  --> more.xml


目前未完成功能：
1. 商圈。
2. 查看指定版块下的帖子列表中的“置顶”、“精华”、“搜索”。这些功能与“全部”是一样的
3. 查看指定的帖子详细中的“只看楼主”、“最新回复”。这些功能与“默认”是一样的
4. 查看指定的帖子详细可的图片，表情处理（这个还有点技术问题需要解决）。
5. 发帖时的插入表情（这个还有点技术问题需要解决）。
        发帖时的拍照或选择本地上传
6. 回复功能。与发帖功能类似。
7. 更多--》清除缓存。