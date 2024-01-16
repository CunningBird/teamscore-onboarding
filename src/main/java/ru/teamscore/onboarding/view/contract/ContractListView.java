package ru.teamscore.onboarding.view.contract;

import ru.teamscore.onboarding.entity.Contract;

import ru.teamscore.onboarding.view.main.MainView;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "contracts", layout = MainView.class)
@ViewController("Contract.list")
@ViewDescriptor("contract-list-view.xml")
@LookupComponent("contractsDataGrid")
@DialogMode(width = "64em")
public class ContractListView extends StandardListView<Contract> {
}