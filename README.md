# retry_handler
This library can be used implementing retrying.

# Usage

## import lib.
```java
import jp.toastkid.libs.retry.Retry;
```

## and use it.
```java:use_by_instance
new Retry.Builder().setAction((i) -> {
         if (i == 1) {
             System.out.println("retry.");
             return;
         }
         throw new RuntimeException("Please retry.");
     }).setMaxRetry(3).setIntervalMs(1000L).build().attempt();
```

```java:use_static
Retry.attempt((i) -> {System.out.println(i + " static retry.");}, 2, 1000L);
```

# LICENSE
This library is licensed 2-clause BSD license.
