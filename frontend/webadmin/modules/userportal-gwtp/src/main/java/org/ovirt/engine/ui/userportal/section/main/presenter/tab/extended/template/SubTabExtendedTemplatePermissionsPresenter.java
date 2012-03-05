package org.ovirt.engine.ui.userportal.section.main.presenter.tab.extended.template;

import org.ovirt.engine.core.common.businessentities.VmTemplate;
import org.ovirt.engine.ui.common.presenter.AbstractSubTabPresenter;
import org.ovirt.engine.ui.uicommonweb.models.configure.PermissionListModel;
import org.ovirt.engine.ui.userportal.gin.ClientGinjector;
import org.ovirt.engine.ui.userportal.place.ApplicationPlaces;
import org.ovirt.engine.ui.userportal.uicommon.model.template.TemplatePermissionListModelProvider;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.TabDataBasic;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;

public class SubTabExtendedTemplatePermissionsPresenter
        extends AbstractSubTabExtendedTemplatePresenter<PermissionListModel, SubTabExtendedTemplatePermissionsPresenter.ViewDef, SubTabExtendedTemplatePermissionsPresenter.ProxyDef> {

    @ProxyCodeSplit
    @NameToken(ApplicationPlaces.extendedTempplatePersmissionsSubTabPlace)
    public interface ProxyDef extends TabContentProxyPlace<SubTabExtendedTemplatePermissionsPresenter> {
    }

    public interface ViewDef extends AbstractSubTabPresenter.ViewDef<VmTemplate> {
    }

    @TabInfo(container = ExtendedTemplateSubTabPanelPresenter.class)
    static TabData getTabData(ClientGinjector ginjector) {
        return new TabDataBasic(ginjector.getApplicationConstants().extendedTemplatePermissionsSubTabLabel(), 4);
    }

    @Inject
    public SubTabExtendedTemplatePermissionsPresenter(EventBus eventBus, ViewDef view, ProxyDef proxy,
            PlaceManager placeManager, TemplatePermissionListModelProvider modelProvider) {
        super(eventBus, view, proxy, placeManager, modelProvider);
    }

}
