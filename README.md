<!--[![Build Status](https://travis-ci.org/java-native-access/jna.svg?branch=master)](https://travis-ci.org/java-native-access/jna)-->

# org.mm2python
micro-manager 2.0 plugin that enables python control using Py4j remote procedure calls and IPC data transfer using memory mapped files

# dependencies
org.mm2python is a plugin for the open-source microscopy control project Micro-Manager (https://micro-manager.org/) (https://github.com/micro-manager/micro-manager)
Please download and install version 2.0 from that site.

org.mm2python depends on the Py4j project (https://www.py4j.org/).  It is included as a dependency in the gradle build

# getting started
For micro-manager users:
> copy the org.mm2python .jar file from build/libs folder to your micro-manager plugin directory.
> The next time you start micro-manager, it should be selectable from the menu.

For developers:
> org.mm2python uses the gradle build system to manage tests, dependencies and build.
>
> Additionally, it was developed in IntelliJ and includes .idea files to help manage debugging configurations.
> The package .jar can be built by running the "Jar" configuration.  Then that .jar can be copied to your micro-manager directory by running "copyCoreToMM" configuration.
> Finally, you can launch the external micro-manager program from within IntelliJ (normally or in debug mode) by running the "Run_external_MM" configuration
> 
> To develop, you must have a recent installation of micro-manager 2.0 on your computer.  Modify the build.gradle file so that the "core_plugin_path" and "app_path" reflect your installation's plugin folder and root folder, respectively.
>

# how to use
org.mm2python has a simple UI with three tabs:
> 1) Main Pane:  Contains a console to display events.  Initiate the bridge to python (using py4j) by clicking the "create python bridge" button.  Initiate the event monitor to track data generation by clicking the "START monitor" button

> 2) Configuration:  Currently, there is only one implemented messenger interface: Py4j.  You can select one of two temp-file management systems: fixed or dynamic.

> 3) Disk Management:  Define the path to temp folder to store memory mapped files.  While not necessary, you can also create a RAM disk to reserve a portion of system ram for temp file placement.  On Windows systems, this requires you have installed the imdisk program.  After RAM disk creation, you will need to format that drive.

For most users, you will simply use default values.  Then the only need is to click "Create Python Bridge" and "START monitor"

> #### accessing the data from python
> 1 - install py4j by using pip or anaconda: 
> ``` buidoutcfg
> conda install py4j
> pip install py4j
> ```
> 
> 2 - type the following:
> ``` buildoutcfg
> from py4j.java_gateway import JavaGateway, GatewayParameters
> gateway = JavaGateway(gateway_parameters=GatewayParameters(auto_field=True))
> gate = gateway.entry_point
> ```
>
> 3 - to access data using org.mm2python (method 1):
> ``` buildoutcfg
> # if you want the newest data
> meta = gate.getLastMeta()
>
> # if you want the oldest data
> meta = gate.getFirstMeta()
> 
> dat = np.memmap(meta.getFilepath(), 
>                 dtype="uint16", 
>                 mode='r+', offset=0,
>                 shape=(meta.getxRange(), meta.getyRange()))
> ```
> "dat" is an ndarray-like object that can be used interchangeably with numpy arrays.
>
> 4 - to access data using org.mm2python (method 2):
> ``` buildoutcfg
> # to retrieve any data based on coordinates, or subset of coordinates, use the builder
> paramBuilder = gate.getParameterBuilder()
> 
> # create coordinate with time=5, position=10, channel=0
> coordinate = paramBuilder.time(5).position(10).channel(0).buildMDSParams()
> 
> # get all MetaDataStores matching those coordinates (even if an unspecified coordinate differs)
> list_of_meta = gate.getMetaByParameters(coordinate)
> 
> list_of_mmap_paths = [item.getFilepath() for item in list_of_meta]
> ```
> If the above acquisition contained time, position, channel and slices, "list_of_meta" would be a list of all slices at those time, position, channel coordinates.
>
> 5 - access specific meta information:
> ``` buildoutcfg
> # once you have a MetaDataStore object you can retrieve standard z, p, t, c coordinates with:
> meta.getZ(), meta.getPosition(), meta.getTime(), meta.getChannel()
> 
> # additionally, the image's maximum X, Y, and bitdepth are available:
> meta.getxRange(), meta.getyRange(), meta.getBitDepth()
>
> # finally, if defined at acquisition, the image's ChannelName, autosave prefix, windowname, and mmap file name are accessible:
> meta.getChannelName(), meta.getPrefix(), meta.getWindowName(), meta.getFilepath()

> #### controlling micro-manager from python
> Install py4j like above, then include this code:
> ``` buildoutcfg
> from py4j.java_gateway import JavaGateway, GatewayParameters
> gateway = JavaGateway(gateway_parameters=GatewayParameters(auto_field=True))
> gate = gateway.entry_point
> mmc = gate.getCMMCore()
> mm = gate.getStudio()
> ```
> This gives you access to all micromanager core and studio methods 
>
> https://valelab4.ucsf.edu/~MM/doc-2.0.0-beta/mmcorej/mmcorej/CMMCore.html
>
> https://valelab4.ucsf.edu/~MM/doc-2.0.0-beta/mmstudio/org/micromanager/Studio.html
>
> Be careful to use appropriate types when passing values.  Most micro-manager methods require float.

# about
> clicking "START monitor" causes org.mm2python to register for some global and datastore events (https://micro-manager.org/wiki/Version_2.0_API_Events).
> Every time a "New Image Event" occurs, org.mm2python pulls a new thread to:
> 1) create a MetaDataStore object that contains coordinate and additional information.
> 2) write a memory mapped file, representing this data's location in system memory, to disk.
> 3) write the MetaDataStore object in 1 to a concurrent hashmap and to a concurrent linked deque
>
> Methods to retrieve this MetaDataStore information are available through the parameter builder, or through convenience methods in the gateway.entry_point

# License
Chan Zuckerberg Biohub Software License

This software license is the 2-clause BSD license plus clause a third clause
that prohibits redistribution and use for commercial purposes without further
permission.

Copyright © 2019. Chan Zuckerberg Biohub.
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1.	Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2.	Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

3.	Redistributions and use for commercial purposes are not permitted without
the Chan Zuckerberg Biohub's written permission. For purposes of this license,
commercial purposes are the incorporation of the Chan Zuckerberg Biohub's
software into anything for which you will charge fees or other compensation or
use of the software to perform a commercial service for a third party.
Contact ip@czbiohub.org for commercial licensing opportunities.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
