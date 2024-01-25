package ru.teamscore.onboarding.view.contract;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import io.jmix.appsettings.AppSettings;
import io.jmix.core.DataManager;
import io.jmix.core.metamodel.datatype.impl.DateDatatype;
import io.jmix.core.metamodel.datatype.impl.DoubleDatatype;
import io.jmix.core.metamodel.datatype.impl.StringDatatype;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.SupportsTypedValue;
import io.jmix.flowui.component.checkbox.JmixCheckbox;
import io.jmix.flowui.component.datepicker.TypedDatePicker;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.DataContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.teamscore.onboarding.entity.*;

import ru.teamscore.onboarding.view.main.MainView;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Route(value = "contracts/:id", layout = MainView.class)
@ViewController("Contract.detail")
@ViewDescriptor("contract-detail-view.xml")
@EditedEntityContainer("contractDc")
public class ContractDetailView extends StandardDetailView<Contract> {

    @Autowired
    private AppSettings appSettings;

    @ViewComponent
    private JmixCheckbox zeroVatParam;

    @ViewComponent
    private TypedTextField<Double> totalAmountField;

    @ViewComponent
    private TypedTextField<Double> vatField;

    @ViewComponent
    private TypedTextField<Double> amountField;

    @Autowired
    private DataManager dataManager;

    @Autowired
    private Notifications notifications;

    @ViewComponent
    private DataContext dataContext;

    @ViewComponent
    private CollectionPropertyContainer<Stage> stagesDc;
    @ViewComponent
    private TypedTextField<Double> stageAmountField;
    @ViewComponent
    private TypedDatePicker<Date> stageDateFromField;
    @ViewComponent
    private TypedDatePicker<Date> stageDateToField;
    @ViewComponent
    private TypedTextField<String> stageDescriptionField;
    @ViewComponent
    private TypedTextField<String> stageNameField;
    @ViewComponent
    private TypedTextField<Double> stageVatField;
    @ViewComponent
    private TypedTextField<Double> stageTotalAmountField;
    @ViewComponent
    private DataGrid<Stage> stagesDataGrid;
    @ViewComponent
    private JmixButton createInvoice;
    @ViewComponent
    private JmixButton createServiceCompletionCertificate;

    @ViewComponent
    private Div uploadDiv;

    @Subscribe
    public void onInit(final InitEvent event) {
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAutoUpload(true);
        upload.setDropAllowed(true);

        upload.addSucceededListener(e -> {
            System.out.println("Uploaded files - " + e.getFileName());
//            content = buffer.getInputStream(e.getFileName()).readAllBytes();
//            // TODO proccess file
        });

        this.uploadDiv.add(upload); // insert component to div
    }

    @Subscribe
    public void onInitEntity(InitEntityEvent<Contract> event) {
        Contract contract = event.getEntity();
        contract.setStatus(ContractStatus.NEW);
    }

    // TODO optimize
    @Subscribe("amountField")
    public void onAmountFieldTypedValueChange(final SupportsTypedValue.TypedValueChangeEvent<TypedTextField<Double>, Double> event) {
        Boolean zeroVat = zeroVatParam.getValue();
        Double amount = event.getValue();
        Double vatPercent = appSettings.load(CostSettings.class).getVat();

        Double vatValue = calculateVat(zeroVat, amount, vatPercent);
        vatField.setValue(vatValue.toString());
        totalAmountField.setValue(calculateTotalAmount(vatValue, amount).toString());
    }

    // TODO optimize
    @Subscribe("stageAmountField")
    public void onStageAmountFieldTypedValueChange(final SupportsTypedValue.TypedValueChangeEvent<TypedTextField<Double>, Double> event) {
        Boolean zeroVat = zeroVatParam.getValue();
        Double amount = event.getValue();
        Double vatPercent = appSettings.load(CostSettings.class).getVat();

        Double vatValue = calculateVat(zeroVat, amount, vatPercent);
        stageVatField.setValue(vatValue.toString());
        stageTotalAmountField.setValue(calculateTotalAmount(vatValue, amount).toString());
    }

    // TODO optimize
    @Subscribe("zeroVatParam")
    public void onZeroVatComponentValueChange(final AbstractField.ComponentValueChangeEvent<JmixCheckbox, Boolean> event) {
        Boolean zeroVat = event.getValue();
        Double vatPercent = appSettings.load(CostSettings.class).getVat();

        Double contractAmount = amountField.getTypedValue();
        Double contractVatValue = calculateVat(zeroVat, contractAmount, vatPercent);
        vatField.setValue(contractVatValue.toString());
        totalAmountField.setValue(calculateTotalAmount(contractVatValue, contractAmount).toString());

        Double stageAmount = stageAmountField.getTypedValue();
        Double stageVatValue = calculateVat(zeroVat, stageAmount, vatPercent);
        stageVatField.setValue(stageVatValue.toString());
        stageTotalAmountField.setValue(calculateTotalAmount(stageVatValue, stageAmount).toString());
    }

    @Subscribe("stageAdd")
    public void onStageAddClick(final ClickEvent<JmixButton> event) {
        Stage stage = dataContext.create(Stage.class);

        stage.setContract(getEditedEntity());
        stage.setAmount(stageAmountField.getTypedValue());
        stage.setName(stageNameField.getTypedValue());
        stage.setDateFrom(stageDateFromField.getTypedValue());
        stage.setDateTo(stageDateToField.getTypedValue());
        stage.setVat(stageVatField.getTypedValue());
        stage.setTotalAmount(stageTotalAmountField.getTypedValue());
        stage.setDescription(stageDescriptionField.getTypedValue());

        stagesDc.getMutableItems().add(stage);

        stageAmountField.clear();
        stageNameField.clear();
        stageDateFromField.clear();
        stageDateToField.clear();
        stageVatField.clear();
        stageTotalAmountField.clear();
        stageDescriptionField.clear();
    }

    @Subscribe(target = Target.DATA_CONTEXT)
    public void onPreSave(final DataContext.PreSaveEvent event) {
        Boolean zeroVat = zeroVatParam.getValue();
        Double amount = amountField.getTypedValue();
        Double vatPercent = appSettings.load(CostSettings.class).getVat();

        Contract entity = getEditedEntity();
        Double vatValue = calculateVat(zeroVat, amount, vatPercent);
        entity.setVat(vatValue);
        entity.setTotalAmount(calculateTotalAmount(vatValue, amount));

        if (entity.getStages() == null || entity.getStages().isEmpty()) {
            Stage stage = dataContext.create(Stage.class);
            stage.setContract(entity);
            stage.setAmount(entity.getAmount());
            stage.setName("Основные работы");
            stage.setDateFrom(entity.getDateFrom());
            stage.setDateTo(entity.getDateTo());
            stage.setVat(entity.getVat());
            stage.setTotalAmount(entity.getTotalAmount());
            stage.setDescription("Начало и окончание");

            stagesDc.getMutableItems().add(stage);
        }
    }

    // TODO only when selected
    @Subscribe("createInvoice")
    public void onCreateInvoiceClick(final ClickEvent<JmixButton> event) {
        Stage stage = stagesDataGrid.getSingleSelectedItem();

        assert stage != null;
        assert stage.getContract() != null;
        assert stage.getInvoice() == null;

        Invoice invoice = dataContext.create(Invoice.class);
        invoice.setNumber(stage.getContract().getNumber());
        invoice.setDate(new Date());
        invoice.setStage(stage);
        invoice.setAmount(stage.getAmount());
        invoice.setVat(stage.getVat());
        invoice.setTotalAmount(stage.getTotalAmount());
        invoice.setDescription(stage.getDescription());

        stage.setInvoice(invoice);
        // TODO confirm saving
    }

    @Subscribe("createServiceCompletionCertificate")
    public void onCreateServiceCompletionCertificateClick(final ClickEvent<JmixButton> event) {
        Stage stage = stagesDataGrid.getSingleSelectedItem();

        assert stage != null;
        assert stage.getContract() != null;
        assert stage.getServiceCompletionCertificate() == null;

        ServiceCompletionCertificate certificate = dataContext.create(ServiceCompletionCertificate.class);
        certificate.setStage(stage);
        certificate.setNumber(stage.getContract().getNumber());
        certificate.setDate(new Date());
        certificate.setStage(stage);
        certificate.setAmount(stage.getAmount());
        certificate.setVat(stage.getVat());
        certificate.setTotalAmount(stage.getTotalAmount());
        certificate.setDescription(stage.getDescription());

        stage.setServiceCompletionCertificate(certificate);
        // TODO confirm saving
    }

    private Double calculateVat(Boolean zeroVat, Double amount, Double vatPercent) {
        return (zeroVat) ? 0.0 : amount * vatPercent;
    }

    private Double calculateTotalAmount(Double vatValue, Double amount) {
        return vatValue + amount;
    }
}