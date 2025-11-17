<#
╔══════════════════════════════════════════════════════╗
║            ADB CONNECT SCRIPT - USAGE GUIDE          ║
╠══════════════════════════════════════════════════════╣
║ You can run the script in these ways:                ║
║                                                      ║
║ 1. Default device (1) with custom port:              ║
║    .\connect_adb.ps1 45133                           ║
║                                                      ║
║ 2. Specific device number and port:                  ║
║    .\connect_adb.ps1 2 46739                         ║
║                                                      ║
║ 3. Third device (device 3):                          ║
║    .\connect_adb.ps1 3 5555                          ║
║                                                      ║
║ Device 1 IP: 192.168.10.165                          ║
║ Device 2 IP: 192.168.43.163                          ║
║ Device 3 IP: 192.168.0.190                           ║
╚══════════════════════════════════════════════════════╝
#>

# Predefined IPs
$device1 = "192.168.10.165"
$device2 = "192.168.43.163"
$device3 = "192.168.0.190"

# Read arguments
$deviceNum = $args[0]
$port = $args[1]

# Handle case: if only port is passed
if ($args.Count -eq 1) {
    $port = $args[0]
    $deviceNum = 1
}

# Handle default device
if (-not $deviceNum) {
    $deviceNum = 1
}

# Select device IP
switch ($deviceNum) {
    1 { $ip = $device1 }
    2 { $ip = $device2 }
    3 { $ip = $device3 }
    default {
        Write-Host "Invalid device number. Please choose 1, 2, or 3."
        exit 1
    }
}

Write-Host "Connecting to $ip`:$port..."
adb connect "$ip`:$port"
