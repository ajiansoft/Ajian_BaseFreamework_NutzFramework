package com.nutz.framework.utils.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UploadFile {
	/**
	 * upload方法概要说明 把文件上传到指定目录
	 * @param file 文件
	 * @param destPath 指定目标目录
	 * @return
	 */
	public static boolean upload(File file, String destPath, String fileName) {
		createDirectory(destPath);
		try {
			InputStream inputStream = new FileInputStream(file);
			FileOutputStream outputStream = new FileOutputStream(destPath
					+ fileName);
			int size = 0;
			while ((size = inputStream.read()) != -1) {
				outputStream.write(size);
			}
			outputStream.close();
			inputStream.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * createDirectory方法概要说明 创建给定路径的文件夹
	 * 
	 * @param destPath
	 */
	public static void createDirectory(String destPath) {
		// 上传实现无限级文件夹建立
		String[] pathList = destPath.split("/");
		StringBuffer tempDir = new StringBuffer(pathList[0]);
		for (int i = 1; i < pathList.length; i++) {
			tempDir.append("/");
			tempDir.append(pathList[i]);
			// 检查文件目录是否存在，如果不存在,创建目录
			java.io.File myPath = new java.io.File(tempDir.toString());
			if (!myPath.exists()) {
				myPath.mkdir();
			}
		}
	}

	/**
	 * removeFile方法概要说明 删除指定路径的文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @return
	 */
	public static boolean removeFile(String filePath) {
		// 得到该文件
		File file = new File(filePath);
		// 删除文件
		if (file.exists()) {
			if (file.isDirectory()) {
				if (file.listFiles().length == 0) {
					file.delete();
					return true;
				} else {
					// 得到文件夹下文件数组
					File fileList[] = file.listFiles();
					int i = file.listFiles().length;
					for (int j = 0; j < i; j++) {
						// 若文件夹下面是文件夹则递归
						if (fileList[j].isDirectory()) {
							removeFile(fileList[j].getAbsolutePath());
						}
						fileList[j].delete();
					}
					file.delete();
					return true;
				}
			} else {
				file.delete();
				return true;
			}
		}
		return false;
	}

	/**
	 * uploadZipAndUnZip方法概要说明 上传ZIP文件并解压ZIP文件,删除源文件(自己指定)
	 * 
	 * @param file
	 *            上传的文件
	 * @param destPath
	 *            上传目标路径
	 * @param fileName
	 *            文件名称
	 * @param destZipPath
	 *            解压文件路径
	 * @param flage
	 *            是否删除源文件
	 * @return
	 */
	public static boolean uploadZipAndUnZip(File file, String destPath,
			String fileName, String destZipPath, boolean flage) {
		boolean isOk = false;
		// 检查文件目录是否存在，如果不存在,创建目录
		java.io.File myPath = new java.io.File(destPath);
		if (!myPath.exists()) {
			// 创建目标目录
			myPath.mkdir();
		}
		// 输入输出流上传文件
		try {
			// 读取文件
			InputStream inputStream = new FileInputStream(file);
			// 写入到指定目录的指定文件
			FileOutputStream outputStream = new FileOutputStream(destPath
					+ fileName);
			int size;
			while ((size = inputStream.read()) != -1) {
				// 写入
				outputStream.write(size);
			}
			// 关闭输入输出流
			outputStream.close();
			inputStream.close();
			// 解压文件
			if (unZipFile(destPath + fileName, destZipPath)) {
				isOk = true;
			} else {
				isOk = false;
			}
			// 删除源文件
			if (flage == true) {
				// 删除
				File f = new File(destPath + fileName);
				if (f.exists()) {
					f.delete();
				}
			}
		} catch (IOException e) {
			isOk = false;
		}
		return isOk;
	}

	/**
	 * unZipFile方法概要说明 解压ZIP文件到指定文件夹，不删除源ZIP文件
	 * 
	 * @param zipFilePath
	 *            源zip文件路径
	 * @param destPath
	 *            目标路径
	 * @return
	 * @throws IOException
	 */
	public static boolean unZipFile(String zipFilePath, String destPath)
			throws IOException {
		try {
			ZipFile zip = new ZipFile(zipFilePath);
			Enumeration<?> en = zip.entries();
			ZipEntry entry = null;
			byte[] buffer = new byte[8192];
			int length = -1;
			InputStream inputStream = null;
			BufferedOutputStream bos = null;
			File file = null;
			while (en.hasMoreElements()) {
				entry = (ZipEntry) en.nextElement();
				if (entry.isDirectory()) {
					continue;
				}
				inputStream = zip.getInputStream(entry);
				file = new File(destPath, entry.getName());
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				bos = new BufferedOutputStream(new FileOutputStream(file));
				while (true) {
					length = inputStream.read(buffer);
					if (length == -1) {
						break;
					}
					bos.write(buffer, 0, length);
				}
				bos.close();
				inputStream.close();
			}
			zip.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
