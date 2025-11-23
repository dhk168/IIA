import { webview } from '@kit.ArkWeb';

@Entry
@Component
struct HttpLoadTestPage {
  controller: webview.WebviewController = new webview.WebviewController();
  @State mode: MixedMode = MixedMode.All;
  @State url: string = 'http://39.106.26.90:4488/';

  build() {
    Column() {
      Web({ src: this.url, controller: this.controller })
        .mixedMode(this.mode)
        .fileAccess(true)
        .javaScriptAccess(true)
        .domStorageAccess(true)
        .onlineImageAccess(true)
    }
  }
}
