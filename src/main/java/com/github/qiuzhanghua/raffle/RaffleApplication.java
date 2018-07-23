package com.github.qiuzhanghua.raffle;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@SpringBootApplication
public class RaffleApplication {

  @Profile("!test")
  @Bean
  public ApplicationRunner appRunner(RafffleConfiguration configuration) {
    return (args -> {
      List<String> digitals = new ArrayList<>();
      try {
        ClassPathResource resource = new ClassPathResource("digitals.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        digitals = reader.lines().collect(Collectors.toList());
        reader.close();
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(-1); //
      }

      int max = min(999, configuration.getCount());  // 不会超过1000人
      if (max < 100) max = 100;

      Random r = new Random();

      List<Integer> result = new ArrayList<>();

      try (Terminal terminal = TerminalBuilder.builder()
          .jna(true)
          .system(true)
          .build();
           Reader reader = terminal.reader();
           PrintWriter writer = terminal.writer();

      ) {
        terminal.enterRawMode();
        for (; ; ) {
          int read = reader.read();
          if (read == 27) break;

          writer.print((char) 27 + "[2J");   // clear screen
          writer.print((char) 27 + "[0;0H"); // move to top/left
          int random = r.nextInt(max) + 1;
          while (result.contains(random)) random = r.nextInt(max) + 1;
//    random = 291;  //test
          int c = random % 10;
          int t = ((random - c) / 10) % 10;
          int h = (random - t * 10 - c) / 100;
          String color = Ansi.RED;
          int colorRandom = r.nextInt(3);
          if (colorRandom == 0) color = Ansi.GREEN;
          if (colorRandom == 1) color = Ansi.BLUE;

//    System.out.printf("%d%d%d\n", h , t, c);
          writer.print(generateBuffer(h, t, c, configuration.getLeftSpaces(), color, digitals));
          result.add(random);
          if (result.size() + 1 == max) result.clear();  // 没有更多随机数了。
          writer.print(result.stream().map(integer -> Integer.toString(integer)).collect(Collectors.joining(",", "[", "]")));
          writer.println();
          writer.println(result.size());
//    System.out.println(generateBuffer(1, 6 , 0, 5, Ansi.RED, digitals));
          writer.flush();
        }
        writer.print(Ansi.SANE);
        String filename = "raffle-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) + ".txt";
        String text = result.stream().map(integer -> Integer.toString(integer)).collect(Collectors.joining("\n"));
        Files.write(Paths.get(filename), text.getBytes());
      } catch (IOException e) {
        System.err.println("IO Exception: " + e.getMessage());
      }


    });
  }

  ;

  public static void main(String[] args) {

    SpringApplication.run(RaffleApplication.class, args);
//    System.out.print((char) 27 + "[2J");
//    System.out.print((char) 27 + "[0;0H");
//    String msg = Ansi.Blink.colorize("BOOM!");
//    System.out.print(msg);
//    msg = Ansi.BLUE + " scientific";
//    System.out.print(msg);
//    msg = Ansi.Red.and(Ansi.BgYellow).and(Ansi.RapidBlink).colorize("Hello");
//    System.out.print(msg);


  }


  public String generateBuffer(int h, int t, int c, int pre, String color, List<String> digitals) {
    StringBuffer sb = new StringBuffer();

    sb.append(color);
    for (int i = 0; i < digitals.size(); i++) {
      for (int j = 0; j < pre; j++) sb.append(" ");
      String line = digitals.get(i);
      int tail = line.length();
      sb.append(line.substring(h * 16, min((h + 1) * 16, tail)));
      sb.append(line.substring(t * 16, min((t + 1) * 16, tail)));
      sb.append(line.substring(c * 16, min((c + 1) * 16, tail)));
      sb.append("\n");
    }
    sb.append(Ansi.SANE);
    return sb.toString();
  }

  public int min(int x, int y) {
    if (x < y) return x;
    return y;
  }

}


// https://en.wikipedia.org/wiki/ANSI_escape_code
// http://www.network-science.de/ascii/
