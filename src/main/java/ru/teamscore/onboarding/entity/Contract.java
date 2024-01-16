package ru.teamscore.onboarding.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.UUID;

@JmixEntity
@Table(name = "CONTRACT", indexes = {
        @Index(name = "IDX_CONTRACT_CUSTOMER", columnList = "CUSTOMER_ID"),
        @Index(name = "IDX_CONTRACT_PERFORMER", columnList = "PERFORMER_ID"),
        @Index(name = "IDX_CONTRACT_STATUS", columnList = "STATUS_ID")
})
@Entity
public class Contract {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    @Composition
    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private Organization customer;

    @JoinColumn(name = "PERFORMER_ID", nullable = false)
    @Composition
    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private Organization performer;

    @Column(name = "CONTRACT_NUMBER", nullable = false)
    @NotNull
    private String number;

    @Column(name = "SIGNED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date signedDate;

    @Column(name = "CONTRACT_TYPE", nullable = false)
    @NotNull
    private String type;

    @Column(name = "DATE_FROM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;

    @Column(name = "DATE_TO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;

    @Column(name = "AMOUNT")
    private Integer amount;

    @Column(name = "VAT")
    private Integer vat;

    @Column(name = "TOTAL_AMOUNT", nullable = false)
    @NotNull
    private Integer totalAmount;

    @JoinColumn(name = "STATUS_ID", nullable = false)
    @Composition
    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getVat() {
        return vat;
    }

    public void setVat(Integer vat) {
        this.vat = vat;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public ContractType getType() {
        return type == null ? null : ContractType.fromId(type);
    }

    public void setType(ContractType type) {
        this.type = type == null ? null : type.getId();
    }

    public Date getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(Date signedDate) {
        this.signedDate = signedDate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Organization getPerformer() {
        return performer;
    }

    public void setPerformer(Organization performer) {
        this.performer = performer;
    }

    public Organization getCustomer() {
        return customer;
    }

    public void setCustomer(Organization customer) {
        this.customer = customer;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}