package com.wpq.tracker;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * IO工具
 *
 * @author wpq
 * @version 1.0
 */
public final class IOUtil {

    private IOUtil() {
        throw new AssertionError("cannot be instantiated");
    }

    /**
     * 静默关闭流
     *
     * @param closeables e.g. SQLiteDatabase, Cursor, InputStream.
     */
    static void closeQuietly(Closeable... closeables) {
        if (closeables == null)
            return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
//                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 按行读取内容
     */
    static String read(Reader reader) {
        BufferedReader br = null;
        //noinspection StringBufferMayBeStringBuilder
        StringBuffer buffer = new StringBuffer();
        try {
            br = new BufferedReader(reader);
            String next;
            while ((next = br.readLine()) != null) {
                buffer.append(next);
//                buffer.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            buffer.delete(0, buffer.length());
        } finally {
            closeQuietly(br, reader);
        }
        return buffer.toString().trim();
    }

    static String readFile(String filePath) throws FileNotFoundException {
        return IOUtil.read(new FileReader(filePath));
    }

    static void write(String data, String filePath) throws IOException {
        Writer out = new FileWriter(filePath);
        try {
            out.write(data);
        } finally {
            closeQuietly(out);
        }
    }
}
