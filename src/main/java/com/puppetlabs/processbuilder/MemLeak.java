package com.puppetlabs.processbuilder;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MemLeak {
    public static void main(String[] args) throws Exception {
        List<String> cmd = new ArrayList<String>();
        cmd.add("hostname");

        System.out.println("java version:" + System.getProperty("java.version"));
        long duration = TimeUnit.DAYS.toMillis(1);
        long start = System.currentTimeMillis();
        int i = 0;
        do {
            i++;
            System.out.println("iteration " + i);
//            ProcessBuilder pb = new ProcessBuilder("hostname");
//            Process p = pb.start();
//            p.waitFor();
//            System.out.println(IOUtils.toString(p.getInputStream()));
            ProcessBuilderWrapper pb = new ProcessBuilderWrapper(cmd);
            System.out.println("Command has terminated with status: " + pb.getStatus());
            System.out.println("Output:\n" + pb.getInfos());
            System.out.println("Error: " + pb.getErrors());
        } while ((System.currentTimeMillis() - start) <= duration);

    }
}
