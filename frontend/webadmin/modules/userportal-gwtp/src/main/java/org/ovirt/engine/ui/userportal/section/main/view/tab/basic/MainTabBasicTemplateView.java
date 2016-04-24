package org.ovirt.engine.ui.userportal.section.main.view.tab.basic;

import org.ovirt.engine.core.common.businessentities.VmTemplate;
import org.ovirt.engine.ui.common.idhandler.ElementIdHandler;
import org.ovirt.engine.ui.common.idhandler.WithElementId;
import org.ovirt.engine.ui.common.system.ClientStorage;
import org.ovirt.engine.ui.common.utils.ElementIdUtils;
import org.ovirt.engine.ui.common.view.AbstractView;
import org.ovirt.engine.ui.common.widget.table.HasActionTable;
import org.ovirt.engine.ui.uicommonweb.UICommand;
import org.ovirt.engine.ui.userportal.ApplicationConstants;
import org.ovirt.engine.ui.userportal.ApplicationTemplates;
import org.ovirt.engine.ui.userportal.gin.AssetProvider;
import org.ovirt.engine.ui.userportal.section.main.presenter.tab.basic.MainTabBasicTemplatePresenterWidget;
import org.ovirt.engine.ui.userportal.section.main.view.tab.extended.SideTabExtendedTemplateView;
import org.ovirt.engine.ui.userportal.uicommon.model.template.UserPortalTemplateListProvider;
import org.ovirt.engine.ui.userportal.widget.action.UserPortalButtonDefinition;
import org.ovirt.engine.ui.userportal.widget.table.UserPortalSimpleActionTable;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class MainTabBasicTemplateView
        extends AbstractView implements MainTabBasicTemplatePresenterWidget.ViewDef, HasActionTable<VmTemplate> {

    interface ViewIdHandler extends ElementIdHandler<MainTabBasicTemplateView> {
        ViewIdHandler idHandler = GWT.create(ViewIdHandler.class);
    }

    interface ViewUiBinder extends UiBinder<Widget, MainTabBasicTemplateView> {
        ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
    }

    private final static ApplicationTemplates templates = AssetProvider.getTemplates();
    private final static ApplicationConstants constants = AssetProvider.getConstants();

    private final EventBus eventBus;
    private final ClientStorage clientStorage;
    private final UserPortalTemplateListProvider modelProvider;
    private BasicTemplateTableResources tableResources = GWT.create(BasicTemplateTableResources.class);

    @UiField
    SimplePanel containerPanel = new SimplePanel();

    @WithElementId
    private UserPortalSimpleActionTable<VmTemplate> templateTable;

    @Inject
    public MainTabBasicTemplateView(UserPortalTemplateListProvider provider,
            EventBus eventBus,
            ClientStorage clientStorage) {
        ViewIdHandler.idHandler.generateAndSetIds(this);
        this.eventBus = eventBus;
        this.clientStorage = clientStorage;
        this.modelProvider = provider;
        initWidget(containerPanel);
        templateTable = createTemplateTable();
        containerPanel.add(templateTable);
    }

    private UserPortalSimpleActionTable<VmTemplate> createTemplateTable() {
        final UserPortalSimpleActionTable<VmTemplate> table = new UserPortalSimpleActionTable<>(modelProvider,
                getTableResources(),
                getTableHeaderResources(),
                eventBus,
                clientStorage);

        final String elementIdPrefix = table.getContentTableElementId();

        Cell<VmTemplate> nameCell = new AbstractCell<VmTemplate>() {
            @Override
            public void render(Context context, VmTemplate template, SafeHtmlBuilder sb) {
                sb.append(templates.vmNameCellItem(
                        ElementIdUtils.createTableCellElementId(elementIdPrefix, "name", context), //$NON-NLS-1$
                        template.getName()));
            }
        };

        Column<VmTemplate, VmTemplate> nameColumn = new Column<VmTemplate, VmTemplate>(nameCell) {
            @Override
            public VmTemplate getValue(VmTemplate template) {
                return template;
            }
        };
        table.addColumn(nameColumn, constants.template(), "250px"); //$NON-NLS-1$

        UserPortalButtonDefinition<VmTemplate> buttonDef =
                new UserPortalButtonDefinition<VmTemplate>(constants.createVmFromTemplate()) {
                    @Override
                    protected UICommand resolveCommand() {
                        return modelProvider.getModel().getCreateVmFromTemplateCommand();
                    }
                };

        table.addActionButton(buttonDef);
        return table;
    }

    private CellTable.Resources getTableResources() {
        return tableResources;
    }

    private CellTable.Resources getTableHeaderResources() {
        return SideTabExtendedTemplateView.TEMPLATE_TABLE_HEADER_RESOURCES;
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == MainTabBasicTemplatePresenterWidget.TYPE_TemplateListContent) {
            setPanelContent(containerPanel, content);
        } else {
            super.setInSlot(slot, content);
        }
    }

    @Override public UserPortalSimpleActionTable<VmTemplate> getTable() {
        return templateTable;
    }

    public interface BasicTemplateTableResources extends CellTable.Resources {
        interface TableStyle extends CellTable.Style {
        }

        @Override
        @Source({ CellTable.Style.DEFAULT_CSS, "org/ovirt/engine/ui/userportal/css/ExtendedVmListTable.css" })
        TableStyle cellTableStyle();
    }

}
