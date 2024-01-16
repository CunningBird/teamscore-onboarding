package ru.teamscore.onboarding.view.status;

import ru.teamscore.onboarding.entity.Status;

import ru.teamscore.onboarding.view.main.MainView;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "statuses", layout = MainView.class)
@ViewController("Status.list")
@ViewDescriptor("status-list-view.xml")
@LookupComponent("statusesDataGrid")
@DialogMode(width = "64em")
public class StatusListView extends StandardListView<Status> {
}