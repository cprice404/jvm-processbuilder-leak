package com.puppetlabs.processbuilder.reproducers;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class ProcessBuilderReproducer implements Reproducer {
    @Override
    public void execute() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("hostname");
        Process p = pb.start();
        p.waitFor();
        System.out.println(IOUtils.toString(p.getInputStream()));
    }

}
