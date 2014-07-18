package com.puppetlabs.processbuilder;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MemLeak {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("java version:" + System.getProperty("java.version"));
        long duration = TimeUnit.DAYS.toMillis(1);
        long start = System.currentTimeMillis();
        int i = 0;
        do {
            i++;
            System.out.println("iteration " + i);
            ProcessBuilder pb = new ProcessBuilder("hostname");
            Process p = pb.start();
            p.waitFor();
            System.out.println(IOUtils.toString(p.getInputStream()));
        } while ((System.currentTimeMillis() - start) <= duration);

    }
}
