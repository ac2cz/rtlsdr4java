package com.g0kla.rtlsdr4java;

import org.usb4java.ConfigDescriptor;
import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

public class RtlDevice {

	Context context;
	DeviceHandle deviceHandle;

	public RtlDevice(short vendorId, short productId) {
		// Initialize the default context
		context = new Context();
		int result = LibUsb.init(context);
		if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to initialize libusb.", result);
		deviceHandle = findDevice(vendorId, productId);
		//deviceHandle = LibUsb.openDeviceWithVidPid(context, vendorId, productId);
//		Device device = LibUsb.getDevice(deviceHandle);
	}

	public void exit() {
		LibUsb.exit(context);
	}
	
	public void init() {
		
	}
	

	DeviceHandle findDevice(short vendorId, short productId) {
		// Read the USB device list
		DeviceList list = new DeviceList();
		int result = LibUsb.getDeviceList(context, list);
		if (result < 0) throw new LibUsbException("Unable to get device list", result);

		try
		{
			// Iterate over all devices and scan for the right one
			for (Device device: list)
			{
				DeviceDescriptor descriptor = new DeviceDescriptor();

				result = LibUsb.getDeviceDescriptor(device, descriptor);
				if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to read device descriptor", result);
				if (descriptor.idVendor() == vendorId && descriptor.idProduct() == productId) {
					System.out.println(descriptor);
					ConfigDescriptor config = new ConfigDescriptor();
					result = LibUsb.getConfigDescriptor(device, (byte) 0, config);
					if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to read config descriptor", result);
					System.out.println(config);
					deviceHandle = new DeviceHandle();
					result = LibUsb.open(device, deviceHandle);
					if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to open USB device", result);
					return deviceHandle;
				}
			}
		}
		finally
		{
			// Ensure the allocated device list is freed
			// Note that we need to not free the list before we have opened the device that we want, otherwise that fails
			LibUsb.freeDeviceList(list, true);
		}
		return null;
	}
}
