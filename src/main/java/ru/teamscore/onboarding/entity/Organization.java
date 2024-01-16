package ru.teamscore.onboarding.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "ORGANIZATION")
@Entity
public class Organization {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @JoinTable(name = "ORGANIZATION_EMPLOYERS",
            joinColumns = @JoinColumn(name = "ORGANIZATION_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"))
    @ManyToMany
    private List<User> employers;

    @NotNull
    @InstanceName
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @Column(name = "TAX_NUMBER", nullable = false)
    private String taxNumber;

    @NotNull
    @Column(name = "REGISTRATION_NUMBER", nullable = false)
    private String registrationNumber;

    @NotNull
    @Column(name = "ESCAPE_VAT", nullable = false)
    private Double escapeVat;

    public void setEscapeVat(Double escapeVat) {
        this.escapeVat = escapeVat;
    }

    public Double getEscapeVat() {
        return escapeVat;
    }

    public List<User> getEmployers() {
        return employers;
    }

    public void setEmployers(List<User> employers) {
        this.employers = employers;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}