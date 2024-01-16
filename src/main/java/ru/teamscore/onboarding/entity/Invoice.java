package ru.teamscore.onboarding.entity;

import io.jmix.core.DeletePolicy;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDeleteInverse;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.UUID;

@JmixEntity
@Table(name = "INVOICE", indexes = {
        @Index(name = "IDX_INVOICE_STAGE", columnList = "STAGE_ID")
})
@Entity
public class Invoice {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "INVOICE_NUMBER", nullable = false)
    @NotNull
    private String number;

    @Column(name = "INVOICE_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date date;

    @NotNull
    @Column(name = "AMOUNT", nullable = false)
    private Double amount;

    @NotNull
    @Column(name = "VAT", nullable = false)
    private Double vat;

    @Column(name = "TOTAL_AMOUNT")
    private Double totalAmount;

    @InstanceName
    @Column(name = "DESCRIPTION")
    private String description;

    @OnDeleteInverse(DeletePolicy.DENY)
    @JoinColumn(name = "STAGE_ID")
    @OneToOne(fetch = FetchType.LAZY)
    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public Double getVat() {
        return vat;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}