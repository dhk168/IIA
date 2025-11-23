// 导入WebView模块
import web_webview from '@ohos.web.webview';

@Entry
@Component
struct Index {
  // 创建WebView控制器
  private webViewController: web_webview.WebviewController = new web_webview.WebviewController();

  build() {
    // 使用Column作为容器
    Column() {
      // 添加WebView组件，设置为加载百度首页
      Web({ 
        src: 'https://www.baidu.com',
        controller: this.webViewController
      })
      // 设置WebView占满整个屏幕
      .width('100%')
      .height('100%')
    }
    .width('100%')
    .height('100%')
  }
}