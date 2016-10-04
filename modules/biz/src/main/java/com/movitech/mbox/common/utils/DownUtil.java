package com.movitech.mbox.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class DownUtil {

	public static void download(String urlString, String filename,
			String savePath) throws Exception {
		// 构造URL
		URL url = new URL(urlString);
		// 打开连接
		URLConnection con = url.openConnection();
		// 设置请求超时为5s
		con.setConnectTimeout(5 * 1000);
		// 输入流
		InputStream is = con.getInputStream();

		// 1K的数据缓冲
		byte[] bs = new byte[1024];
		// 读取到的数据长度
		int len;
		// 输出的文件流
		File sf = new File(savePath);
		if (!sf.exists()) {
			sf.mkdirs();
		}
		File file = new File(savePath + File.separator + filename);
		if (file.exists())
			file.delete();// 删除原有文件
		OutputStream os = new FileOutputStream(file);
		// 开始读取
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		// 完毕，关闭所有链接
		os.close();
		is.close();
	}
	
	

	public static String download(File file, String savePath) throws Exception {
		// 输入流
		InputStream is = new FileInputStream(file);
		// 1K的数据缓冲
		byte[] bs = new byte[1024];
		// 读取到的数据长度
		int len;
		// 输出的文件流
		File sf = new File(savePath);
		if (!sf.exists()) {
			sf.mkdirs();
		}
		String filename = file.getName();
		String name = null;
		String extName = null;
		// 扩展名格式：
		if (filename.lastIndexOf(".") >= 0) {
			extName = filename.substring(filename.lastIndexOf("."));
		}
		File localFile = null;
		do {
			// 生成文件名：
			name = UUID.randomUUID().toString();
			localFile = new File(savePath+File.separator + name + extName);
		} while (localFile.exists());

		OutputStream os = new FileOutputStream(localFile);
		// 开始读取
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		// 完毕，关闭所有链接
		os.close();
		is.close();
		return localFile.getAbsolutePath();
	}

}
