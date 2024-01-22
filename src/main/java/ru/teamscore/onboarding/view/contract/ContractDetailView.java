package ru.teamscore.onboarding.view.contract;

import com.vaadin.flow.component.AbstractField;
import io.jmix.appsettings.AppSettings;
import io.jmix.flowui.component.SupportsTypedValue;
import io.jmix.flowui.component.checkbox.JmixCheckbox;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.model.DataContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.teamscore.onboarding.entity.Contract;

import ru.teamscore.onboarding.entity.ContractStatus;
import ru.teamscore.onboarding.entity.CostSettings;
import ru.teamscore.onboarding.view.main.MainView;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

import java.util.Collection;
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

    @Subscribe(target = Target.DATA_CONTEXT)
    public void onPreSave(final DataContext.PreSaveEvent event) {
        Collection<Contract> contracts = (Collection<Contract>) event.getModifiedInstances();
        Optional<Contract> contract = contracts.stream().findFirst();
        if (contract.isPresent()) {
            Contract entity = contract.get();
            Boolean zeroVat = zeroVatParam.getValue();
            Double amount = amountField.getTypedValue();
            Double vatPercent = appSettings.load(CostSettings.class).getVat();

            Double vatValue = calculateVat(zeroVat, amount, vatPercent);
            entity.setVat(vatValue);
            entity.setTotalAmount(calculateTotalAmount(vatValue, amount));
        }
    }

    private Double calculateVat(Boolean zeroVat, Double amount, Double vatPercent) {
        return (zeroVat) ? 0.0 : amount * vatPercent;
    }

    private Double calculateTotalAmount(Double vatValue, Double amount) {
        return vatValue + amount;
    }
}