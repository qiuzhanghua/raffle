package com.github.qiuzhanghua.raffle;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "raffle")
public class RafffleConfiguration {
  private int count;  // 人数，不超过1000
  private int leftSpaces;  // 左边增加的空格

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public int getLeftSpaces() {
    return leftSpaces;
  }

  public void setLeftSpaces(int leftSpaces) {
    this.leftSpaces = leftSpaces;
  }
}
