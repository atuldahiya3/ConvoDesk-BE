package com.convodesk.backend.Security;

public class TenantContext {

    private static final ThreadLocal<Long> tenantHolder = new ThreadLocal<>();

    public static void setTenantId(Long tenantId) {
        tenantHolder.set(tenantId);
    }

    public static Long getTenantId() {
        return tenantHolder.get();
    }

    public static void clear() {
        tenantHolder.remove();
    }
}