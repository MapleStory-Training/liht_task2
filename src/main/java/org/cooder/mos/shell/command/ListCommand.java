/*
 * This file is part of MOS <p> Copyright (c) 2021 by cooder.org <p> For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
package org.cooder.mos.shell.command;

import java.io.IOException;

import org.cooder.mos.Utils;
import org.cooder.mos.api.MosFile;

import picocli.CommandLine.Command;
import picocli.CommandLine.Help.TextTable;

@Command(name = "ls", aliases = {"ll"}, description = "列出给与路径下的文件或文件夹")
public class ListCommand extends MosCommand {
    @Override
    public int runCommand() {
        TextTable textTable = forColumnWidths(10, 14, 15);

        String path = shell.currentPath();
        MosFile file = new MosFile(path);
        MosFile[] files = file.listFiles();
        for (MosFile f : files) {
            String size = String.valueOf(f.length());
            String time = Utils.time2String(f.lastModified());
            String name = f.getName() + (f.isDir() ? "/" : "");
            textTable.addRowValues(size, time, name);
        }
        try {
            // 清掉最后一个换行符
            String msg = textTable.toString();
            if (msg.length() > 0) {
                msg = msg.substring(0, msg.lastIndexOf('\n'));
            }
            // 写内容
            out.write(msg.replaceAll("\n", "\r\n").getBytes());
            // 如果当前的流是cli流才需要换行，否则不需要
            if (out == shell.out) {
                Utils.writeNewLineNotFlush(shell.out);
            }
        } catch (IOException e) {
            Utils.printlnErrorMsg(err, e.getMessage());
        }
        return 0;
    }
}
