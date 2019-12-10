# TestAccessibilityService
自动点击服务 accessibilityservice sdk，为Android6.0以上的设备进行自动点击授权

### 引入库

    api 'cn.com.startai:common:1.1.31'
    api 'cn.com.startai:accessibilityservice:1.1.1'

### 初始化

    StartAIAccessibilityService.initAccessbilityService(this);
    
### 注意事项

1. 若设备有Root权限，调用initAccessbilityService后服务会自动打开
2. 若设备没有Root权限，需要手动打开，打开方式 设置->其他设置->无障碍（拉到最下方）->自动点击授权服务。    
3. 若出现无法正常点击授权的情况，请将授权动作延迟500ms后进行。