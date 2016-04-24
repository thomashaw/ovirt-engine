package org.ovirt.engine.ui.userportal.section.main.presenter.tab.basic;

import java.util.List;

import org.ovirt.engine.core.common.businessentities.VmTemplate;
import org.ovirt.engine.ui.common.widget.table.HasActionTable;
import org.ovirt.engine.ui.uicommonweb.models.userportal.UserPortalTemplateListModel;
import org.ovirt.engine.ui.uicommonweb.place.UserPortalApplicationPlaces;
import org.ovirt.engine.ui.userportal.section.main.presenter.AbstractModelActivationPresenter;
import org.ovirt.engine.ui.userportal.section.main.presenter.tab.MainTabBasicPresenter;
import org.ovirt.engine.ui.userportal.uicommon.model.template.UserPortalTemplateListProvider;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;

import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;


public class MainTabBasicTemplatePresenterWidget
        extends AbstractModelActivationPresenter<VmTemplate, UserPortalTemplateListModel, MainTabBasicTemplatePresenterWidget.ViewDef, MainTabBasicTemplatePresenterWidget.ProxyDef> {

    public interface ViewDef extends View, HasActionTable<VmTemplate> {
    }

    @ContentSlot
    public static final GwtEvent.Type<RevealContentHandler<?>> TYPE_TemplateListContent =
            new GwtEvent.Type<RevealContentHandler<?>>();


    @ProxyCodeSplit
    @NameToken(UserPortalApplicationPlaces.extendedTemplateSideTabPlace)
    public interface ProxyDef extends ProxyPlace<MainTabBasicTemplatePresenterWidget> {
    }

    @Override
    protected void onBind() {
        super.onBind();

        registerHandler(getView().getTable().getSelectionModel()
                .addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
                    @Override
                    public void onSelectionChange(SelectionChangeEvent event) {
                        // Update model selection
                        modelProvider.setSelectedItems(getSelectedItems());
                    }
                }));
    }

    /**
     * Returns items currently selected in the table.
     */
    protected List<VmTemplate> getSelectedItems() {
        return getView().getTable().getSelectionModel().getSelectedList();
    }

    @Inject
    public MainTabBasicTemplatePresenterWidget(EventBus eventBus, ViewDef view, ProxyDef proxy,
            UserPortalTemplateListProvider modelProvider) {
        super(eventBus, view, proxy, modelProvider, MainTabBasicPresenter.TYPE_TemplatePanelContent);
    }
}
