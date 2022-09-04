package com.cloudtravel.common.smencrypt;

/**
 * @description: 加密分组填充模式,
 * 分组填充方式: 加密数据时用来填充数据的一种模式.PKCS7Padding/PKCS5Padding/ZeroPadding
 *    block_size:块大小 , 进行加解密时 , 为避免明文长度过长导致一次加密的数据量太大. 会进行分块处理.
 *               而分块的时候则是参照block_size进行分.大多数分块大小默认都是64bits[8个字节] .
 *               如果明文的大小不是分块的整数倍,就需要在末尾进行填充.
 *    PKCS5Padding:可以理解为为PKCS7的子集 , 分块大小标准为8个字节.那么当最后一块的数据长度为n[n<8]时,
 *               则需要填充8-n个差值数据. 例:最后一块数据有5个字节,则需要填充3个0x03 .
 *               若明文数据为分块大小的整数倍,当使用PKCS5填充时 , 同样需要填充一组[8个]0x08
 *    PKCS7Padding:原理和PKCS5Padding一样,只是分块长度不一样 , 它的分块大小可以在1~255bytes之间.
 *    ZeroPadding : 数据长度不对齐时用0填充 . 否则不填充
 * 解密时: 由于最后一个字节肯定为填充数据的长度 , 所以在解密时可以准确删除填充的数据 . 而使用ZeroPadding
 *      的时候,由于最后填充的是0,没有办法区分真实数据和填充数据,所以只适合以\0结尾的字符串加解密
 *          \0: ASCII码为0,表示一个字符串结尾的标志.这是转义字符,底层处理时会视为整体的一个字符.内存中表现为8个0.
 *          0:位数字 . 内存中为32位的
 *         '0';字符.ASCII表示48.内存中为00110000
 * @author: walker
 * @DATE: 2022/9/4
 */
public enum AlgorithmPaddingModeEnums {

    /**
     * 分块标准再1~255bytes之间 .
     * 对明文按照块大小进行分组后,最后一组明文长度差N个不足块大小时.
     * 则在明文后填充n个0X0N进行补足
     */
    PKCS7_PADDING("PKCS7Padding"),

    /**
     * 可以看做为PKCS7Padding的子集.只是其块大小固定为8.
     */
    PKCS5_PADDING("PKCS5Padding"),

    /**
     * 零填充.数据不为0时填充.否则不填充 .只适合明文结尾为:\0的密文.基本不咋用
     */
    Zero_Padding("ZeroPadding");

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    AlgorithmPaddingModeEnums(String code) {
        this.code = code;
    }
}
