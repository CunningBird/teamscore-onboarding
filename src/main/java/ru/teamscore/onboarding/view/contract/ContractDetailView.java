package ru.teamscore.onboarding.view.contract;

import io.jmix.appsettings.AppSettings;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import ru.teamscore.onboarding.entity.Contract;

import ru.teamscore.onboarding.entity.CostSettings;
import ru.teamscore.onboarding.view.main.MainView;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "contracts/:id", layout = MainView.class)
@ViewController("Contract.detail")
@ViewDescriptor("contract-detail-view.xml")
@EditedEntityContainer("contractDc")
public class ContractDetailView extends StandardDetailView<Contract> {

    @Autowired
    private AppSettings appSettings;

//    @PostConstruct
//    public void flex() {
//        Double vat = appSettings.load(CostSettings.class).getVat();
//        System.out.println(vat);
//    }
}