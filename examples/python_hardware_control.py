# import
from py4j.java_gateway import JavaGateway, GatewayParameters
import numpy as np

# connect to org.mm2python
gateway = JavaGateway(gateway_parameters=GatewayParameters(auto_field=True))

# link to class
ep = gateway.entry_point
mm = ep.getStudio()
mmc = ep.getCMMCore()

# ===========================================================
# ====== micro-manager core or studio control with py4j =====
# ===========================================================

# https://valelab4.ucsf.edu/~MM/doc-2.0.0-beta/mmstudio/org/micromanager/Studio.html
# https://valelab4.ucsf.edu/~MM/doc-2.0.0-beta/mmcorej/mmcorej/CMMCore.html



# DEVICE CONTROL USING CORE

# Z stage
mmc.getPosition()
mmc.getPosition("Z")
mmc.setPosition(5.9999)

# XY Stage
mmc.getXPosition()
mmc.getYPosition()

# camera
mmc.setCameraDevice("camera name")
mmc.setExposure(100.0)

# set any general device's property
mmc.setProperty("device name", "device property", "value")



# DEVICE CONTROL USING STUDIO

af = mm.getAutofocusManager()
afp = af.getAutofocusMethod()



# RETRIEVING DATA

# get metadata of the most recent image taken
meta = ep.getLastMeta()

# get path of memory mapped file
meta.getFilepath()

# load the file
data = np.memmap(meta.getFilepath(), dtype="uint16", mode='r+', offset=meta.getBufferPosition(),
                 shape=(meta.getxRange(), meta.getyRange()))
