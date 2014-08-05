package com.puppetlabs.processbuilder.reproducers;

import java.io.IOException;

public interface Reproducer {
    void execute() throws Exception;
}
