package com.cloudtravel.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

import net.coobird.thumbnailator.Thumbnails;

/**
 * @Title: ImageHelper.java
 * @Package com.jarvis.base.util
 * @Description:图片处理工具类。
 * @author java11
 * @date 2022.02.27
 * @version V2.0  
 */
@SuppressWarnings("restriction")
public class ImageHelper {
	/**
	 * @描述：Base64解码并生成图片
	 * @入参：@param imgStr
	 * @入参：@param imgFile
	 * @入参：@throws IOException
	 * @出参：void
	 */
	public static void generateImage(String imgStr, String imgFile) throws IOException {
		Base64.Decoder decoder = Base64.getMimeDecoder();
		// Base64解码
		byte[] bytes;
		OutputStream out = null;
		try {
			bytes = decoder.decode(imgStr);
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {// 调整异常数据
					bytes[i] += 256;
				}
			}
			// 生成图片
			out = new FileOutputStream(imgFile);
			out.write(bytes);
			out.flush();
		} catch (IOException e) {
			throw new IOException();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * @throws IOException
	 * @描述：根据路径得到base编码后图片
	 * @入参：@param imgFilePath
	 * @入参：@return
	 * @出参：String
	 */
	public static String getImageStr(String imgFilePath) throws IOException {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		byte[] data = null;

		// 读取图片字节数组
		try {
			InputStream in = new FileInputStream(imgFilePath);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			throw new IOException();
		}

		// 对字节数组Base64编码
		Base64.Encoder encoder = Base64.getMimeEncoder();
		return encoder.encodeToString(data);// 返回Base64编码过的字节数组字符串
	}

	/**
	 * @throws IOException
	 * @描述：图片旋转
	 * @入参：@param base64In 传入的图片base64
	 * @入参：@param angle 图片旋转度数
	 * @入参：@throws Exception
	 * @出参：String 传出的图片base64
	 */
	public static String imgAngleRevolve(String base64In, int angle) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			Thumbnails.of(base64ToIo(base64In)).scale(1.0).rotate(angle).toOutputStream(os);
		} catch (IOException e) {
			throw new IOException();
		}
		byte[] bs = os.toByteArray();
		String s = Base64.getMimeEncoder().encodeToString(bs);
		return s;
	}

	/**
	 * @描述：base64转为io流
	 * @入参：@param strBase64
	 * @入参：@return
	 * @入参：@throws IOException
	 * @出参：InputStream
	 */
	public static InputStream base64ToIo(String strBase64) throws IOException {
		// 解码，然后将字节转换为文件
		byte[] bytes = Base64.getMimeDecoder().decode(strBase64); // 将字符串转换为byte数组
		return new ByteArrayInputStream(bytes);
	}
}