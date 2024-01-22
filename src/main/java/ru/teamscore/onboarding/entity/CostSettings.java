package ru.teamscore.onboarding.entity;

import io.jmix.appsettings.defaults.AppSettingsDefault;
import io.jmix.appsettings.entity.AppSettingsEntity;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@JmixEntity
@Table(name = "COST_SETTINGS")
@Entity
public class CostSettings extends AppSettingsEntity {

    @AppSettingsDefault("0.1")
    @Column(name = "VAT")
    private Double vat;

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public Double getVat() {
        return vat;
    }


}