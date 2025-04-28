package com.sw.cmc.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * packageName    : com.sw.cmc.common.util
 * fileName       : GzipUtil
 * author         : ihw
 * date           : 2025. 4. 29.
 * description    : gzip 데이터 유틸
 */
public class GzipUtil {
    /**
     * methodName : decompressGzip
     * author : IM HYUN WOO
     * description : gzip 압축 해제
     *
     * @param compressed String
     * @return string
     * @throws IOException the io exception
     */
    public static String decompressGzip(String compressed) throws IOException {
        byte[] compressedBytes = Base64.getDecoder().decode(compressed); // (1)
        try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressedBytes));
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = gis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            return out.toString(StandardCharsets.UTF_8);
        }
    }

    /**
     * methodName : compressGzip
     * author : IM HYUN WOO
     * description : gzip 압축
     *
     * @param original String
     * @return string
     * @throws IOException the io exception
     */
    public static String compressGzip(String original) throws IOException {
        byte[] originalBytes = original.getBytes(StandardCharsets.UTF_8);

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             GZIPOutputStream gos = new GZIPOutputStream(bos)) {

            gos.write(originalBytes);
            gos.finish();

            byte[] compressedBytes = bos.toByteArray();
            return Base64.getEncoder().encodeToString(compressedBytes);
        }
    }
}
