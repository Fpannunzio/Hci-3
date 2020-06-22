package com.example.hci_3.api.DeviceStates;

import java.util.Map;

public interface DeviceState {
    Map<String, String> compareToNewerVersion(DeviceState state);
}
