package ru.teamscore.onboarding.view.organization;

import ru.teamscore.onboarding.entity.Organization;
import ru.teamscore.onboarding.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.*;
import io.jmix.flowui.view.*;

@Route(value = "organizations", layout = MainView.class)
@ViewController("Organization.list")
@ViewDescriptor("organization-list-view.xml")
@LookupComponent("organizationsDataGrid")
@DialogMode(width = "64em")
public class OrganizationListView extends StandardListView<Organization> {

    @ViewComponent
    private DataContext dataContext;

    @ViewComponent
    private CollectionContainer<Organization> organizationsDc;

    @ViewComponent
    private InstanceContainer<Organization> organizationDc;

    @ViewComponent
    private InstanceLoader<Organization> organizationDl;

    @ViewComponent
    private VerticalLayout listLayout;

    @ViewComponent
    private FormLayout form;

    @ViewComponent
    private HorizontalLayout detailActions;

    @Subscribe
    public void onInit(final InitEvent event) {
        updateControls(false);
    }

    @Subscribe("organizationsDataGrid.create")
    public void onOrganizationsDataGridCreate(final ActionPerformedEvent event) {
        dataContext.clear();
        Organization entity = dataContext.create(Organization.class);
        organizationDc.setItem(entity);
        updateControls(true);
    }

    @Subscribe("organizationsDataGrid.edit")
    public void onOrganizationsDataGridEdit(final ActionPerformedEvent event) {
        updateControls(true);
    }

    @Subscribe("saveBtn")
    public void onSaveButtonClick(final ClickEvent<JmixButton> event) {
        dataContext.save();
        organizationsDc.replaceItem(organizationDc.getItem());
        updateControls(false);
    }

    @Subscribe("cancelBtn")
    public void onCancelButtonClick(final ClickEvent<JmixButton> event) {
        dataContext.clear();
        organizationDl.load();
        updateControls(false);
    }

    @Subscribe(id = "organizationsDc", target = Target.DATA_CONTAINER)
    public void onOrganizationsDcItemChange(final InstanceContainer.ItemChangeEvent<Organization> event) {
        Organization entity = event.getItem();
        dataContext.clear();
        if (entity != null) {
            organizationDl.setEntityId(entity.getId());
            organizationDl.load();
        } else {
            organizationDl.setEntityId(null);
            organizationDc.setItem(null);
        }
    }

    private void updateControls(boolean editing) {
        form.getChildren().forEach(component -> {
            if (component instanceof HasValueAndElement<?, ?> field) {
                field.setReadOnly(!editing);
            }
        });

        detailActions.setVisible(editing);
        listLayout.setEnabled(!editing);
    }
}