package com.y3r9.c47.dog.script.ntaoutoforder;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.msgpack.MessagePack;
import org.msgpack.unpacker.Unpacker;

/**
 * The class Main.
 *
 * @version 1.0
 */
public final class Main {

    public static void main(String[] args) throws Exception {
        FileSearcher fs = new RecursiveFileSearcher(Paths.get("E:\\nta"), "*.nta.pack.sz");
        List<Path> ntaFiles = fs.search();

        String lastTs = null;
        for (Path ntaFile : ntaFiles) {
            System.out.println("Processing " + ntaFile.toString());
            FileInputStream fis = new FileInputStream(ntaFile.toFile());
            SzInputStreamWrapper szIs = new SzInputStreamWrapper(fis);

            final MessagePack msgpack = new MessagePack();
            lastTs = null;
            int line = 1;
            while (true) {
                try {
                    final Unpacker unpacker = msgpack.createUnpacker(szIs);
                    String body = MsgPack.unpackBody(unpacker);
                    int index = body.indexOf('\t');
                    String ts = body.substring(0, index);
                    if (lastTs == null) {
                        lastTs = ts;
                    } else {
                        if (ts.compareTo(lastTs) < 0) {
                            System.err.println(String.format("Out of order at line:%d in file:%s", line, ntaFile.toString()));
                        } else {
                            lastTs = ts;
                        }
                    }
                    line++;
                } catch (EOFException e) {
                    break;
                }
            }

        }



    }
}
