#!/bin/bash
#
# ╔══════════════════════════════════════════════════════╗
# ║            ADB CONNECT SCRIPT - USAGE GUIDE          ║
# ╠══════════════════════════════════════════════════════╣
# ║ You can run the script in these ways:                ║
# ║                                                      ║
# ║ 1. Default device (1) with custom port:              ║
# ║    ./connect_adb.sh 45133                            ║
# ║                                                      ║
# ║ 2. Specific device number and port:                  ║
# ║    ./connect_adb.sh 2 46739                          ║
# ║                                                      ║
# ║ 3. Third device (device 3):                          ║
# ║    ./connect_adb.sh 3 5555                           ║
# ║                                                      ║
# ║ Device 1 IP: 192.168.10.165                          ║
# ║ Device 2 IP: 192.168.43.163                          ║
# ║ Device 3 IP: 192.168.0.190                           ║
# ╚══════════════════════════════════════════════════════╝
#

# Predefined IPs
device1="192.168.10.165"
device2="192.168.43.163"
device3="192.168.0.190"

deviceNum=$1
port=$2

# If only one argument is given, treat it as the port for device 1
if [ $# -eq 1 ]; then
    port=$1
    deviceNum=1
fi

# Default device number
if [ -z "$deviceNum" ]; then
    deviceNum=1
fi

# Select device IP
case $deviceNum in
    1) ip=$device1 ;;
    2) ip=$device2 ;;
    3) ip=$device3 ;;
    *) echo "Invalid device number. Please choose 1, 2, or 3."; exit 1 ;;
esac

# Default port if not provided
if [ -z "$port" ]; then
    port=5555
fi

echo "Connecting to $ip:$port..."
adb connect "$ip:$port"

