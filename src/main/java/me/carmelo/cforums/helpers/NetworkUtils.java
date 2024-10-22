package me.carmelo.cforums.helpers;

import jakarta.servlet.http.HttpServletRequest;

public class NetworkUtils {

    public static String getClientIpAddr(HttpServletRequest request) {
        String[] headers = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "HTTP_VIA",
                "REMOTE_ADDR"
        };

        String ip = null;

        for (String header : headers) {
            ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip) && !ip.contains(":"))
                break;
        }

        if ((ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) || ip.contains(":"))
            ip = request.getRemoteAddr();

        if (ip != null && ip.contains(":"))
            ip = "127.0.0.1";

        return ip;
    }

}
