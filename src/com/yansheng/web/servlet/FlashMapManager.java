package com.yansheng.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface FlashMapManager {
    FlashMap retrieveAndUpdate(HttpServletRequest request, HttpServletResponse response);
}
