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

import static org.easymock.EasyMock.expect;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.Test;
import org.ovirt.engine.api.model.Nic;
import org.ovirt.engine.api.model.NicInterface;
import org.ovirt.engine.core.common.action.AddVmTemplateInterfaceParameters;
import org.ovirt.engine.core.common.action.VdcActionType;
import org.ovirt.engine.core.common.businessentities.VmGuestAgentInterface;
import org.ovirt.engine.core.common.businessentities.network.VmNetworkInterface;
import org.ovirt.engine.core.common.businessentities.network.VmNetworkStatistics;
import org.ovirt.engine.core.common.queries.IdQueryParameters;
import org.ovirt.engine.core.common.queries.VdcQueryType;
import org.ovirt.engine.core.compat.Guid;

public class BackendInstanceTypeNicsResourceTest
        extends AbstractBackendCollectionResourceTest<Nic, VmNetworkInterface, BackendTemplateNicsResource> {

    protected static final Guid INSTANCE_TYPE_ID = GUIDS[1];
    protected static final String[] ADDRESSES = { "10.11.12.13", "13.12.11.10", "10.01.10.01" };

    public BackendInstanceTypeNicsResourceTest() {
        super(new BackendTemplateNicsResource(INSTANCE_TYPE_ID), null, null);
    }

    @Override
    protected void setUpQueryExpectations(String query) throws Exception {
        setUpEntityQueryExpectations(1);
        control.replay();
    }

    @Override
    protected void setUpQueryExpectations(String query, Object failure) throws Exception {
        setUpEntityQueryExpectations(1, failure);
        control.replay();
    }

    protected void setUpEntityQueryExpectations(int times) throws Exception {
        setUpEntityQueryExpectations(times, null);
    }

    protected void setUpEntityQueryExpectations(int times, Object failure) throws Exception {
        while (times-- > 0) {
            setUpEntityQueryExpectations(
                VdcQueryType.GetTemplateInterfacesByTemplateId,
                IdQueryParameters.class,
                new String[] { "Id" },
                new Object[] { INSTANCE_TYPE_ID },
                getEntityList(),
                failure
            );
        }
    }

    protected List<VmNetworkInterface> getEntityList() {
        List<VmNetworkInterface> entities = new ArrayList<>();
        for (int i = 0; i < NAMES.length; i++) {
            entities.add(getEntity(i));
        }
        return entities;
    }

    @Override
    protected VmNetworkInterface getEntity(int index) {
        return setUpEntityExpectations(
            control.createMock(VmNetworkInterface.class),
            control.createMock(VmNetworkStatistics.class),
            index
        );
    }

    static VmNetworkInterface setUpEntityExpectations(
            VmNetworkInterface entity,
            VmNetworkStatistics statistics,
            int index) {
        return setUpEntityExpectations(entity, statistics, index, NAMES[2]);
    }

    static VmNetworkInterface setUpEntityExpectations(
            VmNetworkInterface entity,
            VmNetworkStatistics statistics,
            int index,
            String networkName) {
        expect(entity.getId()).andReturn(GUIDS[index]).anyTimes();
        expect(entity.getVmId()).andReturn(INSTANCE_TYPE_ID).anyTimes();
        expect(entity.getNetworkName()).andReturn(networkName).anyTimes();
        expect(entity.getName()).andReturn(NAMES[index]).anyTimes();
        expect(entity.getMacAddress()).andReturn(ADDRESSES[2]).anyTimes();
        expect(entity.getType()).andReturn(0).anyTimes();
        expect(entity.getSpeed()).andReturn(50).anyTimes();
        return setUpStatisticalEntityExpectations(entity, statistics);
    }

    static VmNetworkInterface setUpStatisticalEntityExpectations(
            VmNetworkInterface entity,
            VmNetworkStatistics statistics) {
        expect(entity.getStatistics()).andReturn(statistics).anyTimes();
        expect(statistics.getReceiveRate()).andReturn(1D).anyTimes();
        expect(statistics.getReceiveDropRate()).andReturn(2D).anyTimes();
        expect(statistics.getTransmitRate()).andReturn(3D).anyTimes();
        expect(statistics.getTransmitDropRate()).andReturn(4D).anyTimes();
        expect(statistics.getReceivedBytes()).andReturn(5L).anyTimes();
        expect(statistics.getTransmittedBytes()).andReturn(6L).anyTimes();
        return entity;
    }

    @Override
    protected List<Nic> getCollection() {
        return collection.list().getNics();
    }

    static Nic getModel(int index) {
        Nic model = new Nic();
        model.setName(NAMES[index]);
        model.setInterface(NicInterface.RTL8139_VIRTIO.value());
        return model;
    }

    @Override
    protected void verifyModel(Nic model, int index) {
        verifyModelSpecific(model, index);
        verifyLinks(model);
    }

    static void verifyModelSpecific(Nic model, int index) {
        assertEquals(GUIDS[index].toString(), model.getId());
        assertEquals(NAMES[index].toString(), model.getName());
        assertTrue(model.isSetVm());
        assertEquals(INSTANCE_TYPE_ID.toString(), model.getVm().getId());
        assertTrue(model.isSetMac());
        assertEquals(ADDRESSES[2].toString(), model.getMac().getAddress());
    }

    protected void setGetNetworksQueryExpectations(int times) throws Exception {
        while (times-- > 0) {
            ArrayList<org.ovirt.engine.core.common.businessentities.network.Network> networks = new ArrayList<>();
            org.ovirt.engine.core.common.businessentities.network.Network network = new org.ovirt.engine.core.common.businessentities.network.Network();
            network.setId(GUIDS[0]);
            network.setName("orcus");
            networks.add(network);
            setUpEntityQueryExpectations(VdcQueryType.GetAllNetworksByClusterId,
                    IdQueryParameters.class,
                    new String[] { "Id" },
                    new Object[] { GUIDS[0] },
                    networks);
        }
    }

    protected void setGetGuestAgentQueryExpectations(int times) throws Exception {
        while (times-- > 0) {
            setUpEntityQueryExpectations(VdcQueryType.GetVmGuestAgentInterfacesByVmId,
                    IdQueryParameters.class,
                    new String[] { "Id" },
                    new Object[] { INSTANCE_TYPE_ID },
                    getListOfVmGuestAgentInterfaces());
        }
    }

    @SuppressWarnings("serial")
    private Object getListOfVmGuestAgentInterfaces() {
        VmGuestAgentInterface iface = new VmGuestAgentInterface();
        iface.setMacAddress(ADDRESSES[2]);
        List<VmGuestAgentInterface> list = new ArrayList<>();
        list.add(iface);
        return list;
    }
    @Test
    public void testAddNic() throws Exception {
        setUriInfo(setUpBasicUriExpectations());
        setUpCreationExpectations(
            VdcActionType.AddVmTemplateInterface,
            AddVmTemplateInterfaceParameters.class,
            new String[] {},
            new Object[] {},
            true,
            true,
            null,
            VdcQueryType.GetTemplateInterfacesByTemplateId,
            IdQueryParameters.class,
            new String[] { "Id" },
            new Object[] { INSTANCE_TYPE_ID },
            asList(getEntity(0))
        );
        Nic model = getModel(0);

        Response response = collection.add(model);
        assertEquals(201, response.getStatus());
        assertTrue(response.getEntity() instanceof Nic);
        verifyModel((Nic) response.getEntity(), 0);
    }

    @Test
    public void testAddNicCantDo() throws Exception {
        doTestBadAddNic(false, true, CANT_DO);
    }

    @Test
    public void testAddNicFailure() throws Exception {
        doTestBadAddNic(true, false, FAILURE);
    }

    private void doTestBadAddNic(boolean valid, boolean success, String detail) throws Exception {
        setUriInfo(
            setUpActionExpectations(
                VdcActionType.AddVmTemplateInterface,
                AddVmTemplateInterfaceParameters.class,
                new String[] { "VmTemplateId" },
                new Object[] { INSTANCE_TYPE_ID },
                valid,
                success
            )
        );
        Nic model = getModel(0);

        try {
            collection.add(model);
            fail("expected WebApplicationException");
        } catch (WebApplicationException wae) {
            verifyFault(wae, detail);
        }
    }

    @Test
    public void testAddIncompleteParameters() throws Exception {
        Nic model = new Nic();
        model.setName(null);

        setUriInfo(setUpBasicUriExpectations());
        control.replay();
        try {
            collection.add(model);
            fail("expected WebApplicationException on incomplete parameters");
        } catch (WebApplicationException wae) {
            verifyIncompleteException(wae, "Nic", "add", "name");
        }
    }

    @Test
    public void testSubResourceLocatorBadGuid() throws Exception {
        control.replay();
        try {
            collection.getNicResource("foo");
            fail("expected WebApplicationException");
        } catch (WebApplicationException wae) {
            verifyNotFoundException(wae);
        }
    }

    @Test
    @Override
    public void testList() throws Exception {
        UriInfo uriInfo = setUpUriExpectations(null);
        setUpQueryExpectations("");
        collection.setUriInfo(uriInfo);
        verifyCollection(getCollection());
    }
}
