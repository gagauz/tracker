package com.gagauz.tracker.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class BashUtils {

    private static final String NL = System.getProperty("line.separator");

    private BashUtils() {
    }

    public static String join(String command, String... args) {
        StringBuilder commandArgs = new StringBuilder(command);
        for (String arg : args) {
            commandArgs.append(' ').append(arg);
        }
        return commandArgs.toString();
    }

    public static String execute(File directory, String format, Object... args) {
        StringBuilder output = new StringBuilder();
        execute(directory, output, format, args);
        return output.toString();
    }

    public static int execute(File directory, StringBuilder output, String format, Object... args) {
        Process process = null;

        try {

            if (args.length > 0) {
                format = String.format(format, args);
            }

            ProcessBuilder builder = new ProcessBuilder();
            for (Map.Entry<String, String> e : System.getenv().entrySet()) {
                builder.environment().put(e.getKey(), e.getValue());
            }
            builder.command(format.split(" "));
            builder.redirectErrorStream(true);
            builder.directory(directory);
            process = builder.start();

            String thread = "thread " + Thread.currentThread().getId();
            System.out.println(thread + " : " + format);

            readStream(process.getInputStream(), output);

            return process.waitFor();

        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1001;
        } catch (IOException e) {
            e.printStackTrace();
            return -1002;
        } finally {
            if (process != null) {
                process.destroy(); // Close process streams                
            }
        }
    }

    public static String executeMultiline(String... lines) {
        Process process = null;

        String out = "";
        try {

            //            ProcessBuilder builder = new ProcessBuilder(lines[0]);
            //            builder.redirectErrorStream(true);
            //            process = builder.start();
            //            builder.

            String thread = "thread " + Thread.currentThread().getId();
            System.out.println(thread + " : " + lines[0]);
            process = Runtime.getRuntime().exec(lines[0]);

            for (int i = 1; i < lines.length; i++) {
                String line = lines[i] + NL;
                System.out.print(">" + line);
                process.getOutputStream().write(line.getBytes());
                process.getOutputStream().flush();
            }
            String err = null;
            err = readStream(process.getErrorStream());

            if (err.length() > 0) {
                System.err.println(thread + " : error : " + err);
            }
            if (process.getInputStream().available() >= 0) {
                out = readStream(process.getInputStream());
            }

            if (out.length() > 0) {
                System.out.println(thread + " : out : " + out);
            }

            int exitVal = process.waitFor();

            if (exitVal != 0) {
                throw new IllegalStateException("Error [" + exitVal + "] " + err);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy(); // Close process streams                
            }
        }

        return out;
    }

    public static String execute(String command, String... args) {
        return executeMultiline(join(command, args));
    }

    public static void cp(String... args) {
        execute("cp", args);
    }

    public static void mv(String... args) {
        execute("mv", args);
    }

    public static void rm(String... args) {
        execute("rm", args);
    }

    public static void main(String[] args) {
        /*
        boolean retry = false;
        
        int retryCount = 0;
        do {
            String output = executeMultiline("ftp -n -v ftp.kupivip.ru",
                    "user exchange exc1010=Aexc",
                    "binary",
                    "put R:/projects/pull.bat /alba/pull.bat",
                    "quit");
            retry = output.contains("425 Unable to build data connection") && retryCount++ < 5;
            if (retry) {
                System.out.println("Failed to upload. Retrying to upload...");
            }
        } while (retry);
        */
        //        execute("cp llf dsfdf");

        System.out.println(execute(new File("R:\\my-projects\\tracker"), "R:\\maven\\bin\\mvn.bat"));
    }

    private static String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        readStream(is, sb);
        return sb.toString();
    }

    private static void readStream(InputStream is, StringBuilder sb) throws IOException {
        byte[] charArray = new byte[1024];
        int r = 0;
        while ((r = is.read(charArray)) > 0) {
            sb.append(new String(charArray, 0, r - 1));
        }
    }
}
