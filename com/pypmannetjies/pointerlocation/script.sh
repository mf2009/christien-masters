#LOGCAT
logcat | grep  POINTERLOCATION >> /sdcard/touch/`date +%m_%d_%H_%M_%S`.csv &
logcat >> /sdcard/touch/`date +%m_%d_%H_%M_%S`.csv &

#GREP
| where {$_ -match "POINTERLOCATION"}

#ADB
#turn on from device
setprop service.adb.tcp.port 5555
stop adbd
start adbd
#turn off from device
setprop service.adb.tcp.port -1
stop adbd
start adbd
#turn on from computer
adb tcpip 5555
adb connect 192.168.0.101:5555
#turn off from computer
adb usb