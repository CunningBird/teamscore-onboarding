package ru.teamscore.onboarding.view.stage;

import ru.teamscore.onboarding.entity.Stage;
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

@Route(value = "stages", layout = MainView.class)
@ViewController("Stage.list")
@ViewDescriptor("stage-list-view.xml")
@LookupComponent("stagesDataGrid")
@DialogMode(width = "64em")
public class StageListView extends StandardListView<Stage> {

    @ViewComponent
    private DataContext dataContext;

    @ViewComponent
    private CollectionContainer<Stage> stagesDc;

    @ViewComponent
    private InstanceContainer<Stage> stageDc;

    @ViewComponent
    private InstanceLoader<Stage> stageDl;

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

    @Subscribe("stagesDataGrid.create")
    public void onStagesDataGridCreate(final ActionPerformedEvent event) {
        dataContext.clear();
        Stage entity = dataContext.create(Stage.class);
        stageDc.setItem(entity);
        updateControls(true);
    }

    @Subscribe("stagesDataGrid.edit")
    public void onStagesDataGridEdit(final ActionPerformedEvent event) {
        updateControls(true);
    }

    @Subscribe("saveBtn")
    public void onSaveButtonClick(final ClickEvent<JmixButton> event) {
        dataContext.save();
        stagesDc.replaceItem(stageDc.getItem());
        updateControls(false);
    }

    @Subscribe("cancelBtn")
    public void onCancelButtonClick(final ClickEvent<JmixButton> event) {
        dataContext.clear();
        stageDl.load();
        updateControls(false);
    }

    @Subscribe(id = "stagesDc", target = Target.DATA_CONTAINER)
    public void onStagesDcItemChange(final InstanceContainer.ItemChangeEvent<Stage> event) {
        Stage entity = event.getItem();
        dataContext.clear();
        if (entity != null) {
            stageDl.setEntityId(entity.getId());
            stageDl.load();
        } else {
            stageDl.setEntityId(null);
            stageDc.setItem(null);
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