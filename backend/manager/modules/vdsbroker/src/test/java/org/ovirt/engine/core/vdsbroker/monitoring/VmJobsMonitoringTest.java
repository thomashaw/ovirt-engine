package org.ovirt.engine.core.vdsbroker.monitoring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.ovirt.engine.core.common.businessentities.VmJob;
import org.ovirt.engine.core.compat.Guid;

@RunWith(MockitoJUnitRunner.class)
public class VmJobsMonitoringTest {

    @Spy
    private VmJobsMonitoring vmJobsMonitoring;

    private static final Guid VM_ID_1 = new Guid("b7dfe5e6-5667-4e40-8ecb-6d97c8df504c");
    private static final Guid VM_ID_2 = new Guid("b7dfe5e6-5667-4e40-8ecb-6d97c8df504d");
    private static final Guid JOB_ID_1 = new Guid("b7dfe5e6-5667-4e40-8ecb-000000000001");
    private static final Guid JOB_ID_2 = new Guid("b7dfe5e6-5667-4e40-8ecb-000000000002");
    private static final Guid JOB_ID_3 = new Guid("b7dfe5e6-5667-4e40-8ecb-000000000003");
    private static final Guid JOB_ID_4 = new Guid("b7dfe5e6-5667-4e40-8ecb-000000000004");
    private static final Guid JOB_ID_5 = new Guid("b7dfe5e6-5667-4e40-8ecb-000000000005");

    @Captor
    private ArgumentCaptor<Collection<VmJob>> vmJobsToUpdateCaptor;
    @Captor
    private ArgumentCaptor<List<Guid>> vmJobIdsToRemoveCaptor;

    @Mock
    private VmJob job1FromDb;
    @Mock
    private VmJob job2FromDb;
    @Mock
    private VmJob job3FromDb;
    @Mock
    private VmJob job4FromDb;

    private VmJob job1FromVdsm;
    @Mock
    private VmJob job2FromVdsm;
    @Mock
    private VmJob job3FromVdsm;
    @Mock
    private VmJob job5FromVdsm;

    @Before
    public void before() {
        when(job1FromDb.getId()).thenReturn(JOB_ID_1);
        when(job2FromDb.getId()).thenReturn(JOB_ID_2);
        when(job3FromDb.getId()).thenReturn(JOB_ID_3);
        when(job4FromDb.getId()).thenReturn(JOB_ID_4);

        when(job2FromVdsm.getId()).thenReturn(JOB_ID_2);
        when(job3FromVdsm.getId()).thenReturn(JOB_ID_3);
        when(job5FromVdsm.getId()).thenReturn(JOB_ID_5);

        when(job1FromDb.getVmId()).thenReturn(VM_ID_1);
        when(job2FromDb.getVmId()).thenReturn(VM_ID_1);
        when(job2FromVdsm.getVmId()).thenReturn(VM_ID_1);

        when(job3FromDb.getVmId()).thenReturn(VM_ID_2);
        when(job4FromDb.getVmId()).thenReturn(VM_ID_2);
        when(job3FromVdsm.getVmId()).thenReturn(VM_ID_2);
        when(job5FromVdsm.getVmId()).thenReturn(VM_ID_2);

        doReturn(Arrays.asList(job1FromDb, job2FromDb)).when(vmJobsMonitoring).getExistingJobsForVm(VM_ID_1);
        doReturn(Arrays.asList(job3FromDb, job4FromDb)).when(vmJobsMonitoring).getExistingJobsForVm(VM_ID_2);
        doNothing().when(vmJobsMonitoring).updateJobs(any());
        doNothing().when(vmJobsMonitoring).removeJobs(any());
    }

    @Test
    public void noVms() {
        vmJobsMonitoring.process(Collections.emptyMap());
        verify(vmJobsMonitoring, never()).getExistingJobsForVm(any());
        verify(vmJobsMonitoring, never()).updateJobs(any());
        verify(vmJobsMonitoring, never()).removeJobs(any());
    }

    @Test
    public void vmWithNoJobs() {
        vmJobsMonitoring.process(Collections.singletonMap(VM_ID_1, null));
        verify(vmJobsMonitoring, never()).getExistingJobsForVm(any());
        verify(vmJobsMonitoring, times(1)).updateJobs(vmJobsToUpdateCaptor.capture());
        assertTrue(vmJobsToUpdateCaptor.getValue().isEmpty());
        verify(vmJobsMonitoring, times(1)).removeJobs(vmJobIdsToRemoveCaptor.capture());
        assertTrue(vmJobIdsToRemoveCaptor.getValue().isEmpty());
    }

    @Test
    public void vmsWithJobs() {
        vmJobsMonitoring.process(initJobsFromVdsm());
        verify(vmJobsMonitoring, times(2)).getExistingJobsForVm(any());
        verify(vmJobsMonitoring, times(1)).updateJobs(vmJobsToUpdateCaptor.capture());
        assertEquals(2, vmJobsToUpdateCaptor.getValue().size());
        assertTrue(vmJobsToUpdateCaptor.getValue().contains(job2FromVdsm));
        assertTrue(vmJobsToUpdateCaptor.getValue().contains(job3FromVdsm));
        assertFalse(vmJobsToUpdateCaptor.getValue().contains(job1FromVdsm));
        verify(vmJobsMonitoring, times(1)).removeJobs(vmJobIdsToRemoveCaptor.capture());
        assertEquals(1, vmJobIdsToRemoveCaptor.getValue().size());
        assertTrue(vmJobIdsToRemoveCaptor.getValue().contains(JOB_ID_4));
    }

    @SuppressWarnings("serial")
    public Map<Guid, List<VmJob>> initJobsFromVdsm() {
        job1FromVdsm = job1FromDb;
        return new HashMap<Guid, List<VmJob>>() {{
            put(VM_ID_1, Arrays.asList(job1FromVdsm, job2FromVdsm));
            put(VM_ID_2, Arrays.asList(job3FromVdsm, job5FromVdsm));
        }};
    }
}
