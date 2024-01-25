package ru.teamscore.onboarding.entity;

import io.jmix.core.DeletePolicy;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDeleteInverse;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.webdav.entity.WebdavDocument;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "SERVICE_COMPLETION_CERTIFICATE", indexes = {
        @Index(name = "IDX_SERVICE_COMPLETION_CERTIFICATE_STAGE", columnList = "STAGE_ID")
})
@Entity
public class ServiceCompletionCertificate {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @NotNull
    @Column(name = "CERTIFICATE_NUMBER", nullable = false)
    private String number;

    @Column(name = "CERIFICATE_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date date;

    @Column(name = "AMOUNT", nullable = false)
    @NotNull
    private Double amount;

    @Column(name = "VAT", nullable = false)
    @NotNull
    private Double vat;

    @Column(name = "TOTAL_AMOUNT", nullable = false)
    @NotNull
    private Double totalAmount;

    @InstanceName
    @Column(name = "DESCRIPTION")
    private String description;

    @OnDeleteInverse(DeletePolicy.DENY)
    @JoinColumn(name = "STAGE_ID")
    @OneToOne(fetch = FetchType.LAZY)
    private Stage stage;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @JoinTable(name = "SERVICE_COMPLETION_CERTIFICATE_FILES",
            joinColumns = @JoinColumn(name = "SERVICE_COMPLETION_CERTIFICATE_ID"),
            inverseJoinColumns = @JoinColumn(name = "WEBDAV_DOCUMENT_ID"))
    @ManyToMany
    private List<WebdavDocument> files;

    public List<WebdavDocument> getFiles() {
        return files;
    }

    public void setFiles(List<WebdavDocument> files) {
        this.files = files;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getVat() {
        return vat;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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