package com.jwj.demo.androidapidemo.util;

import com.jwj.demo.androidapidemo.logger.LogUtil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/11/14
 * Copyright: Ctrip
 */

public class IoUtil {

    public static final int DEFAULT_BUFFER_SIZE = 32768;

    private IoUtil() {
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException var2) {
                LogUtil.e("IBU_Support", var2, "[ignore] closeSilently failed");
            }
        }
    }

    public static boolean copyStream(InputStream is, OutputStream os) throws IOException {
        return copyStream(is, os, '耀');
    }

    public static boolean copyStream(InputStream is, OutputStream os, int bufferSize) throws IOException {
        byte[] bytes = new byte[bufferSize];

        int count;
        while ((count = is.read(bytes, 0, bufferSize)) != -1) {
            os.write(bytes, 0, count);
        }

        os.flush();
        return true;
    }

    public static InputStream string2InputStream(String input) {
        byte[] bytes = input.getBytes(Charset.forName("UTF-8"));
        return new ByteArrayInputStream(bytes);
    }

    public static boolean copyStringToOutputStream(String str, OutputStream os) {
        try {
            return copyStream(string2InputStream(str), os);
        } catch (IOException var3) {
            LogUtil.e("IBU_Support", var3, "copyStringToOutputStream");
            return false;
        }
    }

    public static boolean copyStringToWriter(String str, Writer writer) {
        try {
            writer.write(str);
            writer.flush();
            return true;
        } catch (IOException var3) {
            LogUtil.e("IBU_Support", var3, "copyStringToWriter");
            return false;
        }
    }

    public static String inputStreamToString(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = null;

        while ((line = br.readLine()) != null) {
            sb.append("\n");
            sb.append(line);
        }

        return sb.toString();
    }

}
