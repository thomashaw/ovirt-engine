/*
Copyright (c) 2015 Red Hat, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package org.ovirt.engine.api.restapi.resource;

import java.util.List;
import java.util.Objects;
import javax.ws.rs.core.Response;

import org.ovirt.engine.api.model.Vm;
import org.ovirt.engine.api.model.Watchdog;
import org.ovirt.engine.api.model.WatchdogAction;
import org.ovirt.engine.api.model.WatchdogModel;
import org.ovirt.engine.api.model.Watchdogs;
import org.ovirt.engine.api.resource.VmWatchdogResource;
import org.ovirt.engine.api.resource.VmWatchdogsResource;
import org.ovirt.engine.api.restapi.types.WatchdogMapper;
import org.ovirt.engine.core.common.action.VdcActionType;
import org.ovirt.engine.core.common.action.WatchdogParameters;
import org.ovirt.engine.core.common.businessentities.VmWatchdog;
import org.ovirt.engine.core.common.queries.IdQueryParameters;
import org.ovirt.engine.core.common.queries.VdcQueryType;
import org.ovirt.engine.core.compat.Guid;

public class BackendVmWatchdogsResource
        extends AbstractBackendCollectionResource<Watchdog, VmWatchdog>
        implements VmWatchdogsResource {

    private Guid vmId;

    public BackendVmWatchdogsResource(Guid vmId) {
        super(Watchdog.class, VmWatchdog.class);
        this.vmId = vmId;
    }

    @Override
    public Watchdogs list() {
        return mapCollection(getBackendCollection(VdcQueryType.GetWatchdog, new IdQueryParameters(vmId)));
    }

    private Watchdogs mapCollection(List<VmWatchdog> entities) {
        Watchdogs collection = new Watchdogs();
        for (VmWatchdog entity : entities) {
            collection.getWatchdogs().add(addLinks(map(entity)));
        }
        return collection;
    }

    public Response add(Watchdog watchdog) {
        validateParameters(watchdog, "action", "model");
        validateEnums(Watchdog.class, watchdog);
        WatchdogParameters parameters = new WatchdogParameters();
        parameters.setAction(WatchdogMapper.map(WatchdogAction.fromValue(watchdog.getAction())));
        parameters.setModel(WatchdogMapper.map(WatchdogModel.fromValue(watchdog.getModel())));
        parameters.setId(vmId);
        parameters.setVm(true);
        return performCreate(VdcActionType.AddWatchdog, parameters, new WatchdogResolver());
    }

    @Override
    public VmWatchdogResource getWatchdogResource(String watchdogId) {
        return inject(new BackendVmWatchdogResource(watchdogId, vmId));
    }

    @Override
    public Watchdog addParents(Watchdog watchdog) {
        Vm vm = new Vm();
        vm.setId(vmId.toString());
        watchdog.setVm(vm);
        return watchdog;
    }

    private class WatchdogResolver implements IResolver<Guid, VmWatchdog> {
        @Override
        public VmWatchdog resolve(Guid id) throws BackendFailureException {
            List<VmWatchdog> watchdogs = getBackendCollection(VdcQueryType.GetWatchdog, new IdQueryParameters(vmId));
            for (VmWatchdog watchdog : watchdogs) {
                if (Objects.equals(watchdog.getId(), id)) {
                    return watchdog;
                }
            }
            return null;
        }
    }
}
