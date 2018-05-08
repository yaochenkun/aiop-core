package org.bupt.common.util;

import java.io.File;

/**
 * Created by zlren on 2017/7/3.
 */
public class FilenameUtil {

    private static final int NOT_FOUNT = -1;
    private static final String EMPTY_STRING = "";
    private static final char SYSTEM_SEPARATOR = File.separatorChar;
    private static final char UNIX_SEPARATOR = '/';
    private static final char WINDOWS_SEPARATOR = '\\';
    private static final char OTHER_SEPARATOR;
    private static final char EXTENSION_SEPARATOR = '.';

    static {
        if (isSystemWindows()) {
            OTHER_SEPARATOR = UNIX_SEPARATOR;
        } else {
            OTHER_SEPARATOR = WINDOWS_SEPARATOR;
        }
    }

    private static boolean isSystemWindows() {
        return SYSTEM_SEPARATOR == WINDOWS_SEPARATOR;
    }

    /**
     * 处理windows中文件路径中的盘符
     *
     * @param filename 文件名
     * @return ADS offsets
     */
    private static int getAdsCriticalOffset(String filename) {
        int offset1 = filename.lastIndexOf(SYSTEM_SEPARATOR);
        int offset2 = filename.lastIndexOf(OTHER_SEPARATOR);
        if (offset1 == -1) {
            if (offset2 == -1) {
                return 0;
            }
            return offset2 + 1;
        }
        if (offset2 == -1) {
            return offset1 + 1;
        }
        return Math.max(offset1, offset2) + 1;
    }

    /**
     * 返回最后一个路径分隔符的位置
     *
     * @param filename 文件路径名
     * @return 最后一个路径分隔符的位置，如果没有返回-1
     */
    public static int indexOfLastSeparator(final String filename) {
        if (filename == null) {
            return NOT_FOUNT;
        }
        final int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
        final int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
        return Math.max(lastUnixPos, lastWindowsPos);
    }

    /**
     * 返回拓展名分隔符的位置
     *
     * @param filename 文件名
     * @return 分隔符位置，如果没有返回-1
     */
    public static int indexOfExtension(final String filename) {
        if (filename == null) {
            return NOT_FOUNT;
        }
        if (isSystemWindows()) {
            final int offset = filename.indexOf(':', getAdsCriticalOffset(filename));
            if (offset != -1) {
                throw new IllegalArgumentException("NTFS ADS separator (':') in filename is forbidden.");
            }
        }
        final int extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR);
        final int lastSeparator = indexOfLastSeparator(filename);
        return lastSeparator > extensionPos ? NOT_FOUNT : extensionPos;
    }

    /**
     * 获取文件全名
     *
     * @param filename 文件路径
     * @return 文件全名
     */
    public static String getName(final String filename) {
        if (filename == null) {
            return null;
        }
        final int index = indexOfLastSeparator(filename);
        return filename.substring(index + 1);
    }

    /**
     * 删除文件拓展名
     *
     * @param filename 文件名
     * @return 不带拓展名的文件名
     */
    public static String removeExtension(final String filename) {
        if (filename == null) {
            return null;
        }
        final int index = indexOfExtension(filename);
        if (index == NOT_FOUNT) {
            return filename;
        }
        return filename.substring(0, index);
    }

    /**
     * Java 文件操作 获取文件扩展名
     *
     * @param filename 文件路径
     * @return 拓展名，若不存在返回""
     */
    public static String getExtensionName(final String filename) {
        if (filename == null) {
            return null;
        }
        final int index = indexOfExtension(filename);
        if (index == NOT_FOUNT) {
            return EMPTY_STRING;
        }
        return filename.substring(index + 1);
    }

    /**
     * Java 文件操作 获取不带扩展名的文件名
     *
     * @param filename 文件路径
     * @return 不带拓展名的文件名
     */
    public static String getFileNameNoEx(final String filename) {
        return removeExtension(getName(filename));
    }
}
