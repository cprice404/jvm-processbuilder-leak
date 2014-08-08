package com.puppetlabs.processbuilder.reproducers;

import com.puppetlabs.processbuilder.wrapper.ProcessWrapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProcessBuilderWrapperReproducer implements Reproducer {

    private final List<String> cmd;

    public ProcessBuilderWrapperReproducer() {
        cmd = new ArrayList<String>();
        cmd.add("hostname");
    }

    @Override
    public void execute() throws Exception {
        ProcessBuilder pb = new ProcessBuilder(cmd);
        ProcessWrapper wrapper = new ProcessWrapper(pb.start());

        System.out.println("Command has terminated with status: " + wrapper.getStatus());
        System.out.println("Output:\n" + wrapper.getInfos());
        System.out.println("Error: " + wrapper.getErrors());
    }
}