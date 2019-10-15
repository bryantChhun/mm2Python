from py4j.java_gateway import JavaGateway, CallbackServerParameters, GatewayParameters
import numpy as np

import napari


def pull_memmap(entry_point):
    meta = entry_point.getLastMeta()
    if meta is None:
        return None
    else:
        data = np.memmap(meta.getFilepath(), dtype="uint16", mode='r+', offset=meta.getBufferPosition(),
                         shape=(meta.getxRange(), meta.getyRange()))
        return data

# =======================================================
# =========== Visualization classes =====================
# =======================================================


class SimpleNapariWindow(QObject):

    def __init__(self):
        super().__init__()
        print('setting up napari window')
        self.viewer = napari.Viewer()

        N = 2048

        # init image data
        self.init_data_1 = 2 ** 16 * np.random.rand(N, N)

        # init layers with vector data and subscribe to gui notifications
        self.layer1 = self.viewer.add_image(self.init_data_1)

    @pyqtSlot(object)
    def update(self, data: object):
        # print('gui is notified of new data')
        try:
            self.layer1.data = data
            # self.layer1._node.set_data(data)
            # self.layer1._node.update()
            self.layer1.name = "snap"
        except Exception as ex:
            print("exception while updating gui "+str(ex))


# =======================================================
# ============== mm2python data retrieval ===============
# =======================================================

class ImplementPy4J(QObject):

    send_data_to_display = pyqtSignal(object)

    def __init__(self, gate):
        super().__init__()
        self.gateway = gate
        self.ep = self.gateway.entry_point
        listener = self.ep.getListener()
        listener.registerListener(self)

    class Java:
        implements = ["org.mm2python.MPIMethod.Py4J.Py4JListenerInterface"]

    def notify(self, obj):
        """
        When notified by java, this event is called
        """

        print("notified by java via java interface")

        dat = pull_memmap(self.ep)

        while dat is None:
            dat = pull_memmap(self.ep)

        self.send_data_to_display.emit(dat)


if __name__ == '__main__':

    with napari.gui_qt():

        # connect to org.mm2python
        gateway = JavaGateway(gateway_parameters=GatewayParameters(auto_field=True),
                              callback_server_parameters=CallbackServerParameters())

        window = SimpleNapariWindow()
        imp = ImplementPy4J(gateway)

        # connect the window's slot with the monitor's signal
        imp.send_data_to_display.connect(window.update)
