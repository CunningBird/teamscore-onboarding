package ru.teamscore.onboarding.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "CONTRACT", indexes = {
        @Index(name = "IDX_CONTRACT_CUSTOMER", columnList = "CUSTOMER_ID"),
        @Index(name = "IDX_CONTRACT_PERFORMER", columnList = "PERFORMER_ID"),
        @Index(name = "IDX_CONTRACT_CUSTOMER_SIGNER", columnList = "CUSTOMER_SIGNER_ID"),
        @Index(name = "IDX_CONTRACT_PERFORMER_SIGNER", columnList = "PERFORMER_SIGNER_ID")
})
@Entity
public class Contract {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private Organization customer;

    @JoinColumn(name = "PERFORMER_ID", nullable = false)
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

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_FROM", nullable = false)
    private Date dateFrom;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_TO", nullable = false)
    private Date dateTo;

    @NotNull
    @Column(name = "AMOUNT", nullable = false)
    private Double amount;

    @NotNull
    @Column(name = "VAT", nullable = false)
    private Double vat;

    @NotNull
    @Column(name = "TOTAL_AMOUNT", nullable = false)
    private Double totalAmount;

    @JoinColumn(name = "CUSTOMER_SIGNER_ID")
    @OneToOne(fetch = FetchType.LAZY)
    private User customerSigner;

    @JoinColumn(name = "PERFORMER_SIGNER_ID")
    @OneToOne(fetch = FetchType.LAZY)
    private User performerSigner;

    @Column(name = "STATUS", nullable = false)
    @NotNull
    private String status;

    @Composition
    @OneToMany(mappedBy = "contract")
    private List<Stage> stages;

    public void setStatus(ContractStatus status) {
        this.status = status == null ? null : status.getId();
    }

    public ContractStatus getStatus() {
        return status == null ? null : ContractStatus.fromId(status);
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

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public List<Stage> getStages() {
        return stages;
    }

    public void setStages(List<Stage> stages) {
        this.stages = stages;
    }

    public User getPerformerSigner() {
        return performerSigner;
    }

    public void setPerformerSigner(User performerSigner) {
        this.performerSigner = performerSigner;
    }

    public User getCustomerSigner() {
        return customerSigner;
    }

    public void setCustomerSigner(User customerSigner) {
        this.customerSigner = customerSigner;
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