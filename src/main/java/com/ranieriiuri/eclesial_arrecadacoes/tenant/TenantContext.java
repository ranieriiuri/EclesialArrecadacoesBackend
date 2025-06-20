package com.ranieriiuri.eclesial_arrecadacoes.tenant;

import java.util.UUID;

public class TenantContext {

    private static final ThreadLocal<UUID> currentTenant = new ThreadLocal<>();

    // Define o tenant atual (geralmente o ID da igreja)
    public static void setTenantId(UUID tenantId) {
        currentTenant.set(tenantId);
    }

    // Obtém o tenant atual
    public static UUID getTenantId() {
        return currentTenant.get();
    }

    // Limpa o tenant atual (boa prática ao final da requisição)
    public static void clear() {
        currentTenant.remove();
    }

    // Alias para manter compatibilidade com o resto do código
    public static UUID getCurrentTenant() {
        return getTenantId();
    }

    public static void setCurrentTenant(UUID uuid) {
        setTenantId(uuid);
    }
}
