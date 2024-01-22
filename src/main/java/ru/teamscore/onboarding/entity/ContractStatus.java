package ru.teamscore.onboarding.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum ContractStatus implements EnumClass<String> {

    NEW("Новый"),
    PENDING("На согласовании"),
    ACTIVE("Активен"),
    COMPLETED("Завершён"),
    CANCELED("Отменён");

    private final String id;

    ContractStatus(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ContractStatus fromId(String id) {
        for (ContractStatus at : ContractStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}