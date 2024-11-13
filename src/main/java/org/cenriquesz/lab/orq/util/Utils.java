package org.cenriquesz.lab.orq.util;


public class Utils {

  public static void wait(int seconds){
    try {
      Thread.sleep(seconds * 1000L);
    } catch (InterruptedException ignored) {
    }
  }

}
