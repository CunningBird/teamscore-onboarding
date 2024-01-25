package ru.teamscore.onboarding.view.stage;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import io.jmix.flowui.component.datepicker.TypedDatePicker;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.download.Downloader;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.reports.runner.ReportRunner;
import io.jmix.reports.yarg.reporting.ReportOutputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import ru.teamscore.onboarding.entity.*;

import ru.teamscore.onboarding.view.main.MainView;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

import java.util.Date;

@Route(value = "stages/:id", layout = MainView.class)
@ViewController("Stage.detail")
@ViewDescriptor("stage-detail-view.xml")
@EditedEntityContainer("stageDc")
public class StageDetailView extends StandardDetailView<Stage> {

    @ViewComponent
    private TypedTextField<Double> invoiceAmountField;
    @ViewComponent
    private TypedDatePicker<Date> invoiceDateField;
    @ViewComponent
    private TypedTextField<String> invoiceDescriptionField;
    @ViewComponent
    private TypedTextField<String> invoiceNumberField;
    @ViewComponent
    private TypedTextField<Double> invoiceTotalAmountField;
    @ViewComponent
    private TypedTextField<Double> invoiceVatField;
    @ViewComponent
    private TypedTextField<Double> serviceCompletionCertificateAmountField;
    @ViewComponent
    private TypedDatePicker<Date> serviceCompletionCertificateDateField;
    @ViewComponent
    private TypedTextField<String> serviceCompletionCertificateDescriptionField;
    @ViewComponent
    private TypedTextField<String> serviceCompletionCertificateNumberField;
    @ViewComponent
    private TypedTextField<Double> serviceCompletionCertificateTotalAmountField;
    @ViewComponent
    private TypedTextField<Double> serviceCompletionCertificateVatField;
    @ViewComponent
    private FormLayout formInvoice;
    @ViewComponent
    private FormLayout formServiceCompletionCertificate;

    @ViewComponent
    private Div invoiceUploadDiv;

    @ViewComponent
    private Div serviceCompletionCertificateUploadDiv;

    @ViewComponent
    private JmixButton exportInvoice;

    @ViewComponent
    private JmixButton exportServiceCompletionCertificate;

    @Autowired
    private ReportRunner reportRunner;

    @Autowired
    private Downloader downloader;

    @Subscribe
    public void onInit(final InitEvent event) {
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload1 = new Upload(buffer);
        upload1.setAutoUpload(true);
        upload1.setDropAllowed(true);

        upload1.addSucceededListener(e -> {
            System.out.println("Uploaded files - " + e.getFileName());
//            content = buffer.getInputStream(e.getFileName()).readAllBytes();
//            // TODO proccess file
        });

        Upload upload2 = new Upload(buffer);
        upload2.setAutoUpload(true);
        upload2.setDropAllowed(true);

        upload2.addSucceededListener(e -> {
            System.out.println("Uploaded files - " + e.getFileName());
//            content = buffer.getInputStream(e.getFileName()).readAllBytes();
//            // TODO proccess file
        });

        invoiceUploadDiv.add(upload1); // insert component to div
        serviceCompletionCertificateUploadDiv.add(upload2);
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        Stage stage = getEditedEntity();
        Invoice invoice = stage.getInvoice();
        ServiceCompletionCertificate certificate = stage.getServiceCompletionCertificate();

        if (invoice != null) {
            exportInvoice.setEnabled(true);
            formInvoice.setVisible(true);
            invoiceNumberField.setTypedValue(invoice.getNumber());
            invoiceAmountField.setTypedValue(invoice.getAmount());
            invoiceDateField.setTypedValue(invoice.getDate());
            invoiceDescriptionField.setTypedValue(invoice.getDescription());
            invoiceNumberField.setTypedValue(invoice.getNumber());
            invoiceTotalAmountField.setTypedValue(invoice.getTotalAmount());
            invoiceVatField.setTypedValue(invoice.getTotalAmount());
        }

        if (certificate != null) {
            exportServiceCompletionCertificate.setEnabled(true);
            formServiceCompletionCertificate.setVisible(true);
            serviceCompletionCertificateAmountField.setTypedValue(certificate.getAmount());
            serviceCompletionCertificateDateField.setTypedValue(certificate.getDate());
            serviceCompletionCertificateDescriptionField.setTypedValue(certificate.getDescription());
            serviceCompletionCertificateNumberField.setTypedValue(certificate.getNumber());
            serviceCompletionCertificateTotalAmountField.setTypedValue(certificate.getTotalAmount());
            serviceCompletionCertificateVatField.setTypedValue(certificate.getVat());
        }
    }

    @Subscribe("exportInvoice")
    public void onExportInvoiceClick(final ClickEvent<JmixButton> event) {
        Invoice invoice = getEditedEntity().getInvoice();
        ReportOutputDocument document = reportRunner.byReportCode("INVOICE")
                .addParam("entity", invoice)
                .withTemplateCode("DEFAULT")
                .run();

        System.out.println("Created report - " + document.getDocumentName());

        downloader.download(document.getContent(), document.getDocumentName());
    }

    @Subscribe("exportServiceCompletionCertificate")
    public void onExportServiceCompletionCertificateClick(final ClickEvent<JmixButton> event) {
        ServiceCompletionCertificate certificate = getEditedEntity().getServiceCompletionCertificate();
        ReportOutputDocument document = reportRunner.byReportCode("SERVICE_COMPLETION_CERTIFICATE")
                .addParam("entity", certificate)
                .withTemplateCode("DEFAULT")
                .run();

        System.out.println("Created report - " + document.getDocumentName());

        downloader.download(document.getContent(), document.getDocumentName());
    }
}