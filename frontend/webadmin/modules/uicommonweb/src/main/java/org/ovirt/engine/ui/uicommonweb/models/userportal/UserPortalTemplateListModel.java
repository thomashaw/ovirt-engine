package org.ovirt.engine.ui.uicommonweb.models.userportal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.ovirt.engine.core.common.VdcActionUtils;
import org.ovirt.engine.core.common.action.AddVmParameters;
import org.ovirt.engine.core.common.action.VdcActionType;
import org.ovirt.engine.core.common.businessentities.VmTemplate;
import org.ovirt.engine.core.common.businessentities.VmTemplateStatus;
import org.ovirt.engine.core.compat.Guid;
import org.ovirt.engine.ui.frontend.AsyncQuery;
import org.ovirt.engine.ui.frontend.Frontend;
import org.ovirt.engine.ui.frontend.INewAsyncCallback;
import org.ovirt.engine.ui.uicommonweb.dataprovider.AsyncDataProvider;
import org.ovirt.engine.ui.uicommonweb.models.configure.UserPortalPermissionListModel;
import org.ovirt.engine.ui.uicommonweb.models.templates.TemplateGeneralModel;
import org.ovirt.engine.ui.uicommonweb.models.templates.TemplateInterfaceListModel;
import org.ovirt.engine.ui.uicommonweb.models.templates.TemplateListModel;
import org.ovirt.engine.ui.uicommonweb.models.templates.TemplateStorageListModel;
import org.ovirt.engine.ui.uicommonweb.models.templates.TemplateVmListModel;
import org.ovirt.engine.ui.uicommonweb.models.templates.UserPortalTemplateDiskListModel;
import org.ovirt.engine.ui.uicommonweb.models.templates.UserPortalTemplateEventListModel;
import org.ovirt.engine.ui.uicommonweb.models.vms.IconCache;
import org.ovirt.engine.ui.uicommonweb.models.vms.TemplateVmModelBehavior;
import org.ovirt.engine.ui.uicommonweb.models.vms.UnitVmModel;
import org.ovirt.engine.ui.uicommonweb.models.vms.UserPortalTemplateVmModelBehavior;
import org.ovirt.engine.ui.uicommonweb.models.vms.VmModelBehaviorBase;
import org.ovirt.engine.ui.uicommonweb.place.UserPortalApplicationPlaces;
import com.google.inject.Inject;

public class UserPortalTemplateListModel extends TemplateListModel {

    @Inject
    public UserPortalTemplateListModel(TemplateGeneralModel templateGeneralModel,
            TemplateVmListModel templateVmListModel,
            TemplateInterfaceListModel templateInterfaceListModel,
            TemplateStorageListModel templateStorageListModel,
            UserPortalTemplateDiskListModel templateDiskListModel,
            UserPortalTemplateEventListModel templateEventListModel,
            UserPortalPermissionListModel permissionListModel) {
        super(templateGeneralModel,
                templateVmListModel,
                templateInterfaceListModel,
                templateStorageListModel,
                templateDiskListModel,
                templateEventListModel,
                permissionListModel, 2);
        setApplicationPlace(UserPortalApplicationPlaces.extendedTemplateSideTabPlace);
    }

    @Override
    protected void syncSearch() {
        AsyncDataProvider.getInstance().getAllVmTemplates(new AsyncQuery(this, new INewAsyncCallback() {
            @Override
            public void onSuccess(Object model, Object returnValue) {
                Collection<VmTemplate> vmTemplates = (Collection<VmTemplate>) returnValue;
                prefetchIcons(vmTemplates);
            }
        }), getIsQueryFirstTime());
    }

    private void prefetchIcons(final Collection<VmTemplate> vmTemplates) {
        final List<Guid> iconIds = extractSmallIconIds(vmTemplates);
        IconCache.getInstance().getOrFetchIcons(iconIds, new IconCache.IconsCallback() {
            @Override public void onSuccess(Map<Guid, String> idToIconMap) {
                setItems(vmTemplates);
            }
        });
    }

    private List<Guid> extractSmallIconIds(Collection<VmTemplate> vmTemplates) {
        final List<Guid> result = new ArrayList<>();
        for (VmTemplate template: vmTemplates) {
            result.add(template.getSmallIconId());
        }
        return result;
    }

    @Override
    protected void updateActionsAvailability() {
        VmTemplate item = (VmTemplate) getSelectedItem();
        if (item != null) {
            ArrayList items = new ArrayList();
            items.add(item);
            getEditCommand().setIsExecutionAllowed(
                    item.getStatus() != VmTemplateStatus.Locked &&
                            !isBlankTemplateSelected());
            getRemoveCommand().setIsExecutionAllowed(
                    VdcActionUtils.canExecute(items, VmTemplate.class,
                            VdcActionType.RemoveVmTemplate) &&
                            !isBlankTemplateSelected()
                    );
            getCreateVmFromTemplateCommand().
                    setIsExecutionAllowed(item.getStatus() != VmTemplateStatus.Locked && !isBlankTemplateSelected());
        } else {
            getEditCommand().setIsExecutionAllowed(false);
            getRemoveCommand().setIsExecutionAllowed(false);
            getCreateVmFromTemplateCommand().setIsExecutionAllowed(false);
        }
    }

    @Override
    protected String getEditTemplateAdvancedModelKey() {
        return "up_template_dialog"; //$NON-NLS-1$
    }

    @Override protected UnitVmModel createModel(VmModelBehaviorBase behavior) {
        UnitVmModel model = super.createModel(behavior);
        generateDefaultName(model);
        return model;
    }

    protected void generateDefaultName(UnitVmModel model) {
        String spacer = "-"; //$NON-NLS-1$
        String username = Frontend.getInstance().getLoggedInUser().getLoginName();
        String templateName = getSelectedItem().getName();
        String index = getNextIndexNumber();
        // check if unique here + add index number if not
        model.getName().setEntity(username + spacer + templateName + spacer + index);
    }

    @Override
    protected TemplateVmModelBehavior createBehavior(VmTemplate template) {
        return new UserPortalTemplateVmModelBehavior(template);
    }

    @Override
    public void setItems(Collection value) {
        final List<VmTemplate> sortedValues = sortTemplates(value);
        super.setItems(sortedValues);
    }

    @Override
    protected void setPortalSpecificParameters(AddVmParameters parameters){
        parameters.setMakeCreatorExplicitOwner(true);
    }

    /**
     * It sorts {@link org.ovirt.engine.core.common.businessentities.VmTemplate}s using
     * {@link org.ovirt.engine.ui.uicommonweb.models.userportal.UserPortalTemplateListModel.TemplateComparator}
     */
    private List<VmTemplate> sortTemplates(Collection<VmTemplate> value) {
        if (value == null) {
            return null;
        }
        final List<VmTemplate> sortedValues = new ArrayList<>(value);
        Collections.sort(sortedValues, new TemplateComparator());
        return sortedValues;
    }

    public String getNextIndexNumber() {
        Guid selectedBaseId = getSelectedItem().getBaseTemplateId();
        Collection<VmTemplate> templates = getItems();
        Integer index = 0;
        for (VmTemplate template: templates) {
            if (selectedBaseId.equals(template.getBaseTemplateId())) index++;
        }
        return index.toString();
    }

    /**
     * Comparator sorting templates
     * <ul>
     *     <li>alphabetically by base-template name case insensitive</li>
     *     <li>alphabetically by base-template name case sensitive</li>
     *     <li>and then by version number - descending</li>
     * </ul>
     */
    private static class TemplateComparator implements Comparator<VmTemplate>, Serializable {

        @Override
        public int compare(VmTemplate t1, VmTemplate t2) {
            final int baseNameCaseInsensitiveComparison = t1.getName().compareToIgnoreCase(t2.getName());
            if (baseNameCaseInsensitiveComparison != 0) {
                return baseNameCaseInsensitiveComparison;
            }
            final int baseNameComparison = t1.getName().compareTo(t2.getName());
            if (baseNameComparison != 0) {
                return baseNameComparison;
            }
            final int versionComparison =
                    - Integer.signum(Integer.compare(t1.getTemplateVersionNumber(), t2.getTemplateVersionNumber()));
            return versionComparison;
        }

        @Override
        public boolean equals(Object obj) {
            return obj != null && this.getClass().equals(obj.getClass());
        }

        @Override
        public int hashCode() {
            return this.getClass().hashCode();
        }
    }
}
