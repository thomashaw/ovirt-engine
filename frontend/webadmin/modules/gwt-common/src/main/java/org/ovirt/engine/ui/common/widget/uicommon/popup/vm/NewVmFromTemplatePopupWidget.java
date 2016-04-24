package org.ovirt.engine.ui.common.widget.uicommon.popup.vm;

import static org.ovirt.engine.ui.common.widget.uicommon.popup.vm.PopupWidgetConfig.hiddenField;
import static org.ovirt.engine.ui.common.widget.uicommon.popup.vm.PopupWidgetConfig.simpleField;

import org.ovirt.engine.ui.common.idhandler.ElementIdHandler;
import org.ovirt.engine.ui.common.widget.uicommon.popup.AbstractVmPopupWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;

public class NewVmFromTemplatePopupWidget extends AbstractVmPopupWidget {

    interface ViewIdHandler extends ElementIdHandler<NewVmFromTemplatePopupWidget> {
        ViewIdHandler idHandler = GWT.create(ViewIdHandler.class);
    }

    public NewVmFromTemplatePopupWidget(EventBus eventBus){
        super(eventBus);
    }

    @Override protected void generateIds() {
        ViewIdHandler.idHandler.generateAndSetIds(this);
    }

    protected PopupWidgetConfigMap createWidgetConfiguration() {

        PopupWidgetConfigMap widgetConfiguration = new PopupWidgetConfigMap();

        widgetConfiguration.putAll(allTabs(), simpleField().visibleInAdvancedModeOnly()).
                putAll(advancedFieldsFromGeneralTab(), simpleField().visibleInAdvancedModeOnly()).
                putAll(consoleTabWidgets(), simpleField().visibleInAdvancedModeOnly()).
                update(consoleTab, simpleField().visibleInAdvancedModeOnly()).
                update(numOfMonitorsEditor, simpleField().visibleInAdvancedModeOnly()).
                update(isSingleQxlEnabledEditor, simpleField().visibleInAdvancedModeOnly()).
                putOne(isSoundcardEnabledEditor, simpleField().visibleInAdvancedModeOnly()).
                putOne(isConsoleDeviceEnabledEditor, simpleField().visibleInAdvancedModeOnly()).
                putOne(spiceFileTransferEnabledEditor, simpleField().visibleInAdvancedModeOnly()).
                putOne(spiceCopyPasteEnabledEditor, simpleField().visibleInAdvancedModeOnly()).
                putOne(instanceImagesEditor, hiddenField());

        return widgetConfiguration;
    }
}
