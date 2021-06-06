/*
 * This file is part of MOS <p> Copyright (c) 2021 by cooder.org <p> For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
package org.cooder.mos;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.cooder.mos.fs.IFileSystem;

public class Utils {

    public static void close(Closeable res) {
        try {
            if (res != null) {
                res.close();
            }
        } catch (IOException e) {
            // ignore
        }
    }

    public static void printMsgNotFlush(OutputStream outputStream, String msg) {
        try {
            outputStream.write(msg.getBytes());
            // outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeNewLineNotFlush(OutputStream out) {
        try {
            out.write("\r\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printlnErrorMsg(OutputStream err, String msg) {
        try {
            err.write(msg.getBytes());
            writeNewLineNotFlush(err);
            err.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printlnError(OutputStream outputStream, Throwable error) {
        if (outputStream instanceof PrintStream) {
            error.printStackTrace((PrintStream)outputStream);
            return;
        }
        error.printStackTrace(new PrintStream(outputStream));
    }

    public static void copyStreamNoCloseOut(InputStreamReader reader, OutputStream out) throws IOException {
        try {
            int v = 0;
            PrintStream outputStream;
            if (out instanceof PrintStream) {
                outputStream = (PrintStream)out;
            } else {
                outputStream = new PrintStream(out);
            }
            while ((v = reader.read()) != -1) {
                outputStream.print((char)v);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            // out.flush();
            close(reader);
        }
    }

    public static String[] normalizePath(String path) {
        List<String> ret = new ArrayList<>();
        String[] arr = path.split(IFileSystem.separator + "");
        for (String name : arr) {
            if (name.length() > 0) {
                ret.add(name);
            }
        }
        return ret.toArray(new String[0]);
    }

    public static String[] parseArgs(String text) {
        char[] src = text.toCharArray();

        List<String> list = new ArrayList<String>();

        StringBuilder sb = new StringBuilder();
        boolean quote = false, slash = false;
        for (int i = 0; i < src.length; i++) {
            char c = src[i];
            if (slash) {
                if (c == 'n') {
                    sb.append('\n');
                } else if (c == 't') {
                    sb.append('\t');
                } else {
                    sb.append(c);
                }
                slash = false;
                continue;
            } else if (c == '\\') {
                slash = true;
                continue;
            } else if (quote) {
                if (c != '"') {
                    sb.append(c);
                } else {
                    list.add(sb.toString());
                    sb = new StringBuilder();
                    quote = false;
                }
                continue;
            } else if (c == '"') {
                if (sb.length() == 0) {
                    quote = true;
                    continue;
                }
            }

            if (c == '>') {
                if (sb.length() != 0) {
                    list.add(sb.toString());
                    sb = new StringBuilder();
                }

                if (i + 1 < src.length && src[i + 1] == '>') {
                    list.add(">>");
                    i = i + 1;
                } else {
                    list.add(">");
                }
                continue;
            }

            if (!Character.isSpaceChar(c)) {
                sb.append(c);
            } else {
                if (sb.length() > 0) {
                    list.add(sb.toString());
                    sb = new StringBuilder();
                }
            }
        }

        if (sb.length() > 0) {
            list.add(sb.toString());
        }

        return list.toArray(new String[0]);
    }

    public static String time2String(long mills) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(new Date(mills));
    }
}
