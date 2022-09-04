package com.cloudtravel.common.smencrypt;

/**
 * @description: 分组加密模式枚举
 * 分组:密码加解密模式:大类有:分组密码和流密码两种.
 *      分组密码[BlockCipher]:每次处理特定的数据块.[block],其中一个分组的比特数就成为分组长度[block_size]
 *          例如:DES和SM4都是64bits[8字节].AES可以128/192/256几种选择.这类算法一次只加密对应块大小的明文,
 *          并生成对应块大小的密文.
 *      流密码:是对数据进行连续处理的一类密码算法 . 一般以1bits / 8bits /32bits等为单位进行加密和解密.
 *          这里的分组加密模式就是分组密码.
 * 模式:分组面只能对明文进行固定长度分组进行加密.当明文被拆分成多个分组时,就需要对分组密码进行迭代,便于将所有明文都加密.
 *     而迭代的方法就成为分组密码模式.也就是对分组密码进行加密的方式.
 * @author: walker
 * @DATE: 2022/9/4
 */
public enum AlgorithmModeEnums {

    /** ECB:[Electronic CodeBook Mode]:电子密码本模式
     *  其加密方式为:填充后的各分组各自加密后密文拼接则为输出结果.相同分组的明文加密后的结果相同.因此也称之为密码本加密.
     *  攻击时只需按固定长度密文打乱顺序,解密时的明文顺序就会变化.风险较大
     *  使用分析: 支持并行计算.简单快速 . 但因为明文的重复会在密文中体现出来 . 可攻击性高 . 需要填充 */
    ECB("ECB" , false ,"[Electronic CodeBook Mode]电子密码本模式") ,

    /**
     * CBC:[Cipher Block Chaining mode]:密码分组链接模式
     * 将前密文分组和当前明文分组通过异或处理后再进行加密的.这样就避免了ECB模式的明文分组和密文分组一一对应的风险
     * 因为它的明文分组和密文分组依次进行运算相关联 . 所以这里称之为密码链接模式
     * 第一个明文分组加密时因为没有前一个密文分组 , 会准备一个长度为分组的二进制序列代码需要的密文分组作为初始化向量
     * 解密时:则逆向处理,先将当前密文分组进行解密,再和上一个密文进行异或处理,即得到当前分组的明文.依次而行.
     * 使用分析 : 明文重复不会体现在密文中. 仅解密时支持并行计算 . 需要填充
     * */
    CBC("CBC" , true , "[Cipher Block Chaining Mode]密码分组链接模式"),

    /**
     * CFB:[Cipher FeedBack mode]:密文反馈模式
     * 区别于CBC的地方在于,初始化向量和密钥进行加密后和明文分组进行异或处理直接得到密文分组,然后密文分组再加密后和后续的明文分组进行异或处理,
     * 依次处理.相比而言,每次异或之前上一组的密文分组[或初始化向量]会先进行加密.再进行异或.
     * CFB模式中由密码算法生成的二进制序列类似于时一种流式的数据处理
     * 解密时:则逆向处理,先将上一个密文分组进行加密[注意这里依然是加密],再进行异或处理,即得到当前分组的明文.依次而行.
     * 使用分析 : 不需要填充,解密时支持并行计算 .
     * 区别于CBC的点 : 在于先对初始化向量或者前一组密文进行加密再进行异或处理
     * */
    CFB("CFB" , true , "[Cipher FeedBack Mode]密文反馈模式"),

    /**
     * OFB:[OutPut FeedBack mode]:输出反馈模式
     * 加密时: 初始化向量先进行加密,和第一个明文分组异或处理后得到当前明文分组对应的密文分组.
     * 下一个明文分组则 :再次对加密后的向量密文进行加密,再和明文分组进行异或处理得到密文分组.区别于CFB的是每次加密对象
     * 都是上一个初始化向量加密后的密文.而CFB则是对密文分组进行加密
     * 解密时:也是对初始化向量进行加密.再和密文分组进行异或处理得到明文分组 , 依次而行
     * 类似于于流密码
     * 使用分析 : 不需要填充,加密解密时可实现准备好对初始化向量的加密序列 .不支持并行
     * 区别于CFB的点在于:每次加密都是针对初始化向量加密后的密文
     * */
    OFB("OFB" , true , "[Output FeedBack Mode]输出反馈模式") ,

    /**
     *  CTR:[Counter Mode]:计数器模式
     *  每个分组对应一个逐次累加的计数器,并通过计数器进行加密生成密钥流.再和明文分组进行异或处理得到密文分组.
     *  解密时:同样是对计数器进行加密处理而不是解密
     *  类似于于流密码
     *  使用分析 : 不需要填充,持并行计算（加密、解密）
     */
    CTR("CTR" , true , "[Counter mode]计数器模式");


    private String modeName;

    private Boolean needIV;

    private String desc;


    AlgorithmModeEnums(String modeName, Boolean needIV, String desc) {
        this.modeName = modeName;
        this.needIV = needIV;
        this.desc = desc;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public Boolean getNeedIV() {
        return needIV;
    }

    public void setNeedIV(Boolean needIV) {
        this.needIV = needIV;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
