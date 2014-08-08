package com.puppetlabs.processbuilder;

import com.puppetlabs.processbuilder.reproducers.ProcessBuilderReproducer;
import com.puppetlabs.processbuilder.reproducers.ProcessBuilderWrapperReproducer;
import com.puppetlabs.processbuilder.reproducers.Reproducer;
import com.puppetlabs.processbuilder.reproducers.UNIXProcessReproducer;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MemLeakDriver {

    private static void showUsageAndExit() {
        System.err.println("System property 'reproType' must be set to one of:");
        System.err.println("\t'pb' - for plain ProcessBuilder w/o wrapped output/error streams");
        System.err.println("\t'pbw' - for wrapped ProcessBuilder that consumes output/error streams");
        System.err.println("\t'up' - for direct call to UnixProcess");
        System.exit(-1);
    }

    private static Reproducer getReproducerForType(String reproducerType) {
        if (reproducerType.equals("pb")) {
            System.out.println("Reproducing via unwrapped ProcessBuilder");
            return new ProcessBuilderReproducer();
        } else if (reproducerType.equals("pbw")) {
            System.out.println("Reproducing via wrapped ProcessBuilder");
            return new ProcessBuilderWrapperReproducer();
        } else if (reproducerType.equals("up")) {
            System.out.println("Reproducing via wrapped UnixProcess");
            return new UNIXProcessReproducer();
        } else {
            System.err.println("System property 'reproType' set to unrecognized value: '" + reproducerType + "'\n");
            showUsageAndExit();
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("java version:");
        System.out.println(System.getProperty("java.version"));
        System.out.println(System.getProperty("java.vm.version"));
        System.out.println(System.getProperty("java.vm.name"));
        System.out.println(System.getProperty("java.runtime.name"));
        long duration = TimeUnit.MINUTES.toMillis(30);
        long start = System.currentTimeMillis();

        String reproducerType = System.getProperty("reproType");
        if (reproducerType == null) {
            System.err.println("Missing required system property: 'reproType'\n");
            showUsageAndExit();
        }
        Reproducer r = getReproducerForType(reproducerType);
        Thread.sleep(2000);

        int i = 0;
        do {
            i++;
            System.out.println("iteration " + i);
            r.execute();
        } while ((System.currentTimeMillis() - start) <= duration);

        System.out.println("Exiting loop; sleeping.");
        CountDownLatch latch = new CountDownLatch(1);
        latch.await();

    }

}
