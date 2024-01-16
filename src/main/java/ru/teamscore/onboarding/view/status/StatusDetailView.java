package ru.teamscore.onboarding.view.status;

import ru.teamscore.onboarding.entity.Status;

import ru.teamscore.onboarding.view.main.MainView;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "statuses/:id", layout = MainView.class)
@ViewController("Status.detail")
@ViewDescriptor("status-detail-view.xml")
@EditedEntityContainer("statusDc")
public class StatusDetailView extends StandardDetailView<Status> {
}