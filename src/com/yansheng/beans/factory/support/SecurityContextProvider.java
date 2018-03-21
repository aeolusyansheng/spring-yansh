package com.yansheng.beans.factory.support;

import java.security.AccessControlContext;

public interface SecurityContextProvider {

    AccessControlContext getAccessControlContext();
}
