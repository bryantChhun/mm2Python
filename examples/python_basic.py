# bchhun, {2019-06-18}

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

#device control
mmc.setPosition(5.9999)

af = mm.getAutofocusManager()
afp = af.getAutofocusMethod()
afp.fullFocus()

# standard Micro-manager 2.0 api for creating a datastore, snapping an image, and placing the data
autosavestore = mm.data().createMultipageTIFFDatastore("path_to_your_savefile", True, True)
mm.displays().createDisplay(autosavestore)

c, z, p = 1.0, 1.0, 1.0
mmc.snapImage()
tmp1 = mmc.getTaggedImage()
channel0 = mm.data().convertTaggedImage(tmp1)
channel0 = channel0.copyWith(
    channel0.getCoords().copy().channel(c).z(z).stagePosition(p).build(),
    channel0.getMetadata().copy().positionName("" + str(p)).build())
autosavestore.putImage(channel0)


# ===========================================
# ===== access org.mm2python MetaDataStores =====
# ===========================================

# retrieve MetaData object for the most recent image
meta = ep.getLastMeta()

# retrieve MetaData object for the oldest image
meta = ep.getFirstMeta()

meta.getChannel()
meta.getZ()
meta.getPosition()
meta.getTime()
meta.getxRange()
meta.getyRange()
meta.getBitDepth()
meta.getChannelName()
meta.getPrefix()
meta.getWindowName()
meta.getFilepath()

# also have
# ["mmap filepath1", "mmap filepath2", ...] = ep.getFilesByChannel( <int> )
# ["mmap filepaths", "mmap filepath2", ...] = ep.getFilesByTime( <int> )

# [MDS1, MDS2, ...] = ep.getMetaByChannel(<int>)
# [MDS1, MDS2, ...] = ep.getMetaByTime(<int>)

# ===========================================================
# ===== given an mmap path, load the data in python =========
#============================================================

path = meta.getFilepath()
numpy_array = np.memmap(meta.getFilepath(), dtype="uint16", mode='r+', offset=0,
                    shape=(meta.getxRange(), meta.getyRange()))


# =========================
# ======== example ========

aq = mm.getAcquisitionManager()
aq.runAcquisitionNonblocking()

# wait some time for several cycles
ch = ep.getMetaByChannelName("Rhodamine")
for i in range(len(ch)):
    print(ch[i].getZ())

# convenience function
def get_snap_data(mm, gate):
    # Snaps and writes to snap/live view
    mm.live().snap(True)

    # Retrieve data from memory mapped files, np.memmap is functionally same as np.array
    meta = gate.getLastMeta()
    dat = np.memmap(meta.getFilepath(), dtype="uint16", mode='r+', offset=0,
                    shape=(meta.getxRange(), meta.getyRange()))

    return dat

