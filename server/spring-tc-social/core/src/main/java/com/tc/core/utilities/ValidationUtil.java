package com.tc.core.utilities;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {

    public static boolean isNullOrBlank(String str) {
        if (str == null || str.trim().equals("")) {
            return true;
        }
        return false;
    }

    public static boolean isNumeric(String str) {
        if (isNullOrBlank(str)) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isEmail(String str) {
        if (str == null || str.trim().length() == 0)
            return false;
        str = str.trim();
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(str.trim());
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public static boolean isPhoneNumber(String str) {
        if (str == null || str
                .trim().isBlank())
            return false;

        String patterns
                = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";
        Pattern regexPattern = Pattern.compile(patterns);
        Matcher regMatcher = regexPattern.matcher(str);
        if (regMatcher.matches()) {
            return true;
        }
        return false;
    }

    public static boolean isImage(MultipartFile file) {
        if(StringUtils.cleanPath(file.getOriginalFilename()).contains("..")){
            return false;
        }
        String contentType = file.getContentType();
        return contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("application/octet-stream");
    }

}
