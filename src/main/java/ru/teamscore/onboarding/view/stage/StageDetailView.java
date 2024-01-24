package ru.teamscore.onboarding.view.stage;

import com.vaadin.flow.component.formlayout.FormLayout;
import io.jmix.flowui.component.datepicker.TypedDatePicker;
import io.jmix.flowui.component.textfield.TypedTextField;
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

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        Stage stage = getEditedEntity();
        Invoice invoice = stage.getInvoice();
        ServiceCompletionCertificate certificate = stage.getServiceCompletionCertificate();

        if (invoice != null) {
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
            formServiceCompletionCertificate.setVisible(true);
            serviceCompletionCertificateAmountField.setTypedValue(certificate.getAmount());
            serviceCompletionCertificateDateField.setTypedValue(certificate.getDate());
            serviceCompletionCertificateDescriptionField.setTypedValue(certificate.getDescription());
            serviceCompletionCertificateNumberField.setTypedValue(certificate.getNumber());
            serviceCompletionCertificateTotalAmountField.setTypedValue(certificate.getTotalAmount());
            serviceCompletionCertificateVatField.setTypedValue(certificate.getVat());
        }
    }


}