# Raffle

一个简单的随机数生成，并生成到80 * 25 的TTY/Terminal上。


```bash
 mvn -Pnexus package -DskipTests
```

```bash
java -jar target/raffle-0.0.1-SNAPSHOT.jar --raffle.left-spaces=15 --raffle.count=500
```
count是最多人数

left-spaces是生成的数字左边的空格数。

