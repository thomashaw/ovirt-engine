package org.ovirt.engine.ui.userportal.section.main.view.popup.vm;

import org.ovirt.engine.ui.common.idhandler.ElementIdHandler;
import org.ovirt.engine.ui.common.view.popup.AbstractVmPopupView;
import org.ovirt.engine.ui.common.view.popup.VmPopupResources;
import org.ovirt.engine.ui.common.widget.uicommon.popup.vm.NewVmFromTemplatePopupWidget;
import org.ovirt.engine.ui.userportal.section.main.presenter.popup.vm.NewVmFromTemplatePopupPresenterWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

public class NewVmFromTemplatePopupView extends AbstractVmPopupView implements NewVmFromTemplatePopupPresenterWidget.ViewDef {

    interface ViewIdHandler extends ElementIdHandler<NewVmFromTemplatePopupView> {
        ViewIdHandler idhandler = GWT.create(ViewIdHandler.class);
    }

    @Inject
    public NewVmFromTemplatePopupView (EventBus eventBus, VmPopupResources resources) {
        super(eventBus, new NewVmFromTemplatePopupWidget(eventBus), resources);
        ViewIdHandler.idhandler.generateAndSetIds(this);
    }

}




