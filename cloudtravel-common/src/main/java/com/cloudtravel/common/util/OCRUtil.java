package com.cloudtravel.common.util;

import com.alibaba.fastjson.JSON;
import com.cloudtravel.common.model.OCRResult;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.util.ImageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class OCRUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(OCRUtil.class);

    /** 训练库地址 */
    private static final String TESSDATA_DEFAULT = "D:\\software\\ocr\\jTessBoxEditor\\tesseract-ocr\\tessdata";

    /** 默认为中文识别 */
    private static final String LANGUAGE_DEFAULT = "chi_sim";

    private static final String ERROR_BASE = "OCR Error : ";

    public static OCRResult readImage(File file , String tessData , String language) {
        tessData = StringUtils.isBlank(tessData) ? TESSDATA_DEFAULT : tessData;
        language = StringUtils.isBlank(language) ? LANGUAGE_DEFAULT : language;
        OCRResult result = new OCRResult();
        long startTime = System.currentTimeMillis();
        try {
            BufferedImage textImage = ImageIO.read(file);
            //对图片做黑白处理,增强识别率.这里先通过截图,截取图片中需要识别的部分
            textImage = ImageHelper.convertImageToGrayscale(textImage);
            //图片锐化
//            textImage = ImageHelper.convertImageToBinary(textImage);
            //图片放大倍数 . 增强识别率.以防止图片较小时,图片中文字不可识别
            textImage = ImageHelper.getScaledInstance(textImage , textImage.getWidth() * 2 ,
                    textImage.getHeight() * 2);
            textImage = ImageHelper.convertImageToBinary(textImage);
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath(TESSDATA_DEFAULT);
            tesseract.setLanguage(language);
            tesseract.setTessVariable("user_defined_dpi", "70");
            String res = tesseract.doOCR(textImage);
            result.setData(res);
        }catch (Exception e) {
            LOGGER.error(ERROR_BASE + "OCR read Error, message = {}" , e.getMessage());
            result.setCode("-1");
            result.setErrorMessage(e.getMessage());
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info(ERROR_BASE + "OCR stop , executeTime = {} , result = {}" , endTime - startTime , result);
        return result;
    }

    public static OCRResult readImage(String filePath , String tessData , String language) {
        File file = new File(filePath);
        if(!file.exists()) {
            throw new RuntimeException(ERROR_BASE + "file is now exists ,filePath = " + filePath);
        }
        return readImage(file , tessData , language);
    }

    public static void main(String[] args) {
        OCRResult ocrResult =  OCRUtil.readImage("C:\\Users\\Administrator\\Desktop\\微信图片_20220803110305.jpg" , TESSDATA_DEFAULT, LANGUAGE_DEFAULT);
        System.out.println(JSON.toJSONString(ocrResult));
    }
}
