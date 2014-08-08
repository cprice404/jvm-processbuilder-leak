package com.puppetlabs.processbuilder.reproducers;

import com.puppetlabs.processbuilder.wrapper.ProcessWrapper;
import java.lang.reflect.Constructor;

public class UNIXProcessReproducer implements Reproducer {
    @Override
    public void execute() throws Exception {
        byte[] prog = toCString("hostname");
        byte[] argBlock = new byte[0];
        int argc = 0;
        int[] envc = new int[1];
        byte[] envBlock = null;
        byte[] dir = toCString("/tmp");
        int[] fdfs = new int[] { -1, -1, -1 };
        boolean redirectErrorStream = false;

        Class upClass = Class.forName("java.lang.UNIXProcess");
        Constructor[] ctors = upClass.getDeclaredConstructors();
        assert ctors.length == 1;
        Constructor ctor = ctors[0];
        ctor.setAccessible(true);
        Process p = (Process) ctor.newInstance(prog, argBlock, argc,
                envBlock, envc[0], dir, fdfs, redirectErrorStream);
        ProcessWrapper wrapper = new ProcessWrapper(p);

        System.out.println("Command has terminated with status: " + wrapper.getStatus());
        System.out.println("Output:\n" + wrapper.getInfos());
        System.out.println("Error: " + wrapper.getErrors());
    }

    private static byte[] toCString(String s) {
        if (s == null)
            return null;
        byte[] bytes = s.getBytes();
        byte[] result = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0,
                result, 0,
                bytes.length);
        result[result.length-1] = (byte)0;
        return result;
    }
}
