package org.ovirt.engine.core.common.queries;

public enum ConfigurationValues {
    MaxNumOfVmCpus(ConfigAuthType.User),
    MaxNumOfVmSockets(ConfigAuthType.User),
    MaxNumOfCpuPerSocket(ConfigAuthType.User),
    MaxNumOfThreadsPerCpu(ConfigAuthType.User),
    VdcVersion(ConfigAuthType.User),
    MaxVmsInPool(ConfigAuthType.User),
    MaxVdsMemOverCommit(ConfigAuthType.User),
    MaxVdsMemOverCommitForServers(ConfigAuthType.User),
    ValidNumOfMonitors(ConfigAuthType.User),
    SpiceProxyDefault(ConfigAuthType.User),
    RemapCtrlAltDelDefault(ConfigAuthType.User),
    ClientModeSpiceDefault(ConfigAuthType.User),
    ClientModeVncDefault(ConfigAuthType.User),
    ClientModeRdpDefault(ConfigAuthType.User),
    UseFqdnForRdpIfAvailable(ConfigAuthType.User),
    WebSocketProxy(ConfigAuthType.User),
    SpiceUsbAutoShare(ConfigAuthType.User),
    FenceProxyDefaultPreferences,
    SearchResultsLimit(ConfigAuthType.User),
    MaxBlockDiskSize(ConfigAuthType.User),
    VmPriorityMaxValue(ConfigAuthType.User),
    WarningLowSpaceIndicator(ConfigAuthType.User),
    CriticalSpaceActionBlocker(ConfigAuthType.User),
    StorageDomainNameSizeLimit(ConfigAuthType.User),
    StoragePoolNameSizeLimit(ConfigAuthType.User),
    UserDefinedVMProperties(ConfigAuthType.User),
    PredefinedVMProperties(ConfigAuthType.User),
    VdsFenceOptionTypes,
    VdsFenceOptionMapping,
    VdsFenceType,
    SupportedClusterLevels(ConfigAuthType.User),
    ProductRPMVersion(ConfigAuthType.User),
    RhevhLocalFSPath,
    HotPlugCpuSupported(ConfigAuthType.User),
    IoThreadsSupported(ConfigAuthType.User),
    ApplicationMode(ConfigAuthType.User),
    PopulateDirectLUNDiskDescriptionWithLUNId,
    WANDisableEffects(ConfigAuthType.User),
    WANColorDepth(ConfigAuthType.User),
    NetworkConnectivityCheckTimeoutInSeconds,
    AllowClusterWithVirtGlusterEnabled,
    GlusterVolumeOptionGroupVirtValue,
    GlusterVolumeOptionOwnerUserVirtValue,
    GlusterVolumeOptionOwnerGroupVirtValue,
    GlusterDefaultBrickMountPoint,
    GlusterMetaVolumeName,
    CpuPinMigrationEnabled,
    VncKeyboardLayout(ConfigAuthType.User),
    VncKeyboardLayoutValidValues(ConfigAuthType.User),
    PreDefinedNetworkCustomProperties,
    UserDefinedNetworkCustomProperties,
    HostNetworkQosSupported,
    MaxAverageNetworkQoSValue,
    MaxPeakNetworkQoSValue,
    MaxBurstNetworkQoSValue,
    MaxHostNetworkQosShares,
    QoSInboundAverageDefaultValue,
    QoSInboundPeakDefaultValue,
    QoSInboundBurstDefaultValue,
    QoSOutboundAverageDefaultValue,
    QoSOutboundPeakDefaultValue,
    QoSOutboundBurstDefaultValue,
    MaxVmNameLength(ConfigAuthType.User),
    MaxVmNameLengthSysprep(ConfigAuthType.User),
    DefaultGeneralTimeZone,
    DefaultWindowsTimeZone,
    SpeedOptimizationSchedulingThreshold,
    SchedulerAllowOverBooking,
    SchedulerOverBookingThreshold,
    UserSessionTimeOutInterval(ConfigAuthType.User),
    DefaultMaximumMigrationDowntime,
    ClusterRequiredRngSourcesDefault(ConfigAuthType.User),
    SpiceFileTransferToggleSupported(ConfigAuthType.User),
    DefaultMTU,
    MaxThroughputUpperBoundQosValue,
    MaxReadThroughputUpperBoundQosValue,
    MaxWriteThroughputUpperBoundQosValue,
    MaxIopsUpperBoundQosValue,
    MaxReadIopsUpperBoundQosValue,
    MaxWriteIopsUpperBoundQosValue,
    MaxCpuLimitQosValue,
    AutoConvergenceSupported(ConfigAuthType.User),
    MigrationCompressionSupported(ConfigAuthType.User),
    CORSSupport,
    CORSAllowedOrigins,
    CinderProviderSupported,
    NetworkSriovSupported(ConfigAuthType.User),
    NetworkExclusivenessPermissiveValidation,
    HostDevicePassthroughCapabilities,
    LiveStorageMigrationBetweenDifferentStorageTypes,
    MaxIoThreadsPerVm(ConfigAuthType.User),
    MultipleGraphicsSupported(ConfigAuthType.User),
    DisplayUncaughtUIExceptions,
    DisplaySupportedBrowserWarning,
    RefreshLunSupported,
    UploadImageUiInactivityTimeoutInSeconds(ConfigAuthType.User),
    UploadImageChunkSizeKB,
    UploadImageXhrTimeoutInSeconds,
    UploadImageXhrRetryIntervalInSeconds,
    UploadImageXhrMaxRetries;

    public static enum ConfigAuthType {
        Admin,
        User
    }

    private ConfigAuthType authType;

    private ConfigurationValues(ConfigAuthType authType) {
        this.authType = authType;
    }

    private ConfigurationValues() {
        this(ConfigAuthType.Admin);
    }

    public ConfigAuthType getConfigAuthType() {
        return authType;
    }

    public boolean isAdmin() {
        return ConfigAuthType.Admin == authType;
    }

    public int getValue() {
        return ordinal();
    }

    public static ConfigurationValues forValue(int value) {
        return values()[value];
    }
}
