package com.github.qiuzhanghua.raffle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleApplicationTests {

  @Test
  public void contextLoads() {
  }

  @Test
  public void generate() {
    int width = 160;
    int height = 24;
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics g = image.getGraphics();
    g.setFont(new Font("Arial Rounded MT", Font.BOLD, 24));

    Graphics2D graphics = (Graphics2D) g;
    graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    graphics.drawString("0123456789", 0, 20);

    //save this image
    try {
      ImageIO.write(image, "png", new File("target" + File.separator + "digitals.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    for (int y = 0; y < height; y++) {
      StringBuilder sb = new StringBuilder();
      for (int x = 0; x < width; x++) {
        char  c = (char)('0' + (x + 1) / 16);
        sb.append(image.getRGB(x, y) == -16777216 ? " " : c);
      }

      if (sb.toString().trim().isEmpty()) {
        continue;
      }

      System.out.println(sb);
    }
//    System.out.println ((char)27 + "[2J");
  }

}
