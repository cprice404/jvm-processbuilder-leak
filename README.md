jvm-processbuilder-leak
=======================

Repro case to illustrate memory leak in Java ProcessBuilder class on linux x64 platforms
----------------------------------------------------------------------------------------

We have noticed that in some recent versions of the Oracle JDK and OpenJDK on
64-bit linux platforms, the `ProcessBuilder` class appears to leak a tiny bit of
native memory whenever an external process is executed.  The code in this repository
is intended to provide a minimal repro case to illustrate the leak.

Here is a graph showing memory usage of various runs of this code over time:

https://docs.google.com/a/puppetlabs.com/spreadsheets/d/14U1lMIj4b3ZitiIONuetIiFWsr2XoaIAWEOOt-zoohI/edit#gid=0

For Oracle JDK, the leak does *not* appear to be present in 7u45, but *does* appear
to be present in 7u55.  For OpenJDK, the leak does *not* appear to be present in 7u55,
but *does* appear to be present in 7u55.  (TODO: add link to graph)

To repro, simply run:

    mvn compile
    mvn exec:exec

Then, in another shell, you can run:

    ./watchpidmem.sh

This script will periodically check the resident memory usage of the java process
according to the operating system (this value is shown in the third column of the
script's output).  The maven exec plugin is configured to launch the JVM with a max
heap size of 32m, but within a short period of time (on affected versions of the JDK)
you will see the resident memory usage climb well above what would be an expected
maximum value (even taking into account that a bit of extra non-heap memory for permgen,
etc.).  The memory usage appears to continue to climb indefinitely.

Alternate repro scenarios
-------------------------

We've included code to repro the leak in a few different fashions, in order to try
to make sure this is not obvious user error.  To override the reproducer scenario
you can optionally set a java system property `reproType` on the maven command line.
(NOTE that all of these scenarios seem to exhibit the same behavior, in terms of
leaking predictably on affected versions of the JDK, and not leaking at all on
older versions.)

Examples:

    mvn -DreproType=pb exec:exec

This uses a very naive call to `ProcessBuilder`, which does not make any attempt to
ensure that the process's stdout/stderr streams are consumed or closed.

    mvn -DreproType=pbw exec:exec

(NOTE: This is the default repro scenario if you simply run `mvn exec:exec`.)
This uses a slightly more sophisticated approach to try to make sure it consumes
and closes the output/error streams of the spawned process.  It uses wrapper around
`ProcessBuilder`, which creates some background threads that are responsible for
consuming and closing those streams.  The wrapper also makes sure that those
background threads are joined (and thus finished with their work) before returning
control to the main thread.
