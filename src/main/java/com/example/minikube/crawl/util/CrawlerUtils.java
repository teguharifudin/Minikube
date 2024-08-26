package com.example.minikube.crawl.util;

import org.apache.commons.lang3.StringUtils;

public class CrawlerUtils {
    public static String removeLastSlash(String input) {
        if (StringUtils.endsWith(input, "/")) {
            return input.substring(0, input.length() - 1);
        }
        return input;
    }
}