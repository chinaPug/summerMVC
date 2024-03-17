package top.zhixingege.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public final class StreamUtil {
    private static final Logger LOGGER= LoggerFactory.getLogger(StreamUtil.class);
    public static String getString(InputStream in){
        StringBuilder sb=new StringBuilder();
        try {
            BufferedReader reader=new BufferedReader(new InputStreamReader(in));
            String line;
            while (Objects.isNull(line = reader.readLine())){
                sb.append(line);
            }
        } catch (Exception e) {
            LOGGER.error("get String failure",e);
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
