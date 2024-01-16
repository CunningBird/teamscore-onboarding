package ru.teamscore.onboarding.view.contract;

import ru.teamscore.onboarding.entity.Contract;

import ru.teamscore.onboarding.view.main.MainView;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "contracts/:id", layout = MainView.class)
@ViewController("Contract.detail")
@ViewDescriptor("contract-detail-view.xml")
@EditedEntityContainer("contractDc")
public class ContractDetailView extends StandardDetailView<Contract> {
}