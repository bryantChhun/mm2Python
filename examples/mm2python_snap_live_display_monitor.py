from py4j.java_gateway import JavaGateway, CallbackServerParameters, GatewayParameters
import numpy as np
from PyQt5.QtCore import QRunnable, QThreadPool, QObject, pyqtSlot, pyqtSignal
import time

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
"""
The "ProcessRunnable" class launches the monitor in another thread.
Otherwise, the while loop will prevent the display from updating until everything is done
"""

class ProcessRunnable(QRunnable):
    def __init__(self, target, args):
        super().__init__()
        self.t = target
        self.args = args

    def run(self):
        self.t(*self.args)

    def start(self):
        QThreadPool.globalInstance().start(self)

"""
Simple monitor to send new data to napari window
"""


class MonitorPy4j(QObject):

    send_data_to_display = pyqtSignal(object)

    def __init__(self, gateway_):
        super(MonitorPy4j, self).__init__()
        self.gateway = gateway_
        self.ep = self.gateway.entry_point

    def launch_monitor_in_process(self,):
        p = ProcessRunnable(target=self.monitor_process, args=())
        p.start()

    def monitor_process(self):
        print('monitor launched')
        self.ep.clearQueue()
        count = 0
        while True:
            time.sleep(0.001)

            data = pull_memmap(self.ep)
            while data is None:
                data = pull_memmap(self.ep)
            self.send_data_to_display.emit(data)

            if count >= 1000:
                # timeout is 2.5 minutes = 15000
                print("timeout waiting for more data")
                break
            else:
                count += 1
                if count % 100 == 0:
                    print('waiting')


if __name__ == '__main__':

    with napari.gui_qt():

        # connect to org.mm2python
        gateway = JavaGateway(gateway_parameters=GatewayParameters(auto_field=True),
                              callback_server_parameters=CallbackServerParameters())

        window = SimpleNapariWindow()
        monitor = MonitorPy4j(gateway)

        # connect the window's slot with the monitor's signal
        monitor.send_data_to_display.connect(window.update)

        monitor.launch_monitor_in_process()
