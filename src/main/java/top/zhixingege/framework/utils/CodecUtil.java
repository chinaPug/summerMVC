package top.zhixingege.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.URIParameter;

public final class CodecUtil {
    private static final Logger LOGGER= LoggerFactory.getLogger(CodecUtil.class);

    public static String encodeURL(String source){
        String target;
        try{
            target= URLEncoder.encode(source, StandardCharsets.UTF_8);
        }catch (Exception e){
            LOGGER.error("encode url failure",e);
            throw new RuntimeException(e);
        }
        return target;
    }

    public static String decodeURL(String source){
        String target;
        try {
            target= URLDecoder.decode(source,StandardCharsets.UTF_8);
        }catch (Exception e){
            LOGGER.error("decode url failure",e);
            throw new RuntimeException(e);
        }
        return target;
    }
}
