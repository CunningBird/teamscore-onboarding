package ru.teamscore.onboarding.view.contract;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.formlayout.FormLayout;
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
import ru.teamscore.onboarding.entity.Contract;

import ru.teamscore.onboarding.entity.ContractStatus;
import ru.teamscore.onboarding.entity.CostSettings;
import ru.teamscore.onboarding.entity.Stage;
import ru.teamscore.onboarding.view.main.MainView;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

import java.util.Collection;
import java.util.Date;
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

    @Subscribe
    public void onInitEntity(InitEntityEvent<Contract> event) {
        Contract contract = event.getEntity();
        contract.setStatus(ContractStatus.NEW);
    }

    @Subscribe("amountField")
    public void onAmountFieldTypedValueChange(final SupportsTypedValue.TypedValueChangeEvent<TypedTextField<Double>, Double> event) {
        Boolean zeroVat = zeroVatParam.getValue();
        Double amount = event.getValue();
        Double vatPercent = appSettings.load(CostSettings.class).getVat();

        Double vatValue = calculateVat(zeroVat, amount, vatPercent);
        vatField.setValue(vatValue.toString());
        totalAmountField.setValue(calculateTotalAmount(vatValue, amount).toString());
    }

    @Subscribe("zeroVatParam")
    public void onZeroVatComponentValueChange(final AbstractField.ComponentValueChangeEvent<JmixCheckbox, Boolean> event) {
        Boolean zeroVat = event.getValue();
        Double amount = amountField.getTypedValue();
        Double vatPercent = appSettings.load(CostSettings.class).getVat();

        Double vatValue = calculateVat(zeroVat, amount, vatPercent);
        vatField.setValue(vatValue.toString());
        totalAmountField.setValue(calculateTotalAmount(vatValue, amount).toString());
    }

    @Subscribe("stageAdd")
    public void onStageAddClick(final ClickEvent<JmixButton> event) {
        Stage stage = dataContext.create(Stage.class);

        System.out.println(stageAmountField.getTypedValue().getClass().getName() + " - " + stageAmountField.getTypedValue());
        System.out.println(stageDescriptionField.getTypedValue().getClass().getName() + " - " + stageNameField.getTypedValue());
        System.out.println(stageDateFromField.getTypedValue().getClass().getName() + " - " + stageDateFromField.getTypedValue());
        System.out.println(stageDateToField.getTypedValue().getClass().getName() + " - " + stageDateToField.getTypedValue());
        System.out.println(stageVatField.getTypedValue().getClass().getName() + " - " + stageVatField.getTypedValue());
        System.out.println(stageTotalAmountField.getTypedValue().getClass().getName() + " - " + stageTotalAmountField.getTypedValue());
        System.out.println(stageDescriptionField.getTypedValue().getClass().getName() + " - " + stageDescriptionField.getTypedValue());

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

    private Double calculateVat(Boolean zeroVat, Double amount, Double vatPercent) {
        return (zeroVat) ? 0.0 : amount * vatPercent;
    }

    private Double calculateTotalAmount(Double vatValue, Double amount) {
        return vatValue + amount;
    }
}