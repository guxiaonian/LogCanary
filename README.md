<div align="center">

## LogCanary

**`Android`Log日志打印输出**

[![Download](https://api.bintray.com/packages/guxiaonian/logcanary/logcanary/images/download.svg) ](https://bintray.com/guxiaonian/logcanary/logcanary/_latestVersion)
[![GitHub issues](https://img.shields.io/github/issues/guxiaonian/LogCanary.svg)](https://github.com/guxiaonian/LogCanary/issues)
[![GitHub forks](https://img.shields.io/github/forks/guxiaonian/LogCanary.svg)](https://github.com/guxiaonian/LogCanary/network)
[![GitHub stars](https://img.shields.io/github/stars/guxiaonian/LogCanary.svg)](https://github.com/guxiaonian/LogCanary/stargazers)
[![GitHub license](https://img.shields.io/github/license/guxiaonian/LogCanary.svg)](http://www.apache.org/licenses/LICENSE-2.0)

</div>
<br>

# 效果展示

![log1_logo](./img/img1.jpg)
![log2_logo](./img/img2.jpg)

# 依赖

```gradle
debugImplementation  'fairy.easy.logcanary:logcanary:{latestVersion}'
releaseImplementation  'fairy.easy.logcanary:logcanary-no-op:{latestVersion}'
//androidX使用
//debugImplementation  'fairy.easy.logcanary:logcanary-androidx:{latestVersion}'

```
      
# 调用方式

```java
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LogCanary.install(this);
    }
}

```
