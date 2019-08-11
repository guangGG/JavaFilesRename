package gapp.season.jar.filerename;

import java.io.File;

public class FileUtil {
	public static boolean isEmpty(String str) {
		return str == null || str.length() <= 0;
	}

	/**
	 * 替换字符串中的指定字符
	 */
	public static String replaceStr(String text, char c, String newStr) {
		if (text == null || newStr == null) {
			return text;
		}
		char[] array = text.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			if (c == array[i]) {
				sb.append(newStr);
			} else {
				sb.append(array[i]);
			}
		}
		return sb.toString();
	}

	/**
	 * 替换字符串中的指定字符串
	 */
	public static String replaceStr(String text, String str, String newStr) {
		if (text == null || str == null || newStr == null) {
			return text;
		}
		int l1 = text.length();
		int l2 = str.length();
		if (l1 < l2) {
			return text;
		} else {
			StringBuilder sb = new StringBuilder();
			int i = 0;
			for (; i < l1 - l2 + 1; i++) {
				String subStr = text.substring(i, i + l2);
				if (str.equals(subStr)) {
					sb.append(newStr);
					i = i + l2 - 1;
				} else {
					sb.append(text.charAt(i));
				}
			}
			for (; i < l1; i++) {
				sb.append(text.charAt(i));
			}
			return sb.toString();
		}
	}

	public static String getFileName(String strFilePath) {
		if (isEmpty(strFilePath))
			return "";

		File f = new File(strFilePath);
		return f.getName();
	}

	public static String getParentPath(String strFilePath) {
		if (isEmpty(strFilePath))
			return "";

		File f = new File(strFilePath);
		return f.getParent().toString();
	}

	public static String getFileName(String strFilePath, char spliterChar) {
		if (isEmpty(strFilePath))
			return "";

		String strFileName = strFilePath;
		int lastIndex = strFilePath.lastIndexOf(spliterChar);
		if (-1 != lastIndex) {
			strFileName = strFilePath.substring(lastIndex + 1);
		}
		return strFileName;
	}

	public static String getFileExtendName(String fileName) {
		if (isEmpty(fileName)) {
			return null;
		}
		int index = fileName.lastIndexOf('.');
		if (index < 0) {
			return "unknown";
		} else {
			return fileName.substring(index + 1);
		}
	}

	/**
	 * 获取后缀名
	 */
	public static String getExtName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return "";
	}

	public static String getFileNameWithoutExtName(String pathName) {
		String fileName = getFileName(pathName);
		if (fileName != null && fileName.length() > 0) {
			int dot = fileName.lastIndexOf('.');
			if ((dot > 0) && (dot < (fileName.length() - 1))) {
				return fileName.substring(0, dot);
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	/**
	 * 从路径中提取文件名
	 */
	public static String extractFileNameFromPath(String filePath) {
		int start = filePath.lastIndexOf("/");
		if (-1 == start) {
			return filePath;
		} else {
			return filePath.substring(start + 1);
		}
	}

	/**
	 * 把多余的"/"去掉(系统File类的方法)
	 * (如："///a//b/c/"-->"/a/b/c")
	 */
	public static String fixSlashes(String origPath) {
		if (origPath == null) {
			return null;
		}
		// Remove duplicate adjacent slashes.
		boolean lastWasSlash = false;
		char[] newPath = origPath.toCharArray();
		int length = newPath.length;
		int newLength = 0;
		for (int i = 0; i < length; ++i) {
			char ch = newPath[i];
			if (ch == '/') {
				if (!lastWasSlash) {
					newPath[newLength++] = '/';
					lastWasSlash = true;
				}
			} else {
				newPath[newLength++] = ch;
				lastWasSlash = false;
			}
		}
		// Remove any trailing slash (unless this is the root of the file system).
		if (lastWasSlash && newLength > 1) {
			newLength--;
		}
		// Reuse the original string if possible.
		return (newLength != length) ? new String(newPath, 0, newLength) : origPath;
	}
}
